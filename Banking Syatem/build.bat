@echo off
echo === Simple Banking System Build Script ===
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 8 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo WARNING: Maven not found, using manual compilation
    goto manual_build
)

echo Using Maven to build project...
mvn clean compile
if errorlevel 1 (
    echo Maven build failed, trying manual compilation...
    goto manual_build
)

echo.
echo Build successful! Running application...
echo.
mvn exec:java -Dexec.mainClass="BankingSystem"
goto end

:manual_build
echo.
echo === Manual Compilation ===
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir lib

REM Download MySQL JDBC driver if not present
if not exist "lib\mysql-connector-java-8.0.33.jar" (
    echo Downloading MySQL JDBC driver...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar' -OutFile 'lib\mysql-connector-java-8.0.33.jar'"
    if errorlevel 1 (
        echo ERROR: Failed to download MySQL JDBC driver
        echo Please download it manually from: https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/
        pause
        exit /b 1
    )
)

REM Compile Java files
echo Compiling Java files...
javac -cp "lib\*" -d "bin" src\main\java\*.java src\main\java\com\banking\model\*.java src\main\java\com\banking\dao\*.java src\main\java\com\banking\service\*.java src\main\java\com\banking\util\*.java
if errorlevel 1 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Build successful! Running application...
echo.
java -cp "lib\*;bin" BankingSystem

:end
echo.
echo Application finished.
pause 