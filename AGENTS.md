# AGENTS.md — OpenNext Android

## Overview

OpenNext is the Android companion to the OpenNOW GeForce NOW client. It is a Kotlin/Jetpack Compose Android app that connects to NVIDIA's GFN servers for cloud gaming.

**Target platforms:** Phone and Tablet
**Min SDK:** 26 (Android 8.0)
**Target SDK:** 35
**Language:** Kotlin 2.0
**UI:** Jetpack Compose + Material 3

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Networking | Ktor Client (OkHttp engine) |
| Serialization | Kotlinx Serialization |
| Local Storage | Room (DB) + DataStore (prefs) |
| Streaming | WebRTC Android SDK |
| Async | Kotlin Coroutines + Flow |
| Navigation | Compose Navigation |
| Auth | Chrome Custom Tabs |
| Image Loading | Coil |

## Module Structure

```
android/
├── app/                              # Main Android app module
│   └── src/main/java/com/opennext/app/
│       ├── OpenNextApp.kt            # Application class (Hilt)
│       ├── MainActivity.kt           # Single activity
│       ├── di/                       # Hilt modules
│       └── ui/
│           ├── theme/                # Color, Type, Theme
│           ├── navigation/           # Screen routes, NavHost
│           └── screens/              # All screens
├── .github/workflows/
│   └── build.yml                     # CI/CD pipeline
├── build.gradle.kts                  # Root build file
├── settings.gradle.kts               # Project settings
├── gradle/libs.versions.toml         # Version catalog
└── AGENTS.md                         # This file
```

## Design Language

- **Dark background:** #0A0A0F
- **Surface:** #111118
- **Card:** #16161F
- **Blue accent:** #1A73E8 (primary)
- **Player green:** #76B947 (play button)
- **On-dark text:** #E8E8ED
- **On-dark muted:** #9898A6
- **Border radius:** 12-16dp
- **Gamey aesthetic:** Bold typography, hero banners, horizontal game rows

## Adaptive Layout Rules

| Behavior | Phone (<600dp) | Tablet (≥600dp) |
|---|---|---|
| Game grid | Fluid 2-3 columns | Fluid 3-5 columns |
| Navigation | Bottom nav bar | Bottom nav bar |
| Game detail | Push new screen | Split view (list + detail) |
| Stream view | Fullscreen + overlay | Fullscreen + overlay |
| Settings | Single column | Single column |

## Screen Inventory

| Screen | Route | Description |
|---|---|---|
| Login | `login` | Single sign-in button |
| Home | `home` | Hero banner + horizontal game rows |
| Library | `library` | Owned games grid with filters |
| Game Detail | `game/{gameId}` | Full game info + play button |
| Settings | `settings` | Stream, audio, about sections |
| Stream Loading | `stream/loading/{gameId}` | Queue position + step indicator |
| Stream View | `stream/{gameId}` | WebRTC stream + overlay controls |

## GFN API Reference

### Authentication
- **Auth endpoint:** `https://login.nvidia.com/authorize`
- **Token endpoint:** `https://login.nvidia.com/token`
- **Client ID:** `ZU7sPN-miLujMD95LfOQ453IB0AtjM8sMyvgJ9wCXEQ`
- **Scopes:** `openid consent email tk_client age`
- **Flow:** OAuth2 Authorization Code + PKCE (S256)
- **Service URLs:** `https://pcs.geforcenow.com/v1/serviceUrls`

### Client Headers
```
Authorization: "GFNJWT <token>"
nv-client-id: "ec7e38d4-03af-4b58-b131-cfb0495903ab"
nv-client-type: "NATIVE"
nv-client-version: "2.0.80.173"
nv-client-streamer: "WEBRTC"
nv-device-os: "WINDOWS"
nv-device-type: "DESKTOP"
```

### Game Catalog
- **GraphQL endpoint:** `https://games.geforcenow.com/graphql`
- **Panels query hash:** `f8e26265a5db5c20e1334a6872cf04b6e3970507697f6ae55a6ddefa5420daf0`
- **VPC ID:** Resolved via `<streamingServiceUrl>/v2/serverInfo`

### Session Management (CloudMatch)
- **Base URL:** `<streamingBaseUrl>/v2/session`
- **Create:** `POST /v2/session?keyboardLayout=en-US&languageCode=en_US`
- **Poll:** `GET /v2/session/{sessionId}`
- **Stop:** `DELETE /v2/session/{sessionId}`
- **Status codes:** 1=Queue, 2=Ready, 3=Streaming, 4-5=Error, 6=Cleanup

### WebRTC Signaling
- **WebSocket:** `wss://<host>/nvst/sign_in?peer_id=<name>&version=2&peer_role=1&pairing_id=<sessionId>`
- **Protocol:** `x-nv-sessionid.<sessionId>`
- **Heartbeat:** `{ hb: 1 }` every 5s
- **SDP:** Server sends offer → Client sends answer + nvstSdp
- **ICE:** Server is ice-lite; inject `mediaConnectionInfo` as manual candidate

### Input Protocol (Binary)
- **Keyboard:** 18 bytes [type 4B LE][keycode 2B BE][modifiers 2B BE][scancode 2B BE][timestamp 8B BE]
- **Mouse:** 22 bytes [type 4B LE][dx 2B BE][dy 2B BE][reserved 6B BE][timestamp 8B BE]
- **Gamepad:** 38 bytes [type 4B LE][payload_size 2B LE][index 2B LE][bitmap 2B LE][inner_size 2B LE][buttons 2B LE][triggers 2B LE][lx 2B LE][ly 2B LE][rx 2B LE][ry 2B LE][reserved 2B LE][magic 2B LE][reserved 2B LE][timestamp 8B LE]
- **Protocol v3+ framing:** `[0x23][8B timestamp][0x22][payload]`

## CI/CD

- **Pipeline:** `.github/workflows/build.yml`
- **Triggers:** Push/PR to `main`, `dev`, or `feature/**` when `android/**` changes
- **Jobs:** Lint → Build → Test → Release (main branch only)
- **Artifacts:** Debug APK, Release APK, Release AAB
- **Release:** Auto-creates GitHub Release on main branch pushes

## Checks

- Run `./gradlew lint` for code style
- Run `./gradlew assembleDebug` to verify build
- Run `./gradlew testDebugUnitTest` for unit tests

## Build Order (UI-first)

| Phase | Screens | Status |
|---|---|---|
| Phase 1 | Project scaffold, theme, navigation | Done |
| Phase 2 | Login screen | Done (placeholder) |
| Phase 3 | Home + Game cards | Done (placeholder) |
| Phase 4 | Library | Done (placeholder) |
| Phase 5 | Game Detail | Done (placeholder) |
| Phase 6 | Settings | Done (placeholder) |
| Phase 7 | Stream Loading | Done (placeholder) |
| Phase 8 | Stream View | Done (placeholder) |
| Phase 9 | API integration | Pending |
| Phase 10 | WebRTC streaming | Pending |
| Phase 11 | Input protocol | Pending |
| Phase 12 | Polish & animations | Pending |
