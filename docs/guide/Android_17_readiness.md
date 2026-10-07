# Android 17 (API 37) readiness

This document records the Android 17 (API level 37) readiness work for InstantSearch Android:
the toolchain bump, the dependency updates, and the behavior-change audit required by the
[Android 17 migration guide](https://developer.android.com/about/versions/17/migration).

InstantSearch Android is a **library**: it ships no `Activity`, `Service`, `BroadcastReceiver`,
widget, or notification, and it only talks to Algolia's public HTTPS endpoints. Most Android 17
behavior changes therefore apply to the *host application* rather than to the library itself.
Where an item is "not applicable" below, the reason is given so that app teams can re-check it
against their own code.

## Table of Contents

- [Toolchain and build](#toolchain-and-build)
- [Dependency updates](#dependency-updates)
- [Behavior changes for apps targeting Android 17](#behavior-changes-for-apps-targeting-android-17)
- [Behavior changes for all apps](#behavior-changes-for-all-apps)
- [Testing](#testing)
- [Impact on consumers](#impact-on-consumers)
- [Google Play target API deadline](#google-play-target-api-deadline)

## Toolchain and build

| Component                     | Before            | After                                   | Notes |
|-------------------------------|:------------------|:----------------------------------------|:------|
| `compileSdk` (all modules)    | 35 (examples 36)  | **37**                                  | Single source of truth: `android-compileSdk` in `gradle/libs.versions.toml`. |
| `targetSdk` (example apps)    | 35                | **37**                                  | Libraries do not declare a `targetSdk`. |
| `minSdk`                      | 23                | 23                                      | Unchanged. |
| Android Gradle Plugin         | 8.7.2             | **9.3.3**                               | `compileSdk 37` requires AGP ≥ 9.1.1. |
| Gradle                        | 8.9               | **9.5.0**                               | Minimum for AGP 9.3. Runs on JDK 17 (CI) and 21. |
| Kotlin                        | 2.2.0             | **2.4.20**                              | Fully supports Gradle 9.5 / AGP 9.3. |
| Compose compiler plugin       | 2.2.0             | **2.4.20**                              | Follows the Kotlin version. |
| SDK Platform                  | android-35        | **android-37.0**                        | Auto-downloaded by AGP (`android.builder.sdkDownload`). |
| Build-Tools                   | 34/36.1           | 36.0.0 (AGP 9.3 default)                | AGP 9.3 does not require Build-Tools 37.x; not pinned. |
| JVM bytecode (Android targets)| 1.8               | **11**                                  | Current AndroidX (`core`, `appcompat`, `work`, …) ship Java 11 bytecode and inline functions; 1.8 no longer compiles. Pure-JVM targets (`instantsearch-core`, `coroutines-extensions`, the `jvm()` targets) stay on 1.8. |
| Android Studio                | Otter (2025.2)    | **Panda 3 (2025.3.3 Patch 1) or newer** | Required to open an AGP 9.3 / API 37 project. Not a repository change. |

### AGP 9 migration performed

AGP 9 removes the legacy DSL and the `kotlin-android` plugin path, and no longer allows
`org.jetbrains.kotlin.multiplatform` together with `com.android.library` in the same module.
The repository was migrated instead of using the temporary `android.newDsl=false` /
`android.builtInKotlin=false` opt-outs (which are removed in AGP 10):

- KMP modules (`instantsearch`, `instantsearch-insights`, `instantsearch-utils`,
  `instantsearch-agent`) now use `com.android.kotlin.multiplatform.library` with the
  `kotlin { android { … } }` DSL. Robolectric unit tests moved from `src/androidUnitTest` to
  `src/androidHostTest` and run through `testAndroidHostTest`.
- Android-only modules (`instantsearch-compose`, `extensions/android-paging3`,
  `extensions/android-loading`) and the example apps use AGP's built-in Kotlin
  (`kotlin { compilerOptions { … } }`); the `kotlin-android` plugin was removed.
- `android.experimental.lint.version` override removed (AGP 9.3 Lint supports Kotlin 2.4).

## Dependency updates

| Dependency                              | Before  | After   | Notes |
|-----------------------------------------|:--------|:--------|:------|
| `androidx.compose.ui` / `material`      | 1.10.0 / 1.3.1 | 1.12.1 | Compose 1.12 *requires* `compileSdk 37` + AGP 9. |
| `androidx.compose.material:material-icons-*` | 1.3.1 | 1.7.8 | Icons are frozen at 1.7.x and are no longer a transitive dependency of `material`; `material-icons-core` is now declared explicitly by `instantsearch-compose`. |
| `androidx.core:core-ktx`                | 1.9.0   | 1.19.1  | |
| `androidx.appcompat`                    | 1.6.1   | 1.8.0   | |
| `androidx.recyclerview`                 | 1.2.1   | 1.4.0   | |
| `androidx.swiperefreshlayout`           | 1.1.0   | 1.2.0   | |
| `androidx.paging:paging-runtime`        | 3.1.1   | 3.5.1   | Example app migrated off the removed `items(LazyPagingItems)` helper. |
| `androidx.work`                         | 2.11.0  | 2.11.2  | WorkManager 2.12+ requires `minSdk 24`; 2.11.x is the last line compatible with `minSdk 23`. |
| `com.google.android.material`           | 1.8.0   | 1.14.0  | |
| `kotlinx-coroutines`                    | 1.10.2  | 1.11.0  | |
| `kotlinx-serialization-json`            | 1.5.1   | 1.11.0  | Now in the version catalog. |
| `kotlinx-atomicfu`                      | 0.20.0  | 0.33.0  | |
| Ktor                                    | 3.3.3   | 3.6.0   | OkHttp engine; OkHttp does not yet integrate ECH (see below). |
| `algoliasearch-client-kotlin`           | 3.49.0  | 3.49.0  | Already latest. |
| Robolectric                             | 4.9.2   | 4.17    | Supports SDK 37 and JDK 17/21 hosts. |
| `androidx.test` (ext/runner/espresso)   | 1.1.5 / 1.5.2 / 3.5.1 | 1.3.0 / 1.7.0 / 3.7.0 | |
| MockK / Turbine                         | 1.13.4 / 0.12.1 | 1.14.11 / 1.2.1 | |
| SLF4J / Logback (tests)                 | 2.0.6 / 1.4.5 | 2.0.20 / 1.5.38 | |
| `com.vanniktech.maven.publish`          | 0.34.0  | 0.37.0  | Supports publishing with the Android-KMP library plugin. |
| Spotless                                | 6.16.0  | 8.10.3  | Plugin id is now `com.diffplug.spotless`. |
| Dokka                                   | 2.1.0   | 2.2.0   | |

Firebase and Play services are not used by this repository.

## Behavior changes for apps targeting Android 17

Source: [Behavior changes: Apps targeting Android 17 or higher](https://developer.android.com/about/versions/17/behavior-changes-17).

| Change | Status | Audit result |
|--------|:------:|--------------|
| **Local network access blocked by default** (`ACCESS_LOCAL_NETWORK`) | N/A | The library only connects to Algolia's public HTTPS hosts (`*.algolia.net`, `*.algolianet.com`) via Ktor/OkHttp. No LAN, mDNS, multicast, or loopback addresses anywhere in library or example code (`grep` for `localhost`, `127.0.0.1`, `10.0.2.2`, `ACCESS_LOCAL_NETWORK`: no matches). Nothing to request. |
| **Large screens: orientation / resizability / aspect-ratio opt-outs ignored (sw ≥ 600dp)** | Fixed | Library has no activities. Example apps: the only opt-out was `android:screenOrientation="landscape"` on the Android TV sample; it is dead configuration at target 37 and was removed. No `resizeableActivity`, `minAspectRatio`/`maxAspectRatio`, or `setRequestedOrientation()` usage. All library widgets are plain `View`s / composables that follow their parent's size. |
| **Background audio hardening** (FGS with while-in-use required) | N/A | No audio playback, focus, or volume APIs in the library (`MediaPlayer`, `AudioManager`, `AudioTrack`, `requestAudioFocus`: no matches). The Android sample's voice search uses `RecognizerIntent` / `instantsearch-voice` recording while in the foreground, not playback. |
| **Safer Dynamic Code Loading for native libraries** (`System.load()` files must be read-only) | N/A | No native code, `System.load`/`loadLibrary`, `jniLibs`, or `externalNativeBuild`. The only `.so` in the dependency graph is `libandroidx.graphics.path.so` (Compose), loaded by the system from the APK via `System.loadLibrary`, which is unaffected. |
| **Lock-free `MessageQueue`** (reflection into internals breaks) | N/A | No references to `MessageQueue` or its private members. Third-party audit: `kotlinx-coroutines-android` (public `Handler` API), OkHttp, Ktor, WorkManager, AndroidX do not reflect into `MessageQueue`. Robolectric shadows it but only on the JVM test host. LeakCanary (example app, `debugImplementation` only) uses the public `Looper.myQueue().addIdleHandler`. |
| **`static final` fields unmodifiable via reflection/JNI** | N/A | No reflection on fields anywhere (`getDeclaredField`, `setAccessible`, `Modifier`: no matches). `kotlinx.serialization` uses compiler-generated serializers, not reflection. MockK is test-only. |
| **Delayed SMS OTP access** | N/A | No SMS permissions or `Telephony`/`SmsRetriever` usage. |
| **Keystore 50,000-key limit** | N/A | The library does not use `AndroidKeyStore`. Insights persists events in `SharedPreferences`, not encrypted keys. |
| **ECH (Encrypted Client Hello) enabled** | Monitor | ECH is only negotiated when the networking stack integrates it. The library uses Ktor's OkHttp engine; OkHttp does not integrate platform ECH, so behavior is unchanged. If the host app configures `<ech>` in its network security config, Algolia endpoints must tolerate ECH GREASE, which standard TLS servers do. Verified in the regression run below. |
| **Certificate Transparency on by default** | OK | Algolia endpoints use publicly trusted, CT-logged certificates. The library performs no certificate pinning and installs no custom `TrustManager`, so the platform default applies. |
| **`BluetoothSocket.read()` returns -1** | N/A | No Bluetooth usage. |
| **RemoteViews / widget memory limits** | N/A | No `RemoteViews`, app widgets, or custom notification views. |
| **Activity security (BAL hardening, `IntentSender`)** | N/A | No `PendingIntent`, `ActivityOptions`, or background activity starts. |
| **`setContentCaptureEnabled` deprecation** | N/A | Not used. |
| **Hiding passwords on physical keyboards** | N/A | No password input fields; search boxes use plain text input. |
| **Contacts Provider (CP2) PII / strict SQL** | N/A | No `ContactsContract` usage. |
| **Accessibility text-change types in `TextView`** | OK | Enabled automatically for standard `TextView`/`EditText`-based widgets (`SearchBoxView*`, `StatsTextView`); no custom `InputConnection` in the library. |

## Behavior changes for all apps

Source: [Behavior changes: all apps](https://developer.android.com/about/versions/17/behavior-changes-all).

| Change | Status | Audit result |
|--------|:------:|--------------|
| **Cross-profile loopback traffic blocked** | N/A | No loopback networking (see local-network row above). |
| **App memory limits** | OK | The library holds only search state; large image loading is the host app's concern (the samples use Coil). |
| **SMS OTP protection (WebOTP)** | N/A | No SMS usage. |
| **`usesCleartextTraffic` deprecation plan** | Follow-up (examples only) | The library never uses cleartext. The Android sample declares `android:usesCleartextTraffic="true"`; no `http://` URL exists in the sample sources, but demo index image URLs are not under repository control, so the flag was left in place. Migrate the sample to a network security config when the flag is formally deprecated. |
| **Implicit URI grants (`ACTION_SEND`, `ACTION_IMAGE_CAPTURE`)** | N/A | Not used. |
| **Per-app keystore limits** | N/A | See Keystore row above. |
| **Default IME visibility not restored after rotation** | N/A (library) | Search box widgets do not force the IME; a host app that wants the keyboard restored after an unhandled configuration change must request it (`windowSoftInputMode="stateAlwaysVisible"` or in `onCreate`). |
| **Touchpad relative events during pointer capture** | N/A | No pointer capture. |
| **Background audio hardening (all apps)** | N/A | No audio. |
| **Bluetooth autonomous re-pairing** | N/A | No Bluetooth. |

## Testing

- `./gradlew assemble` builds every library module and all three example apps against
  `compileSdk 37` with AGP 9.3.3 / Kotlin 2.4.20.
- `./gradlew spotlessCheck runDebugUnitTest` (the CI task set) is green: `instantsearch` host
  tests (204), `instantsearch-insights` (45), `instantsearch-compose` (46), `instantsearch-core`
  (86), `instantsearch-agent` (9), `coroutines-extensions` (2), `android-loading` (1).
  Robolectric tests are pinned to `@Config(sdk = [P])` and are unaffected by the platform bump.
- Device regression (Android 17 emulator/hardware, including sw ≥ 600dp / desktop mode) is a
  release-gate activity performed with the example apps before and after flipping `targetSdk 37`;
  see the ticket's acceptance criteria. The Android SDK Upgrade Assistant in Android Studio can be
  used to double-check the per-item list above.

## Impact on consumers

- Consuming apps must build with **`compileSdk 37` and AGP 9.1.1+**. AGP 9 writes
  `minCompileSdk = 37` into the published AAR metadata by default, and Compose 1.12 (a
  dependency of `instantsearch-compose`) requires `compileSdk 37` anyway.
- The Android artifacts now contain **Java 11 bytecode**; consuming Android apps need
  `compileOptions { sourceCompatibility/targetCompatibility = 11 }` (D8 desugars this for
  `minSdk 23`). The pure-JVM artifacts are unchanged (Java 8).
- `minSdk` stays at **23**; no runtime permission or manifest change is required by the library.

## Google Play target API deadline

Google Play requires `targetSdk 36` for new apps and updates from August 31, 2026. This library
does not declare a `targetSdk` (libraries never do), so it neither helps nor blocks a host app's
compliance; the example apps in this repository target **37**, which satisfies the requirement
independently of this work.
