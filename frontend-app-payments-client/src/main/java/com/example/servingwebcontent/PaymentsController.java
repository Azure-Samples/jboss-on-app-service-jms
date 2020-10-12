package com.example.servingwebcontent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;
import java.util.Properties;
import java.util.Random;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSProducer;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Controller
public class PaymentsController {

    private static final Logger log = Logger.getLogger(PaymentsController.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "SBCF";
    private static final String DEFAULT_DESTINATION = "QUEUE";
    private static final String DEFAULT_MESSAGE_COUNT = "51";
    private static final String DEFAULT_USERNAME = System.getenv("SB_SAS_POLICY");
    private static final String DEFAULT_PASSWORD = System.getenv("SB_SAS_KEY");
    private static final String INITIAL_CONTEXT_FACTORY = "org.apache.qpid.jms.jndi.JmsInitialContextFactory";
    private static final String PROVIDER_URL = System.getenv("PROVIDER_URL");
	private static final String DESTINATION_QUEUE = System.getenv("SB_QUEUE");
	private static final Random random = new Random();

	private static final String[] firstNames = {
		"Donald",
		"Bill",
		"Henry",
		"Ritesh",
		"Oliver",
		"Harry",
		"George",
		"Noah",
		"Jack",
		"Jacob",
		"Leo",
		"Olivia",
		"Amelia",
		"Isla",
		"Ava",
		"Emily",
		"Mia",
		"Lilly"
	};
	
	private static final String[] lastNames = {
		"Smith",
		"Brown",
		"Johnson",
		"Williams",
		"Jones",
		"Davis",
		"Miller",
		"Wilson",
		"Adams",
		"Bailey",
		"Baker",
		"Armstrong",
		"Anderson",
		"Allen"
	};
	
	@GetMapping("/payments/process")
	public ResponseEntity<?> greeting(){

		try {
			//
			// Send messages
			//
			new Thread(new Runnable() {
				public void run() {
					sendMessages();
				}
			}).start();

			return ResponseEntity.status(HttpStatus.ACCEPTED).body("September payments initiated!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing September payments!");
		}
	}

	private String getFirstName() {
		int index = random.nextInt(firstNames.length);
		return firstNames[index];
	}

	private String getLastName() {
		int index = random.nextInt(lastNames.length);
		return lastNames[index];
	}

	private String getSalary() {
		Integer value = 4000 + random.nextInt(5000);
		return value.toString();
	}

	private String getEmployeeId() {
		Integer value = 1000 + random.nextInt(2000);
		return value.toString();
	}

	private String GetMessage() {
		String name = getFirstName() + " " + getLastName();
		String salary = getSalary();

		String message = "Month: September | Employee ID: " + getEmployeeId() + " | Name: " + name + " | Salary: " + salary;
		System.out.println(message);

		return message;
	}

	private void sendMessages() {
		Context namingContext = null;

        try {
            String userName = System.getProperty("username", DEFAULT_USERNAME);
            String password = System.getProperty("password", DEFAULT_PASSWORD);

            // Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put("connectionfactory.SBCF", PROVIDER_URL);
            env.put("queue.QUEUE", DESTINATION_QUEUE);
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            namingContext = new InitialContext(env);

            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(connectionFactoryString);
            log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination",DEFAULT_DESTINATION);
            log.info("Attempting to acquire destination \"" + destinationString + "\"");
            Destination destination = (Destination) namingContext.lookup(destinationString);
            log.info("Found destination \"" + destinationString + "\" in JNDI");

            int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));
            String content = System.getProperty("message.content", DEFAULT_MESSAGE);

            try (JMSContext context = connectionFactory.createContext(userName, password)) {
                log.info("Sending " + count + " messages with content: " + content);
				
				// Send messages for specifed seconds
				long initialTime = System.currentTimeMillis();
				JMSProducer producer = context.createProducer();
                while ((System.currentTimeMillis() - initialTime) < 90*1000) {
					producer.send(destination, GetMessage());
					Thread.sleep(5);
                }
            }
        } catch (NamingException e) {
            log.severe(e.getMessage());
        } catch (InterruptedException e) {
            log.severe(e.getMessage());
        } finally {
            if (namingContext != null) {
                try {
                    namingContext.close();
                } catch (NamingException e) {
                    log.severe(e.getMessage());
                }
            }
        }
	}

}
