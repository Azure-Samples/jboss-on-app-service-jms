/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.mdb;

// import java.util.logging.ConsoleHandler;
// import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
//import javax.jms.Destination;
//import java.util.Hashtable;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.jms.MessageConsumer;
//import javax.jms.JMSContext;
//import javax.naming.Context;
//import javax.jms.ConnectionFactory;
//import javax.jms.Connection;
//import javax.jms.Session;

class MyFormatter extends java.util.logging.Formatter {
    // Create a DateFormat to format the logger timestamp.
    private static final java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new java.util.Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}

/**
 * <p>
 * A simple Message Driven Bean that asynchronously receives and processes the messages that are sent to the queue.
 * </p>
 *
 * @author Serge Pagop (spagop@redhat.com)
 */
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@MessageDriven(name = "HelloWorldQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "connectionFactory", propertyValue = "java:global/remoteJMS/SBF"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:global/remoteJMS/jmstestqueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class HelloWorldQueueMDB implements MessageListener {

    private static Logger LOGGER = null;
    private static final Logger LOGGER2 = Logger.getLogger(".");
    private static final String workerName = System.getenv("COMPUTERNAME");

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "X %n");

        LOGGER = Logger.getLogger("MYCLASS");
        LOGGER.setUseParentHandlers(false);

        MyFormatter formatter = new MyFormatter();
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(formatter);

        LOGGER.addHandler(handler);
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;

        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                LOGGER2.info("\n\n================================BEGIN======================================" +
                // "\n\n" + "WORKER NAME: " + workerName +
                "\n\n" + msg.getText() +
                "\n\n=================================END=======================================" +
                "\n\n");
            } else {
                LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            LOGGER.warning("Exception on message : "+e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
