# README

## GCP Setup

### Main GCP account has no rights to update Org Policy

Default Org Policy disables the ability to create key for service accounts.

$ gcloud organizations add-iam-policy-binding 1030048410468 --member='user:gulen.chongtham@gmail.com' --role='roles/orgpolicy.policyAdmin'

$ gcloud org-policies delete "constraints/iam.disableServiceAccountKeyCreation" --organization=1030048410468 

## Cloud Run Deployment

### Git repository cloned in Cloud Shell

```log
gulen_chongtham@cloudshell:~$ cd repos

gulen_chongtham@cloudshell:~/repos$ git clone https://github.com/gulench/gcp-pubsub-demo.git
Cloning into 'gcp-pubsub-demo'...
remote: Enumerating objects: 35, done.
remote: Counting objects: 100% (35/35), done.
remote: Compressing objects: 100% (21/21), done.
remote: Total 35 (delta 0), reused 35 (delta 0), pack-reused 0 (from 0)
Receiving objects: 100% (35/35), 11.81 KiB | 11.81 MiB/s, done.

gulen_chongtham@cloudshell:~/repos$ cd gcp-pubsub-demo/
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo$ ls
mvnw  mvnw.cmd  pom.xml  README.md  src
```

### Default Configuration

```log
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo$ gcloud config list
[accessibility]
screen_reader = True
[component_manager]
disable_update_check = True
[compute]
gce_metadata_read_timeout_sec = 30
[core]
account = gulen.chongtham@gmail.com
disable_usage_reporting = False
universe_domain = googleapis.com
[metrics]
environment = devshell

Your active configuration is: [cloudshell-5663]
```

### Set Project and Region

```log
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo$ gcloud config set project project-602c1df5-e612-4947-a79

gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo (project-602c1df5-e612-4947-a79)$ gcloud config set run/region us-central1
Updated property [run/region].
```

### Enable Services

```log
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo (project-602c1df5-e612-4947-a79)
$ gcloud services enable \
  run.googleapis.com \
  artifactregistry.googleapis.com \
  cloudbuild.googleapis.com
Operation "operations/acf.p2-698267352421-39650541-5f2d-468f-9f52-2097ff44ea54" finished successfully.
```

### Create "Artifact Registry"

```log
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo (project-602c1df5-e612-4947-a79)
$ gcloud artifacts repositories create app-images --repository-format=docker --location=us-central1 --description="App images for Cloud Run"
Create request issued for: [app-images]
Waiting for operation [projects/project-602c1df5-e612-4947-a79/locations/us-central1/operations/bc08bc01-04a3-4d47-b1bd-6026db014995] to complete...done.
Created repository [app-images].
```

### Build Application's Docker Container Image

```log
gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo (project-602c1df5-e612-4947-a79)
$ cat build-app-image.sh
# Set some convenience variables
PROJECT_ID=$(gcloud config get-value project)
REGION=$(gcloud config get-value run/region)
REPO=app-images
IMAGE_NAME=gcp-pubsub-demo
IMAGE_URL=${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO}/${IMAGE_NAME}

# Build the image
gcloud builds submit \
  --tag "${IMAGE_URL}" \
  .

gulen_chongtham@cloudshell:~/repos/gcp-pubsub-demo (project-602c1df5-e612-4947-a79)
$ ./build-app-image.sh
Your active configuration is: [cloudshell-5663]
Your active configuration is: [cloudshell-5663]
Creating temporary archive of 15 file(s) totalling 29.1 KiB before compression.
Some files were not included in the source upload.

Check the gcloud log [/tmp/tmp.6Dxi50rnVF/logs/2026.02.08/07.00.21.817299.log] to see which files and the contents of the
default gcloudignore file used (see `$ gcloud topic gcloudignore` to learn
more).

Uploading tarball of [.] to [gs://project-602c1df5-e612-4947-a79_cloudbuild/source/1770534022.243195-1e91d2b821854649a9b0cd42a9cb9940.tgz]
ERROR: (gcloud.builds.submit) INVALID_ARGUMENT: could not resolve source: googleapi: Error 403: 698267352421-compute@developer.gserviceaccount.com does not have storage.objects.get access to the Google Cloud Storage object. Permission 'storage.objects.get' denied on resource (or it may not exist)., forbidden

```

