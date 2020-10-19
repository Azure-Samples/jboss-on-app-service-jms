# JMS with JBoss EAP on Azure App Service

This repository contains a sample that demonstrates how a Java EE app can be deployed to a managed JBoss server in App Service. The `frontend-app-payments-client` directory contains a Java SE app that sends JMS messages to Azure Service Bus. The Java EE app in the `backend-app-jbosseap-jms-mdb` directory uses Message Driven Beans for processing JMS messages sent by the Java SE app. When the MDB processes the JMS messages, you will be able to see log messages on the application's stdout (log stream in Azure portal).

## Features

This project framework provides the following features:

* Azure App Service (a Spring Boot app and a JBoss EAP app)
* Azure Service Bus

## Prerequisites

- An Azure Subscription
- The Azure CLI installed locally

## Tutorial

Run the following commands in `bash` on Linux or `WSL` on Windows. Carefully note the comments in the instructions below that specify manual steps needed. The steps will build and deploy the app in Azure in the resource group you specifiy in `setenv-azure.sh`. NOTE: Because of the way the script deploys the JBoss app, it takes around 5 minutes for the app to be up and running.

### Setup

First, log into the Azure CLI and set the subscription you want to use for this tutorial. Replace `SUBSCRIPTION-ID` with your subscription ID.

```bash
az login

az account set -s SUBSCRIPTION-ID.
```

Next, copy the script templates from `scripts/` to `.scripts/`.

```bash
source scripts/fork.sh
```

### Create resource groups

1. Once the files are copied to `.scripts/`, open [setenv-azure.sh](.scripts/setenv-azure.sh) and replace the placeholder (`__REPLACEME__`) with the string you want to use for the resource group.
1. Run the script to set the resource group and resource names.

    ```bash
    source .scripts/setenv-azure.sh
    ```

### Create Service Bus

1. Create the Service Bus namespace and queue. This script will also set the *listen* and *send* authorizations on the queue.

    ```bash
    source .scripts/configure-servicebus.sh
    ```

2. The next script will copy the Service Bus SAS Key and set it as an environment variable. This SAS key will be set as an environment variable on the web app in the next section.

    ```bash
    source .scripts/setenv-servicebus.sh
    ```

3. Test the Service Bus queue

    ```bash
    source console-app-jms/run.sh
    ```

### Create web apps

```
# Build backend app
pushd backend-app-jbosseap-jms-mdb/
source run.sh
popd

# Build frontend app
pushd frontend-app-payments-client/
source run.sh
popd

```
