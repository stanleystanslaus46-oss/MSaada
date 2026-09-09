# MSAADA — VS Code Ready

This package is prepared for VS Code-first development.

## Requirements
- JDK compatible with the project's Android/Gradle toolchain
- Android SDK + platform tools
- USB debugging enabled for a physical device
- Internet for the first Gradle distribution/dependency download
- VS Code

## Open
Open this ROOT folder in VS Code. Do not open `app/` alone.

## Windows
PowerShell/VS Code terminal:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
.\gradlew.bat testDebugUnitTest
```

## macOS/Linux

```bash
./gradlew assembleDebug
./gradlew installDebug
./gradlew testDebugUnitTest
```

The included bootstrap scripts download Gradle 9.3.1 into `.gradle-local` on first use. This is used because the original source did not include the official Gradle wrapper JAR.

## Android device

```bash
adb devices
```

Then:

```powershell
.\gradlew.bat installDebug
```

## VS Code tasks
Use `Terminal > Run Task` and choose:
- MSAADA: assembleDebug
- MSAADA: installDebug
- MSAADA: testDebugUnitTest

## Important
Do not put Google Play service-account JSON, passwords, keystore passwords, or other secrets in this project.
