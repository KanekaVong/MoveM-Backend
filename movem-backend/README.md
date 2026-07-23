# Movem Backend

Spring Boot backend for the Movem mobile app (fitness/trip/task tracking).

## Tech Stack
- Java / Spring Boot
- Spring Security + JWT (jjwt)
- MySQL + JPA/Hibernate

## Setup

1. Clone the repo
2. Set the following environment variables on your machine:
    - `DB_PASSWORD` — your local MySQL root password
    - `JWT_SECRET` — shared secret, ask a teammate (do not generate your own; it must match across the team)

   **Windows (PowerShell):**
```powershell
   setx DB_PASSWORD "your_local_mysql_password"
   setx JWT_SECRET "shared_secret_from_team"
```
Restart your terminal/IDE after running this.

**Mac/Linux (add to `~/.zshrc` or `~/.bashrc`):**
```bash
   export DB_PASSWORD="your_local_mysql_password"
   export JWT_SECRET="shared_secret_from_team"
```

**IntelliJ (alternative):** Run/Debug Configurations → Environment Variables → add both there.

3. Make sure MySQL is running locally and a database called `movem_db` exists (or let the app auto-create it — `createDatabaseIfNotExist=true` is already set).

4. Run the app. It should start on `http://localhost:8080`.

## API Endpoints 

- `POST /api/auth/register` — register a new user
- `POST /api/auth/login` — login, returns JWT token
- Protected routes require `Authorization: Bearer <token>` header