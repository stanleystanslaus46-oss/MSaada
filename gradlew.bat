@echo off
setlocal
set "ROOT=%~dp0"
set "GRADLE_VERSION=9.3.1"
set "CACHE=%ROOT%.gradle-local"
set "DIST=%CACHE%\gradle-%GRADLE_VERSION%-bin.zip"
set "HOME=%CACHE%\gradle-%GRADLE_VERSION%"
set "EXE=%HOME%\bin\gradle.bat"
if exist "%EXE%" goto run
if not exist "%CACHE%" mkdir "%CACHE%"
if not exist "%DIST%" (
  echo Downloading Gradle %GRADLE_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-9.3.1-bin.zip' -OutFile '%DIST%'"
  if errorlevel 1 exit /b 1
)
if not exist "%HOME%" mkdir "%HOME%"
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Force '%DIST%' '%CACHE%\_extract'"
if errorlevel 1 exit /b 1
if exist "%CACHE%\_extract\gradle-%GRADLE_VERSION%" move /Y "%CACHE%\_extract\gradle-%GRADLE_VERSION%" "%HOME%" >nul
:run
call "%EXE%" %*
exit /b %ERRORLEVEL%
