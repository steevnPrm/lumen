# Lumen — Design System

> La galerie numérique qui connecte artistes, organisateurs et mécènes.

Un design system inspiré des principes Apple : clarté, déférence au contenu, profondeur. L'interface s'efface pour laisser respirer les œuvres.

---

## 1. Principes directeurs

- **Clarté** — le texte est lisible à toute taille, les icônes sont précises, les éléments décoratifs sont subtils et ne rivalisent jamais avec le contenu.
- **Déférence** — l'UI est un support. Les œuvres, portfolios et expositions occupent le premier plan ; les contrôles restent discrets jusqu'à ce qu'on en ait besoin.
- **Profondeur** — hiérarchie visuelle par les calques, les ombres et le mouvement, pour donner du sens à la navigation sans surcharger.

---

## 2. Couleurs

### Palette principale

| Token | Hex | Usage |
|---|---|---|
| `color/background/primary` | `#FFFFFF` | Fond principal (light) |
| `color/background/primary-dark` | `#000000` | Fond principal (dark) |
| `color/background/secondary` | `#F5F5F7` | Sections, cartes |
| `color/background/secondary-dark` | `#1C1C1E` | Sections, cartes (dark) |
| `color/text/primary` | `#1D1D1F` | Texte principal |
| `color/text/secondary` | `#6E6E73` | Texte secondaire, légendes |
| `color/text/inverse` | `#F5F5F7` | Texte sur fond sombre |

### Couleur de marque

| Token | Hex | Usage |
|---|---|---|
| `color/accent/primary` | `#0071E3` | Actions principales, liens, focus |
| `color/accent/primary-hover` | `#0077ED` | État hover |
| `color/accent/primary-pressed` | `#005BB5` | État pressé |

### Sémantique

| Token | Hex | Usage |
|---|---|---|
| `color/success` | `#34C759` | Confirmation, succès |
| `color/warning` | `#FF9F0A` | Avertissement |
| `color/error` | `#FF3B30` | Erreur, action destructive |
| `color/separator` | `#D2D2D7` | Séparateurs, bordures fines |

---

## 3. Typographie

Police système : **SF Pro Display** (titres) / **SF Pro Text** (corps). Fallback web : `-apple-system, "Inter", "Helvetica Neue", sans-serif`.

| Style | Taille | Poids | Line-height | Usage |
|---|---|---|---|---|
| `type/display` | 56px | 700 | 1.05 | Hero, page d'accueil |
| `type/title-1` | 40px | 700 | 1.1 | Titres de section |
| `type/title-2` | 28px | 600 | 1.15 | Sous-titres |
| `type/title-3` | 22px | 600 | 1.2 | En-têtes de carte |
| `type/body` | 17px | 400 | 1.5 | Texte courant |
| `type/body-emphasis` | 17px | 600 | 1.5 | Texte mis en avant |
| `type/caption` | 13px | 400 | 1.4 | Légendes, métadonnées |
| `type/footnote` | 11px | 400 | 1.3 | Mentions légales |

**Règle** : jamais plus de 2 poids par écran. Le contraste vient de la taille et de l'espacement, pas de l'accumulation de styles.

---

## 4. Espacement

Système en base **8px**.

| Token | Valeur | Usage |
|---|---|---|
| `space/xs` | 4px | Espacement interne minimal |
| `space/sm` | 8px | Entre éléments proches (icône + label) |
| `space/md` | 16px | Padding standard de carte |
| `space/lg` | 24px | Entre blocs de contenu |
| `space/xl` | 40px | Entre sections |
| `space/2xl` | 64px | Marges de page, hero |
| `space/3xl` | 96px | Respiration entre grandes sections |

---

## 5. Rayons & élévation

### Border radius

| Token | Valeur | Usage |
|---|---|---|
| `radius/sm` | 8px | Boutons, champs |
| `radius/md` | 16px | Cartes, modales |
| `radius/lg` | 24px | Grandes cartes, hero visuels |
| `radius/full` | 999px | Pills, avatars |

### Élévation (ombres)

