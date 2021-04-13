# Projet Gallery

Projet hébergé sur [GitHub](https://github.com/bastiensoucasse/gallery), avec un
miroir sur le [GitLab](https://gitlab.emi.u-bordeaux.fr/bsoucasse/gallery) de
l'Université de Bordeaux (CREMI).

## Prérequis

### MySQL

Le projet nécessite une base de données MySQL. Veuillez donc installer MySQL
avant de poursuivre si ce n'est pas déjà le cas. Si vous ne savez pas, vous
pouvez vérifier en entrant la commande suivante.

```bash
mysql -V
```

Si une version de MySQL est affichée, vous pouvez passer à [l'installation du
projet](#installation-automatique). Sinon il faut installer MySQL. Pour un
système Debian ou basé sur Debian (par exemple Ubuntu), les paquets requis sont
présents sur les dépôts système. Il suffit d'entrer la commande suivante pour
lancer l'installation.

```bash
sudo apt install mysql-server mysql-client
```

Si vous êtes sous un autre système, référez-vous à la documentation de celui-ci.

## Installation automatique

Pour installer et lancer la galerie, il suffit d'utiliser le script bash dédié,
depuis la racine du projet.

```bash
./run.sh
```

Ce script va procéder aux vérifications de votre système afin d'installer et de
lancer le projet comme il le faut pour votre machine. Pour clore le serveur,
il suffira d'appuyer sur la touche q.

**NB :** Lorsque le message `Gallery project launched. Check logs in the logs
folder.` apparaît, il se peut que vous deviez patienter encore quelques secondes
avant de pouvoir accéder à l'application. Le serveur a été mis en route, mais
il peut prendre quelques secondes avant d'être effectif. Vous pouvez vérifier
cela dans les logs du backend, dans le fichier `logs/backend.log`.

Si vous relancez le projet, vous pourrez également choisir de réaliser une
nouvelle installation complète (en réinstallant les dépendances) en utilisant
l'option `--build`, également abrégée en `-b`, ou encore de réinitialiser la
base de données à ses valeurs par défaut grâce à l'option `--reset` ou `-r`. Il
est aussi possible de combiner les options.

Vous pouvez avoir accès à la liste des options en tapant la commande d'aide.

```bash
./run.sh --help
```

## Installation manuelle

**Attention :** Nous vous recommandons de passer par l'installation automatique
afin d'être sûr que le serveur est correctement installé et lancé selon les
paramètres de votre machine.

### Initialisation de la base de données

Assurez-vous que votre serveur de base de donées MySQL est bien démarré. Si ce
n'est pas le cas, entrez la commande suivante pour le démarrer.

```bash
sudo service mysql start
```

Vous pouvez également utiliser cette commande si la précédente venait à ne pas
fonctionner.

```bash
# Si la commande précédente n'a pas fonctionné
sudo /etc/init.d/mysql start
```

Rendez vous dans le client de MySQL depuis le répertoire racine du projet.

``` bash
sudo mysql
```

Si vous souhaitez réinitialiser la base de données, entrez cette commande.
Sinon vous pouvez directement passer à la commande suivante.

```sql
-- Si vous souhaitez réinitialiser la base de données
source database/db_reset.sql;
```

Exécutez le script d'initialisation de la base de données.

```sql
source database/db_init.sql;
```

Vous pouvez quitter le client MySQL.

```sql
quit;
```

### Installation du projet

Dans le répertoire racine du projet, executer les commandes suivantes.

- Installation des dépendances s'il s'agit du premier lancement ou d'une
réinstallation.

```bash
# S'il s'agit de la première installation ou d'une réinstallation
mvn clean install
```

- Installation du backend.

```bash
mvn --projects backend spring-boot:run
```

- Installation du frontend (le backend ayant pris le contrôle du terminal, il
faudra en ouvrir un nouveau toujours à la racine du projet).

```bash
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

Voici un diagramme explicatif de l'implémentation de la base de données.\
[Visualiser dans un navigateur](https://drawsql.app/pdl/diagrams/pdl/embed)
