param(
  [Parameter(Mandatory = $true)][string]$FemalePath,
  [Parameter(Mandatory = $true)][string]$MalePath,
  [Parameter(Mandatory = $true)][string]$SheetPath,
  [double]$MaleScale = 1.0,
  [double]$FemaleScale = 1.0,
  [int]$AlphaThreshold = 1,
  [bool]$ClipPanels = $true
)

Add-Type -AssemblyName System.Drawing

if (!(Test-Path $FemalePath)) {
  throw "Missing female image: $FemalePath"
}
if (!(Test-Path $MalePath)) {
  throw "Missing male image: $MalePath"
}
if (!(Test-Path $SheetPath)) {
  throw "Missing sheet: $SheetPath"
}

try {
  $fs = [System.IO.File]::Open($SheetPath, 'Open', 'ReadWrite', 'None')
  $fs.Close()
} catch {
  throw "Sprite sheet is locked: $($_.Exception.Message)"
}

function Get-OpaqueBounds {
  param($img)
  if ($AlphaThreshold -lt 1) {
    $AlphaThreshold = 1
  }
  $minX = $img.Width
  $minY = $img.Height
  $maxX = 0
  $maxY = 0
  $found = $false

  for ($y = 0; $y -lt $img.Height; $y++) {
    for ($x = 0; $x -lt $img.Width; $x++) {
      $c = $img.GetPixel($x, $y)
      if ($c.A -ge $AlphaThreshold) {
        if ($x -lt $minX) { $minX = $x }
        if ($y -lt $minY) { $minY = $y }
        if ($x -gt $maxX) { $maxX = $x }
        if ($y -gt $maxY) { $maxY = $y }
        $found = $true
      }
    }
  }

  if (-not $found) {
    return [System.Drawing.Rectangle]::new(0, 0, $img.Width, $img.Height)
  }

  return [System.Drawing.Rectangle]::new(
    $minX,
    $minY,
    ($maxX - $minX + 1),
    ($maxY - $minY + 1)
  )
}

function Draw-Character {
  param(
    $g,
    $img,
    [double]$panelX,
    [double]$panelW,
    [double]$panelH,
    [double]$scaleMultiplier,
    [bool]$clipPanel
  )

  if ($null -eq $img) {
    return
  }

  $bounds = Get-OpaqueBounds $img
  $srcW = [double]$bounds.Width
  $srcH = [double]$bounds.Height
  if ($srcW -le 0 -or $srcH -le 0) {
    return
  }

  if ($scaleMultiplier -le 0) {
    $scaleMultiplier = 1.0
  }

  $scale = [math]::Min($panelW / $srcW, $panelH / $srcH) * $scaleMultiplier
  $destW = $srcW * $scale
  $destH = $srcH * $scale
  $destX = $panelX + (($panelW - $destW) / 2.0)
  $destY = $panelH - $destH
  $destRect = [System.Drawing.RectangleF]::new($destX, $destY, $destW, $destH)
  if ($clipPanel) {
    $panelRect = [System.Drawing.RectangleF]::new($panelX, 0, $panelW, $panelH)
    $g.SetClip($panelRect)
  }

  $g.DrawImage($img, $destRect, $bounds, [System.Drawing.GraphicsUnit]::Pixel)

  if ($clipPanel) {
    $g.ResetClip()
  }
}

$existingSrc = [System.Drawing.Bitmap]::FromFile($SheetPath)
$existing = $existingSrc.Clone(
  [System.Drawing.Rectangle]::new(0, 0, $existingSrc.Width, $existingSrc.Height),
  $existingSrc.PixelFormat
)
$existingSrc.Dispose()

$sheetW = $existing.Width
$sheetH = $existing.Height
$panelW = $sheetW / 3.0

$newSheet = [System.Drawing.Bitmap]::new(
  $sheetW,
  $sheetH,
  [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
)
$g = [System.Drawing.Graphics]::FromImage($newSheet)
$g.Clear([System.Drawing.Color]::Transparent)
$g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
$g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
$g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality

$srcRect = [System.Drawing.RectangleF]::new($panelW * 2, 0, $panelW, $sheetH)
$destRect = [System.Drawing.RectangleF]::new($panelW * 2, 0, $panelW, $sheetH)
$g.DrawImage($existing, $destRect, $srcRect, [System.Drawing.GraphicsUnit]::Pixel)

$female = [System.Drawing.Bitmap]::FromFile($FemalePath)
$male = [System.Drawing.Bitmap]::FromFile($MalePath)

Draw-Character -g $g -img $male -panelX 0 -panelW $panelW -panelH $sheetH -scaleMultiplier $MaleScale -clipPanel $ClipPanels
Draw-Character -g $g -img $female -panelX $panelW -panelW $panelW -panelH $sheetH -scaleMultiplier $FemaleScale -clipPanel $ClipPanels

$g.Dispose()
$existing.Dispose()
$female.Dispose()
$male.Dispose()

if (Test-Path $SheetPath) {
  Remove-Item -Force $SheetPath
}

$newSheet.Save($SheetPath, [System.Drawing.Imaging.ImageFormat]::Png)
$newSheet.Dispose()
