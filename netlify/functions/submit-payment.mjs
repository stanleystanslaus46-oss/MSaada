const SMS_TO = process.env.MSAADA_SMS_TO || "+255650039639";
const WHATSAPP_TO = process.env.MSAADA_WHATSAPP_TO || "+255742259683";

function basicAuth(username, password) {
  return "Basic " + Buffer.from(`${username}:${password}`).toString("base64");
}

async function sendTwilioMessage({ to, from, body }) {
  const accountSid = process.env.TWILIO_ACCOUNT_SID;
  const authToken = process.env.TWILIO_AUTH_TOKEN;
  if (!accountSid || !authToken || !from || !to) {
    throw new Error("Twilio notification configuration is incomplete.");
  }

  const url = `https://api.twilio.com/2010-04-01/Accounts/${accountSid}/Messages.json`;
  const form = new URLSearchParams({ To: to, From: from, Body: body });

  const response = await fetch(url, {
    method: "POST",
    headers: {
      Authorization: basicAuth(accountSid, authToken),
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: form,
  });

  const text = await response.text();
  if (!response.ok) throw new Error(`Twilio HTTP ${response.status}: ${text}`);
  return JSON.parse(text);
}

export default async (req) => {
  if (req.method !== "POST") {
    return new Response(JSON.stringify({ error: "Method not allowed" }), {
      status: 405,
      headers: { "Content-Type": "application/json" },
    });
  }

  let data;
  try {
    data = await req.json();
  } catch {
    return new Response(JSON.stringify({ error: "Invalid JSON" }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }

  const { reference, plan, amount, currency, customerPhone, transactionId, appVersion } = data;
  if (!reference || !plan || !customerPhone || !transactionId) {
    return new Response(JSON.stringify({ error: "Missing required payment fields" }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }

  const expectedAmount = plan === "yearly" ? 45000 : 5000;
  if (Number(amount) !== expectedAmount || currency !== "TZS") {
    return new Response(JSON.stringify({ error: "Invalid plan amount" }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }

  const message =
`MSAADA PRO PAYMENT REPORT
Reference: ${reference}
Plan: ${plan}
Amount: TSh ${Number(amount).toLocaleString("en-TZ")}
Customer phone: ${customerPhone}
Transaction ID: ${transactionId}
App version: ${appVersion || "unknown"}

STATUS: Payment reported by customer. Please verify manually before activating PRO.`;

  const results = { sms: false, whatsapp: false };

  try {
    const smsFrom = process.env.TWILIO_SMS_FROM;
    await sendTwilioMessage({ to: SMS_TO, from: smsFrom, body: message });
    results.sms = true;
  } catch (error) {
    console.error("SMS notification failed:", error);
  }

  try {
    const whatsappFrom = process.env.TWILIO_WHATSAPP_FROM;
    const whatsappBody = message;
    await sendTwilioMessage({
      to: `whatsapp:${WHATSAPP_TO}`,
      from: whatsappFrom?.startsWith("whatsapp:") ? whatsappFrom : `whatsapp:${whatsappFrom || ""}`,
      body: whatsappBody,
    });
    results.whatsapp = true;
  } catch (error) {
    console.error("WhatsApp notification failed:", error);
  }

  if (!results.sms && !results.whatsapp) {
    return new Response(JSON.stringify({
      error: "Payment report was received but notifications could not be sent.",
    }), {
      status: 502,
      headers: { "Content-Type": "application/json" },
    });
  }

  return new Response(JSON.stringify({
    ok: true,
    reference,
    notifications: results,
  }), {
    status: 200,
    headers: { "Content-Type": "application/json" },
  });
};
