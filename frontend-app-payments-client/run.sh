(
    set -x;
    mvn --batch-mode clean package azure-webapp:deploy
)
