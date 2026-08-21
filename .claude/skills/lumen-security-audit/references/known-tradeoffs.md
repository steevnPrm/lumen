# Compromis déjà connus — à re-vérifier, pas à re-signaler

Avant de rapporter quelque chose comme un finding nouveau, vérifie cette table. Si
une ligne correspond, ton travail est de confirmer que la situation sous-jacente n'a
pas changé, pas d'annoncer une découverte.

| Compromis | Documenté dans | Ce qu'il faut re-vérifier (pas re-signaler) |
|---|---|---|
| Signup personnalisé, hors Universal Login — perte de la détection de bots et du feedback de force de mot de passe hébergés par Auth0 | `docs/adr/0006-custom-signup-form.md` | Aucun rate-limiting/mitigation anti-bot n'est devenu nécessaire depuis (ex. abus de signup constatés) sans que l'ADR-0006 ait été révisée |
| `csrf().disable()` sur l'API | **non documenté nulle part** | Toujours vrai qu'aucune route ne repose sur une session cookie ; signale une fois l'absence de documentation, suggère une ADR si la décision est réaffirmée |
| Credentials de dev en clair pour Postgres/MinIO (`lumen`/`lumen`, `lumen`/`lumen12345`) | `docs/adr/0005-postgresql-docker-compose-dev.md`, `docs/adr/0007-minio-stockage-visuels-utilisateurs.md` (externalisation via variables d'env mentionnée) | Ces defaults sont toujours inatteignables dans un environnement déployé — les variables d'env sont bien positionnées là où c'est le cas, pas silencieusement absentes |
| Absence de `CorsConfiguration` sur l'API Spring | Implicite dans l'architecture ADR-0003 (Next.js sert d'intermédiaire serveur pour tous les appels API) | Le navigateur n'appelle toujours jamais l'API Spring directement — à re-grep à chaque fois, pas à supposer |
| `NimbusJwtDecoder` construit depuis l'URI JWKS directement plutôt que `JwtDecoders.fromIssuerLocation` | Commentaire in-code dans `SecurityConfig.java` | Ce choix évite une dépendance réseau à Auth0 au démarrage — vérifier que la validation d'issuer/clé a bien lieu au premier décodage de token (comportement Nimbus standard), pas juste faire confiance au commentaire |
