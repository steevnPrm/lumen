# 0006. Formulaire d'inscription personnalisé, en dehors de la page Auth0 hébergée

## Statut
Acceptée — modifie partiellement [ADR-0003](./0003-frontend-nextjs-auth0.md).

## Contexte
[ADR-0003](./0003-frontend-nextjs-auth0.md) déléguait l'intégralité des flux de connexion/inscription à la page Universal Login hébergée par Auth0 (redirection via `/auth/login`), pour ne pas avoir à gérer nous-mêmes les identifiants. Le besoin produit est désormais qu'un nouvel utilisateur renseigne en une seule fois, sur un seul formulaire : les informations d'authentification (email, mot de passe, confirmation du mot de passe) et les informations de profil non sensibles (pseudo, prénom, nom — voir [PRO-117](https://linear.app/product-hub1908/issue/PRO-117)). La page Universal Login hébergée par Auth0 ne permet pas d'ajouter des champs personnalisés à son formulaire d'inscription.

## Décision
Le formulaire d'inscription (`auth-client/app/signup/page.tsx`) est désormais un formulaire propre à l'application, qui appelle directement l'API d'authentification Auth0 (`POST https://{domaine}/dbconnections/signup`) via une route serveur (`app/api/signup/route.ts`), plutôt que de rediriger vers Universal Login. La connexion (`/auth/login`) reste inchangée et continue de rediriger vers la page hébergée par Auth0.

La confirmation du mot de passe est une vérification uniquement côté client (les deux champs doivent correspondre avant l'envoi) ; seul le mot de passe validé est transmis à Auth0, jamais sa confirmation.

Créer le compte via l'API ne crée pas de session : Auth0 déconseille le grant `password` (ROPC) qui permettrait de l'éviter. Après inscription, l'utilisateur est donc redirigé vers `/auth/login?login_hint={email}`, qui l'amène une seule fois sur la page de connexion Auth0 (email pré-rempli) pour établir sa session.

Les champs pseudo/prénom/nom sont envoyés en `user_metadata` lors de l'appel d'inscription (copie durable côté Auth0), puis synchronisés vers notre base via `PUT /api/users/me` ([PRO-117](https://linear.app/product-hub1908/issue/PRO-117)) juste après la connexion (`app/profile-sync.tsx` + `app/api/profile-sync/route.ts`), de façon best-effort : un échec de synchronisation ne bloque jamais l'utilisateur.

## Conséquences
- On perd, pour l'étape d'inscription spécifiquement, l'UI hébergée et maintenue par Auth0 (retour en direct sur la force du mot de passe, détection de bots). La connexion garde ces bénéfices puisqu'elle reste sur Universal Login.
- La synchronisation du profil vers le backend nécessite un access token au format JWT, ce qui suppose qu'une API (Resource Server) soit déclarée côté tenant Auth0 avec un identifiant transmis en tant qu'`audience` (variable `AUTH0_AUDIENCE`, absente par défaut). Sans cette configuration — qui relève de [PRO-115](https://linear.app/product-hub1908/issue/PRO-115) — Auth0 émet un token opaque que le backend ne peut pas valider ; la synchronisation échoue silencieusement mais les champs restent conservés en `user_metadata` côté Auth0.
- Le nom de la connexion Auth0 utilisée pour l'inscription (`AUTH0_CONNECTION`, par défaut `Username-Password-Authentication`) doit correspondre à celle configurée sur le tenant.
