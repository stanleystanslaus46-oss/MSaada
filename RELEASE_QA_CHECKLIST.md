# MSAADA Release QA Checklist

Use this checklist in Android Studio before publishing.

## Build
- [ ] Gradle sync succeeds
- [ ] `./gradlew test` succeeds
- [ ] Debug APK installs
- [ ] Release AAB builds
- [ ] Release signing configured

## Core UX
- [ ] Splash artwork is unchanged
- [ ] Onboarding appears only on first launch
- [ ] Home opens correctly after onboarding
- [ ] Navigation/back behavior works
- [ ] Search returns correct tools
- [ ] Favorites persist
- [ ] Recent tools update correctly

## Data
- [ ] Shopping list persists
- [ ] Prayer list persists
- [ ] Study planner persists
- [ ] Exam countdown persists
- [ ] Church planner persists
- [ ] Saved documents persist
- [ ] RisitiSafe receipts persist
- [ ] Umeme history persists for PRO
- [ ] Form profiles persist for PRO
- [ ] Contracts persist for PRO
- [ ] Kikoba groups persist for PRO
- [ ] Biashara products persist for PRO
- [ ] Clear All Data removes all local records

## Monetization
- [ ] Free limits are enforced
- [ ] PRO features are clearly labelled
- [ ] DEBUG PRO simulator is absent from release
- [ ] No fake payment success is shown in release
- [ ] Billing is added before commercial launch

## Device checks
- [ ] Android 7+ / minSdk 24 device
- [ ] Small phone
- [ ] Large phone
- [ ] Tablet
- [ ] Portrait orientation
- [ ] Dark mode
- [ ] Offline mode
