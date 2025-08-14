@echo off
echo === Simple Banking System GUI Build Script ===
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 8 or higher
    pause
    exit /b 1
)

echo Using manual compilation for GUI version...
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir lib

REM Check if MySQL JDBC driver exists
if not exist "lib\mysql-connector-java-8.0.33.jar" (
    echo ERROR: MySQL JDBC driver not found
    echo Please run build.bat first to download the driver
    pause
    exit /b 1
)

REM Compile Java files
echo Compiling Java files for GUI...
javac -cp "lib\*" -d "bin" src\main\java\*.java src\main\java\com\banking\model\*.java src\main\java\com\banking\dao\*.java src\main\java\com\banking\service\*.java src\main\java\com\banking\util\*.java
if errorlevel 1 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Build successful! Running GUI application...
echo.
java -cp "lib\*;bin" BankingSystemGUI

echo.
echo GUI Application finished.
pause
