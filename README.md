# Projet Gallery

Projet hébergé sur [GitHub](https://github.com/bastiensoucasse/gallery),
avec un miroir sur le [GitLab](https://gitlab.emi.u-bordeaux.fr/bsoucasse/gallery)
de l'Université de Bordeaux (CREMI).

## Installation

Dans le répertoire racine du projet, executer les commandes suivantes.

- Installation des dépendances

```
mvn clean install
```

- Lancement du serveur

```
mvn --projects backend spring-boot:run
```

- Déplacement dans le dossier frontend et lancement du client

```
cd frontend && npm run serve
```

Se connecter à http://localhost:8089.

## Tests

Le serveur a été testé sur les configurations suivantes.

**Systèmes :** Ubuntu 20.04 et 20.10 (via WSL2 sur Windows 10 20H2) et Debian 10 (Buster).\
**Navigateurs :** Google Chrome, Microsoft Edge et Mozilla Firefox.

## Base de données
Ci dessous un diagramme explicatif de l'implémentation de la base de données.

<iframe width="100%" height="500px" style="box-shadow: 0 2px 8px 0 rgba(63,69,81,0.16); border-radius:15px;" allowtransparency="true" allowfullscreen="true" scrolling="no" title="Embedded DrawSQL IFrame" frameborder="0" src="https://drawsql.app/pdl/diagrams/pdl/embed"></iframe>

Pour visualiser dans un navigateur:
https://drawsql.app/pdl/diagrams/pdl/embed