| Token | Valeur | Usage |
|---|---|---|
| `shadow/sm` | `0 1px 3px rgba(0,0,0,0.08)` | Cartes au repos |
| `shadow/md` | `0 4px 16px rgba(0,0,0,0.12)` | Cartes au survol |
| `shadow/lg` | `0 12px 32px rgba(0,0,0,0.16)` | Modales, éléments flottants |

---

## 6. Composants

### Boutons

| Variante | Fond | Texte | Usage |
|---|---|---|---|
| **Primary** | `accent/primary` | Blanc | Action principale (Publier, Soutenir) |
| **Secondary** | Transparent + bordure `separator` | `text/primary` | Action secondaire |
| **Ghost** | Transparent | `accent/primary` | Action tertiaire, liens d'action |
| **Destructive** | `error` | Blanc | Suppression, désabonnement |

- Hauteur standard : `44px`
- Padding horizontal : `space/lg`
- Radius : `radius/full` (style pill, signature Apple)
- Transition : `200ms ease-out` sur hover/press

### Cartes (Œuvre / Exposition)

- Radius : `radius/md`
- Ombre : `shadow/sm` au repos, `shadow/md` au survol
- Image en `radius/md` sur les bords supérieurs, ratio 4:5 pour les œuvres
- Padding interne : `space/md`
- Titre en `type/title-3`, métadonnées en `type/caption` (`text/secondary`)

### Navigation

- Barre translucide avec effet de flou (`backdrop-filter: blur(20px)`), fond semi-transparent
- Hauteur : `52px`
- Items centrés verticalement, `type/body-emphasis`
- Indicateur d'état actif : soulignement fin en `accent/primary`, jamais de fond plein

### Champs de formulaire

- Hauteur : `44px`
- Radius : `radius/sm`
- Bordure : `1px solid separator`, passe à `accent/primary` au focus (sans halo agressif)
- Placeholder en `text/secondary`

---

## 7. Mouvement

| Interaction | Durée | Courbe |
|---|---|---|
| Hover (boutons, cartes) | 200ms | `ease-out` |
| Ouverture modale | 300ms | `cubic-bezier(0.32, 0.72, 0, 1)` |
| Transition de page | 400ms | `cubic-bezier(0.4, 0, 0.2, 1)` |
| Micro-feedback (like, soutien) | 150ms | `ease-in-out` avec léger scale (1 → 1.05 → 1) |

**Règle** : le mouvement accompagne l'intention de l'utilisateur, jamais l'inverse. Pas d'animation décorative gratuite.

---

## 8. Grille & layout

- Grille 12 colonnes, gouttière `24px`
- Largeur max de contenu : `1200px`, centré
- Marges latérales : `space/2xl` (desktop), `space/md` (mobile)
- Breakpoints :

| Nom | Largeur |
|---|---|
| `mobile` | < 480px |
| `tablet` | 480–1024px |
| `desktop` | > 1024px |

---

## 9. Iconographie

- Style **outline**, épaisseur de trait constante (1.5px)
- Taille standard : `20px` (inline), `24px` (navigation)
- Jamais de remplissage plein sauf pour indiquer un état actif/sélectionné
- Cohérence stricte avec la famille SF Symbols en inspiration (pas de mélange de styles d'icônes)

---

## 10. Ton & contenu

- Phrases courtes, verbes à l'impératif pour les CTA (« Publier », « Soutenir cet artiste »)
- Pas de jargon technique côté utilisateur final
- Messages d'erreur explicites et bienveillants, jamais culpabilisants

---

## Annexe — Tokens résumés (JSON)

```json
{
  "color": {
    "background": { "primary": "#FFFFFF", "secondary": "#F5F5F7" },
    "text": { "primary": "#1D1D1F", "secondary": "#6E6E73" },
    "accent": { "primary": "#0071E3" }
  },
  "space": { "xs": 4, "sm": 8, "md": 16, "lg": 24, "xl": 40, "2xl": 64 },
  "radius": { "sm": 8, "md": 16, "lg": 24, "full": 999 }
}
```
