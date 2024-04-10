# Étape 1: Utiliser une image de base Maven+JDK pour construire et exécuter l'application
FROM maven:3.8.4-openjdk-21

# Copier les fichiers sources et le pom.xml dans l'image
COPY src /home/app/src
COPY pom.xml /home/app
COPY csv_directory/csv_devops.csv /home/app/
# Définir le répertoire de travail
WORKDIR /home/app

# Commande pour exécuter l'application directement avec Maven
CMD ["mvn", "exec:java", "-Dexec.args=/home/app/csv_devops.csv"]


