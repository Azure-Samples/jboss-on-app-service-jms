# Abridged Instructions

For those in a hurry, here is a shortened version of the deployment instructions.

1. Complete the [setup instructions](README.md#setup)
2. Run the following commands from the repository root

    ```bash
    source setenv-azure.sh

    source configure-servicebus.sh

    source setenv-servicebus.sh

    pushd backend-app-jbosseap-jms-mdb/
    source run.sh
    popd

    pushd frontend-app-payments-client/
    source run.sh
    popd
    ```

3. Now you can follow [the demo steps](README.md#Demo)
