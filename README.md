# Projet DevOps : Une bibliothèque d’analyse de données en Java

Le but de ce projet est de mettre en place une procédure d'intégration continue d'un logiciel.
Nous cherhcons ici à metre en place un maximum d'ouitls, de façon cohérente, pour développer
une bibliothèque d'analyse de données en Java inspiré par la bibliothèque Pandas.

![badges_tests](https://github.com/Romb38/rslmdevops/actions/workflows/run-test.yml/badge.svg)
![badges_maven_deploy](https://github.com/Romb38/rslmdevops/actions/workflows/maven-deploy.yml/badge.svg?branch=master)
![badges_documentation](https://github.com/Romb38/rslmdevops/actions/workflows/DoxygenDocs.yml/badge.svg?branch=master)

## Table des matières
- [Description des fonctionnalités](#description-des-fonctionnalistés)
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

## Choix des outils
### Maven
_explication_
### Junit
_explication_
### Github
_explication_
### Docker
_explication_

## Workflow git
### features branch
Nous avons choisi pour procédure la méthode de workflow Features branch. Ce workflow est simple
à mettre en place est suffisant pour notre projet. Chaque nouvelle fonctionnalités sera développer sur
une nouvelle branche. Chaque branchedevra passer les test et sera pull/merge à la suite d'un pull/merge request validé par au moins un autre
collaborateurs. _suite explication_

### procédure de validation des Pull/Merge requests

## Docker
_liste des images et liens vers le dépot Docker_

## Feedback

[Go to Top](#table-des-matières)
