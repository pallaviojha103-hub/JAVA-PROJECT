@echo off
cd /d "%~dp0"
TITLE VIT Library Management System - Terminal CLI Mode
cls
echo ================================================================================
echo    VIT Library Management System - Command Line Interface (CLI Mode)
echo    Fully executable from terminal. No GUI required.
echo ================================================================================
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

%JAVA%\java.exe --enable-native-access=ALL-UNNAMED -cp "%CP%" com.vityarthi.library.CLI
pause
