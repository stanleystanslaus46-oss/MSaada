import { json, store, normalizePhone, hashCode, generateCode, requireAdmin, expiryForPlan } from './activation-utils.mjs';

export default async (event) => {
  if (event.httpMethod !== 'POST') return json({ message: 'Method not allowed.' }, 405);
  try {
    requireAdmin(event);
    const body = JSON.parse(event.body || '{}');
    const customerPhone = normalizePhone(body.customerPhone);
    const transactionId = String(body.transactionId || '').trim();
    const plan = body.plan === 'yearly' ? 'yearly' : body.plan === 'monthly' ? 'monthly' : '';
    const amount = Number(body.amount);
    if (!/^255\d{9}$/.test(customerPhone)) return json({ message: 'Invalid customer phone number.' }, 400);
    if (transactionId.length < 4 || transactionId.length > 100) return json({ message: 'Invalid transaction ID.' }, 400);
    if (!plan || ![5000, 45000].includes(amount)) return json({ message: 'Invalid plan or amount.' }, 400);

    const code = generateCode();
    const codeHash = hashCode(code);
    const expiryTime = expiryForPlan(plan);
    const record = {
      codeHash,
      customerPhone,
      transactionId,
      plan,
      amount,
      currency: 'TZS',
      createdAt: new Date().toISOString(),
      expiryTime,
      redeemedAt: null,
      status: 'unused',
    };
    await store.setJSON(codeHash, record);
    return json({ ok: true, code, plan, amount, expiryTime });
  } catch (error) {
    if (error.message === 'Unauthorized') return json({ message: 'Unauthorized.' }, 401);
    return json({ message: 'Could not create activation code.' }, 500);
  }
};
