@echo off
cd /d "%~dp0"
TITLE VIT Library Management System Launcher
Color 0A
cls
echo ====================================================================
echo    VIT Library Management System - One-Click Launcher
echo ====================================================================
echo.
echo Launching system... Please wait a moment.
echo.

SET JAVA="C:\Users\PALLAVI KUMARI\.jdks\openjdk-26.0.1-1\bin"
SET CP=bin;lib\sqlite-jdbc.jar

IF NOT EXIST bin MKDIR bin

%JAVA%\javac.exe -cp "%CP%" -d bin src\com\vityarthi\library\*.java test\com\vityarthi\library\*.java
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

start "" %JAVA%\javaw.exe --enable-native-access=ALL-UNNAMED -cp "%CP%" com.vityarthi.library.Main
exit
