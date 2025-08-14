@echo off
echo === Database Setup Script ===
echo This script will help you set up the MySQL database for the Banking System
echo.

REM Check if MySQL is installed
mysql --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: MySQL is not installed or not in PATH
    echo Please install MySQL 5.7 or higher
    echo Download from: https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
)

echo MySQL found! Starting database setup...
echo.

REM Get database credentials
set /p DB_USER=Enter MySQL username (default: root): 
if "%DB_USER%"=="" set DB_USER=root

set /p DB_PASS=Enter MySQL password: 
if "%DB_PASS%"=="" (
    echo WARNING: No password provided
    set DB_PASS=
)

set /p DB_NAME=Enter database name (default: banking_system): 
if "%DB_NAME%"=="" set DB_NAME=banking_system

echo.
echo Creating database and tables...

REM Create database
if "%DB_PASS%"=="" (
    mysql -u %DB_USER% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME%;"
) else (
    mysql -u %DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME%;"
)

if errorlevel 1 (
    echo ERROR: Failed to create database
    echo Please check your MySQL credentials and try again
    pause
    exit /b 1
)

REM Run SQL script
echo Running database schema...
if "%DB_PASS%"=="" (
    mysql -u %DB_USER% %DB_NAME% < src\main\resources\database.sql
) else (
    mysql -u %DB_USER% -p%DB_PASS% %DB_NAME% < src\main\resources\database.sql
)

if errorlevel 1 (
    echo ERROR: Failed to run database schema
    pause
    exit /b 1
)

echo.
echo === Database Setup Complete! ===
echo Database: %DB_NAME%
echo Username: %DB_USER%
echo.
echo Please update the database connection details in:
echo src\main\java\com\banking\util\DatabaseConnection.java
echo.
echo Current settings:
echo URL: jdbc:mysql://localhost:3306/%DB_NAME%
echo Username: %DB_USER%
echo Password: %DB_PASS%
echo.
pause 