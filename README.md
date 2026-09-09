# MSAADA Android App

**Zana Rahisi. Kazi Rahisi. Maisha Rahisi.**

MSAADA is a lightweight, offline-first Tanzanian digital toolbox built with native Android, Kotlin and Jetpack Compose.

## Open locally

Prerequisite: Android Studio.

1. Open this project directory in Android Studio.
2. Allow Gradle sync and Android Studio to update compatibility files if prompted.
3. Run the app on an emulator or physical Android device.
4. For a production release, configure your own signing key through Android Studio or the documented environment variables.

## Release signing environment variables

- `KEYSTORE_PATH`
- `STORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Do not commit keystores, passwords, API keys or other secrets.

## Current architecture

- Native Android / Kotlin / Jetpack Compose / Material 3
- MVVM
- Room local database
- Offline-first design
- No Firebase/backend required by the current app runtime
- Light mode default; Light / Dark / System theme options

## Validation

Run from the project root after Gradle is available:

```bash
./gradlew test
```

Then perform device QA before release. See `PHASE5_RELEASE_AUDIT.md`.


## Production billing configuration

Build the Android app with `MSAADA_BILLING_API_URL` set to the deployed HTTPS Netlify verification function. Do not place Google service-account JSON or other private credentials in the Android project. The backend secret belongs in Netlify environment variables.

See `PHASE9_FINAL_RELEASE_AUDIT.md` and `RELEASE_PHASE9_CHECKLIST.md` before publishing.


## Current monetization mode

MSAADA currently uses manual mobile-money payment reporting instead of Google Play Billing. See `MANUAL_PAYMENT_SETUP.md`.


Current payment setup: MSAADA PRO payment `+255742259683`; SMS notifications `+255650039639`; WhatsApp notifications `+255742259683`.

## PRO Activation System

This build uses manual mobile-money verification with one-time activation codes. Customers pay `+255742259683`, submit their transaction ID, and you receive the report through the configured SMS/WhatsApp notification channels. After you verify the payment, use the deployed `/admin/` page to create a one-time code tied to the customer's payment phone number. The customer enters that code inside MSAADA to activate PRO.

See `MANUAL_PAYMENT_SETUP.md` and `ADMIN_ACTIVATION_SETUP.md` for setup details.
