


public class LocationCalculator {

	private static final Integer screenSize = 12;
	private static final Integer elemHeight = 10;
	private static LocationCalculator instance;
	
	private LocationCalculator() {
		super();
	}
	
	public static LocationCalculator getInstance() {
		if (instance == null)
			instance = new LocationCalculator();
		return instance;
	}
	
	public String getPosition() {
		return null;
	}
	
	public String getSize() {
		return null;
	}
	
}
