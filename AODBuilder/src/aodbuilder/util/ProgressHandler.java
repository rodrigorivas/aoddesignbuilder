package aodbuilder.util;

import aodbuilder.importerLayer.process.AODBuilderRunner;

public class ProgressHandler {

	public static int DEFAULT_PROGRESS = 5;

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
