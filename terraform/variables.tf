# ======================================
#
# Remplissez ici l'ID de votre projet avec PROJECT_ID (cf: ./README)
#
# ======================================
variable "project" {
  default = "rslm-devops"
}

# Région de la VM, similaire aux serveur de jeu dans les jeux vidéos
variable "region" {
  default = "us-west1"
}

# Zone plus précise de création de la VM
variable "zone" {
  default = "us-west1-a"
}

# Nom de l'instance qui apparaitra dans le cloudManager
variable "instance-name" {
  default = "romb38-instance"
}