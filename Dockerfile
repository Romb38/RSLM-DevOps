FROM openjdk:21-slim

# Créer un répertoire pour votre application
WORKDIR /app

# Copier le JAR de l'application dans l'image Docker
# Assurez-vous que le chemin et le nom du fichier JAR correspondent à ceux de votre projet
COPY target/ESLM-DevOps-1.0-SNAPSHOT.jar /app/ESLM-DevOps.jar

# Copier également le fichier CSV dans l'image Docker, à un emplacement connu
# Remplacer 'path/to/your/csv_directory' par le chemin vers le répertoire contenant votre fichier CSV dans votre projet
COPY csv_directory/csv_devops.csv /home/app

# Exécuter l'application Java, en passant le chemin du fichier CSV comme argument
CMD ["java", "-jar", "ESLM-DevOps.jar", "csv_devops.csv"]
