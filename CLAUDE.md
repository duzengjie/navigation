# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Navigation is a full-stack web application for managing URL bookmarks organized by environments (e.g., dev, staging, prod). It provides a card-based UI where users can create, edit, delete, and access URLs grouped by environment tabs.

**Tech Stack:**

- **Backend:** Spring Boot 3.5.11 (Java 21) + MyBatis Plus + MySQL
- **Frontend:** Vue 3 + Vite 4 + Element Plus
- **Package Manager:** pnpm (frontend), Maven (backend)

## Commands

### Backend (from project root)

```bash
# Build backend
mvn clean package

# Run backend (after build)
java -jar navigation.jar

# Run backend in development
mvn spring-boot:run
```

### Frontend (from `front/` directory)

```bash
# Install dependencies
pnpm install

# Development server (proxies to backend at localhost:8888)
pnpm run dev

# Build for production
pnpm run build

# Preview production build
pnpm run preview
```

### Full Build (production deployment)

Run `PackageAll.main` test class which:
1. Builds frontend with `pnpm run build`
2. Copies frontend dist to Spring Boot resources (`templates/back/` and `static/`)
3. Packages everything into `navigation.jar`

## Architecture

### Backend Structure

```
src/main/java/com/duzj/navigation/
├── NavigationApplication.java    # Entry point
├── config/                       # MyBatis Plus, CORS config
├── controller/                   # REST endpoints
│   ├── EnvironmentInfoController # /env/api/* - environment CRUD, backup/restore
│   ├── UrlInfoController         # /url/api/* - URL CRUD with change logging
│   └── PageController            # Serves frontend pages
├── service/                      # Business logic
├── mapper/                       # MyBatis Plus mappers
├── entity/                       # Domain models
│   ├── EnvironmentInfo           # Environment (tab)
│   ├── UrlInfo                   # URL bookmark
│   ├── UrlInfoChangeLog          # Change history for deletions/updates
│   ├── base/ResultDTO            # Standard API response wrapper
│   ├── dto/                      # Data transfer objects
│   ├── request/                  # Request bodies
│   └── response/                 # Response bodies
├── beanmapper/                   # MapStruct mappers
├── aspect/                       # AOP aspects (HTTP logging)
├── exceptions/                   # Exception handling
└── excel/                        # Apache Fesod import/export handlers
```

**Key Backend Patterns:**
- All changes to `UrlInfo` (update/delete) are logged to `UrlInfoChangeLog` for audit trail
- `EnvironmentInfoService.downloadAllByExcel()` exports all data for backup
- `EnvironmentInfoService.backupRecoverByExcel()` clears and restores from Excel upload
- Uses Jakarta EE (Spring Boot 3.x) instead of javax

### Frontend Structure

```
front/src/
├── main.js           # Vue app entry, registers Element Plus
├── App.vue           # Root component, renders Home
├── components/
│   ├── Home.vue      # Main view with environment tabs, CRUD dialogs
│   └── Card.vue      # Individual URL card with edit/delete actions
└── http/
    └── request.js    # Axios wrapper with interceptors
```

**Frontend-Backend Communication:**

- Dev: Vite proxy `/navigation` -> `http://localhost:8888`
- Prod: Frontend served by Spring Boot at port 8888
- Context path: `/navigation` (all API routes prefixed)
- Environment config via `.env.development` and `.env.production`

### Database Schema

Three tables (see `sql/init.sql`):
- `environment_info` - Environment names (displayed as tabs)
- `url_info` - URLs linked to environments via `environment_id`
- `url_info_change_log` - Audit trail for URL changes

## Configuration

### Backend (`src/main/resources/`)

- `application.yml` - Main config, activates profile via `spring.profiles.active`
- `application-mac.yml`, `application-win.yml`, `application-debian.yml` - Environment-specific DB configs
- Server port: 8888
- Context path: `/navigation`
- Uses Log4j2 (default logging excluded)

### Frontend Environment Variables
- `VITE_SERVER_URL` - Backend API base URL
- `VITE_REQUEST_TIMEOUT` - Request timeout (20000ms default)
- `VITE_BASE_PATH` - Base path for static assets

## Deployment

1. Initialize MySQL database: `mysql < sql/init.sql`
2. Configure database connection in appropriate `application-{profile}.yml`
3. Run `PackageAll.main` to build full package
4. Deploy: `java -jar navigation.jar`
