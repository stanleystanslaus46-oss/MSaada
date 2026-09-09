# MSAADA — Phase 10 Real-Device QA

## Purpose
This phase prepares the project for real-device and Google Play testing. It does not claim a successful Android build from this environment.

## Required environment
- Android Studio with Android SDK installed
- Network access for Gradle dependency resolution
- A physical Android phone (recommended Android 8+)
- USB debugging enabled for device testing
- Google Play test account for billing tests

## Smoke test matrix

### App startup
- [ ] Fresh install opens Splash → Onboarding → Home
- [ ] Returning install opens Splash → Home
- [ ] Light theme is default on fresh install
- [ ] System/Dark theme selection persists

### Navigation
- [ ] Every Home category opens
- [ ] Every registered ToolRegistry tool opens its intended screen
- [ ] Back navigation works without unexpected app exit
- [ ] No blank/error route is shown

### Calculators
- [ ] Budget
- [ ] Savings
- [ ] Percentage
- [ ] Loan
- [ ] Kikoba
- [ ] Profit
- [ ] Discount
- [ ] Markup
- [ ] Biashara
- [ ] Umeme
- [ ] Fuel/Trip cost
- [ ] Grade/Unit conversion

### Documents
- [ ] RisitiSafe creates a valid receipt
- [ ] Invoice/Quotation/Receipt/Reports render correctly
- [ ] Msaidizi wa Fomu validates required fields
- [ ] Mkataba Rahisi shows legal disclaimer
- [ ] Save/share/export actions work where implemented

### Passport Photo
- [ ] Camera/gallery input works
- [ ] Crop, zoom, reposition and rotate work
- [ ] Printable sheet renders correctly
- [ ] First five completed generations are free
- [ ] Sixth generation is blocked for non-PRO
- [ ] PRO user can generate without the free counter increasing

### Local data
- [ ] Favorites persist after restart
- [ ] Room data persists after restart
- [ ] Clear All Data clears supported tables/preferences
- [ ] Database upgrade from existing install does not lose supported data

### Billing
- [ ] Monthly product resolves from Play Billing
- [ ] Yearly product resolves from Play Billing
- [ ] Purchase requires backend verification before PRO activation
- [ ] Restore/relaunch recovers an active verified entitlement
- [ ] Cancelled/expired entitlement does not remain permanently active
- [ ] Verification endpoint is HTTPS
- [ ] Network failure fails safely without granting PRO

## Evidence to capture
For each failed test record:
1. device model
2. Android version
3. app versionCode/versionName
4. exact steps
5. expected result
6. actual result
7. Logcat error or screenshot

## Release gate
Do not publish until all critical smoke tests pass on a physical device and billing has been tested through a Play Console test track.
