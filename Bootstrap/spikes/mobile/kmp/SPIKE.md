# Spike Kotlin Multiplatform + Compose Multiplatform (client mobile AREA)

- **Date :** commencé le 2026-11-23 à 15:40:41, mis en pause à 15:41:24 (exécution de Gradle bloquée, voir
  plus bas), repris le 2026-11-24 de 11:43:08 à 11:56:39. Détail dans [../TIMELOG.md](../TIMELOG.md).
- **Timebox :** 60 min
- **Temps consommé effectif :** ≈ 15 min 30 (≈ 2 min avant la pause + 13 min 31 après)
- **Versions :** Kotlin 2.4.10, Compose Multiplatform 1.11.1 (Material 3 1.11.0-alpha07), AGP 9.1.1,
  Gradle 9.6.1, Ktor 3.5.1, kotlinx-serialization 1.11.0, JDK 21, compileSdk / targetSdk 37
- **Position à l'arrêt :** **J6 / J6**

## Où j'en étais quand le temps s'est arrêté

| Jalon | Résultat | Preuve |
| --- | --- | --- |
| J1 Outillage | pas de commande `create` en CLI : projet écrit à la main (13 fichiers), versions reprises du template officiel `Kotlin/KMP-App-Template` | [app/](app/) |
| J2 Build | `gradle :androidApp:assembleDebug` OK **du premier coup** | build **2 min 16**, APK debug **13,9 Mo** |
| J3 Configuration | `providers.environmentVariable("AREA_SERVER_URL")` → `BuildConfig.AREA_SERVER_URL`, passé à l'écran partagé | `BuildConfig.java` généré contient `http://10.0.2.2:8080` |
| J4 Réseau + tests | client `/ping` (Ktor + `kotlinx.serialization`) et écran Compose **dans `commonMain`** (Android et iOS) ; `:shared:jvmTest` : **3/3 verts** (client, erreurs, écran via son label) | [PingTest.kt](app/shared/src/commonTest/kotlin/eu/epitech/area/spike/PingTest.kt) |
| J5 Exécution réelle | « Statut : ok » sur l'émulateur Android 36 ; le serveur logue `GET /ping ua=ktor-client` | [emulator-ping-ok.png](emulator-ping-ok.png) ; build incrémental **4 s** |
| J6 Build reproductible | image officielle `gradle:9.6.1-jdk21-noble` + outils Android ; `docker build --no-cache` : APK release **10,2 Mo**, installé et testé, **du premier coup** | [app/Dockerfile](app/Dockerfile) ; build à froid **3 min 47** |

## Ce qui a consommé le plus de minutes

1. **Le blocage du démarrage.** Le template officiel cloné embarque son `gradle-wrapper.jar`. L'outil
   qui m'assistait a refusé trois fois de l'exécuter, puisque c'est du code venu d'un dépôt tiers, alors
   même que sa somme SHA-256 était identique au binaire officiel de Gradle 9.6.1. La solution retenue
   a été d'écrire le projet moi-même et d'exécuter Gradle téléchargé depuis `services.gradle.org`, somme
   vérifiée. Cela a coûté environ 2 min de chrono, mais une demi-journée d'attente en temps réel.
2. **Écrire le projet** (≈ 2 min 30) : KMP n'a ni `flutter create` ni `create-expo-app`. On part du
   wizard web (kmp.jetbrains.com), d'IntelliJ / Android Studio, ou d'un template à cloner.
3. **Les tests** (≈ 1 min) : un faux vert et deux API dépréciées, détaillés plus bas.

Les builds, eux, ont très peu coûté : 2 min 16, puis 4 s en incrémental, et 3 min 47 en Docker à froid.

## Surprises et pièges rencontrés

