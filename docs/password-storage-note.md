# Password Storage Change (Local Testing Only)

Date: 2025-11-28

Summary:
- Password hashing (BCrypt) has been removed in the controllers and the PasswordEncoder bean has been removed from the application configuration.
- Registered/added users will have their `password` stored as plain text in the `patients` and `doctors` tables.

Why:
- Requested for local convenience/testing.

Security Warning:
- Storing plain-text passwords is insecure. Anyone with database access can read user passwords.
- Do NOT use this state in production or on a public network.

Recommended next steps to restore secure behavior:
1. Re-enable `PasswordEncoder` bean and calls to `passwordEncoder.encode(...)` in `PatientController` and `DoctorController`.
2. Update login logic to use `passwordEncoder.matches(rawPassword, hashedPassword)` when authenticating.
3. Migrate existing plain-text passwords to hashed values:
   - For each user, generate a hash of the current plain password and update the DB, or require users to reset their password on next login.
4. Add an environment-based switch (e.g., `APP_ALLOW_PLAIN_PASSWORDS=false`) to prevent plain-text storage except when explicitly enabled for dev.

Git instructions (suggested):
```powershell
# create a branch for this change
git checkout -b feature/plaintext-password-local
# add changes
git add .
git commit -m "feat: temporarily store raw passwords for local testing (NOT FOR PROD)"
git push -u origin feature/plaintext-password-local
```

If you want, I can prepare a second commit that restores BCrypt and adds a dev-only flag instead (recommended).
