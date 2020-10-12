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
package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

//import javax.annotation.Resource;
//import javax.inject.Inject;
import javax.jms.Destination;
import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.JMSContext;
import javax.naming.Context;
//import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
//import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
//import javax.jms.Queue;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Definition of the two JMS destinations used by the quickstart
 * (one queue and one topic).
 */

@JMSDestinationDefinitions(
    value = {
      /* @JMSDestinationDefinition(
            name = "java:/queue/HELLOWORLDMDBQueue",
            interfaceName = "javax.jms.Queue",
            destinationName = "HelloWorldMDBQueue"
        ),
        @JMSDestinationDefinition(
            name = "java:/topic/HELLOWORLDMDBTopic",
            interfaceName = "javax.jms.Topic",
            destinationName = "HelloWorldMDBTopic"
        )*/
    }
)

/**
 * <p>
 * A simple servlet 3 as client that sends several messages to a queue or a topic.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /HelloWorldMDBServletClient using the {@linkplain WebServlet
 * @HttpServlet}.
 * </p>
 *
 * @author Serge Pagop (spagop@redhat.com)
 *
 */
@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    private static final int MSG_COUNT = 5;
    private static final String DEFAULT_CONNECTION_FACTORY = "SBCF";
    private static final String DEFAULT_DESTINATION = "QUEUE";
    private static final String DEFAULT_MESSAGE_COUNT = "1";
    private static final String DEFAULT_USERNAME = System.getenv("SB_SAS_POLICY");
    private static final String INITIAL_CONTEXT_FACTORY = "org.apache.qpid.jms.jndi.JmsInitialContextFactory";
    private static final String PROVIDER_URL = System.getenv("PROVIDER_URL");
    private static final String DESTINATION_QUEUE = System.getenv("SB_QUEUE");

  //@Inject
    //private JMSContext context;
  //  @Resource(lookup="java:comp/env/AzureSBConnectionFactory")
 //  ConnectionFactory cf;
   // @Resource(lookup = "java:/queue/HELLOWORLDMDBQueue")
 //  @Resource(lookup = "jmstestqueue")
  //  private Queue queue;

   // @Resource(lookup = "java:/topic/HELLOWORLDMDBTopic")
    private Topic topic;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write("<h1>Quickstart: Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in JBoss EAP.</h1>");
        try {
            MessageProducer producer = null;
            Hashtable<String, String> hashtable = new Hashtable<>();
            hashtable.put("connectionfactory.SBCF", PROVIDER_URL);
            hashtable.put("queue.QUEUE", DESTINATION_QUEUE);
            hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
            Context context = new InitialContext(hashtable);
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
            String destinationString = System.getProperty("destination",DEFAULT_DESTINATION);
            Destination queue = (Destination) context.lookup(destinationString);
            boolean useTopic = req.getParameterMap().keySet().contains("topic");
            final Destination destination = useTopic ? topic : queue;

            // Destination destination = (Destination) namingContext.lookup(destinationString);
            // boolean useTopic = req.getParameterMap().keySet().contains("topic");
            // destination = useTopic ? topic : queue;
           // Create Context and send Messages
            try (JMSContext connection = connectionFactory.createContext(System.getenv("SB_SAS_POLICY"), java.net.URLEncoder.encode(System.getenv("SB_SAS_KEY"), java.nio.charset.StandardCharsets.UTF_8.toString()))) {
            out.write("<p>Sending messages to <em>" + destination + "</em></p>");
            out.write("<h2>The following messages will be sent to the destination:</h2>");
            for (int i = 0; i < MSG_COUNT; i++) {
                String text = "This is message " + (i + 1);
                connection.createProducer().send(destination,text);
                out.write("Message (" + i + "): " + text + "</br>");
            }
            out.write("<p><i>Go to your JBoss EAP server console or server log to see the result of messages processing.</i></p>");
         }
        } catch( NamingException e) {
          out.write("Exception" + e.getMessage());
        }
        finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}