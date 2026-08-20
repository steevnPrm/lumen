# 0001. Consigner les décisions d'architecture via des ADR

## Statut
Acceptée

## Contexte
Le projet Lumen est en tout début de vie : le socle backend (Spring Boot) et frontend (Next.js) viennent d'être posés, ainsi qu'un premier design system. Plusieurs choix techniques structurants ont déjà été faits (framework backend, base de données de dev, authentification, stack frontend) sans trace écrite du raisonnement derrière chacun. Sans historique, ces choix devront être ré-expliqués ou ré-justifiés à chaque nouvelle personne qui rejoint le projet, ou risquent d'être remis en question sans connaître les contraintes d'origine.

## Décision
Toute décision d'architecture significative est documentée dans `docs/adr/` sous forme d'Architecture Decision Record, suivant le format décrit dans [`docs/adr/README.md`](./README.md).

## Conséquences
- Le contexte d'une décision reste accessible même après le départ de la personne qui l'a prise.
- Remettre en cause un choix existant passe par la lecture de l'ADR correspondante avant toute discussion, ce qui évite de refaire les mêmes débats.
- Ça ajoute une étape (rédiger l'ADR) aux décisions structurantes — acceptable au vu du bénéfice pour un projet appelé à grandir avec plusieurs contributeurs.
