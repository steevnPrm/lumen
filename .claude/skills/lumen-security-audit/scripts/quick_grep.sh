#!/usr/bin/env bash
# Points d'ancrage déterministes pour la checklist de sécurité Lumen. Lecture seule.
set -euo pipefail
cd "$(git rev-parse --show-toplevel)"

echo "== SecurityConfig.java =="
grep -n "csrf\|frameOptions\|permitAll\|anyRequest\|hasRole\|hasAuthority\|@PreAuthorize\|@Secured" \
  api/src/main/java/steevnPrm/lumen/authentification/SecurityConfig.java || true

echo
echo "== CORS config dans api/ =="
grep -rn "CorsConfiguration\|@CrossOrigin\|\.cors(" api/src || echo "  (aucune)"

echo
echo "== application*.properties : lignes de forme credential =="
grep -n "password\|secret\|key\|username" api/src/main/resources/application*.properties || true

echo
echo "== Pattern de scoping par propriétaire (findByXAndUser / getSubject) =="
grep -rn "findBy.*AndUser\|getSubject()\|AuthenticationPrincipal" api/src/main/java/steevnPrm/lumen || true

echo
echo "== Gestion de AUTH0_AUDIENCE =="
grep -rn "AUTH0_AUDIENCE" auth-client --include="*.js" --include="*.ts" --include="*.tsx" \
  --exclude-dir={node_modules,.next,dist,build,coverage} || true
[ -f auth-client/.env.local ] && grep -q "^AUTH0_AUDIENCE=" auth-client/.env.local \
  && echo "  auth-client/.env.local: AUTH0_AUDIENCE présent" \
  || echo "  auth-client/.env.local: AUTH0_AUDIENCE absent ou fichier introuvable"

echo
echo "== next.config.ts : headers de sécurité =="
grep -n "headers(" auth-client/next.config.ts || echo "  (pas de bloc headers())"

echo
echo "== proxy.js =="
cat auth-client/proxy.js

echo
echo "== Couverture des .gitignore =="
for f in .gitignore api/.gitignore auth-client/.gitignore; do
  [ -f "$f" ] && echo "  présent : $f" || echo "  ABSENT : $f"
done

echo
echo "== Appel direct de l'API Spring depuis le navigateur (devrait être vide) =="
grep -rn "API_BASE_URL\|localhost:8080\|NEXT_PUBLIC.*API" auth-client/app || echo "  (aucun trouvé)"

echo
echo "== Fichiers app-config/env potentiellement commités par erreur =="
# --pretty=format: supprime le message de commit, ne laisse que les noms de fichiers
git log --all --diff-filter=A --name-only --pretty=format: 2>/dev/null \
  | grep -iE "application-|\.env|credential|secret" | sort -u || echo "  (aucun historique suspect)"

echo
echo "== ADR existantes =="
ls docs/adr/*.md 2>/dev/null || echo "  (aucune)"

echo
echo "== CI / scan de dépendances / secrets =="
[ -d .github ] && echo "  .github/ présent" || echo "  pas de .github/ — aucune CI, Dependabot ou CodeQL"
