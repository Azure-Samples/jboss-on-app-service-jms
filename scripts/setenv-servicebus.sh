apt install -qqq -y jq

# Strip the leading and trailing double quotes
export SB_SAS_KEY=`az servicebus queue authorization-rule keys list \
    --name ${SB_SAS_POLICY} \
    --namespace-name ${DEFAULT_SBNAMESPACE} \
    --queue-name ${SB_QUEUE} \
    --resource-group ${SERVICEBUS_RESOURCEGROUP_NAME} \
    | jq '.primaryKey' \
    | sed -e 's/"//g' `
    
## Compose secrets
export PROVIDER_URL=amqps://${DEFAULT_SBNAMESPACE}.servicebus.windows.net?amqp.idleTimeout=120000
