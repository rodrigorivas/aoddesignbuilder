package util;

import main.AODBuilderRunner;

public class ProgressHandler {

	private static ProgressHandler instance;
	private ProgressHandler() {
	}
	
	public static ProgressHandler getInstance(){
		if (instance==null)
			instance = new ProgressHandler();
		
		return instance;
	}
	
	public void setProgress(int progress){
		AODBuilderRunner task = AODBuilderRunner.getInstance();
		if (task!=null){
			task.incrementProgress(progress-task.getProgress());
		}
	}
	
	public void incrementProgress(int increment){
		AODBuilderRunner task = AODBuilderRunner.getInstance();
		if (task!=null){
			task.incrementProgress(increment);
		}
	}
	
}
