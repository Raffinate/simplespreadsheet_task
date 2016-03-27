package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class DataCell implements Cell {
	
	public DataCell(final Object data) {
		this.data = data;
	}

	public Object calculateValue(final Hashtable<CellAddress, Object> vars) {
		
		return data;
	}

	public Set<CellAddress> getDependencies() {
		return new HashSet<CellAddress>();
	}
	
	public String toString() {
		return "<DATA: " + data.toString() + ">";
	}
	
	private Object data;

}
