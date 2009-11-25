package beans;


public class Advice extends Responsability {
	enum advice_type {AFTER, BEFORE, AROUND};
	private advice_type type;

	public advice_type getType() {
		return type;
	}
	public void setType(advice_type type) {
		this.type = type;
	}
	
}
