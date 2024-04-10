#!/bin/bash

# ================== Changez ici ==================
PROJECT_ID="rslm-devops"
INSTANCE_NAME="devops-instance"
#======================================================

# ================== NE PAS TOUCHER ==================
ZONE="us-central1-b"
SCRIPT_PATH="./setupVM.sh"
MACHINE_TYPE="n1-standard-1"
IMAGE_FAMILY="debian-10"
IMAGE_PROJECT="debian-cloud"

# ================== Création de l'instance ==================
# Vérifier si l'instance existe déjà
echo "Vérification de l'existence de l'instance..."
if gcloud compute instances describe $INSTANCE_NAME --zone=$ZONE --project=$PROJECT_ID &>/dev/null; then
    echo "L'instance existe déjà. Pas besoin de créer une nouvelle instance."
else
    # Création de l'instance
    echo "Création de l'instance..."
    gcloud compute instances create $INSTANCE_NAME \
        --zone=$ZONE \
        --project=$PROJECT_ID \
        --machine-type=$MACHINE_TYPE \
        --image-family=$IMAGE_FAMILY \
        --image-project=$IMAGE_PROJECT \
        --tags=http-server,https-server
fi

# ================== Démarrage de l'instance ==================
echo "Démarrage de l'instance..."
gcloud compute instances start $INSTANCE_NAME --zone=$ZONE --project=$PROJECT_ID

# ================== Transfert du script setupVM.sh vers l'instance ==================
echo "Transfert du script setup.sh vers l'instance..."
gcloud compute scp $SCRIPT_PATH $INSTANCE_NAME:~/ --zone=$ZONE --project=$PROJECT_ID

# ================== Exécution du script setupVM.sh sur l'instance ==================
echo "Exécution du script setup.sh sur l'instance..."
gcloud compute ssh $INSTANCE_NAME --zone=$ZONE --project=$PROJECT_ID --command="chmod +x ~/setupVM.sh && ~/setupVM.sh"

# ================== Arrêt de l'instance ==================
echo "Arrêt de l'instance..."
gcloud compute instances stop $INSTANCE_NAME --zone=$ZONE --project=$PROJECT_ID --quiet

echo "Opération terminée."
