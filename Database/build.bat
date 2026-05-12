@echo off
setlocal
title ChatApp Builder

echo ============================================
echo          ChatApp Build Script
echo ============================================

REM ── Paths (edit these to match your machine) ──────────────────────────────
set JAVAFX_LIB=lib\javafx\lib
set SQLITE_JAR=lib\sqlite-jdbc.jar
set SRC_DIR=src
set OUT_DIR=out
set CP="%JAVAFX_LIB%\javafx.controls.jar;%JAVAFX_LIB%\javafx.fxml.jar;%JAVAFX_LIB%\javafx.base.jar;%JAVAFX_LIB%\javafx.graphics.jar;%SQLITE_JAR%"

REM ── Create output directory ────────────────────────────────────────────────
if not exist %OUT_DIR% mkdir %OUT_DIR%

REM ── Collect all .java files ────────────────────────────────────────────────
echo Collecting source files...
dir /s /b %SRC_DIR%\*.java > sources.txt 2>nul

REM ── Compile ───────────────────────────────────────────────────────────────
echo Compiling...
javac -cp %CP% -d %OUT_DIR% @sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Check errors above.
    del sources.txt
    pause
    exit /b 1
)

del sources.txt
echo.
echo [OK] Compilation successful!  Output: %OUT_DIR%\
echo.
echo Run the server:   run-server.bat
echo Run the client:   run-client.bat
echo ============================================
pause