- **Le template officiel utilise une API dépréciée** : le bloc `androidLibrary { }` du plugin
  `com.android.kotlin.multiplatform.library` doit devenir `android { }` avec AGP 9.1. L'ancien template
  `JetBrains/compose-multiplatform-template` est **archivé depuis 2023**, alors que beaucoup de tutoriels
  y renvoient encore.
- **`runComposeUiTest` est déprécié** au profit de `androidx.compose.ui.test.v2.runComposeUiTest`
  (qui utilise un `StandardTestDispatcher`).
- **Faux vert avec `runTest`** : le temps virtuel fait expirer le `withTimeout(5 s)` du client
  *avant* que le moteur mock réponde. Le test d'erreur passait donc pour une mauvaise raison (timeout au
  lieu de HTTP 500). Les tests du client tournent maintenant sur `Dispatchers.Default` et vérifient le
  message exact (« HTTP 500 », « Adresse invalide »).
- **iOS ne compile pas sous Linux** (« Native task 'iosSimulatorArm64Test' is disabled »), comme pour
  Flutter et React Native : il faut un Mac. Ici, en revanche, le code partagé (client, écran) est
  le même que celui qu'iOS utiliserait.
- **Accessibilité à vérifier** : dans l'arbre exposé à Android, le label « Adresse du serveur » est un
  enfant non focusable du champ, et `hint` est vide. TalkBack devrait fusionner ces enfants dans l'annonce
  du champ, mais c'est moins explicite que Flutter (`hint`) ou React Native (`content-desc`). À confirmer
  avec TalkBack ou Accessibility Scanner.
- `usesCleartextTraffic` et la signature avec la clé debug sont propres au spike, comme pour les deux
  autres technos.

## Points forts observés, au regard du sujet

- **Le build le plus léger des trois.** Il n'y a ni NDK ni code natif à compiler : 3 min 47 en Docker à froid, contre
  7 min 36 pour Flutter et 7 min 32 (non froid) pour React Native, et aucun risque d'OOM.
- **Les APK les plus petits** : 13,9 Mo en debug et 10,2 Mo en release, contre 158 / 47,3 Mo pour Flutter et
  144 / 37,8 Mo pour React Native. Le release n'est même pas minifié (R8 désactivé).
- **Tout a marché du premier coup** : J2, J4 (à un faux vert près), J5 et J6.
- **Une image Docker officielle** (`gradle`) existe et suit les versions, contrairement à l'image Flutter
  (communautaire et en retard) et à React Native (pas d'image, Node à installer).
- **Tests d'interface sans émulateur** : la cible JVM fait tourner le test Compose de l'écran en 2 à 5 s.
- Kotlin est le langage natif d'Android : toute la documentation Android (Compose, permissions,
  accessibilité) s'applique directement.

## Biais à garder en tête

- KMP est passé **en dernier**. Il a donc bénéficié d'un cache Gradle déjà rempli et de l'expérience des
  deux premiers spikes (émulateur éteint pendant le build Docker, modèle de Dockerfile).
- Les temps mesurent la **friction de l'outillage**, pas une vitesse de frappe humaine. Le projet a été
  écrit par un assistant, donc pour un humain qui découvre Kotlin et Gradle, J1 prendrait nettement plus longtemps
  que 2 min 30. C'est le coût caché de l'absence de commande `create`.

## Ce qu'il faudrait pour aller plus loin

- Tester le **login OIDC avec PKCE** : sur Android, AppAuth ou les Custom Tabs s'utilisent nativement ; côté
  partagé, il faut une abstraction `expect`/`actual` par plateforme.
- i18n avec les **Compose Resources** (`composeResources/values-fr/strings.xml`), partagés entre Android et iOS.
- Générer le client depuis l'**OpenAPI** du serveur (`openapi-generator`, générateur `kotlin` multiplateforme
  avec Ktor).
- Activer R8 en release, signer avec une vraie clé, et ajouter un cache Gradle BuildKit au Dockerfile.
- Vérifier le rendu iOS sur un Mac (bonus du sujet).
