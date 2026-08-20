# 0004. Base de données H2 en mémoire pour le développement local

## Statut
Remplacée par [ADR-0005](./0005-postgresql-docker-compose-dev.md).

## Contexte
Le backend dépend de `spring-boot-starter-data-jpa` et du driver PostgreSQL, mais sans URL de connexion configurée, `mvn spring-boot:run` échoue au démarrage (`Failed to determine a suitable driver class`). Aucune instance PostgreSQL n'est encore provisionnée pour le développement local (pas de docker-compose, pas de cluster partagé), et le modèle de données n'a pas encore d'entités JPA définies.

## Décision
Ajout d'une dépendance H2 (`scope=runtime`) et configuration d'une base en mémoire dans `api/src/main/resources/application.properties`, avec `spring.jpa.hibernate.ddl-auto=update` pour laisser Hibernate créer le schéma automatiquement, et la console H2 activée (`/h2-console`) pour l'inspection en dev.

## Conséquences
- `mvn spring-boot:run` fonctionne immédiatement sans installation ni configuration de PostgreSQL.
- Les données ne sont pas persistées entre deux redémarrages de l'application (base en mémoire) — sans impact tant qu'il n'y a pas de données de dev à conserver.
- H2 et PostgreSQL n'ont pas exactement le même dialecte SQL ; du SQL spécifique à Postgres (types, fonctions) ne sera pas testable en l'état sur H2. À surveiller quand des requêtes natives ou des types Postgres spécifiques apparaîtront.
- Cette configuration est explicitement temporaire : dès qu'un environnement Postgres local (Docker ou cluster local) sera mis en place, `application.properties` doit être mis à jour pour pointer dessus, et la dépendance H2 peut être déplacée en scope `test` uniquement si elle reste utile pour les tests d'intégration.
