package local.ss;

public class SimpleSpreadSheetException extends Exception {

	public SimpleSpreadSheetException(String longDescr, String shortDescr) {
		super("#"+shortDescr);
		this.longDescr = longDescr;
	}
	
	public String longDescr() {
		return this.longDescr;
	}
	
	public String toString() {
		return super.getMessage();
	}
	
	public static final long serialVersionUID = 1L;
	
	private String longDescr;
}
