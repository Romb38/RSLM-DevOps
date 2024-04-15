
# Définition du fournisseur de vm
provider "google" {
  credentials = "${file("mycredentials.json")}"
  project     = var.project
  region      = var.region
  zone        = var.zone
}

# Déclaration de/des vm
resource "google_compute_instance" "vm_instance" {
  # Nombre de VM crées
  count        = 1

  # Nom de chaque VM
  name         = "${var.instance-name}-${count.index}"

  # Type de la machine, ici c'est une des plus petites que l'on peut trouver
  machine_type = "f1-micro"

  # OS que l'on mets sur la machine, ici debian-10
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
    }
  }

  # On ne fait pas de résaux donc on laisse les configurations telles quelles
  network_interface {
    network = "default"
    access_config {
    }
  }

  # On ajoute notre clé publique dans la liste des hôtes acceptés sur la machine
  metadata = {
    # ======================================
    #
    # Ajoutez ici votre clé publique (cf : ./README) de la manière suivante
    # dev:<VOTRE_CLE> dev
    #
    # ======================================
    "ssh-keys" = <<EOT
      dev:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDqz+4twE1qfgOKt2b7KUlfxBuezyp6E3tGnrJz+RTMISaZFye75Jfi8R8kwTsdnAAs4ABfjtNE3IBUhB9AZT/JCmNK3uzj2X3LiZaGHMVROKJEVfUEa0usbSFnacElZa3+3iWN3iYU1AVjE/kky8Cz7I7OlwD15hiNW6NaxHJGQ0eJOmiBJwAMW8b0htq6qnySE19BZ9GJQueucuFq8AITVDoYOSj95qS9yx2Q9nKBhUYP41sJs1GA0NuKgdZYx0uYESrlLOUazAKrME6++c8IlRogb8/7AWUe4CKRaeQNRLvtdvK2yLz0tO54P4rjrfDRrMP3Ao7emmCDl1FsXLu8Zhg9P287ZxLGgoi9S8IAGqvkCm/8z50/qaP36UdnA+WCT0srBoiiq35tNDEs9xFh0NAReSYr5u11yatRRCfI5lItEFz2xcWTFFxlThL2/xV6y52UEvm4LWA1KgVTs20u3AakXm7GsP5qLBaOUzdtVTNZe5Rw+HupZ+hdmX2c/Ac= dev
      EOT
  }

  # On commence ensuite à executer du code sur la VM
  provisioner "remote-exec" {
    # On fait les mise à jours classiques et on installe python et autres outillages
    inline = [
      "sudo apt update",
      "sudo apt install -y software-properties-common"
    ]

    # On utilise la clé ssh pour se connecter, elle utilise la clé privée que vous avez en local dans ~/.shh/
    connection {
      type        = "ssh"
      # L'utilisateur dev est défini grâce a la clé ssh fourni dans les metadata
      user        = "dev"
      # Adresse IP de la machine
      host        = self.network_interface[0].access_config[0].nat_ip
    }
  }

  # Ensuite on exécute depuis un fichier local une commande pour modifier la VM
  # Pour cela, on utilise ansible et une connexion ssh avec la même clé privée que la dernière fois
  provisioner "local-exec" {
    command = "ansible-playbook -u dev -i ${self.network_interface.0.access_config.0.nat_ip}, ./ansible/main.yml"
  }
}

# On extrait l'adresse IP de la VM que l'on à créé pour pouvoir s'y connecter
output "ip" {
 value = "${google_compute_instance.vm_instance[0].network_interface.0.access_config.0.nat_ip}"
}



