# Spike Flutter (client mobile AREA)

- **Date :** 2026-11-23, 14:34:25 → 14:52:50
- **Timebox :** 60 min (justification dans [../README.md](../README.md))
- **Temps consommé :** 18 min 25 s
- **Versions :** Flutter 3.50.1 (stable, sortie le 2026-11-20), Dart 3.14.0, `http` 1.6, AGP / Gradle fournis par le template, JDK 21
- **Position à l'arrêt :** **J6 / J6.** Cible atteinte, chrono arrêté sans prolongation.

## Où j'en étais quand le temps s'est arrêté

Les six jalons sont atteints, preuves à l'appui :

| Jalon | Résultat | Preuve |
| --- | --- | --- |
| J1 Outillage | `flutter create --platforms android,ios` OK | SDK téléchargé en 2 min 03 |
| J2 Build | `flutter build apk --debug` OK | premier build à froid **5 min 20**, APK debug 158 Mo |
| J3 Configuration | `String.fromEnvironment('AREA_SERVER_URL')`, alimenté par `--dart-define=AREA_SERVER_URL=$AREA_SERVER_URL` | le champ affiche `http://10.0.2.2:8080` au lancement |
| J4 Réseau + tests | client `/ping` typé (`sealed class PingResult`), `flutter test` : **3/3 verts** (client, erreurs, widget + label sémantique) | [app/test/widget_test.dart](app/test/widget_test.dart) |
| J5 Exécution réelle | « Statut : ok » sur l'émulateur Android 36 ; le serveur logue `GET /ping ua=Dart/3.14 (dart:io)` | [emulator-ping-ok.png](emulator-ping-ok.png) ; build incrémental **11 s** |
| J6 Build reproductible | `docker build --output` : APK release **47,3 Mo** (3 ABI), installé et testé sur l'émulateur | [app/Dockerfile](app/Dockerfile) ; build Docker à froid **7 min 36** |

## Ce qui a consommé le plus de minutes

1. **Les builds à froid** (environ 13 min sur 18) : téléchargement de Gradle, d'AGP et de la plateforme Android 36
   au premier `build apk` (5 min 20), puis le build Docker (7 min 36), qui télécharge en plus le **NDK r28c (~1 Go)** pendant
   l'étape Gradle.
2. **L'image Docker.** L'image Flutter de référence (`ghcr.io/cirruslabs/flutter`) est **communautaire et en
   retard** : sa dernière version est 3.44.0, contre 3.50.1 utilisée ici. J'ai donc écrit un Dockerfile qui épingle JDK, outils
   Android et Flutter, ce qui est plus robuste mais à maintenir nous-mêmes.
3. Le code lui-même : environ 3 min. 156 lignes de Dart, tests compris.

## Surprises et pièges rencontrés

- **`package:flutter/material.dart` est déprécié depuis Flutter 3.47** au profit de `package:material_ui`
  (Material est sorti du framework). `flutter analyze` le signale (niveau *info*). La plupart des tutoriels et
  exemples en ligne ne le reflètent pas encore.
- **La permission `INTERNET` n'est que dans le manifest *debug*** du template. Sans l'ajouter au manifest
  `main`, l'APK release n'a pas de réseau, et rien ne le signale à la compilation.
- **HTTP en clair bloqué par défaut** (Android 9+) : il faut `usesCleartextTraffic` (spike) ou une
  `network_security_config` restreinte (à faire pour le vrai projet).
- `--dart-define` n'est pas une lecture *directe* de l'environnement : c'est la commande de build qui relaie
  la variable. Ça reste une valeur hors du code source, et `--dart-define-from-file` permet de passer un `.env`.
- Le release build télécharge le NDK dans l'étape Gradle : **chaque modification du code source refait ce
  téléchargement** dans Docker tant qu'on ne l'installe pas dans une couche dédiée.

## Points forts observés, au regard du sujet

- Le **moteur est précompilé** : aucun C++ à compiler, donc un build Docker frugal en RAM (aucun problème
  avec l'émulateur allumé en parallèle).
- **Accessibilité testable en test unitaire** : `find.bySemanticsLabel(...)` vérifie le label TalkBack sans
  émulateur, ce qui aidera pour WCAG AA en CI.
- `flutter analyze` + `flutter test` + `flutter build apk` : trois commandes, rien d'autre à installer, une CI
  très simple.
- Un seul langage (Dart) et un seul outil pour Android et iOS.

## Ce qu'il faudrait pour aller plus loin

- Installer le NDK et précharger le cache Gradle dans une couche Docker (ou un cache BuildKit
  `--mount=type=cache`), pour que le rebuild après modification du code reste sous 2 min.
- Tester le **login OIDC avec PKCE** (`flutter_appauth`, custom tabs, redirection par deep link) : c'est le vrai
  risque technique du client mobile (SEC-AUTH exige les mêmes protections sur mobile).
- Générer le client HTTP depuis la description **OpenAPI** du serveur (`openapi-generator` cible `dart-dio`).
- i18n avec `flutter gen-l10n` (fichiers ARB), puisque deux langues sont obligatoires.
- iOS : nécessite un Mac avec Xcode. C'est impossible à vérifier sous Linux, comme pour les deux autres technos.
- Signer l'APK release avec une vraie clé (aujourd'hui signé avec la clé debug du conteneur).
