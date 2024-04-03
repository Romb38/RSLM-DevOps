# Utiliser l'image de base Ubuntu
FROM ubuntu:latest

# Mettre à jour les paquets et installer Java
RUN apt-get update && \
    apt-get install -y openjdk-11-jre

# Copier le fichier JAR compilé dans l'image
COPY target/project_devops_2024.jar /usr/app/

# Définir le répertoire de travail
WORKDIR /usr/app

# Commande pour exécuter l'application
CMD ["java", "-jar", "project_devops_2024.jar"]
