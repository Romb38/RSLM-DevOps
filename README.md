# Projet DevOps : Une bibliothèque d’analyse de données en Java

Le but de ce projet est de mettre en place une procédure d'intégration continue d'un logiciel.

Nous cherchons ici à mettre en place un maximum d'outils, de façon cohérente, pour développer
une bibliothèque d'analyse de données en Java inspirée par la bibliothèque Pandas.

![badges_tests](https://github.com/Romb38/rslmdevops/actions/workflows/run-test.yml/badge.svg)
![badges_maven_deploy](https://github.com/Romb38/rslmdevops/actions/workflows/workflow.yml/badge.svg?branch=master)

## Table des matières
- [Description des fonctionnalités](#description-des-fonctionnalistés)
- [Documentation](#documentation)
- [Choix des outils](#choix-des-outils)
- [Workflow git](#workflow-git)
- [Docker](#docker)
- [Feedback](#feedback)

## Description des fonctionnalistés
**Dataframe** : Le type d’objet principal manipulé par RSLM sont les Dataframe. Les Dataframes sont
des tableaux en deux dimensions où chaque colonne est identifiée par un label et chaque ligne par un
index. Chaque colonne stock des données d’un seul type. Cependant deux colonnes différentes peuvent
stocker des types différents.

**Affichage d’un dataframe** : permet d'afficher les Dataframes. Il exites plusieurs vairantes de cette
méthode.
- affichage du dataframe en entier :
- affichage des premières lignes du Dataframe
- affichage des dernières lignes du Dataframe

**Selection de sous-ensemble** : permet de créer un nouveau Dataframe à partir d'une selection
d'un sous-ensemble d'un Dataframe.
- sélection d'un sous-ensemble de lignes
- sélection d'un sous-ensemble de colonnes
- séléction à partir d'un critère de valeur sur les colonnes (supérieur, inférieur, égalité)


**Selection avancée** :
permet de filtrer un DataFrame en fonction de critères complexes où pour chaque colonne qui permet de déterminer quelles lignes doivent être conservées.

## Documentation

Vous trouverez la documentation de ce projet sur [cette page](https://romb38.github.io/rslmdevops/index.html)

## Choix des outils
### Maven
Maven associés à différents plugin nous a permis d'organiser la gestion de ce projet.
#### JUnit (v4.13.2) 
Nous avons mis en place les tests unitaires de notre bibliothèque à l'aide de JUnit. Cet outils nous a permis d'automatiser les tests et les rapports de tests.
#### Jackson Mapper
Nous nous sommes servi de la bibliothèque Jackson Mapper pour la déserialisation des données CSV.
#### Doxygen-Maven-Plugin
Le plugin Doxygen-Maven nous a permis de mettre en place la documentation automatique de notre bibliothèque.
#### JaCoCo
JaCoCo mesure la couverture de code des tests unitaires mis en place à l'aide de JUnit. Nous avons choisi qu'une couverture de code par les tests de 80% minimum serait nécessaire pour notre bibliothèque.
### Github
Les fonctionalités de collaboration offertes par Github nous ont permis de mettre en place un développement en parallèle. Nous avons pu intégrer les pipelines de test automatisés et de couverture de code sur l'ensemble de nos branches. Ainsi que le déploiement automatisé sur la branche _master_ et la mise à jour automatique de la documentation sur la branche _github-pages_
### Terraform et Ansible
Terraform est un outils de création de machine virtuelle relié à différents services. Nous l'avons utiliser pour déployer des machines virtuelles sur Google Cloud. Il execute également sur la machine un script Ansible. Ce script permet d'installer Docker et lance la simulation que nous avons créé. Ensemble, ils permettent de déployer et de gérer l'infrastructure de manière reproductible et automatisée.
### Docker
Docker nous a permis de créer des conteurs. Cette plateforme offre des avantages pour le déploiement et la gestions des conteneurs, ce qui permet une meilleure portabilité de la bibliothèque. Le Dockerfile configure une image Docker pour une application Java, utilisant openjdk:2 comme image de base. Il définit un répertoire de travail (/app), copie le fichier JAR de l'application et un fichier CSV nécessaires pour l'exécution d'un scénario de notre bibliothèque. L'application est lancée avec le fichier CSV en argument.

# Workflows GitHub Actions

Ce dépôt contient plusieurs workflows configurés avec GitHub Actions pour assurer la qualité du code, automatiser le déploiement et gérer notre infrastructure de manière efficace.

## Aperçu des Workflows

### 1. Exécution des Tests JUnit et Couverture de Code à 80%

Ce workflow est conçu pour s'exécuter automatiquement sur toutes les branches sauf `master` et `github-pages`. Il garantit que chaque modification poussée passe tous les tests unitaires et atteint au moins 80% de couverture de code.

#### Déclencheurs
- Événements de push sur toutes les branches sauf `master` et `github-pages`.

#### Jobs
- **Exécution des Tests** : Exécute les tests unitaires en utilisant Maven et continue même si certains tests échouent pour permettre une analyse plus approfondie.
- **Vérification de la Couverture de Code** : Vérifie si la couverture de code est d'au moins 80%. Si ce n'est pas le cas, le workflow échoue pour empêcher la fusion de code insuffisamment testé.

### 2. Application Terraform

Ce workflow automatise le déploiement de l'infrastructure définie dans les scripts Terraform. Il est crucial pour maintenir des environnements cohérents et sécurisés à travers les phases de développement, de test et de production.

#### Déclencheurs
- Événements de push sur toutes les branches.

#### Jobs
- **Application Terraform** : Initialise et applique les configurations Terraform, approuve les changements automatiquement et détruit les déploiements précédents si nécessaire.

### 3. Exécution des Tests, Assurance de 80% de Couverture de Code, et Déploiement de la Documentation

Ce workflow complet gère plusieurs aspects du cycle de vie du développement logiciel. Il est déclenché sous conditions spécifiques pour tester, vérifier la couverture de code, publier de la documentation, et déployer des paquets et des conteneurs.

#### Déclencheurs
- Événements de push sur la branche `debug/doxygen`.
- Pull requests sur la branche `master`.

#### Jobs
- **Exécution des Tests et de la Couverture** : Similaire au premier workflow mais déclenché pour des branches et PR spécifiques.
- **Publication de Paquet** : Déploie le paquet logiciel maven si les tests et la couverture de code sont satisfaisants.
- **Déploiement de la Documentation** : Génère et déploie la documentation du projet en utilisant Doxygen.
- **Déploiement Docker** : Construit et publie une image Docker du projet, facilitant une distribution et un déploiement rapides.




## Workflow git 
### features branch 
Nous avons choisi pour procédure la méthode de workflow Features branch. Ce workflow est simple
à mettre en place est suffisant pour notre projet. Chaque nouvelle fonctionnalités sera développée sur 
une nouvelle branche.

### Procédure de validation des Pull/Merge requests
Chaque branches devra passer les tests et sera pull/merge à la suite d'un pull/merge request validé par au moins un autre 
collaborateurs. Nous avons choisi pour notre projet que chaque Pull/Merge requests devrait être validés par une personne minimum.

## Docker
[lien vers le dépôt Docker](https://hub.docker.com/repository/docker/skander23000/project_devops_2024/general)

## Feedback
Ce projet a été très stimulant pour nous, nous avons pu mettre en application de nombreux outils de travail collaboratif. De plus la large liberté dans le choix des outils et méthode à utiliser nous a permis de faire face à nos erreurs et adaptés nos choix en pleine conscience.
[Go to Top](#table-des-matières)
