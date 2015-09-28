package jeranvier.util.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerInit{

    private static boolean verbose = false;


	/** instantiates the logger.
     * 
     * @return the actual logger that will be used to display messages
     */
    public static void init(String root, boolean v) {
    	verbose = v;
    	Logger logger= Logger.getLogger(root);
        logger.setUseParentHandlers(false);
        Handler consoleHandler = new ConsoleHandler();

        consoleHandler.setLevel(Level.FINEST);
        for(Handler handler : logger.getHandlers()){
            logger.removeHandler(handler);
        }
        
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.FINEST);
        logger.finest("logger "+root+" initialized");
    }
    
    public static void init(String root) {
    	init(root, false);
    }
    
    public static void init() {
    	init("", false);
    }

	public static boolean verbose() {
		return verbose;
	}


	public static void verbose(boolean verbose) {
		LoggerInit.verbose = verbose;
	}
    
}
