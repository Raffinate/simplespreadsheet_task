package local.ss;

import java.util.Hashtable;

public class IntegerVariableValue implements IntegerValue {
	
	public IntegerVariableValue(final CellAddress cell) {
		this.cell = cell;		
	}
	
	public Integer getValue(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException{
		if (!vars.containsKey(cell)) {
			throw new SimpleSpreadSheetException("Variable " + cell + " is not Integer.", "EA:" + cell.toString());
		}
		
		Object value = vars.get(cell);
		
		if (!(value instanceof Integer)) {
			throw new SimpleSpreadSheetException("Varibale " + cell + " is not Integer.", "EA:" + cell.toString());
		}
		
		return (Integer)value;
	}
	
	public String toString() {
		return cell.toString();
	}
	
	private CellAddress cell;
	
}
