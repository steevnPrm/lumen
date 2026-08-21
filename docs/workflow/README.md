# Workflow de développement

## Prérequis

- Java 21 (backend)
- Node.js + npm (frontend)
- [Auth0 CLI](https://github.com/auth0/auth0-cli) (`auth0`), pour créer/gérer l'application Auth0 utilisée par le client
- Docker + Docker Compose, pour PostgreSQL et MinIO en local (voir [ADR-0005](../adr/0005-postgresql-docker-compose-dev.md) et [ADR-0007](../adr/0007-minio-stockage-visuels-utilisateurs.md))

## Structure du repo

| Dossier | Rôle |
|---|---|
| `api/` | Backend Spring Boot (API REST, persistance, sécurité) |
| `auth-client/` | Client web Next.js, authentification Auth0 |
| `docs/` | Documentation : design system, ADR, workflow |

## Backend (`api/`)

```bash
# Démarrer PostgreSQL et MinIO (une seule fois, depuis la racine du repo)
docker compose up -d

cd api
./mvnw spring-boot:run
```

- Démarre sur `http://localhost:8080`.
- Se connecte à PostgreSQL via Docker Compose ([ADR-0005](../adr/0005-postgresql-docker-compose-dev.md)) — base `lumen`, utilisateur `lumen`, port `5432`.
- Se connecte à MinIO via Docker Compose ([ADR-0007](../adr/0007-minio-stockage-visuels-utilisateurs.md)) — API sur le port `9000`, console web sur `http://localhost:9001` (identifiants `lumen` / `lumen12345` en local), bucket `lumen-user-visuals` créé automatiquement au démarrage. Variables d'environnement disponibles : `MINIO_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`, `MINIO_BUCKET`, `MINIO_PRESIGNED_URL_EXPIRY_SECONDS`.
- Les tests (`./mvnw test`) utilisent H2 en mémoire ([`src/test/resources/application.properties`](../../api/src/test/resources/application.properties)) et n'ont pas besoin de Docker.
- Health check : `http://localhost:8080/actuator/health`.

## Frontend (`auth-client/`)

### 1. Configurer Auth0

Si l'application Auth0 n'existe pas encore côté tenant :

```bash
auth0 login
auth0 apps create --name "Lumen API" --type regular --callbacks http://localhost:3000/auth/callback
```

### 2. Variables d'environnement

Copier/renseigner `auth-client/.env.local` (non versionné) avec les valeurs de l'application Auth0 créée ci-dessus :

```
APP_BASE_URL=http://localhost:3000
AUTH0_DOMAIN=
AUTH0_CLIENT_ID=
AUTH0_CLIENT_SECRET=
AUTH0_SECRET=

# Optionnel — nom de la connexion base de données utilisée pour l'inscription
# (voir ADR-0006). Par défaut "Username-Password-Authentication".
AUTH0_CONNECTION=

# Optionnel — identifiant de l'API Auth0 (Resource Server) déclarée pour le
# backend Spring. Sans cette valeur, l'access token émis par Auth0 n'est pas
# un JWT et le backend ne peut pas le valider (voir ADR-0006, PRO-115).
AUTH0_AUDIENCE=

# Optionnel — URL du backend Spring, utilisée côté serveur Next.js pour
# synchroniser le profil juste après la connexion. Par défaut http://localhost:8080.
API_BASE_URL=
```

`AUTH0_SECRET` peut être généré avec `openssl rand -hex 32`.

### 3. Lancer le serveur de dev

```bash
cd auth-client
npm install
npm run dev
```

- Démarre sur `http://localhost:3000` (bascule automatiquement sur un autre port si occupé).
- Hot reload via Turbopack : les changements dans `app/` sont reflétés sans redémarrage.

## Convention de branches et commits

- Branches : `<pseudo>/<description-courte>` (ex. `steevnprm/add-spring-boilerplate`).
- La branche `main` reste toujours déployable ; le travail en cours passe par une branche de feature avant merge.
- Décision d'architecture significative → ajouter une ADR dans `docs/adr/` ([format](../adr/README.md)) dans la même PR que le changement qu'elle justifie.

## Design system

Toute UI ajoutée au client doit suivre les tokens et composants définis dans [`docs/design-system/`](../design-system/README.md) (couleurs, typographie, espacement, rayons, mouvement) plutôt que d'introduire de nouvelles valeurs ad hoc.
