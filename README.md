# JMS on

This repository contains a sample that demonstrates how a Java EE app can be deployed to a managed JBoss server in App Service. The `frontend-app-payments-client` directory contains a Java SE app that sends JMS messages to Azure Service Bus. The Java EE app in the `backend-app-jbosseap-jms-mdb` directory uses Message Driven Beans for processing JMS messages sent by the Java SE app. When the MDB processes the JMS messages, you will be able to see log messages on the application's stdout (log stream in Azure portal).

## Features

This project framework provides the following features:

* Azure App Service (a Spring Boot app and a JBoss EAP app)
* Azure Service Bus

## Getting Started

### Prerequisites

- An Azure Subscription
- The Azure CLI installed locally

## Tutorial

Run the following commands in `bash` on Linux or `WSL` on Windows. Carefully note the comments in the instructions below that specify manual steps needed. The steps will build and deploy the app in Azure in the resource group you specifiy in `setenv-azure.sh`. NOTE: Because of the way the script deploys the JBoss app, it takes around 5 minutes for the app to be up and running.

```bash

az login

# MANUAL TODO: Set the subscription you want to use
az account set -s __REPLACEME__SUBSCRIPTIONID__

# MANUAL TODO: cd jbosseap-jms-mdb-sample

# Copy the files in scripts/ to .scripts/
source scripts/fork.sh

# MANUAL TODO: Modify .scripts/setenv-azure.sh 
source .scripts/setenv-azure.sh

# SKIP if re-using existing service bus
source .scripts/configure-servicebus.sh

source .scripts/setenv-servicebus.sh

# Test service bus using console app
pushd console-app-jms/
source run.sh
popd

# Build backend app
pushd backend-app-jbosseap-jms-mdb/
source run.sh
popd

# Build frontend app
pushd frontend-app-payments-client/
source run.sh
popd

```
