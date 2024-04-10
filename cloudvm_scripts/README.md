# Initialisation d'une VM avec google cloud

Ce script utilise l'API de google cloud pour lancer un service sur une machine virtuelle.

## Dépendances

Ce script suppose que vous avez installé [Google Cloud CLI](https://cloud.google.com/sdk/docs/install-sdk), que vous
l'avez confiqurer avec votre compte et que vous avez créé un projet sur le cloud.

## Execution

En premier lieu il vous faut aller dans le fichier init.sh et modifier les valeurs suivantes

```bash
PROJECT_ID="<Le nom de votre projet>"
INSTANCE_NAME="<Le nom que vous souhaitez donner à votre machine virtuelle>"
```

Ensuite, exécuter simplement la commande dans le fichier cloudvm_scripts

```bash
sudo chmod u+x ./init.sh && chmod u+x setupVM.sh && ./init.sh
```
L'API google étant lente, pour lancer plusieurs fois ce script, il est nécessaire d'attendre un certain temps.


Il est possible qu'il vous demande de créer une pair de clé la première fois que vous lancez le script. Il utilise ces
clé pour se connecter en shh. Créez en simplement une.

## Description

Le fichier **init.sh** se charge de lancer une instance de machine virtuelle (sous débian), d'exécuter le fichier
setupVM.sh et puis (dans notre cas) supprime la machine virtuelle. Dans le cas de l'implémentation d'un service continu,
il faudrait simplement la laisser tourner.

Le fichier **setupVM.sh** configure la machine virtuelle pour qu'elle puisser faire tourner notre scénario. Dans les
détails elle exécute les actions suivantes :

- Installation de docker
- Pull de l'image
- Exécution de l'image