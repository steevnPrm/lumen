# Lumen

> La galerie numérique qui connecte artistes, organisateurs et mécènes.

Lumen est une plateforme d'exposition d'art numérique qui permet aux artistes de présenter leurs œuvres, aux organisateurs de créer des expositions en ligne limitées dans le temps, et aux spectateurs de soutenir les créateurs.

## ✨ Vision

Les artistes ont besoin d'un espace simple pour mettre en valeur leur portfolio. Les organisateurs doivent pouvoir monter et animer des expositions numériques, tandis que les spectateurs recherchent une façon directe de découvrir et soutenir les artistes.

## 👥 Utilisateurs

- **Artistes** : publient et présentent leurs contenus sur leur profil/portfolio.
- **Organisateurs** : créent et gèrent des événements d'exposition à durée limitée.
- **Spectateurs** : découvrent les œuvres et soutiennent les artistes via une monnaie virtuelle.

## 🎯 Périmètre initial

- Profils et portfolios d'artistes
- Publication de contenus artistiques
- Création d'expositions numériques temporaires
- Mécanisme de soutien aux artistes fondé sur une monnaie virtuelle

## 🛠️ Stack technique

| Domaine | Technologie |
|---|---|
| Authentification | Auth0, Spring Security |
| API | Spring MVC, REST Client |
| Stockage et persistance | AWS S3, JPA |
| Client web | Next.js |

## 🗺️ Roadmap (Milestones)

1. **MVP - profil & portfolio** — Création de compte, upload d'œuvres, portfolio personnalisable.
2. **Publication de contenu** — Ajout, édition, catégorisation des œuvres.
3. **Expositions temporaires** — Création et animation d'expositions numériques limitées dans le temps.
4. **Monnaie virtuelle et soutien** — Achat, transfert et suivi des contributions des spectateurs vers les artistes.

## 📦 Installation

```bash
# Cloner le repo
git clone https://github.com/<votre-user>/lumen.git
cd lumen

# Installer les dépendances (frontend)
cd client
npm install
npm run dev

# Backend (Spring Boot)
cd ../api
./mvnw spring-boot:run
```

## 📄 Résultat attendu

Une expérience cohérente pour exposer, organiser et découvrir l'art numérique, tout en facilitant le soutien des spectateurs aux artistes.

## 📋 Licence

À définir.# lumen
