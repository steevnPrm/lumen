# Workflow de développement

## Prérequis

- Java 21 (backend)
- Node.js + npm (frontend)
- [Auth0 CLI](https://github.com/auth0/auth0-cli) (`auth0`), pour créer/gérer l'application Auth0 utilisée par le client
- PostgreSQL 16, uniquement si vous testez la configuration cible (voir [ADR-0004](../adr/0004-local-dev-database-h2.md) — non requis pour le développement courant, qui utilise H2 en mémoire)

## Structure du repo

| Dossier | Rôle |
|---|---|
| `api/` | Backend Spring Boot (API REST, persistance, sécurité) |
| `auth-client/` | Client web Next.js, authentification Auth0 |
| `docs/` | Documentation : design system, ADR, workflow |

## Backend (`api/`)

```bash
cd api
./mvnw spring-boot:run
```

- Démarre sur `http://localhost:8080`.
- Utilise une base H2 en mémoire par défaut ([ADR-0004](../adr/0004-local-dev-database-h2.md)) — aucune installation de base de données requise. Console H2 disponible sur `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:lumen`, user `sa`, mot de passe vide).
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
