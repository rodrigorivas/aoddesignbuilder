package util;

import java.util.HashSet;
import java.util.Set;

public class ProcessingProgress extends Observable{

	Set<Observer> observers = new HashSet<Observer>();
	
	private static ProcessingProgress instance;

	private int progress=0;
		
	private ProcessingProgress() {
	}
	
	public static ProcessingProgress getInstance(){
		if (instance==null){
			instance = new ProcessingProgress();
		}
		return instance;
	}
	
	public void addObserver(Observer e){
		observers.add(e);
	}
	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		if (progress>100){
			progress = 100;
		}
		this.progress = progress;
		notifyObservers();
	}
	
	private void notifyObservers() {
		for (Observer o: observers){
			o.update(this);
		}
	}

	public boolean isComplete(){
		return progress>=100;
	}
	
	public void reset(){
		progress = 0;
	}

	public void incrementProgress(int progress) {
		setProgress(this.progress+progress);		
	}
}
