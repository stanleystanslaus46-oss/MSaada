# MSAADA Manual Payment + PRO Activation

## Customer payment
- Payment number: `+255742259683`
- Monthly PRO: `TSh 5,000`
- Yearly PRO: `TSh 45,000`
- Customer submits phone number + transaction ID in the app.
- Netlify sends the payment report to the configured SMS and WhatsApp destinations.

## After you verify the payment
1. Open your deployed MSAADA admin page: `/admin/`.
2. Enter your private `MSAADA_ADMIN_KEY`.
3. Enter the customer's payment phone number exactly as used for the payment.
4. Enter the verified transaction ID.
5. Select Monthly or Yearly.
6. Click **CREATE ACTIVATION CODE**.
7. Send the generated code to the customer by WhatsApp/SMS.
8. The customer opens **MSAADA PRO → Activate Code**, enters their payment phone number and code, then taps **ACTIVATE PRO**.

The activation code is one-time use and is tied to the verified payment phone number. Monthly codes expire after 30 days; yearly codes expire after 365 days.

## Netlify environment variables
Set these in Netlify Site configuration → Environment variables:

```text
MSAADA_ADMIN_KEY=use-a-long-random-secret
MSAADA_PAYMENT_NUMBER=+255742259683
MSAADA_SMS_TO=+255650039639
MSAADA_WHATSAPP_TO=+255742259683
MSAADA_ACTIVATION_API_URL=https://YOUR-NETLIFY-SITE.netlify.app/api/redeem-pro
```

Keep `MSAADA_ADMIN_KEY` private. Do not put it inside the Android app.

Existing Twilio variables remain required for automatic SMS/WhatsApp payment-report notifications:

```text
TWILIO_ACCOUNT_SID=
TWILIO_AUTH_TOKEN=
TWILIO_SMS_FROM=
TWILIO_WHATSAPP_FROM=whatsapp:
```

## Storage
Activation records are stored in **Netlify Blobs**, so no separate database is required for this first version. The code is stored as a SHA-256 hash, and redemption uses a conditional write so the same code cannot be redeemed twice concurrently.

## Important
This system does **not** decide whether a transaction ID represents a real payment. You must verify the payment first. The activation panel only creates the entitlement after your manual verification.
