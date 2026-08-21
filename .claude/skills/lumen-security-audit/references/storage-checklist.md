# Checklist stockage — MinIO / données utilisateur

Fichiers de référence : `api/src/main/java/steevnPrm/lumen/storage/`,
`api/src/main/java/steevnPrm/lumen/visual/`.

Ce fichier documente ce qui est **déjà fait correctement** ici — utilise-le comme
gabarit pour évaluer tout nouvel endpoint touchant des données possédées par un
utilisateur, pas seulement comme une chasse aux bugs sur le code existant.

## Ce qui est en place

- Bucket MinIO privé (`lumen-user-visuals`), jamais de politique de lecture
  publique.
- Toute lecture passe par une URL pré-signée à durée limitée
  (`minio.presigned-url-expiry-seconds`, 900s / 15 min par défaut) — aucun accès
  objet direct/public.
- Clés d'objet namespacées `users/{id}/{uuid}{ext}` (`VisualService.upload`),
  où `{id}` est toujours l'id interne de l'utilisateur dérivé du `sub` du JWT
  (`jwt.getSubject()` dans `VisualController`), jamais d'un id fourni par le client.
- Scoping par propriétaire **au niveau de la requête DB**, pas juste dans le
  contrôleur : `UserVisualRepository.findByIdAndUser(id, user)` — un visuel qui
  n'appartient pas à l'appelant renvoie `VisualNotFoundException` → 404, jamais les
  données d'un tiers, et jamais une erreur qui distingue "n'existe pas" de
  "appartient à quelqu'un d'autre".
- Upload restreint côté serveur : type de contenu `image/*` uniquement, 5 Mo max
  (`spring.servlet.multipart.max-file-size`/`max-request-size`).

## Grille à appliquer sur tout nouvel endpoint touchant des données utilisateur

1. **Utilisateur dérivé du JWT**, jamais d'un id/paramètre fourni par le client
   (`@PathVariable`, `@RequestParam`, corps de requête).
2. **Chaque requête DB scopée par cet utilisateur** — un pattern
   `findByXAndUser(...)`, pas "chercher par id puis vérifier le propriétaire dans le
   code applicatif" (ce second pattern est facile à oublier sur un chemin de code,
   le premier est structurellement impossible à contourner).
3. **404, pas 403**, en cas de non-appartenance — évite de confirmer à un appelant
   non autorisé qu'un id existe.
4. **Validation serveur du type/taille** si c'est un upload — jamais uniquement
   côté client.

Si l'un de ces quatre points manque sur un nouvel endpoint, c'est un finding
concret et actionnable — cite ce pattern exact (`UserVisualRepository`/
`VisualController`) comme référence du correctif.
