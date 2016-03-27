package local.ss;

import java.util.Hashtable;

public interface IntegerValue {
	
	public Integer getValue(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException;

}
