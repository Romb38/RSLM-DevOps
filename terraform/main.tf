variable "project" { }
variable "credentials_file" { }
variable "gcp_user" { }
variable "ssh_public_key" { }
variable "vm_name" { default = "virtualmachine" }
variable "vm_type" { default = "f1-micro" }
variable "region" {  default = "europe-west1"}
variable "zone" {  default = "europe-west1-b"}
variable "image" { default = "ubuntu-os-cloud/ubuntu-2004-focal-v20210623" }

terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = ">= 3.5.0"
    }
  }
  required_version = ">= 1.2.3"
}

provider "google" {
  credentials = file(var.credentials_file)
  project = var.project
  region  = var.region
  zone    = var.zone
}

resource "google_compute_instance" "vm_instance" {
  name         = var.vm_name
  machine_type = var.vm_type
  tags         = ["http-server"]

  metadata_startup_script = "sudo apt-get update ; sudo apt-get install -yq python3"

  metadata = {
    ssh-keys = "${var.gcp_user}:${file(var.ssh_public_key)}"
  }


  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network = "default"
    access_config {
    }
  }
}