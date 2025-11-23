# Hydroponic MQTT Flutter Java ActiveMQ

This repository now contains a Spring Boot backend and a Flutter frontend template for monitoring and controlling a hydroponic system. The backend persists telemetry, handles pH targets, broadcasts alerts, and exposes REST + SSE endpoints that the Flutter app consumes.

## How to run everything locally

### Prerequisites
- Java 17
- Maven 3.9+
- Flutter SDK 3.22+ with a working emulator/device or Chrome for web
- (Optional) Docker for running a local ActiveMQ broker

### Backend (Spring Boot)
- Location: `backend/`
- Features:
  - JWT authentication with admin/user roles and one equipment per user.
  - Telemetry ingestion and persistence for pH, temperature, luminosity, and TDS with a 30-sample buffer.
  - pH target management with live broadcasts via Server-Sent Events and severe deviation alerts.
  - Report generation that returns averages for a user-specified interval.
  - Optional action logging and a small admin console for listing users/equipment.
- Run locally with the in-memory H2 database:
  ```bash
  cd backend
  mvn spring-boot:run
  ```
  The API starts on `http://localhost:8080`. Default credentials: `admin` / `admin` (auto-provisioned with one equipment).

### ActiveMQ (optional, if you want to experiment with MQTT/STOMP)
The current Flutter client talks to the REST/SSE API only, but you can still spin up an ActiveMQ broker for device tests:

```bash
docker run -d --name activemq -p 61616:61616 -p 8161:8161 rmohr/activemq
```

- Open the admin console at `http://localhost:8161` (user/pass: `admin` / `admin`).
- Point any MQTT/STOMP devices at `tcp://localhost:61616` while relaying messages into the REST API.

### Frontend (Flutter)
- Location: `frontend/` (template from https://github.com/MarceloRobert/aws_mq_app).
- Updated to call the REST API; set the backend base URL via Dart define `API_BASE_URL` (defaults to `http://localhost:8080`).

Run on web (uses Chrome):
```bash
cd frontend
flutter pub get
flutter run -d chrome --dart-define=API_BASE_URL=http://localhost:8080
```

Run on Android emulator/device:
```bash
cd frontend
flutter pub get
flutter run -d emulator-5554 --dart-define=API_BASE_URL=http://10.0.2.2:8080
```

If you already have an APK/IPA target, the same `API_BASE_URL` Dart define applies at build time.

### Admin console
- Navigate to `http://localhost:8080/admin/users` with the admin token to list users and equipment.
- The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:hidroponic`, user: `sa`, blank password).

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

