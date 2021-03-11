# PDL – Application client-serveur

## Backend

### Mise en place d’une application Spring Boot

- Création du projet Spring Boot `Backend` : OK.

### Conception et déploiement de l’application

- Implémentation de l’interface `DAO` : OK.
- Implémentation de la classe `Image` : OK.
- Implémentation de la classe `ImageDAO` : OK.
- Implémentation de la classe `ImageController` : OK.
- Implémentation de la classe `ImageControllerTests` : OK.

Toutes les fonctionnalités ont été ajoutées et les résultats sont concluants. Tous les tests passent à 100% sans encombre. Le projet `Backend` est donc achevé.

## Frontend

### Mise en place d’une application Vue.js

- Création du projet Vue.js `Frontend` : OK.
- Organisation des projets `Backend` et `Frontend` dans un environnement commun `Gallery` : OK.

### Conception et implémentation du client

- Implémentation d’une liste déroulante pour sélectionner une image dans la liste des images du serveur : OK.
- Implémentation de la prévisualisation automatique de l’image sélectionnée _(fonctionnalité supplémentaire)_ : OK.
- Implémentation du téléchargement de l’image sélectionnée : OK.
- Implémentation de la suppression de l’image sélectionnée _(fonctionnalité supplémentaire)_ : OK.
- Implémentation de l’ajout d’une nouvelle image au serveur : OK.
- Implémentation d’un affichage de toutes les images sous forme de galerie : OK.
- Implémentation d’une redirection vers l’image même en cas de clic sur l’une d’entre elles _(fonctionnalité supplémentaire)_ : OK.

### Autres améliorations

- Factorisation du code d’accès au backend : **Échec**.
- Design de la galerie pour un affichage moderne et ergonomique _(fonctionnalité supplémentaire)_ : OK.

Je n’ai pas trouvé comment réaliser la factorisation du code d’accès au backend malgré de multiples tentatives avec différentes approches telles que les trois ci-dessous.

- Appel à un script externe.

```html
<script src="http-api.js"></script>
```

- Importation d’un module API.

```js
import API from "http-api.js";
```

- Importation de fonctions externes.

```js
import * as api from "http-api.js";
```
