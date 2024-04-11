# Fonctionnement du cloud

Pour fonctionner, la partie cloud se base sur 3 outils différents :
```
gcloud
terraform
ansible
```

Ansible ne sera pas explicitement utilisé dans ce tutoriel mais il est interne à l'application.

Pour installer gcloud, il faut suivre [ce tuto](https://cloud.google.com/sdk/docs/install)
Il faut également l'initialiser en suivant [ce tuto](https://cloud.google.com/sdk/docs/initializing)

Pour installer terraform et ansible (sous debian) les commandes suivantes suffisent

```bash
sudo apt install terraform
sudo apt install ansible
```

## Initialisation du gcloud

On commence en créant un projet (défini grâce à son PROJECT_ID) puis on lui ajoute un service. Enfin, on créer un fichier d'informations pour le login.
Ce fichier d'information doit se placer dans le dossier terraform


```bash
#Fill PROJECT_ID and SERVICE_ID with your informations
PROJECT_ID=my_project
SERVICE_ID=my_service

gcloud projects create $(PROJECT_ID)
gcloud iam service-accounts create $(SERVICE_ID)
gcloud projects add-iam-policy-binding rslm-devops --member serviceAccount:$(SERVICE_ID)@$(PROJECT_ID).iam.gserviceaccount.com --role roles/editor
gcloud iam service-accounts keys create mycredentials.json --iam-account mysvcdevops@PROJECT_ID.iam.gserviceaccount.com
```

Il faut également modifier dans le fichier variables.tf le nom du projet pour mettre PROJECT_ID

## Ajout d'une clé SSH

Pour pouvoir se connecter à la machine distante, il faut obligatoirement une clé ssh.
Récuperez la sortie de cette commande et placez la clé publique ssh dans le fichier simple_deployement.tf.

```bash
ssh-keygen -t rsa -b 4096 -f ~/.ssh/cloudkey && cat ~/.ssh/cloudkey.pub
```

## Lancement de la machine virtuelle

Si tout s'est bien déroulé, vous devriez pouvoir lancer les commandes suivantes

```bash
terraform init

# Vérifiez ce que va faire la machine virtuelle
terraform plan

# Lancement de la machine virtuelle
terraform aaply -auto-approve
```