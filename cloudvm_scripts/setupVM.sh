#!/bin/bash

# Installer Docker (si ce n'est pas déjà installé)
echo "Installation de Docker..."
sudo apt-get update
sudo apt-get install -y docker.io

# Télécharger l'image Docker de la baleine qui parle depuis Docker Hub
echo "Téléchargement de l'image Docker de la baleine qui parle..."
sudo docker pull docker/whalesay

# Exécuter le conteneur Docker avec un message spécifique
echo "Exécution du conteneur Docker..."
sudo docker run docker/whalesay cowsay "Je suis une baleine qui parle"
