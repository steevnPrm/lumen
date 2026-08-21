# Checklist frontend — Auth0 / Next.js

Fichiers de référence : `auth-client/lib/auth0.js`, `auth-client/proxy.js`,
`auth-client/next.config.ts`, `auth-client/app/signup/page.tsx`,
`auth-client/app/api/signup/route.ts`, `auth-client/app/api/profile-sync/route.ts`,
`auth-client/.env.local` (jamais lire son contenu à voix haute dans un rapport —
vérifier seulement les *noms* de variables présentes, pas leurs valeurs).

## `AUTH0_AUDIENCE` manquant → token opaque

`lib/auth0.js` ne passe `audience` dans `authorizationParameters` que si
`AUTH0_AUDIENCE` est défini. Sans cette variable, Auth0 émet un **access token
opaque**, pas un JWT — le resource server Spring (`NimbusJwtDecoder`) ne peut pas
le valider, et tout appel authentifié vers l'API échoue silencieusement côté
synchronisation de profil. C'est documenté dans le code et dans
`docs/adr/0006-custom-signup-form.md`, et ce piège précis a déjà été rencontré
pendant le développement de ce projet. Dès que du code touchant l'auth est
modifié : `grep -n "AUTH0_AUDIENCE" auth-client/.env.local` pour confirmer que la
variable est bien présente (pas sa valeur — juste sa présence) dans l'environnement
visé.

## Signup personnalisé (ADR-0006)

Le formulaire d'inscription (`app/signup/page.tsx` → `app/api/signup/route.ts`)
appelle directement l'API Auth0 `POST /dbconnections/signup`, en dehors de la page
Universal Login hébergée. C'est un compromis **assumé et documenté** (ADR-0006) :
perte de la détection de bots et du feedback de force de mot de passe hébergés par
Auth0, pour le signup uniquement (le login garde ces deux bénéfices, il reste sur
Universal Login). Ne signale pas ce compromis comme une découverte. Vérifie plutôt
la régression : y a-t-il un rate-limiting ou une mitigation anti-bot sur
`POST /api/signup` aujourd'hui ? Au moment de la rédaction de cette checklist, non —
l'ADR a accepté de perdre la protection Auth0, sans dire "et on ne la remplace
jamais". Si le contexte a changé (abus de signup constatés, etc.), c'est le moment
de le signaler comme dette, pas comme faille nouvelle.

## `next.config.ts` — aucun header de sécurité

Le fichier est le scaffold par défaut de `create-next-app`, sans bloc `headers()`.
Aucune CSP, aucun `Strict-Transport-Security`, aucun `X-Frame-Options` explicite
côté pages Next.js. C'est indépendant du problème `frameOptions` backend (celui-ci
concerne les réponses de l'API Spring, pas les pages servies par Next) — signale les
deux séparément si les deux sont dans le périmètre de l'audit.

## `proxy.js` — pas de logique de blocage supplémentaire

Le middleware délègue entièrement à `auth0.middleware(request)`, avec un matcher qui
exclut les assets statiques. Pas un finding en soi. Point à vérifier quand une
nouvelle route protégée est ajoutée : `proxy.js` ne fait pas de gating additionnel,
donc la protection doit venir de la vérification de session Auth0 dans le composant
serveur ou le route handler lui-même — confirme qu'elle y est.

## Hygiène des secrets

`.env*` est dans `auth-client/.gitignore` et confirmé jamais tracké
(`git ls-files | grep -i "\.env"` ne remonte rien). Il n'y a **pas** de `.gitignore`
racine pour le monorepo — seuls `auth-client/.gitignore` et `api/.gitignore`
existent, chacun adéquat pour son propre arbre. Vérifie qu'aucun fichier type
`application-prod.properties` avec de vraies valeurs n'a jamais été committé côté
`api/`, même par accident :
`git log --all --diff-filter=A --name-only | grep -iE "application-|\.env|credential|secret"`.
