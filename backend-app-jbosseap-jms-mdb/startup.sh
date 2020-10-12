echo "Generating jndi.properties file in /home/site/wwwroot directory"
echo "connectionfactory.SBF=amqps://${DEFAULT_SBNAMESPACE}.servicebus.windows.net?amqp.idleTimeout=120000&jms.username=${SB_SAS_POLICY}&jms.password=${SB_SAS_KEY}" > /tmp/jndi.properties
echo "connectionfactory.SBCF=amqps://${DEFAULT_SBNAMESPACE}.servicebus.windows.net?amqp.idleTimeout=120000&jms.username=${SB_SAS_POLICY}&jms.password=${SB_SAS_KEY}" >> /tmp/jndi.properties
echo "queue.jmstestqueue=${SB_QUEUE}" >> /tmp/jndi.properties
echo "====== contents of /tmp/jndi.properties ======"
cat /tmp/jndi.properties
echo "====== EOF /tmp/jndi.properties ======"


echo __________________$JBOSS_HOME $HOME

mkdir $JBOSS_HOME/modules/system/layers/base/org/jboss/genericjms/provider
mkdir $JBOSS_HOME/modules/system/layers/base/org/jboss/genericjms/provider/main

cp  /home/site/wwwroot/libs/*.jar $JBOSS_HOME/modules/system/layers/base/org/jboss/genericjms/provider/main/
cp /home/site/wwwroot/module.xml $JBOSS_HOME/modules/system/layers/base/org/jboss/genericjms/provider/main/

cp /tmp/jndi.properties $JBOSS_HOME/standalone/configuration/

$JBOSS_HOME/bin/jboss-cli.sh -c --file=/home/site/wwwroot/commands.cli
