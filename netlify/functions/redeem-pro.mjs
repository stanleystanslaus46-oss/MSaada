import { json, store, normalizePhone, hashCode } from './activation-utils.mjs';

export default async (event) => {
  if (event.httpMethod !== 'POST') return json({ message: 'Method not allowed.' }, 405);
  try {
    const body = JSON.parse(event.body || '{}');
    const code = String(body.code || '').trim().toUpperCase();
    const customerPhone = normalizePhone(body.customerPhone);
    if (!/^MSD-[A-Z0-9]{4}-[A-Z0-9]{4}$/.test(code)) return json({ active: false, message: 'Invalid activation code.' }, 400);
    if (!/^255\d{9}$/.test(customerPhone)) return json({ active: false, message: 'Invalid phone number.' }, 400);

    const key = hashCode(code);
    const entry = await store.getWithMetadata(key);
    if (!entry) return json({ active: false, message: 'Activation code not found.' }, 404);
    const record = JSON.parse(entry.data);
    if (record.status !== 'unused') return json({ active: false, message: 'This activation code has already been used.' }, 409);
    if (record.customerPhone !== customerPhone) return json({ active: false, message: 'Phone number does not match the payment record.' }, 403);
    if (Date.parse(record.expiryTime) <= Date.now()) return json({ active: false, message: 'This activation code has expired.' }, 410);

    const redeemed = { ...record, status: 'redeemed', redeemedAt: new Date().toISOString() };
    const write = await store.setJSON(key, redeemed, { onlyIfMatch: entry.etag });
    if (!write.modified) return json({ active: false, message: 'This activation code was just used.' }, 409);
    return json({ active: true, plan: record.plan, expiryTime: record.expiryTime, message: 'PRO activated successfully.' });
  } catch (error) {
    return json({ active: false, message: 'Activation service is temporarily unavailable.' }, 500);
  }
};