Another build error:

```log
denied: Permission 'artifactregistry.repositories.uploadArtifacts' denied on resource (or it may not exist).
ERROR: failed to push because we ran out of retries.
ERROR
ERROR: error pushing image "us-central1-docker.pkg.dev/project-602c1df5-e612-4947-a79/app-images/gcp-pubsub-demo": retry budget exhausted (10 attempts): step exited with non-zero status: 1
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

INFO: The service account running this build projects/project-602c1df5-e612-4947-a79/serviceAccounts/698267352421-compute@developer.gserviceaccount.com does not have permission to write logs to Cloud Logging. To fix this, grant the Logs Writer (roles/logging.logWriter) role to the service account.

1 message(s) issued.
ERROR: (gcloud.builds.submit) build 900ce888-082e-4340-b7be-9a6874eaac7b completed with status "FAILURE"
```


Run Cloud Run:

```bash
PROJECT_ID=project-602c1df5-e612-4947-a79
REGION=us-central1
REPO=app-images
IMAGE_NAME=gcp-pubsub-demo

SERVICE_NAME=myapp-catalog-subscriber
SA_EMAIL="myapp-catalog-subscriber@project-602c1df5-e612-4947-a79.iam.gserviceaccount.com"

gcloud run deploy ${SERVICE_NAME} \
  --image "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO}/${IMAGE_NAME}" \
  --platform managed \
  --region "${REGION}" \
  --service-account "${SA_EMAIL}" \
  --memory 1Gi \
  --cpu 1 \
  --port 8080
```

Another error:

```log
ERROR: gcloud crashed (TypeError): string indices must be integers, not 'str'

If you would like to report this issue, please run the following command:
  gcloud feedback

To check gcloud for common problems, please run the following command:
  gcloud info --run-diagnostics
```


```bash
gcloud run deploy gcp-pubsub-demo \
--image=us-central1-docker.pkg.dev/project-602c1df5-e612-4947-a79/app-images/gcp-pubsub-demo@sha256:36296ad1213f011340d6f903e41b79a5cf556135d0f7480aac2624a20856b320 \
--min-instances=1 \
--no-cpu-throttling \
--region=us-central1 \
--project=project-602c1df5-e612-4947-a79 \
 && gcloud run services update-traffic gcp-pubsub-demo --to-latest
```

## GKE Deployment

Note that it makes use of the "Artifact Registry" and "Cloud Build" used previously for Cloud Run Deployment.

### General Settings

```shell
PROJECT_ID=project-602c1df5-e612-4947-a79
REGION=us-central1
ZONE=us-central1-a
ARTIFACT_REPO=app-images
IMAGE_NAME=gcp-pubsub-demo
IMAGE_URL=${REGION}-docker.pkg.dev/${PROJECT_ID}/${ARTIFACT_REPO}/${IMAGE_NAME}

SERVICE_NAME=myapp-catalog-subscriber
SA_EMAIL="myapp-catalog-subscriber@project-602c1df5-e612-4947-a79.iam.gserviceaccount.com"
#----
```

### GKE Cluster

To keep costs low, create a "zonal" GKE Cluster.

```shell
#- NOTE: For convenience, remove the `--enable-private-endpoint` option
gcloud container clusters create my-gke-cluster \
  --project=${PROJECT_ID} \
  --zone=${ZONE} \
  --num-nodes=1 \
  --machine-type=e2-medium \
  --workload-pool=${PROJECT_ID}.svc.id.goog \
  --enable-ip-alias \
  --enable-private-nodes \
  --enable-private-endpoint \
  --master-ipv4-cidr=172.16.0.0/28 \
  --cluster-ipv4-cidr=/17 \
  --services-ipv4-cidr=/22 \
  --enable-shielded-nodes \
  --release-channel=regular


Creating cluster my-gke-cluster in us-central1-a... Cluster is being health-checked (Kubernetes Control Plane is healthy)...done.
Created [https://container.googleapis.com/v1/projects/project-602c1df5-e612-4947-a79/zones/us-central1-a/clusters/my-gke-cluster].
To inspect the contents of your cluster, go to: https://console.cloud.google.com/kubernetes/workload_/gcloud/us-central1-a/my-gke-cluster?project=project-602c1df5-e612-4947-a79
kubeconfig entry generated for my-gke-cluster.
NAME: my-gke-cluster
LOCATION: us-central1-a
MASTER_VERSION: 1.34.3-gke.1051003
MASTER_IP: 172.16.0.2
MACHINE_TYPE: e2-medium
NODE_VERSION: 1.34.3-gke.1051003
NUM_NODES: 1
STATUS: RUNNING
STACK_TYPE: IPV4
```

