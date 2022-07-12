#!/bin/sh
# This script creates a Service Bus Namespace and Queue within a resource group.

set -x #echo on

az group create --name ${SERVICEBUS_RESOURCEGROUP_NAME} \
    --location ${REGION}
    
az servicebus namespace create --sku Premium \
    --name  ${DEFAULT_SBNAMESPACE} \
    --resource-group ${SERVICEBUS_RESOURCEGROUP_NAME}

az servicebus queue create \
    --name ${SB_QUEUE} \
    --namespace-name ${DEFAULT_SBNAMESPACE} \
    --resource-group ${SERVICEBUS_RESOURCEGROUP_NAME}

az servicebus queue authorization-rule create \
    --name ${SB_SAS_POLICY} \
    --namespace-name ${DEFAULT_SBNAMESPACE} \
    --queue-name ${SB_QUEUE} \
    --resource-group ${SERVICEBUS_RESOURCEGROUP_NAME} \
    --rights Listen Send
