package qubit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
            String[] debugArgs = {"true"};

            logger.info("Starting application with debug: {}", debugArgs[0]);
        
            // Call the App's main method with debug arguments
            App.main(debugArgs);

            logger.info("Application finished.");
            }
}