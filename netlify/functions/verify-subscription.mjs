import { google } from 'googleapis';

const PACKAGE_NAME = process.env.MSAADA_PACKAGE_NAME || 'com.aistudio.msaada.tzapp';
const ALLOWED_PRODUCTS = new Set([
  'msaada_pro_monthly',
  'msaada_pro_yearly',
]);

function json(statusCode, body) {
  return {
    statusCode,
    headers: {
      'Content-Type': 'application/json',
      'Cache-Control': 'no-store',
    },
    body: JSON.stringify(body),
  };
}

function getServiceAccountCredentials() {
  const raw = process.env.GOOGLE_PLAY_SERVICE_ACCOUNT_JSON;
  if (!raw) throw new Error('Missing GOOGLE_PLAY_SERVICE_ACCOUNT_JSON');
  return JSON.parse(raw);
}

export default async (request) => {
  if (request.method !== 'POST') return json(405, { error: 'Method not allowed' });

  try {
    const payload = await request.json();
    const purchaseToken = String(payload?.purchaseToken || '').trim();
    const productId = String(payload?.productId || '').trim();

    if (!purchaseToken || !productId) {
      return json(400, { active: false, error: 'purchaseToken and productId are required' });
    }
    if (!ALLOWED_PRODUCTS.has(productId)) {
      return json(400, { active: false, error: 'Unknown subscription product' });
    }

    const credentials = getServiceAccountCredentials();
    const auth = new google.auth.GoogleAuth({
      credentials,
      scopes: ['https://www.googleapis.com/auth/androidpublisher'],
    });
    const publisher = google.androidpublisher({ version: 'v3', auth });

    const response = await publisher.purchases.subscriptionsv2.get({
      packageName: PACKAGE_NAME,
      token: purchaseToken,
    });

    const subscription = response.data;
    const state = subscription.subscriptionState;
    const active = state === 'SUBSCRIPTION_STATE_ACTIVE' ||
      state === 'SUBSCRIPTION_STATE_IN_GRACE_PERIOD';

    const lineItem = (subscription.lineItems || []).find(
      (item) => item.productId === productId,
    );

    if (!lineItem) {
      return json(403, { active: false, error: 'Purchase product mismatch' });
    }

    // New subscription purchases must be acknowledged within three days.
    if (subscription.acknowledgementState === 'ACKNOWLEDGEMENT_STATE_PENDING') {
      await publisher.purchases.subscriptions.acknowledge({
        packageName: PACKAGE_NAME,
        subscriptionId: productId,
        token: purchaseToken,
        requestBody: {},
      });
    }

    return json(200, {
      active,
      productId,
      subscriptionState: state,
      expiryTime: lineItem.expiryTime || null,
      autoRenewEnabled: Boolean(lineItem.autoRenewingPlan?.autoRenewEnabled),
    });
  } catch (error) {
    console.error('Subscription verification failed', error?.response?.data || error);
    return json(502, { active: false, error: 'Subscription verification failed' });
  }
};