```shell
gcloud container clusters get-credentials my-gke-cluster \
  --zone=${ZONE} \
  --project=${PROJECT_ID}


ERROR: (gcloud.container.clusters.get-credentials) You do not currently have an active account selected.
Please run:

  $ gcloud auth login

to obtain new credentials.

If you have already logged in with a different account, run:

  $ gcloud config set account ACCOUNT

to select an already authenticated account to use.
```

### Dedicated Node Pool

```shell
gcloud container node-pools delete default-pool \
  --cluster=my-gke-cluster \
  --zone=us-central1-a


gcloud container node-pools create my-gke-nodepool \
  --cluster=my-gke-cluster \
  --zone=${ZONE} \
  --num-nodes=1 \
  --machine-type=e2-medium \
  --node-labels=app=gcp-pubsub-demo \
  --node-taints=app=gcp-pubsub-demo:NoSchedule \
  --workload-metadata=GKE_METADATA

```

### Verification

```shell
# Check Workload Identity enabled
gcloud container clusters describe my-gke-cluster \
  --zone=us-central1-a \
  --format="value(workloadIdentityConfig.workloadPool)"

# Should output: YOUR_PROJECT_ID.svc.id.goog

# List node pools
gcloud container node-pools list --cluster=my-gke-cluster --zone=us-central1-a
```

### IAM Service Account Binding

```shell
#- `myapp-catalog-subscriber` is the service account used in the Cloud Run demo

# Bind KSA principal to GSA (Workload Identity User)
gcloud iam service-accounts add-iam-policy-binding \
  myapp-catalog-subscriber@${PROJECT_ID}.iam.gserviceaccount.com \
  --role="roles/iam.workloadIdentityUser" \
  --member="serviceAccount:${PROJECT_ID}.svc.id.goog[default/gcp-pubsub-demo-sa]"
```

### kubectl network access

```shell
curl -s checkip.dyndns.org | sed -e 's/.*Current IP Address: //' -e 's/<.*$//' 
35.221.175.0

#- existing value
gcloud container clusters describe my-gke-cluster --zone=$ZONE --project=$PROJECT_ID --format="value(masterAuthorizedNetworksConfig.cidrBlocks)"

gcloud container clusters update my-gke-cluster \
  --enable-master-authorized-networks \
  --master-authorized-networks=35.221.175.0/32 \
  --zone=${ZONE} --project=${PROJECT_ID}

ERROR: (gcloud.container.clusters.update) INVALID_ARGUMENT: Invalid value for "cluster.master_authorized_networks_config.cidr_blocks.cidr_block": CIDR range "35.221.175.0/32" is not a reserved network, which is required for private endpoints.
- '@type': type.googleapis.com/google.rpc.RequestInfo
  requestId: '0xb4eed335722507d4'

#- Enable public endpoint, and authorize cloud shell
gcloud container clusters update my-gke-cluster \
  --no-enable-private-endpoint \
  --enable-master-authorized-networks \
  --master-authorized-networks=35.221.175.0/32 \
  --zone=$ZONE --project=$PROJECT_ID
```

### Deploy app in GKE

```shell
# Ensure the GKE Cluster is registered in `~/.kube/config`, before running kubectl
gcloud container clusters get-credentials my-gke-cluster \
  --zone=${ZONE} \
  --project=${PROJECT_ID}

# Test kubectl
kubectl get nodes

# Deploy app, by running this from the root of the project directory
kubectl apply k8s/
```

