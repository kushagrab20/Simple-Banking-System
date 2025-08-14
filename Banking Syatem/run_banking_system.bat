@echo off
title Banking System Launcher
color 0A

echo.
echo ========================================
echo    🏦 SIMPLE BANKING SYSTEM LAUNCHER
echo ========================================
echo.
echo Choose which version to run:
echo.
echo [1] Console Version (Text-based)
echo [2] GUI Version (Graphical Interface)
echo [3] Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" (
    echo.
    echo Starting Console Version...
    echo.
    call build.bat
) else if "%choice%"=="2" (
    echo.
    echo Starting GUI Version...
    echo.
    call build_gui.bat
) else if "%choice%"=="3" (
    echo.
    echo Goodbye!
    pause
    exit
) else (
    echo.
    echo Invalid choice! Please enter 1, 2, or 3.
    echo.
    pause
    call run_banking_system.bat
)
