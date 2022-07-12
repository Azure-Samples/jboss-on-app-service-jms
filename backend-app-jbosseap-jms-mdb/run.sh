#echo "Installing 'zip' utility"
#sudo apt install -qqq -y zip

# Build and deploy using maven, but then use CLI to convert site to JBoss site as Maven plugin does not support that yet
echo "Creating the web app..."
(
    set -x;
    mvn --batch-mode clean package azure-webapp:deploy
)

# Set creds
echo "Retrieving publishing credentials and setting as environment variables for later..."
source ../.scripts/setenv-webapp.sh
set_creds $BACKEND_RESOURCEGROUP_NAME $BACKEND_WEBAPP_NAME

# Package the files into a zip file - out.zip
echo "Copying libraries and configuration scripts to a .zip file for deployment..."
rm -rf target/tmp
mkdir -p target/tmp/libs
cp module.xml target/tmp/
cp startup.sh target/tmp/
cp commands.cli target/tmp/
cp target/backend/WEB-INF/lib/*.jar target/tmp/libs/
cp libs/*.jar target/tmp/libs/
cp target/backend.war target/tmp/app.war
pushd target/tmp
zip -r ../out.zip *
popd

# Deploy out.zip
echo "Deploying the .zip file..."
(
    set -x;
    curl -X POST -u $cred https://${BACKEND_WEBAPP_NAME}.scm.azurewebsites.net/api/zipdeploy --data-binary @'./target/out.zip'
)

# Now that all contents are uploaded, define the startup.sh file
echo "Finalizing configuration for JBoss EAP site..."
az webapp config set -g $BACKEND_RESOURCEGROUP_NAME --name $BACKEND_WEBAPP_NAME --linux-fx-version "JBOSSEAP|7.3-java8" --number-of-workers 1 --startup-file /home/site/wwwroot/startup.sh 

az webapp restart -g ${BACKEND_RESOURCEGROUP_NAME} --name ${BACKEND_WEBAPP_NAME}

echo "Run this command to view log stream:      
    az webapp log tail -g ${BACKEND_RESOURCEGROUP_NAME} --name ${BACKEND_WEBAPP_NAME}"

echo "Run this command to unzip the deployment package to a local destination:    
    unzip target/out.zip -d /mnt/c/home/site/wwwroot/"
