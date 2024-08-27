package qubit;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

public class Main {
    static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);
    static String sender = "aaa";
    static String receiver = "bbb";
    static String amount = "100";

    public static void main(String[] args) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("Starting application with debug: true");
            Main.logger.debug("Running as user: " + System.getProperty("user.name"));

    }
        App.main(args);
        logger.info("Application finished.");
        }
    }