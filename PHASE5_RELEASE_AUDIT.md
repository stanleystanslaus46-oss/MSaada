# MSAADA Phase 5 — Release Readiness

## Completed in this phase

### 1. PRO demo safety
- The offline PRO simulator switch is now compiled into the UI only for `BuildConfig.DEBUG` builds.
- Release builds no longer expose a visible developer/demo PRO switch.
- Real Google Play Billing is intentionally not added yet; the existing `isProUser` state remains a development/local entitlement mechanism until billing is implemented.

### 2. Registry regression tests
Added `ToolRegistryTest.kt` covering:
- unique tool IDs
- unique navigation routes
- valid category references
- required English/Swahili metadata
- non-empty route/tool identifiers

### 3. Release documentation
- Added this release-readiness audit.
- Existing Phase 1/2 and Phase 4 audits remain unchanged.

## Still required on a developer machine

1. Open the project in Android Studio and allow Gradle sync.
2. Run unit tests: `./gradlew test`.
3. Build debug APK and install on a physical Android phone.
4. Test camera/gallery, image crop, QR generation, PDF/document generation and share flows.
5. Test Room persistence after app restart and after upgrade from the previous database version.
6. Test Light/Dark/System themes and small-screen layouts.
7. Test free/PRO limits and all save/delete flows.
8. Remove or disable any remaining demo-only behavior before production.
9. Configure a production upload key and generate a signed AAB when ready for Play Console.
10. Add real Google Play Billing before selling PRO subscriptions through Google Play.

## Known product limitations

- The current PRO entitlement is local/demo state, not a secure purchase entitlement.
- Some Phase 1 tools are intentionally lightweight and do not yet persist their data.
- The Bible sample content should be reviewed for licensing/permission before public release.
- Full Android compilation was not possible in this environment because the complete Gradle distribution/dependency cache is unavailable.
