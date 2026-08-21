# 0007. MinIO pour le stockage des éléments visuels utilisateurs

## Statut
Acceptée.

## Contexte
Les utilisateurs doivent pouvoir associer des éléments visuels (photo de profil, images) à leur compte, affichés ensuite sur leur profil (`/me`). Ce contenu binaire ne relève pas du modèle relationnel déjà en place ([ADR-0002](./0002-backend-spring-boot-postgresql.md)) : il a besoin d'un stockage objet, pas de colonnes PostgreSQL ([PRO-116](https://linear.app/product-hub1908/issue/PRO-116/mettre-en-place-la-base-minio-pour-le-stockage-des-elements-visuels)).

## Décision
Ajout d'un service `minio` (image `minio/minio`) au `docker-compose.yml`, à côté de PostgreSQL, avec un bucket privé dédié (`lumen-user-visuals` par défaut) provisionné par un conteneur one-shot `minio-init` (`minio/mc`) au démarrage — l'API Spring ne gère donc pas elle-même la création du bucket, seulement son contenu.

Côté API, un module `steevnPrm.lumen.storage` encapsule le client MinIO (`io.minio:minio`) derrière un `StorageService` générique (upload / URL de lecture pré-signée / suppression d'un objet), sans connaissance du domaine. Le module `steevnPrm.lumen.visual` porte la convention métier :
- chaque visuel est stocké sous la clé `users/{id}/{uuid}.{extension}`, où `{id}` est l'identifiant interne de l'utilisateur (`User.id`) — ce préfixe est ce qui permet de restreindre les opérations aux fichiers du propriétaire ;
- chaque upload est aussi tracé en base (`UserVisual` : clé objet, nom original, type, taille) pour pouvoir lister et supprimer les visuels d'un utilisateur sans interroger MinIO ;
- la lecture se fait exclusivement via des URLs pré-signées à durée limitée (15 minutes par défaut) — le bucket reste privé, aucune politique de lecture publique n'est appliquée ;
- les endpoints (`GET/POST /api/users/me/visuals`, `DELETE /api/users/me/visuals/{id}`) dérivent systématiquement l'utilisateur courant du `sub` du JWT (comme `UserController`) et scopent les requêtes par utilisateur — un visuel qui n'appartient pas à l'utilisateur authentifié renvoie 404, jamais les données d'un tiers.

Le `MinioClient` est construit sans connexion réseau au démarrage (`MinioClient.builder().build()` ne fait qu'assembler un client HTTP), pour la même raison que le `JwtDecoder` de `SecurityConfig` : ne pas faire dépendre le démarrage de l'application de la disponibilité de MinIO. Les tests (`./mvnw test`) n'ont donc pas besoin d'une instance MinIO active.

## Conséquences
- `docker compose up -d` devient également un prérequis pour l'upload de visuels en local (en plus de PostgreSQL), mais reste sans impact sur `./mvnw test`, qui ne dépend toujours que de H2.
- La configuration MinIO est entièrement externalisée via variables d'environnement (`MINIO_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`, `MINIO_BUCKET`, `MINIO_PRESIGNED_URL_EXPIRY_SECONDS`), avec des valeurs par défaut adaptées au dev local — voir [`docs/workflow/README.md`](../workflow/README.md).
- Seuls les fichiers `image/*` sont acceptés à l'upload (taille max 5 Mo, configurée via `spring.servlet.multipart.max-file-size`) ; toute évolution vers d'autres types de visuels devra revisiter cette contrainte.
- L'intégration côté frontend (affichage effectif des visuels sur la page `/me` d'`auth-client`) n'est pas couverte par cette ADR et reste à faire séparément.
