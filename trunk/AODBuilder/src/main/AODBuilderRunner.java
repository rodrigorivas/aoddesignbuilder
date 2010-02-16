package main;



import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.SwingWorker;


public class AODBuilderRunner extends SwingWorker<Void, Void> {
    private int increment=1;
    private String fileName;
    
    private static AODBuilderRunner instance;
    
    private AODBuilderRunner(String fileName) {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

