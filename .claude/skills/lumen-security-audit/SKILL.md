---
name: lumen-security-audit
description: >-
  Connaissances spécifiques à la sécurité de Lumen (Spring Boot + Auth0 + MinIO +
  Next.js) qu'un scanner générique ne peut pas déduire seul — à utiliser en
  complément du plugin claude-security (le moteur de scan officiel) ou seul pour une
  passe rapide manuelle. Se déclenche sur "audit de sécurité", "security audit",
  "revue de sécurité", "check auth", "is this secure", "review this for security
  issues", "check for vulnerabilities", ou dès qu'un changement touche
  api/src/main/java/steevnPrm/lumen/authentification/SecurityConfig.java,
  api/src/main/resources/application*.properties, les packages storage/ ou visual/,
  auth-client/lib/auth0.js, auth-client/app/api/signup, auth-client/app/api/profile-sync,
  auth-client/proxy.js, ou auth-client/next.config.ts. À utiliser aussi avant de
  merger tout nouvel endpoint touchant des données appartenant à un utilisateur, pour
  vérifier le pattern de scoping par propriétaire. Termine toujours en rappelant ce
  que ce skill et claude-security NE couvrent PAS (scan de dépendances/CVE, secrets).
allowed-tools:
  - Read
  - Grep
  - Glob
  - AskUserQuestion
  - Bash(git status:*)
  - Bash(git diff:*)
  - Bash(git log:*)
  - Bash(bash "${CLAUDE_SKILL_DIR}/scripts/quick_grep.sh")
---

# Audit de sécurité Lumen (compagnon)

Ce skill est de la **connaissance**, pas un moteur de scan. Pour un audit vérifié
(repo complet ou diff), le moteur c'est le plugin officiel `claude-security` —
indique la commande `/claude-security` à l'utilisateur, ne l'invoque **jamais**
toi-même par outil : il est `disable-model-invocation: true` par conception, piloté
par son propre menu `AskUserQuestion` et une confirmation de coût fixe avant tout
scan. Le rôle de ce skill est uniquement de s'assurer que ce qui tourne — claude-
security ou une passe manuelle rapide — connaît les choses sur la stack Lumen
qu'un scan générique ne peut pas deviner.

## Étape 1 — quelle profondeur

**Audit complet / pré-merge / veut des findings vérifiés** → recommande
`/claude-security`. La commande infère le job depuis du texte libre ou ouvre un menu
à 3 choix (scan codebase / scan changes / suggest patches) ; suggère la zone en
langage naturel plutôt que d'inventer une syntaxe de flags précise (ex. "scanne les
changements uniquement sous le code d'auth et les packages storage/visual" —
la recette mappe ça elle-même vers de vrais répertoires). Point important : le job
**"scan changes" ne scanne que les commits déjà commités**, jamais le travail en
cours — dis-le à l'utilisateur s'il a des changements non commités qu'il veut
couvrir (committer/stash d'abord, ou utiliser le scan codebase complet à la place).
Une fois le rapport de claude-security en main, croise-le avec les checklists
ci-dessous : un scanner générique repère probablement les soucis de type
frameOptions/CORS, mais ne sait pas quels compromis sont déjà documentés par une
ADR — c'est ton travail de combler ce vide, pas de dupliquer le scan.

**Vérif rapide / ciblée** ("check auth quickly", une PR, "est-ce que ça fuit les
données d'un autre utilisateur", un seul fichier) → fais-le toi-même, maintenant,
dans cette session :
1. Lance `scripts/quick_grep.sh` pour obtenir des ancres `file:line` reproductibles.
2. Applique la ou les checklists pertinentes (étape 3 ci-dessous).
3. Rapporte au format : sévérité / `file:line` / problème / pourquoi ça compte /
   correctif concret — pour rester cohérent si l'utilisateur lance ensuite un vrai
   scan `claude-security`.

Les deux se combinent bien dans une même conversation : la passe rapide n'est pas un
substitut à la vérification indépendante du scan complet, juste un premier regard
plus rapide.

## Étape 2 — carte de la stack

| Zone touchée | Fichiers/packages | Checklist |
|---|---|---|
| Spring Security, config API | `api/.../authentification/SecurityConfig.java`, `api/src/main/resources/application*.properties` | `references/backend-checklist.md` |
| Auth0, Next.js, signup | `auth-client/lib/auth0.js`, `auth-client/app/api/signup`, `auth-client/app/api/profile-sync`, `auth-client/proxy.js`, `auth-client/next.config.ts` | `references/frontend-checklist.md` |
| Stockage objet, données utilisateur | `api/.../storage/`, `api/.../visual/`, tout nouvel endpoint sur des données possédées par un utilisateur | `references/storage-checklist.md` |

## Étape 3 — appliquer la checklist pertinente

Ne charge que ce qui est réellement touché par la demande — pas besoin de lire les
trois fichiers de référence si seul le backend est en jeu.

## Étape 4 — ne pas re-signaler ce qui est déjà documenté

Avant de rapporter quelque chose comme un "nouveau" finding, vérifie
`references/known-tradeoffs.md` et les ADR (`docs/adr/*.md`, en français, courtes —
une par décision architecturale). Si une ADR couvre déjà le sujet, ton travail est de
confirmer que le raisonnement tient toujours aujourd'hui, pas de le redécouvrir. Si
rien ne le documente (c'est le cas aujourd'hui pour `csrf().disable()`), dis-le une
fois explicitement — "décision non documentée" mérite d'être signalé, mais pas répété
à chaque audit sauf si le code autour a changé.

## Étape 5 — toujours clore avec le hors-périmètre

Termine chaque audit, rapide ou complet, par le contenu de
`references/out-of-scope.md` (ou un résumé proche) : ni ce skill ni `claude-security`
ne remplacent un scan de dépendances/CVE ou de secrets.
