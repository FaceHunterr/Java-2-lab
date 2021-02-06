import java.io.IOException;
import java.util.logging.*;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        try
        {
            Handler fileHandler = new FileHandler(Log.LOG_FILE_NAME.message);
            Handler consoleHandler = new ConsoleHandler();
            fileHandler.setLevel(Level.WARNING);
            consoleHandler.setLevel(Level.WARNING);
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
        }catch (IOException e)
        {
            System.err.println(Log.FILE_HANDLER.message);
        }
        if(args == null || args.length != 1)
        {
            logger.severe(Log.INVALID_ARGUMENT.message);
            return;
        }

        Manager manager = new Manager();
        manager.Run(args[0], logger);


    }
}
