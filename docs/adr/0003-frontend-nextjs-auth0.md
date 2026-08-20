# 0003. Frontend en Next.js avec authentification Auth0

## Statut
Acceptée

## Contexte
Lumen a besoin d'un client web pour les trois profils d'utilisateurs (artistes, organisateurs, spectateurs), avec des pages publiques (découverte d'œuvres, expositions) et des zones authentifiées (portfolio, gestion d'exposition, soutien financier). L'authentification doit être fiable et sécurisée sans réimplémenter la gestion des comptes, mots de passe, et flux OAuth en interne.

## Décision
Le client web (`auth-client/`) est développé avec **Next.js** (App Router) et **React**, stylé avec **Tailwind CSS v4**. L'authentification est déléguée à **Auth0** via le SDK officiel `@auth0/nextjs-auth0`, qui gère les flux de connexion/inscription/déconnexion (`/auth/login`, `/auth/logout`) et la session côté serveur.

## Conséquences
- Auth0 retire de notre périmètre la gestion des identifiants, la sécurité du stockage des mots de passe, et les flux OAuth — bénéfice net vu la sensibilité du sujet et l'absence d'expertise dédiée en interne à ce stade.
- Ça introduit une dépendance à un service tiers (coût, disponibilité, configuration d'application/callbacks côté tenant Auth0) et nécessite un compte/tenant configuré (`auth0 apps create ...`) pour chaque environnement.
- Next.js App Router permet de faire cohabiter des Server Components (lecture de session, rendu initial) et des zones interactives côté client, adapté à une interface qui reste majoritairement orientée contenu (portfolios, expositions).
- Les identifiants Auth0 par environnement sont stockés dans `auth-client/.env.local` (non versionné) — voir [`docs/workflow/`](../workflow/README.md) pour la mise en route.
