# MSAADA PRO Admin Activation

The folder `admin/` contains a small admin page for generating one-time PRO activation codes.

Deploy the project to Netlify, then open:

`https://YOUR-NETLIFY-SITE.netlify.app/admin/`

The admin page calls `/api/admin/create-activation` and never contains the admin secret in source code.

### Recommended admin key
Generate a long random value, for example with PowerShell:

```powershell
[guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N')
```

Store that value only as the Netlify environment variable:

`MSAADA_ADMIN_KEY`

### Activation flow

`Payment report → manual verification → admin creates code → customer redeems code → PRO activated`

Do not treat a submitted transaction ID as proof of payment.
