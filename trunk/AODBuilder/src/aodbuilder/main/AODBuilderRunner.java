package aodbuilder.main;



import java.awt.Toolkit;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import aodbuilder.util.ExceptionUtil;
import aodbuilder.util.Log4jConfigurator;



public class AODBuilderRunner extends SwingWorker<Void, Void> {
    private int increment=1;
    private String fileName;
    private Logger logger;
    private static AODBuilderRunner instance;
    
    private AODBuilderRunner(String fileName) {
		try {
			Log4jConfigurator.getLog4JProperties();
		} catch (Exception e) {
			System.out.println("Error loading log4j file: ");
			e.printStackTrace();
		}
		logger = Log4jConfigurator.getLogger();	

    	this.fileName = fileName;
    }
    
    public static void destroy(){
    	instance = null;
    }
    
	public static AODBuilderRunner getInstance(String fileName){
    	if (instance == null){
    		instance = new AODBuilderRunner(fileName);
    	}
    	
    	return instance;
    }
    
    public static AODBuilderRunner getInstance(){    	
    	return instance;
    }

    
	@Override
    public Void doInBackground() {
        setProgress(0);
        try {
			AODBuilder.getInstance().process(fileName);
		} catch (Exception e) {
			logger.error(ExceptionUtil.getTrace(e));
			cancel(true);
		}
		if (!isCancelled())
			setProgress(100);

    	return null;
    }

    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
    }
        
	public void setProgress(){
		if (getProgress()+increment>100)
			setProgress(100);
		else
			setProgress(getProgress()+increment);
	}
	
	public void incrementProgress(int increment){
		if (getProgress()+increment>100)
			setProgress(100);
		else
			setProgress(getProgress()+increment);
	}
	
	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}
	
}

