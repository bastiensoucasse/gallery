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
