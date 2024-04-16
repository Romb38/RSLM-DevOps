FROM openjdk:21-slim

# Créer un répertoire pour votre application
WORKDIR /app

# Copier le JAR de l'application dans l'image Docker
COPY target/project_devops_2024.jar /app/project_devops_2024.jar

# Copier également le fichier CSV dans l'image Docker, à un emplacement connu
COPY csv_directory/csv_devops.csv /app/csv_devops.csv

# Exécuter l'application Java, en passant le chemin du fichier CSV comme argument
CMD ["java", "-jar", "project_devops_2024.jar", "/app/csv_devops.csv"]