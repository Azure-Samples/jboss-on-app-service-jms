#!/usr/bin/env bash

## Resource Group Name
export RESOURCEGROUP_NAME=<my resource group name>
export REGION=<my region>

## Service Bus secrets
export DEFAULT_SBNAMESPACE=<my Servicebus namespace>
export SB_SAS_POLICY=<my Servicebus Send PolicyName>
export SB_SAS_KEY=<my Servicebus Primary/SecondaryAccessKey>
export SB_QUEUE=<my Servicebus queuename>

## Compose secrets
export PROVIDER_URL=amqps://${DEFAULT_SBNAMESPACE}.servicebus.windows.net?amqp.idleTimeout=120000