package jeranvier.util.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Define the way the logger will display the messages
 */
public class LogFormater extends Formatter {

	private static final SimpleDateFormat simpleDateParser = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat verboseDateParser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    public LogFormater() {
        super();
    }

    /**Defines the format of the log messages.
     * Format is: [time] Class : message
     */
    @Override
    public String format(LogRecord record) {
    	boolean verbose = LoggerInit.verbose();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if(verbose){
        	sb.append(record.getLevel().getName()+" ");
        	sb.append(verboseDateParser.format(Calendar.getInstance().getTime()));
        }
        else{
        	sb.append(simpleDateParser.format(Calendar.getInstance().getTime()));        	
        }
        sb.append("] ");
        if(verbose){
        	sb.append(record.getSourceClassName()+" ");
        }
        sb.append(": ");
        sb.append(formatMessage(record));
        sb.append("\n");
        return sb.toString();
    }
}