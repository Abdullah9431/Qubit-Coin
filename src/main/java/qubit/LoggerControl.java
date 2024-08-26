package qubit;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;

public class LoggerControl {
    public static void turnOffLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("ROOT").setLevel(Level.OFF);
    }
}
