import crypto from 'node:crypto';
import { getStore } from '@netlify/blobs';

export const store = getStore({ name: 'msaada-pro-activations', consistency: 'strong' });

export function json(body, status = 200) {
  return {
    statusCode: status,
    headers: { 'Content-Type': 'application/json', 'Cache-Control': 'no-store' },
    body: JSON.stringify(body),
  };
}

export function normalizePhone(value = '') {
  const raw = String(value).trim().replace(/[\s()-]/g, '');
  if (raw.startsWith('+255')) return `255${raw.slice(4)}`;
  if (raw.startsWith('255')) return raw;
  if (raw.startsWith('0')) return `255${raw.slice(1)}`;
  return raw;
}

export function hashCode(code) {
  return crypto.createHash('sha256').update(String(code).trim().toUpperCase()).digest('hex');
}

export function generateCode() {
  const alphabet = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  const bytes = crypto.randomBytes(8);
  let a = '';
  for (const b of bytes) a += alphabet[b % alphabet.length];
  return `MSD-${a.slice(0, 4)}-${a.slice(4)}`;
}

export function requireAdmin(event) {
  const expected = process.env.MSAADA_ADMIN_KEY?.trim();
  const supplied = event.headers?.['x-msaada-admin-key'] || event.headers?.['X-Msaada-Admin-Key'];
  if (!expected || !supplied || supplied !== expected) throw new Error('Unauthorized');
}

export function expiryForPlan(plan) {
  const days = plan === 'yearly' ? 365 : 30;
  return new Date(Date.now() + days * 24 * 60 * 60 * 1000).toISOString();
}
