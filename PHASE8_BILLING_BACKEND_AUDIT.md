# MSAADA Phase 8 — Billing Verification Backend Audit

## Implemented
- Netlify Function `verify-subscription`.
- Google Play Developer API `purchases.subscriptionsv2.get` verification.
- Product allow-list for monthly/yearly MSAADA PRO subscriptions.
- Subscription line-item product matching.
- Active + grace-period entitlement handling.
- Server-side acknowledgement for pending subscription purchases.
- Android client sends purchase tokens to the secure endpoint before applying PRO entitlement.
- Android PRO screen now launches the actual Google Play subscription flow instead of locally activating PRO.
- Debug-only PRO simulator remains available for development/review builds.

## Required before production
- Create/configure the two subscriptions in Google Play Console.
- Create a least-privilege service account and grant Play Console API access.
- Enable the Android Publisher API in Google Cloud.
- Set `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` in Netlify; never commit it.
- Set `MSAADA_BILLING_API_URL` in the Android release build to the deployed HTTPS endpoint.
- Test with Play Console license testers and an internal testing track.
- Configure subscription base plans/offers and Tanzania pricing in Play Console.

## Important limitation
This phase intentionally does not add user accounts. The purchase token is the authoritative Google Play proof. Local entitlement is refreshed from verified purchases when the app reconnects to Google Play.
