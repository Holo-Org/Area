# Spikes mobiles : protocole commun

Ce dossier compare trois technologies pour le client mobile de l'AREA (Android obligatoire, iOS en bonus) :

| Dossier | Technologie | Ordre |
| --- | --- | --- |
| [flutter/](flutter/) | Flutter (Dart) | 1 |
| [react-native/](react-native/) | React Native avec Expo (TypeScript) | 2 |
| [kmp/](kmp/) | Kotlin Multiplatform + Compose Multiplatform | 3 |

## Pourquoi cette cible

La cible du bootstrap (`GET /ping` sur le port 8080, port lu dans l'environnement) est une cible serveur.
Son équivalent côté mobile est le **client** de ce `/ping`. Les jalons ci-dessous sont dérivés des
contraintes du sujet sur le client mobile : APK Android installable, adresse du serveur configurable,
build reproductible par Docker, WCAG AA, tests en CI.

## Les jalons (identiques pour les trois technologies)

Chaque technologie doit franchir les jalons **dans l'ordre**. La « distance parcourue » est le dernier
jalon atteint quand le chrono sonne (voir [DISTANCE.md](DISTANCE.md)).

| Jalon | Ce qu'il faut atteindre | Comment on le vérifie |
| --- | --- | --- |
| **J1** Outillage | SDK de la techno installé, projet créé depuis le template officiel | la commande de création réussit |
| **J2** Build | APK debug produit en ligne de commande | le fichier `.apk` existe |
| **J3** Configuration | l'adresse du serveur par défaut vient d'une **variable d'environnement au build** (aucune adresse en dur dans le code), et l'utilisateur peut la modifier dans l'app | le champ affiche la valeur injectée |
| **J4** Réseau + tests | `GET {adresse}/ping`, le JSON `{"status":"ok"}` est parsé (pas affiché brut), état d'erreur si injoignable ; tests automatisés du client et de l'écran | la commande de test de la techno est verte |
| **J5** Exécution réelle | l'app tourne sur l'émulateur Android contre le vrai serveur [ping-server/](ping-server/) (`http://10.0.2.2:8080`), affiche « Statut : ok » ; le champ expose un label d'accessibilité | dump `uiautomator` + log du serveur + capture d'écran |
| **J6** Build reproductible | APK release produit **dans un conteneur Docker**, puis installé et testé sur l'émulateur | `docker build --output` puis même vérification qu'en J5 |

## Règles

- **Timebox : 60 min par technologie.** C'est le milieu de la fourchette 40 à 90 min du bootstrap. Un premier
  build Android à froid coûte environ 5 min et un build Docker environ 8 min, ce qui laisse à peu près
  45 min de travail réel par techno.
- **Hors timebox :** le socle Android commun aux trois (SDK, émulateur, image système), installé une seule fois.
- **Ordre :** Flutter passe en premier, car c'était la préférence de départ : il passe avec les caches
  les plus froids, pour ne pas le favoriser.
- Quand le chrono sonne, on s'arrête et on écrit `<techno>/SPIKE.md`.

## Lancer le serveur de test

```sh
cd ping-server && PORT=8080 node server.js
curl http://localhost:8080/ping   # {"status":"ok"}
```

Le journal horodaté de chaque spike est dans [TIMELOG.md](TIMELOG.md).
