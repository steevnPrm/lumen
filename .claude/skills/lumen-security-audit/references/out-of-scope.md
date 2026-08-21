# Ce que ni ce skill ni claude-security ne couvrent

À rappeler explicitement à la fin de **chaque** audit, rapide ou complet — ne jamais
laisser entendre qu'un audit "fait" implique que ces sujets sont couverts :

- **Scan de dépendances/CVE** : rien n'est en place (pas de Trivy, pas de
  `mvn dependency-check` côté `api/`, pas de `npm audit` automatisé côté
  `auth-client/`). `claude-security` le dit lui-même explicitement : il complète,
  sans remplacer, un outillage SCA dédié.
- **Scan de secrets** : rien n'est en place (pas de gitleaks ou équivalent).
- **CI/automatisation** : le repo n'a **aucun répertoire `.github/`** — pas de
  GitHub Actions, pas de Dependabot, pas de CodeQL. Rien de ce qui précède ne
  tourne automatiquement à chaque push ou PR aujourd'hui ; tout audit de sécurité
  ici est, pour l'instant, un acte manuel.

Si l'utilisateur veut combler ces manques, ce sont des sujets séparés (mise en place
d'un scanner de dépendances, de secrets, et/ou d'une CI GitHub Actions) — pas
quelque chose que ce skill ou un audit `claude-security` résout au passage.
