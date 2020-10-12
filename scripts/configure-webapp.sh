enable_logging()
{
    az webapp log config -g $1 --name $2 --application-logging filesystem --detailed-error-messages true --docker-container-logging filesystem --failed-request-tracing true --level verbose --web-server-logging filesystem
}
