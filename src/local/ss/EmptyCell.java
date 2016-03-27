package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class EmptyCell implements Cell{
	
	static public EmptyCell emptyCell() {
		return EMPTY;
	}
	
	private EmptyCell (){}
	
	public Set<CellAddress> getDependencies() {
		return new HashSet<CellAddress>();		
	}

	public String calculateValue(final Hashtable<CellAddress, Object> vars) {
		return "";
	}
	
	public String toString() {
		return "<EMPTY>";
	}
	
	public static final EmptyCell EMPTY = new EmptyCell();
}
