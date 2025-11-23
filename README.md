# Hydroponic MQTT Flutter Java ActiveMQ

This repository now contains a Spring Boot backend and a Flutter frontend template for monitoring and controlling a hydroponic system. The backend persists telemetry, handles pH targets, broadcasts alerts, and exposes REST + SSE endpoints that the Flutter app consumes.

## Backend (Spring Boot)
- Location: `backend/`
- Features:
  - JWT authentication with admin/user roles and one equipment per user.
  - Telemetry ingestion and persistence for pH, temperature, luminosity, and TDS with a 30-sample buffer.
  - pH target management with live broadcasts via Server-Sent Events and severe deviation alerts.
  - Report generation that returns averages for a user-specified interval.
  - Optional action logging and a small admin console for listing users/equipment.
- Run locally:
  ```bash
  cd backend
  mvn spring-boot:run
  ```
  Default credentials: `admin` / `admin` (auto-provisioned with one equipment).

## Frontend (Flutter)
- Location: `frontend/` (template from https://github.com/MarceloRobert/aws_mq_app).
- Updated to call the new REST API instead of ActiveMQ/STOMP while keeping the existing look and feel.
- Set the API base URL via Dart define `API_BASE_URL` (defaults to `http://localhost:8080`).
- Key flows:
  - Login -> stores JWT and equipment binding.
  - Home -> polls telemetry and listens to SSE alerts/target changes.
  - Targets -> updates pH min/max.
  - Reports -> requests averages for an ISO-8601 time window.

## Downloading the project locally
Clone the repository (includes both backend and frontend):

```bash
git clone https://github.com/<your-org>/hidroponic-MQTT-flutter-java-activeMQ.git
```

If you only need an archive without Git history, run from a machine with Git installed:

```bash
git archive --format zip --output hidroponic-hydroponic.zip HEAD
```

Alternatively, from this repository, you can generate a ZIP with the helper script:

```bash
scripts/create-archive.sh
```

The script creates `../hidroponic-project.zip` by default (or accepts a custom path as its first argument) without adding the archive to git.

Copy the resulting `hidroponic-hydroponic.zip` to your local machine (for example with `scp` from a remote server) and unzip it to get the backend and frontend folders.

## Notes about pull requests
Some hosting providers reject PRs that contain binary blobs. To keep diffs text-only, the sample UI screenshots have been removed and added to `.gitignore`. If you add new images (icons are fine), prefer SVG where possible so that future PRs remain compatible with platforms that block binary attachments.

