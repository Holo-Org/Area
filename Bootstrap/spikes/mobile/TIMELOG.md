# Journal de temps des spikes mobiles (heure locale, 2026-11-23)

| Événement | Heure | Note |
| --- | --- | --- |
| Étape 0 (socle commun, hors timebox) : SDK Android, émulateur | 14:31 → 14:34 | + création AVD ; installation via le nouveau binaire `android` |
| **Flutter** START | 14:34:25 | timebox 60 min |
| Flutter J1 (SDK installé, projet créé) | ~14:37 | téléchargement SDK 2 min 03 |
| Flutter J2 (APK debug) | ~14:42 | premier build à froid 5 min 20 |
| Flutter J4 (client /ping + 3 tests verts) | ~14:43 | |
| Flutter J5 (émulateur, "Statut : ok") | 14:44 | |
| Flutter J6 (APK release via Docker, testé) | 14:52:50 | build Docker à froid 7 min 36 |
| **React Native (Expo)** START | ~14:53 | timebox 60 min |
| RN : Node 26 installé, projet Expo créé, prebuild | ~14:53 → ~14:56 | |
| RN : build Gradle interrompu par un redémarrage du PC | ~14:56 → 15:05 | **chrono en pause** ; on reprend à partir de ~3 min consommées |
| RN reprise | 15:05:36 | |
| RN J2 (APK debug) | 15:09:41 | build Gradle 4 min 00, APK debug 143,6 Mo |
| RN J4 (client /ping + 3 tests verts) | 15:11:02 | 2 pièges : TS 6 `types` vide par défaut, RNTL 14 API async |
| RN J5 (émulateur, "Statut : ok") | 15:16:14 | ~1 min perdue sur un ANR de l'UI système (émulateur froid), pas imputable à RN |
| RN J6 tentative 1 | 15:16:38 → 15:17:23 | échec : Node 26 a besoin de `libatomic1` dans l'image |
| RN J6 tentative 2 | 15:17:28 → 15:30:03 | **OOM global** : compilation C++ 4 ABI × 12 cœurs + émulateur ; le noyau tue VS Code |
| pause (redémarrage VS Code / session) | 15:30 → 15:32 | chrono en pause |
| RN J6 tentative 3 | 15:31:40 | 2 ABI, 4 workers, émulateur éteint |
| RN J6 (APK release via Docker, testé) | 15:40:29 | build Docker 7 min 32, APK 37,8 Mo (2 ABI) |
| **RN total effectif** | ≈ 36 min | ~3 + 24 min 46 + 8 min 29 ; dont 12 min 30 perdues dans l'OOM |
| **KMP / Compose Multiplatform** START | 15:40:41 | timebox 60 min |
| KMP : build bloqué par le garde-fou (exécution du wrapper Gradle d'un dépôt cloné) | 15:41:24 | **chrono en pause** en attente de ton accord ; wrapper vérifié identique à l'officiel Gradle 9.6.1 |
| KMP reprise (accord explicite pour lancer Gradle) | 2026-11-24 11:43:08 | ~2 min consommées avant la pause (clone + vérification du wrapper) |
| KMP J1 (Gradle officiel vérifié + projet écrit à la main, pas de `create` en CLI) | 11:45:34 | |
| KMP J2 + J3 (APK debug, BuildConfig depuis l'env) | 11:47:56 | build 2 min 16 (cache Gradle déjà chaud), APK debug 13,9 Mo, 1er essai |
| KMP J4 (client /ping + 3 tests verts sur la cible JVM) | 11:48:42 | pièges : `runTest` + `withTimeout` (temps virtuel), DSL `androidLibrary` et `runComposeUiTest` dépréciés |
| KMP J5 (émulateur, "Statut : ok") | 11:50:09 | build incrémental 4 s ; le label du champ est un enfant non focusable (hint vide), à vérifier avec TalkBack |
| KMP J6 (APK release via Docker, testé) | 11:56:39 | build Docker `--no-cache` 3 min 47 (image de base tirée avant), APK 10,2 Mo, 1er essai |
| **KMP total effectif** | ≈ 15 min 30 | ≈ 2 min (23/11) + 13 min 31 (24/11) |
