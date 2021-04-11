# Projet Gallery

Projet hébergé sur [GitHub](https://github.com/bastiensoucasse/gallery),
avec un miroir sur le [GitLab](https://gitlab.emi.u-bordeaux.fr/bsoucasse/gallery)
de l'Université de Bordeaux (CREMI).

## Installation et lancement

### Installation de la base de données

Le projet nécessite une base de donnée MySQL. Veuillez donc installer MySQL avant
de poursuivre si ce n'est pas déjà le cas. Si vous ne savez pas vous pouvez vérifier
en entrant la commande suivante.

```
mysql -V
```

Si une version de mysql est affichée, vous pouvez vous rendre à
[la partie suivante](#initialisation-de-la-base-de-données). Sinon il faut procéder
à l'installation. Pour un système Debian ou basé sur Debian (par exemple Ubuntu), les
paquets requis sont présents sur les dépôts système. Il suffit d'entrer la commande
suivante pour lancer l'installation.

```
sudo apt install mysql-server mysql-client
```

Si vous êtes sous un autre système, référez-vous à la documentation de votre système.

### Initialisation de la base de données

Assurez-vous que votre serveur de base de donées MySQL est bien démarré. Si
ce n'est pas le cas, entrez la commande suivante pour le démarrer.

```
sudo /etc/init.d/mysql start
```

ou bien cette commande.

```
sudo service mysql start
```

Rendez vous dans le client de MySQL depuis le répertoire racine du projet.

```
sudo mysql
```

Exécutez le script d'initialisation de la base de données.

```
source init_db.sql;
```

Vous pouvez quitter le client MySQL.

```
quit;
```

### Installation du projet

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

### Lancement de la galerie

Connectez-vous à [http://localhost:8089](http://localhost:8089) via
votre navigateur afin d'accéder à votre galerie.

## Tests

Le serveur a été testé sur les configurations suivantes.

**Systèmes :** Ubuntu 20.04 et 20.10 (via WSL2 sur Windows 10 20H2) et Debian 10 (Buster).\
**Navigateurs :** Google Chrome, Microsoft Edge et Mozilla Firefox.

## Base de données

Ci dessous un diagramme explicatif de l'implémentation de la base de données.

<iframe width="100%" height="500px" style="box-shadow: 0 2px 8px 0 rgba(63,69,81,0.16); border-radius:15px;" allowtransparency="true" allowfullscreen="true" scrolling="no" title="Embedded DrawSQL IFrame" frameborder="0" src="https://drawsql.app/pdl/diagrams/pdl/embed"></iframe>

Pour visualiser dans un navigateur:
https://drawsql.app/pdl/diagrams/pdl/embed
