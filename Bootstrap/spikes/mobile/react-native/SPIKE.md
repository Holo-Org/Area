# Spike React Native + Expo (client mobile AREA)

- **Date :** 2026-11-23, ~14:53 → 15:40:29, interrompu deux fois (redémarrage du PC, puis OOM). Le chrono
  était en pause pendant les interruptions (voir [../TIMELOG.md](../TIMELOG.md)).
- **Timebox :** 60 min
- **Temps consommé effectif :** ≈ 36 min, dont **12 min 30 perdues dans une tentative de build Docker tuée
  par manque de mémoire**
- **Versions :** Expo SDK 58.0.26, React Native 0.88.1 (nouvelle architecture activée), React 19.3, TypeScript 6.0,
  Jest 30 + `jest-expo`, `@testing-library/react-native` 14.1, Node 26.13.1 LTS, JDK 21
- **Position à l'arrêt :** **J6 / J6**

## Où j'en étais quand le temps s'est arrêté

| Jalon | Résultat | Preuve |
| --- | --- | --- |
| J1 Outillage | **Node 18 de la machine trop ancien** (Expo 58 exige ≥ 22.13) ; Node 26 installé ; `create-expo-app` 21 s | |
| J2 Build | `expo prebuild` + `./gradlew assembleDebug` OK | build **4 min 00**, APK debug **143,6 Mo** (et il exige Metro pour fonctionner) |
| J3 Configuration | `process.env.EXPO_PUBLIC_AREA_SERVER_URL`, **vraie variable d'environnement** inlinée au bundle | le champ affiche `http://10.0.2.2:8080` |
| J4 Réseau + tests | client `/ping` typé, `tsc --noEmit` propre, `jest` : **3/3 verts** (client, erreurs, écran via label accessible) | [app/__tests__/ping.test.tsx](app/__tests__/ping.test.tsx) |
| J5 Exécution réelle | APK release (bundle JS embarqué) : « Statut : ok » ; le serveur logue `GET /ping ua=okhttp/4.9.2` | [emulator-ping-ok.png](emulator-ping-ok.png) ; `assembleRelease` **4 min 15** |
| J6 Build reproductible | Dockerfile (Node + SDK, `npm ci`, `expo prebuild`, Gradle) : APK release **37,8 Mo (2 ABI)**, installé et testé | [app/Dockerfile](app/Dockerfile) ; build **7 min 32** (3e tentative) |

## Ce qui a consommé le plus de minutes

1. **Le build Docker : trois tentatives, environ 21 min en tout.**
   - Tentative 1 : Node 26 a besoin de `libatomic1`, absente de l'image JDK (45 s).
   - Tentative 2 : **OOM global de la machine (15 Go de RAM).** Le release RN **compile du C++ pour 4 ABI**
     (nouvelle architecture, NDK 27 téléchargé pendant le build), en parallèle sur 12 cœurs, à côté de l'émulateur. Le
     noyau a tué VS Code. Résultat : 12 min 30 perdues et une session interrompue.
   - Tentative 3 : limitée à `arm64-v8a,x86_64` et `--max-workers=4`, émulateur éteint, puis OK.
2. **Les builds Gradle** : 4 min pour un APK debug, 4 min 15 pour un release, *même avec le cache Gradle déjà chaud* grâce
   à Flutter. La compilation native explique la différence.
3. **Deux changements d'API récents** (environ 3 min) :
   - **TypeScript 6** : `types` est désormais vide par défaut, donc `test`, `expect` et `jest` sont introuvables tant
     qu'on n'ajoute pas `"types": ["jest"]` au `tsconfig.json`.
   - **RNTL 14** : `render` est **asynchrone** (`await render(...)`) et `userEvent` remplace `fireEvent`. Un
     test écrit « à l'ancienne » échoue avec ``render` function has not been called``.

## Surprises et pièges rencontrés

- Le template génère un `AGENTS.md` qui avertit : *« Expo ships breaking changes every SDK release »*. Sur un
  projet de trois mois, c'est un risque de maintenance à anticiper (épingler le SDK et ne pas monter de version en cours de route).
- `npx expo install a b c -- --save-dev` a mis les outils de test dans `dependencies` : il a fallu
  corriger `package.json` à la main.
- npm 11 **bloque les scripts d'installation** par défaut (`npm install-scripts approve`). C'est une bonne chose
  pour la sécurité (SEC-DEPS), mais à connaître.
- **Le dossier `android/` est généré** par `expo prebuild` (« Continuous Native Generation ») et se configure via
  `app.json` et ses plugins (`expo-build-properties` pour le cleartext). Ce n'est pas ce que l'on attend : on ne versionne pas
  ce code natif, mais on le régénère, y compris dans Docker.
- **Un APK debug RN ne fonctionne pas seul**, il attend le serveur Metro. Pour tester un vrai APK, il faut un release.
- Le client HTTP natif sous `fetch` est un **OkHttp 4.9.2** (version de 2021).
- Le build cloud **EAS** (la voie « officielle » d'Expo) n'a pas été utilisé : il exige un compte Expo et ne
  satisfait pas l'exigence d'un build reproductible via Docker Compose.

## Points forts observés, au regard du sujet

- **TypeScript**, le même langage que le web client si celui-ci est en React/TS : types partagés possibles, un seul
  langage pour l'équipe front, et une génération OpenAPI (`openapi-typescript`) commune web/mobile.
- Une variable d'environnement réelle (`EXPO_PUBLIC_*`), sans relais par la commande de build.
- Accessibilité de première classe dans les tests (`getByLabelText`, `getByRole('button', { name })`).
- APK release le plus léger des deux (37,8 Mo pour 2 ABI).

## Ce qu'il faudrait pour aller plus loin

- **Maîtriser le coût du build** : NDK et cache Gradle dans une couche Docker, nombre d'ABI réduit, limite
  mémoire en CI. Sans ça, chaque pipeline coûte plus de 7 min et le risque d'OOM est réel sur nos machines.
- Tester le **login OIDC avec PKCE** (`expo-auth-session` ou `react-native-app-auth`, redirection par deep link).
- i18n (`expo-localization` + `i18next`) pour deux langues.
- Vérifier la compatibilité de chaque bibliothèque native avec la nouvelle architecture avant de l'adopter.
- iOS : nécessite un Mac (ou EAS Build, payant au-delà du quota gratuit).
