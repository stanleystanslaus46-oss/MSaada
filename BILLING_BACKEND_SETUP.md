# MSAADA PRO Billing Backend — Phase 8

This folder contains a Netlify Functions verification endpoint for Google Play subscriptions.

## Required Netlify environment variables

- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` — the complete Google Play Developer API service-account JSON. Never commit this file or value to GitHub.
- `MSAADA_PACKAGE_NAME` — optional; defaults to `com.aistudio.msaada.tzapp`.

## Google Play setup

1. Create a Google Cloud service account dedicated to Play Developer API access.
2. Enable the Google Play Android Publisher API.
3. In Play Console, invite the service-account email and grant only the permissions required to manage subscription purchases/entitlements.
4. Create the `msaada_pro_monthly` and `msaada_pro_yearly` subscriptions in Play Console with the desired Tanzania pricing.
5. Deploy this project to Netlify and set the environment variables in the Netlify dashboard.
6. Set the Android `MSAADA_BILLING_API_URL` to the deployed `/api/verify-subscription` endpoint.

The endpoint uses `purchases.subscriptionsv2.get` as the source of truth, rejects unknown product IDs, checks the returned line item, and acknowledges pending subscription purchases after verification.
