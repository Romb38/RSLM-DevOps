locals {
  ssh_user         = "ansible"
  private_key_path = "~/.ssh/ansbile_ed25519"
}


provider "google" {
  credentials = "${file("mysvcdevops.json")}"
  project     = var.project
  region      = var.region
  zone        = var.zone
}

resource "google_compute_instance" "vm_instance" {
  count        = 1
  name         = "${var.instance-name}-${count.index}"
  machine_type = "f1-micro"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
    }
  }

  network_interface {
    network = "default"
    access_config {
    }
  }

  metadata = {
    "ssh-keys" = <<EOT
      dev:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQD8XsRdFu7p54vw6PkZHpMx3d9IliW65/dcioRta1BjB6iv3qaUH0Z5HHS/S09yWfPpioeEJ4A1pZOyKIMz2b/u0lu9ZmxBaCzN89Q2SMj3mGmXZVdEUNxgAknnvtJNCjipiqOsDD37rVNhi8SjeUTvJ/EGifCoMbfLdq+ZpGSqhwOvo5cHc5mm+xFxgJXci2wERe1LGVHVxs7v/yLvYepP9gsb7vu5oD4FxK3mKZxDWGHNcK6aPBlBowsOtMX+ad/UgRN+Gf0IGTy4KnR55zZILiAGvHxXZhCX2wT8gVaBLJZ8BrLAHm6Bo29HYCyqsi/uhPxKx/QUdKKiMnkT3C4cOmHLPSlLDj3OEDZs/Ktc72NtLienis+Z40WfLV7TxoRuP0dNEeay2Dcth8dtcATXgmRFVkbS467ri1I06E18eV1jTDstAsR5OO3PRqyLR6tcN+ekV3GHoj9CEJfQZgFFn91vwsCLsqafmorq+BuADm/zE1aAeQoJglnmqmvAgX8= dev
      EOT
  }


  provisioner "remote-exec" {
    inline = [
      "sudo apt update",
      "sudo apt install -y software-properties-common"
    ]

    connection {
      type        = "ssh"
      user        = "dev"
      host        = self.network_interface[0].access_config[0].nat_ip
    }
  }

  provisioner "local-exec" {
    command = "ansible-playbook -u dev -i ${self.network_interface.0.access_config.0.nat_ip}, ./main.yml"
  }
}

// A variable for extracting the external ip of the instance
output "ip" {
 value = "${google_compute_instance.vm_instance[0].network_interface.0.access_config.0.nat_ip}"
}



