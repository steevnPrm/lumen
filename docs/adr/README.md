# Architecture Decision Records

Ce dossier contient les décisions d'architecture significatives prises sur le projet Lumen, au format ADR (Architecture Decision Record).

## Pourquoi

Une ADR capture une décision technique **au moment où elle est prise** : le contexte, les options considérées, le choix retenu et ses conséquences. L'objectif n'est pas de documenter *tout* le code (ça, c'est le rôle du code lui-même et des commentaires), mais de conserver le *pourquoi* des choix structurants — ceux qui ne se déduisent pas en lisant le code, et qu'on redemanderait sinon dans six mois.

## Quand créer une ADR

- Choix d'un framework, d'une librairie ou d'un service externe structurant
- Décision affectant plusieurs modules ou difficile à revenir en arrière
- Compromis technique avec plusieurs options raisonnables, où le choix retenu mérite d'être justifié

Pas besoin d'ADR pour une décision locale, réversible, ou qui découle naturellement d'une décision déjà documentée.

## Format

Chaque ADR suit la structure suivante :

```markdown
# NNNN. Titre court à l'infinitif ou au participe

## Statut
Proposée | Acceptée | Remplacée par ADR-XXXX | Obsolète

## Contexte
Quel problème, quelle contrainte, quel besoin a motivé cette décision ?

## Décision
Ce qui a été choisi, en une ou deux phrases claires.

## Conséquences
Ce que ce choix implique : bénéfices, compromis, dette technique acceptée, impact sur le reste du système.
```

Les fichiers sont numérotés séquentiellement (`0001-...`, `0002-...`) et ne sont jamais renumérotés ou supprimés : si une décision est remplacée, on crée une nouvelle ADR qui référence l'ancienne et on met à jour le statut de celle-ci.

## Index

| ADR | Titre | Statut |
|---|---|---|
| [0001](./0001-record-architecture-decisions.md) | Consigner les décisions d'architecture via des ADR | Acceptée |
| [0002](./0002-backend-spring-boot-postgresql.md) | Backend en Spring Boot avec PostgreSQL | Acceptée |
| [0003](./0003-frontend-nextjs-auth0.md) | Frontend en Next.js avec authentification Auth0 | Acceptée |
| [0004](./0004-local-dev-database-h2.md) | Base de données H2 en mémoire pour le développement local | Remplacée par ADR-0005 |
| [0005](./0005-postgresql-docker-compose-dev.md) | PostgreSQL via Docker Compose pour le développement local | Acceptée |
