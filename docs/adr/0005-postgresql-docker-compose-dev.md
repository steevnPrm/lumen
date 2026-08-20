# 0005. PostgreSQL via Docker Compose pour le développement local

## Statut
Acceptée — remplace [ADR-0004](./0004-local-dev-database-h2.md).

## Contexte
[ADR-0004](./0004-local-dev-database-h2.md) avait introduit H2 en mémoire comme mesure temporaire, le temps qu'un environnement PostgreSQL soit provisionné pour le développement local. Avec l'ajout de la première entité JPA (`User`, voir [PRO-117](https://linear.app/product-hub1908/issue/PRO-117)) vient le besoin de valider le comportement réel sur PostgreSQL (dialecte SQL, contraintes, types) plutôt que sur H2, dont le dialecte diverge de la cible de production ([ADR-0002](./0002-backend-spring-boot-postgresql.md)).

## Décision
Ajout d'un `docker-compose.yml` à la racine du repo démarrant un conteneur `postgres:16` (base `lumen`, utilisateur `lumen`), avec un volume nommé pour persister les données entre redémarrages. `api/src/main/resources/application.properties` pointe désormais vers ce PostgreSQL local. La dépendance H2 est conservée en scope `test` uniquement, avec `api/src/test/resources/application.properties` dédié pour que les tests restent rapides et sans dépendance externe.

## Conséquences
- Le développement local s'exécute désormais contre le même moteur SQL que la production, éliminant les divergences de dialecte identifiées dans l'ADR-0004.
- `docker compose up -d` devient un prérequis pour lancer `./mvnw spring-boot:run` (voir [`docs/workflow/`](../workflow/README.md)) — ce n'est plus « zéro installation » comme avec H2.
- Les tests (`./mvnw test`) continuent de tourner sur H2 en mémoire via `src/test/resources/application.properties`, donc restent rapides et exécutables en CI sans Docker.
- Les données ne sont plus perdues entre redémarrages de l'application (volume Docker persistant), contrairement à H2 en mémoire.
