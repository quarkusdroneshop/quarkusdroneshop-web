terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "5.25.0"
    }
  }
  required_version = "1.8.1"
}

provider "google" {
  project = "your-gcp-project"
  region  = "asia-northeast1"
}

variable "env" {
  type = string
}

resource "google_storage_bucket" "example" {
  name          = "${var.env}-example-bucket"
  location      = "ASIA"
  storage_class = "MULTI_REGIONAL"
  versioning {
    enabled = true
  }
  uniform_bucket_level_access = true
  public_access_prevention = "enforced"
}