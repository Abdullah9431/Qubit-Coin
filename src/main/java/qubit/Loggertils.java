package qubit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Loggertils {
    public static void SetLogLevel(String level) {
        Logger logbackLogger = Main.logger;
        
        switch (level.toUpperCase()) {
            case "OFF":
                logbackLogger.setLevel(Level.OFF);
                break;
            case "ERROR":
                logbackLogger.setLevel(Level.ERROR);
                break;
            case "WARN":
                logbackLogger.setLevel(Level.WARN);
                break;
            case "INFO":
                logbackLogger.setLevel(Level.INFO);
                break;
            case "DEBUG":
                logbackLogger.setLevel(Level.DEBUG);
                break;
            case "TRACE":
                logbackLogger.setLevel(Level.TRACE);
                break;
            default:
                logbackLogger.setLevel(Level.INFO);  // Default level if input is not recognized
                System.out.println("Unrecognized log level: " + level + ". Defaulting to INFO.");
                break;
        }
    }
}
