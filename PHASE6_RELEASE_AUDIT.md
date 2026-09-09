# MSAADA — Phase 6 Production Hardening Audit

## Scope
Phase 6 hardens entitlement handling and passport-photo usage accounting while keeping the app offline-first.

## Changes
- `MsaadaViewModel.toggleProUser()` now mutates PRO state only in DEBUG builds.
- PRO passport-photo generations no longer increment the free-use counter.
- Added release QA checklist for Android Studio / real-device validation.

## Important release facts
- Real Google Play Billing is **not implemented yet**.
- The PRO simulator is intentionally DEBUG-only.
- The app still requires a developer-owned signing key for production distribution.
- Full Gradle build/runtime testing must be performed in Android Studio because this environment cannot resolve/download Android build dependencies.

## Required device QA
- [ ] Fresh install: Splash → Onboarding → Home
- [ ] Returning launch: Splash → Home
- [ ] Light/Dark/System theme
- [ ] Swahili/English switch
- [ ] Favorites and recent tools persist after restart
- [ ] Room data persists after restart
- [ ] Clear All Data clears Room data and preferences
- [ ] Passport Photo: camera permission, gallery selection, crop/zoom/rotate, save/share
- [ ] Passport Photo: first 5 free generations, 6th blocked for non-PRO
- [ ] Passport Photo: PRO remains unlimited without increasing free counter
- [ ] RisitiSafe validation and saved receipts
- [ ] Umeme validation and PRO history gate
- [ ] Msaidizi wa Fomu / Mkataba Rahisi PRO save gates
- [ ] Kikoba validation and group calculations
- [ ] Biashara calculations including fixed-cost break-even
- [ ] PDF/document preview and share flows
- [ ] Small Android phone and tablet layouts
- [ ] Back navigation from every tool
- [ ] Release build contains no visible PRO simulator

## Next phase
Implement Google Play Billing for the 5,000 TSh/month MSAADA PRO plan, then perform final signed AAB QA.
