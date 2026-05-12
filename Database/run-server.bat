@echo off
title ChatApp – Server
set JAVAFX_LIB=lib\javafx\lib
set SQLITE_JAR=lib\sqlite-jdbc.jar
set OUT_DIR=out
set CP="%OUT_DIR%;%JAVAFX_LIB%\javafx.controls.jar;%JAVAFX_LIB%\javafx.fxml.jar;%JAVAFX_LIB%\javafx.base.jar;%JAVAFX_LIB%\javafx.graphics.jar;%SQLITE_JAR%"
set MODULES=javafx.controls,javafx.fxml

echo Starting Chat Server on port 12345...
java -cp %CP% --module-path "%JAVAFX_LIB%" --add-modules %MODULES% server.Server
pause
