#!/usr/bin/env bash

## **NOTE** Replace everything that says __REPLACEME__

## Resource Group Name
export BASE_RESOURCEGROUP_NAME=__REPLACEME__

## Hardcoded variables
export SB_SAS_POLICY=mysbsaspolicy
export SB_QUEUE=myqueue
export REGION=westus

## Reset env variables
export SB_SAS_KEY=
export PROVIDER_URL=

## Derived variables
export FRONTEND_RESOURCEGROUP_NAME=${BASE_RESOURCEGROUP_NAME}
export BACKEND_RESOURCEGROUP_NAME=${BASE_RESOURCEGROUP_NAME}
export SERVICEBUS_RESOURCEGROUP_NAME=${BASE_RESOURCEGROUP_NAME}

export BASE_DEFAULT_SBNAMESPACE=${BASE_RESOURCEGROUP_NAME}

export BASE_WEBAPP_NAME=${BASE_RESOURCEGROUP_NAME}
export BASE_WEBAPP_PLAN_NAME=${BASE_WEBAPP_NAME}-asp

export FRONTEND_WEBAPP_PLAN_NAME=${BASE_WEBAPP_PLAN_NAME}
export FRONTEND_WEBAPP_NAME=frontend-${BASE_WEBAPP_NAME}

export BACKEND_WEBAPP_PLAN_NAME=${BASE_WEBAPP_PLAN_NAME}
export BACKEND_WEBAPP_NAME=backend-${BASE_WEBAPP_NAME}

export DEFAULT_SBNAMESPACE=${BASE_DEFAULT_SBNAMESPACE}-sbus

echo "
    Environment variables set! Summary is below.
    
    Frontend resource group name:       ${FRONTEND_RESOURCEGROUP_NAME}
    Backend resource group name:        ${BACKEND_RESOURCEGROUP_NAME}
    Service Bus resource group name:    ${SERVICEBUS_RESOURCEGROUP_NAME}
    Region for resources:               ${REGION}

    Frontend webapp name:               ${FRONTEND_WEBAPP_NAME}
    Frontend App Service Plan name:     ${FRONTEND_WEBAPP_PLAN_NAME}

    Backend webapp name:                ${BACKEND_WEBAPP_NAME}
    Backend App Service Plan name:      ${BACKEND_WEBAPP_PLAN_NAME}

    Service Bus Namespace:              ${DEFAULT_SBNAMESPACE}
    Service Bus queue name:             ${SB_QUEUE}
"
