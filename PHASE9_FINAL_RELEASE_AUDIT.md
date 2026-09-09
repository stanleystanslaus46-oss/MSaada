# MSAADA — Phase 9 Final Release Audit

## Scope
Production hardening of the Google Play subscription verification path and release configuration.

## Completed
- Billing verification endpoint is injected from `MSAADA_BILLING_API_URL` at build time.
- The app refuses non-HTTPS billing verification endpoints.
- HTTP redirects are disabled for the verification request.
- ISO-8601 subscription expiry parsing uses `java.time.Instant`, accepting standard Google timestamp variants.
- Google Play purchase verification remains backend-authoritative; the Android callback alone never grants PRO.
- Release signing remains external to the repository.
- No service-account credentials are stored in Android source/assets.
- Developer PRO simulator remains DEBUG-only.

## Required before publishing
1. Deploy `netlify/functions/verify-subscription.mjs`.
2. Configure Netlify `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` as a secret.
3. Configure `MSAADA_PACKAGE_NAME` to the exact Play package if it differs from the default.
4. Build the release with `MSAADA_BILLING_API_URL` set to the deployed HTTPS verification endpoint.
5. Create and activate the `msaada_pro_monthly` and `msaada_pro_yearly` subscriptions in Play Console.
6. Add the exact release application ID `com.aistudio.msaada.tzapp` to the Play Console app.
7. Test purchase, restore, cancellation, grace period and expiry using Play test accounts.
8. Generate a signed AAB and inspect it before upload.

## Not claimed as complete
- A release APK/AAB has not been runtime-built in this environment.
- Google Play Console configuration cannot be verified from the source tree.
- Real purchase testing requires Play Console test tracks and a Google account.
- Real-time Developer Notifications (RTDN) are not implemented yet.

## Release rule
Do not advertise MSAADA PRO as fully production-ready until the required Play Console and backend steps above have been completed and tested on a physical Android device.
