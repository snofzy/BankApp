package bankApp;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

public class LoggerUtil {
	private static Logger logger;

    static {
        try {
            logger = Logger.getLogger("BankLogger");
            FileHandler fileHandler = new FileHandler("bank.log", true); // Loggfil
            fileHandler.setFormatter(new SimpleFormatter()); // Formatter för tydligt format
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL); // Loggnivå för att fånga alla typer av loggningar
        } catch (Exception e) {
            System.err.println("Kunde inte skapa loggfil: " + e.getMessage());
        }
    }
    // Gör loggern tillgänglig för andra klasser
    public static Logger getLogger() {
        return logger;
    }
}
