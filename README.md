# Projet DevOps : Une bibliothèque d’analyse de données en Java

Le but de ce projet est de mettre en place une procédure d'intégration continue d'un logiciel.

Nous cherhcons ici à metre en place un maximum d'ouitls, de façon cohérente, pour développer
une bibliothèque d'analyse de données en Java inspiré par la bibliothèque Pandas.

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
index. Chaque colonne stocke des données d’un seul type. Cependant deux colonnes différentes peuvent
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
- _séléction avancé_


## Documentation

Vous trouverez la documentation de ce projet sur [cette page](https://romb38.github.io/rslmdevops/index.html)

## Choix des outils
### Maven
Maven associés à différents plugin nous a permis d'organiser la gestion de ce projet.
#### JUnit (v4.13.2) 
Nous avons mis en place les tests unitaires de notre bibliothèque à l'aide de JUnit. Cet outils nous a permis d'automatiser les tests et les rapports de tests.
#### Jackson Mapper
Nous nous sommes servi de la bibliothèque Jackson Mapper pour la manipulation des données JSON.
#### Doxygen-Maven-Plugin
Le plugin Doxygen-Maven nous a permis de mettre en place la documentation automatique de notre bibliothèque.
#### JaCoCo
JaCoCo mesure la couverture de code des tests unitaires mis en place à l'aide de JUnit. Nous avons choisi qu'une couverture de code par les tests de 80% minimum serait nécessaire pour notre bibliothèque.
### Github
Les fonctionalités de collaboration offertes par Github nous ont permis de mettre en place un développement en parallèle. Nous avons pu intégrer les pipelines de test automatisés et de couverture de code sur l'ensemble de nos branches. Ainsi que le déploiement automatisé sur la branche _master_ et la mise à jour automatique de la documentation sur la branche _github-pages_
### Terraform et Ansible
Terraform est un outils de création de machine virtuelle relié à différents services. Nous l'avons utiliser pour déployer des machines virtuelles sur Google Cloud. Il execute également sur la machine un script Ansible. Ce script permet d'installer Docker et lance la simulation que nous avons créé. Ensemble, ils permettent de déployer et de gérer l'infrastructure de manière reproductible et automatisée.
### Docker
Docker nous a permis de créer des conteurs. Cette plateforme offre des avantages pour le déploiement et la gestions des conteneurs, ce qui permet une meilleure portabilité de la bibliothèque.


## Workflow git 
### features branch 
Nous avons choisi pour procédure la méthode de workflow Features branch. Ce workflow est simple
à mettre en place est suffisant pour notre projet. Chaque nouvelle fonctionnalités sera développer sur 
une nouvelle branche. Chaque branchedevra passer les test et sera pull/merge à la suite d'un pull/merge request validé par au moins un autre 
collaborateurs. _suite explication_

### Procédure de validation des Pull/Merge requests
Nous avons choisi pour notre projet que chaque Pull/Merge requests devrait être validés par une personne minimum.

## Docker
_liste des images et liens vers le dépot Docker_

## Feedback
Ce projet a été très stimulant pour nous, nous avons pu mettre en application de nombreux outils de travail collaboratif. De plus la large liberté dans le choix des outils et méthode à utiliser nous a permis de faire face à nos erreurs et adaptés nos choix en pleine conscience.
[Go to Top](#table-des-matières)
