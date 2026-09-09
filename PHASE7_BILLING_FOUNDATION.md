# MSAADA — Phase 7 Billing Foundation

## What was added
- Google Play Billing Library **9.1.0**.
- `MsaadaBillingManager` with:
  - automatic Play Billing reconnection
  - monthly and yearly subscription product IDs
  - product detail querying
  - existing subscription querying
  - purchase-flow launching
  - purchase-state handling
  - explicit `PurchaseNeedsVerification` state

## Product IDs
- `msaada_pro_monthly`
- `msaada_pro_yearly`

Configure these as subscription products/base plans in Google Play Console before testing a real purchase.

## Security decision
The app **does not unlock MSAADA PRO solely from a client-side purchase callback**. Google recommends verifying purchase tokens on a secure backend before granting entitlements. This is intentionally left as the next server-side step.

The existing DEBUG-only PRO simulator remains available for development. It must not be used as a production entitlement mechanism.

## Required before production billing
1. Create the Play Console subscription product/base plans.
2. Create a secure backend endpoint for purchase-token verification using Google Play Developer APIs.
3. Store verified entitlement state server-side.
4. Add account/device association and restore-purchase flow.
5. Handle active, grace period, on hold, paused, cancelled and expired subscription states.
6. Add acknowledgement/processing after verification.
7. Test with Play Console license testers and internal testing.
8. Remove/disable developer entitlement controls from production UI.

## Build note
This environment cannot perform a complete Android/Gradle build because the required Gradle wrapper/dependency artifacts are not available offline. Open the project in Android Studio, sync Gradle, and resolve any dependency/API warnings there before creating an AAB.
