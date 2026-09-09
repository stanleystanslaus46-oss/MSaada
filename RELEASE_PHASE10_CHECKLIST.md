# MSAADA — Phase 10 Release Checklist

## Before Android Studio build
- [ ] Open the project root in Android Studio
- [ ] Allow Gradle to sync and download dependencies
- [ ] Confirm `com.aistudio.msaada.tzapp` is the intended application ID
- [ ] Confirm versionCode/versionName
- [ ] Set `MSAADA_BILLING_API_URL` to the deployed HTTPS verification endpoint
- [ ] Keep service-account JSON out of the Android project

## Build validation
- [ ] Debug build succeeds
- [ ] Unit tests succeed
- [ ] Release build succeeds
- [ ] Signed AAB is generated
- [ ] AAB is inspected before upload
- [ ] No debug-only PRO simulator is present in release UI

## Device validation
- [ ] Install debug build on a real device
- [ ] Complete smoke-test matrix in `PHASE10_REAL_DEVICE_QA.md`
- [ ] Test small-screen layout
- [ ] Test rotation where supported
- [ ] Test dark/system theme
- [ ] Test offline behavior

## Play Console
- [ ] Create app entry
- [ ] Configure subscription products
- [ ] Configure base plans/pricing
- [ ] Add test accounts
- [ ] Upload AAB to internal testing
- [ ] Install from Play test track
- [ ] Test purchase and restore
- [ ] Test cancellation/expiry behavior

## Final release
- [ ] Privacy policy URL prepared
- [ ] Store listing prepared
- [ ] App icon and screenshots prepared
- [ ] Content rating completed
- [ ] Data safety form completed accurately
- [ ] Production signing key backed up securely
- [ ] Release AAB archived securely
