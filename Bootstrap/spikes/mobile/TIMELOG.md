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
