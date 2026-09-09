# MSAADA Phase 9 — Device & Play Release Checklist

## Android Studio
- [ ] Sync project successfully
- [ ] `testDebugUnitTest` passes
- [ ] Debug APK installs
- [ ] Release variant assembles
- [ ] Signed AAB generated

## Core app
- [ ] Splash asset renders without distortion
- [ ] First install: splash → onboarding → home
- [ ] Returning user: splash → home
- [ ] Light/Dark/System themes work
- [ ] Swahili/English switching works
- [ ] Navigation opens every registered tool
- [ ] Back navigation is correct
- [ ] App survives rotation/recreation where applicable

## Data
- [ ] Room data persists after restart
- [ ] Clear All Data clears supported local data
- [ ] Database migration from v2 → v3 succeeds
- [ ] No sensitive credentials are stored in app data

## PRO
- [ ] Free passport-photo limit stops at 5
- [ ] PRO users are unlimited
- [ ] PRO simulator exists only in DEBUG
- [ ] Monthly purchase opens Google Play
- [ ] Yearly purchase opens Google Play
- [ ] Purchased token is verified by backend
- [ ] Invalid/mismatched token does not grant PRO
- [ ] Expired subscription removes PRO
- [ ] Restore existing subscription works

## Billing backend
- [ ] HTTPS endpoint only
- [ ] Google service-account JSON stored only in Netlify secret variables
- [ ] Android Publisher API access is enabled
- [ ] Service account has the required Play Console permissions
- [ ] Subscription product IDs match Play Console
- [ ] Package name matches Play Console
- [ ] Acknowledgement succeeds
- [ ] HTTP errors are handled without granting PRO

## Store
- [ ] App name and icon finalized
- [ ] Privacy policy URL prepared if required
- [ ] Support/contact details prepared
- [ ] Store listing screenshots prepared
- [ ] Content rating completed
- [ ] Data safety declaration completed accurately
- [ ] Target audience declaration completed
- [ ] Pricing/subscription disclosures reviewed
