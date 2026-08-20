# 0002. Backend en Spring Boot avec PostgreSQL

## Statut
Acceptée

## Contexte
Lumen a besoin d'une API backend pour gérer les profils d'artistes, les portfolios, les expositions temporaires et la monnaie virtuelle de soutien (voir la roadmap du [README](../../README.md)). Ce backend doit exposer une API REST consommée par le client web, gérer l'authentification/autorisation, et persister des données relationnelles (utilisateurs, œuvres, expositions, transactions) avec des relations claires entre entités.

## Décision
Le backend est développé en **Spring Boot** (Java 21), avec **Spring MVC** pour l'API REST, **Spring Data JPA** pour la persistance, et **PostgreSQL** comme base de données cible. Les dépendances de départ (`api/pom.xml`) incluent `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-validation` et `spring-boot-starter-actuator`.

## Conséquences
- Écosystème mature pour la sécurité (Spring Security), la validation et l'observabilité (Actuator), ce qui couvre une bonne partie des besoins non-fonctionnels dès le départ.
- PostgreSQL est adapté au modèle de données relationnel du projet (utilisateurs, œuvres, expositions, transactions liées) et gère bien la montée en charge à venir.
- Le driver PostgreSQL est en dépendance `runtime` du projet, mais aucune instance PostgreSQL n'est encore provisionnée pour le développement local — voir [ADR-0004](./0004-local-dev-database-h2.md) pour la solution retenue en attendant.
- JPA/Hibernate impose une discipline sur la modélisation des entités et des migrations de schéma (à outiller plus tard, par ex. Flyway/Liquibase, quand le modèle de données se stabilisera).
