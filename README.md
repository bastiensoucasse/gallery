# Projet Gallery

Projet hébergé sur [GitHub](https://github.com/bastiensoucasse/gallery), avec un
miroir sur le [GitLab](https://gitlab.emi.u-bordeaux.fr/bsoucasse/gallery) de
l'Université de Bordeaux (CREMI).

## Prérequis

### MySQL

Le projet nécessite une base de donnée MySQL. Veuillez donc installer MySQL
avant de poursuivre si ce n'est pas déjà le cas. Si vous ne savez pas vous
pouvez vérifier en entrant la commande suivante.

```
mysql -V
```

Si une version de MySQL est affichée, vous pouvez passer à [l'installation du
projet](#installation-automatique). Sinon il faut installer MySQL. Pour un
système Debian ou basé sur Debian (par exemple Ubuntu), les paquets requis sont
présents sur les dépôts système. Il suffit d'entrer la commande suivante pour
lancer l'installation.

```
sudo apt install mysql-server mysql-client
```

Si vous êtes sous un autre système, référez-vous à la documentation de celui-ci.

## Installation automatique

Pour installer et lancer la galerie, il suffit d'utiliser le script bash dédié,
depuis la racine du projet.

```
./run.sh
```

Ce script va procéder aux vérifications de votre système afin d'installer et de
lancer le projet comme il le faut pour votre machine.

## Installation manuelle

**Attention :** Nous vous recommandons de passer par l'installation automatique
afin d'être sûr que le serveur est correctement installé et lancé selon les
paramètres de votre machine.

### Initialisation de la base de données

Assurez-vous que votre serveur de base de donées MySQL est bien démarré. Si ce
n'est pas le cas, entrez la commande suivante pour le démarrer.

```
sudo service mysql start
```

Vous pouvez également utiliser cette commande si la précédente venait à ne pas
fonctionner.

```
sudo /etc/init.d/mysql start
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

- Installation du backend

```
mvn --projects backend spring-boot:run
```

- Installation du client

```
cd frontend && npm run serve
```

## Accès à l'application

Connectez-vous à [http://localhost:8089](http://localhost:8089) via votre
navigateur afin d'accéder à la galerie.

## Informations supplémentaires

### Tests

Le serveur a été testé sur les configurations suivantes.

**Systèmes :** Ubuntu 20.04 et 20.10 (via WSL2 sur Windows 10 20H2) et Debian 10
(Buster).\
**Navigateurs :** Google Chrome, Microsoft Edge et Mozilla Firefox.

### Base de données

Ci dessous un diagramme explicatif de l'implémentation de la base de données.

<iframe width="100%" height="500px" style="box-shadow: 0 2px 8px 0 rgba(63,69,81,0.16); border-radius:15px;" allowtransparency="true" allowfullscreen="true" scrolling="no" title="Embedded DrawSQL IFrame" frameborder="0" src="https://drawsql.app/pdl/diagrams/pdl/embed"></iframe>

[Visualiser dans un navigateur](https://drawsql.app/pdl/diagrams/pdl/embed)
