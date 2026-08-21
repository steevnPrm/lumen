# Checklist backend — Spring Security / API

Fichiers de référence : `api/src/main/java/steevnPrm/lumen/authentification/SecurityConfig.java`,
`api/src/main/resources/application.properties`, `api/src/test/resources/application.properties`.

## `csrf().disable()`

Défendable pour une API stateless à bearer token (pas de session cookie à protéger
contre le CSRF). Avant d'accepter ce raisonnement par défaut, confirme qu'aucune
route ne repose sur une session cookie : `grep -rn "HttpSession\|@SessionAttributes\|SessionCreationPolicy\.IF" api/src`
devrait ne rien remonter d'inattendu (`SessionCreationPolicy.STATELESS` est le seul
usage légitime). Aucune ADR ne documente cette ligne aujourd'hui — signale-le une
fois comme "décision non documentée, à formaliser en ADR si elle est réaffirmée",
pas à chaque audit.

## `frameOptions().disable()` global

`SecurityConfig.java` désactive `frameOptions` sur **toute** la chaîne de filtres,
alors que seule `/h2-console/**` en a besoin (la console H2 s'affiche dans une frame).
Conséquence concrète : toutes les réponses de l'API, y compris en production, perdent
la protection `X-Frame-Options` contre le clickjacking.

Correctif : une seconde `SecurityFilterChain`, matchée uniquement sur
`/h2-console/**` via `securityMatcher(...)`, qui désactive `frameOptions` seulement
là — la chaîne par défaut garde la protection pour tout le reste. Vérifie aussi que
`/h2-console/**` n'est pas atteignable en dehors du profil de dev (aucun gating par
profil Spring trouvé actuellement — `permitAll()` inconditionnel dans la seule chaîne
de filtres existante).

## Absence totale d'autorisation par rôle

`grep -rn "@PreAuthorize\|@Secured\|hasRole\|hasAuthority" api/src` ne remonte rien.
La seule règle d'autorisation est `.anyRequest().authenticated()` — n'importe quel
utilisateur authentifié peut appeler n'importe quel endpoint, la restriction aux
données du bon utilisateur se fait uniquement par scoping de requête (voir
`storage-checklist.md` pour le pattern qui fait ça correctement). C'est acceptable
tant que **tous** les endpoints suivent ce modèle "chacun agit sur ses propres
données". Le jour où un endpoint admin, ou réservé à un rôle particulier, apparaît
sans `@PreAuthorize`/`hasRole` correspondant, c'est un vrai finding, pas une
observation de style.

## Absence de `CorsConfiguration`

`grep -rn "CorsConfiguration\|@CrossOrigin\|\.cors(" api/src` ne remonte rien.
Actuellement non exploitable : `auth-client` n'appelle jamais l'API Spring
directement depuis le navigateur, toujours via une route serveur Next.js
(`app/api/*/route.ts`, `fetch` server-to-server). Vérifie que c'est toujours vrai
avant d'écarter le sujet :
`grep -rn "API_BASE_URL\|localhost:8080\|NEXT_PUBLIC.*API" auth-client/app`.
Le jour où un composant client fetch l'API Spring directement, une vraie
`CorsConfiguration` devient nécessaire.

## Credentials dans `application.properties`

`spring.datasource.username`/`password` sont des littéraux en clair (`lumen`/`lumen`),
sans mécanisme `${VAR:default}` — contrairement aux clés `minio.*` et à
`spring.security.oauth2.resourceserver.jwt.issuer-uri`, qui utilisent bien ce
pattern d'override. Deux choses à confirmer à chaque audit touchant cette zone :
(a) ces valeurs sont bien des defaults de dev, jamais des vraies valeurs en clair
committées ; (b) le déploiement injecte réellement des valeurs pour **toutes** les
clés sensibles, pas seulement celles qui ont déjà le pattern d'override — l'absence
du pattern sur `datasource.*` est justement le signal qu'il n'y a peut-être pas
encore de mécanisme d'override prévu pour ces deux-là spécifiquement.
