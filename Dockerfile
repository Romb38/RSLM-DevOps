# Étape 1: Partir d'une image officielle OpenJDK 21
FROM openjdk:21

# Installer Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copier votre projet dans l'image
COPY src /home/app/src
COPY pom.xml /home/app
COPY csv_directory/csv_devops.csv /home/app

# Définir le répertoire de travail
WORKDIR /home/app

# Exécuter l'application avec Maven, en spécifiant le chemin du fichier CSV
CMD ["mvn", "exec:java", "-Dexec.args=/home/app/csv_devops.csv"]
