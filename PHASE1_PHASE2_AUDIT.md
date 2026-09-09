# MSAADA Phase 1 + Phase 2 Audit / Change Log

This project was modified directly from the uploaded `msaada.zip` source.

## Phase 1 — Functional routing repaired

The following routes no longer open unrelated placeholder screens:

- Home Expense Tracker
- Study Timer
- Travel Checklist
- Trip Cost Splitter
- Emergency Contacts
- Basic First Aid
- Medicine Reminder
- Farm Task Planner
- Farm Profit Calculator
- Planting Planner
- Password Generator
- Text Counter
- Developer Utilities (JSON/Base64/Color)

All routes declared in `ToolRegistry` now have a matching `NavHost` destination.

## Phase 2 — Business logic hardened

- RisitiSafe: validation for business/item data, positive quantities, non-negative prices and payment amount; retains the 5-receipt free limit.
- Umeme: saving meter history is PRO-gated; invalid/non-positive readings and unit price are rejected; tariff/levy UI is clearly presented as an estimate.
- Msaidizi wa Fomu: saving profiles is PRO-gated and required fields must be completed.
- Mkataba Rahisi: saving contracts is PRO-gated and required fields must be completed.
- Kikoba: saving groups is PRO-gated; group/contribution/social-fund validation added; social-fund summary double-counting corrected.
- Biashara Calculator: saving products is PRO-gated; numeric inputs are sanitized; fixed-cost input added; break-even quantity now uses fixed costs / contribution margin instead of the previous misleading formula.
- Biashara product entity now stores fixed costs.

## Data safety

- Room database upgraded from version 2 to 3.
- Added a 2 -> 3 migration for `biashara_products.fixedCosts`.
- Removed destructive migration fallback.
- New installations and cleared preferences default to Light mode.

## Splash screen

- Added the supplied MSAADA splash artwork as `res/drawable/msaada_splash_screen.png`.
- SplashScreen now displays that exact asset with `ContentScale.Fit` and no reconstructed logo/text overlay.
- Existing splash -> onboarding/home navigation flow is preserved.

## Verification performed

- All ToolRegistry routes have matching NavHost routes.
- New functional screen source was parser-checked for syntax/type issues that can be detected without Android/Compose classpaths.
- Archive/source integrity checked after modifications.

## Build limitation

The uploaded project does not contain the Gradle wrapper executable/JAR and this environment does not have a configured Android Gradle installation, so a complete Android APK build cannot be executed here. Android Studio should perform Gradle sync and compile the project on the user's machine.

## Phase 2 hardening pass — continued

- Fixed Settings "Clear all data" so it also removes receipts, Umeme history, saved forms, saved contracts, Kikoba groups and Biashara products.
- Fixed RisitiSafe so malformed numeric payment input is rejected instead of silently being treated as a full payment; receipt number/date are also required before saving.
- Fixed Umeme input parsing so malformed/blank readings and unit prices are rejected instead of silently falling back to defaults.
- Fixed Biashara saved `breakEvenPrice`: it now stores a price, not the break-even quantity. The live break-even quantity remains calculated from fixed costs / contribution margin.
- Fixed Kikoba validation so the member count is explicitly 2–30, preventing the calculated group size from disagreeing with the generated member roster.
