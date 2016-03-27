package local.ss;

import java.util.Hashtable;
import java.util.Set;

public interface Cell {
	
	public Object calculateValue(final Hashtable<CellAddress, Object> vars);
	public Set<CellAddress> getDependencies();

}
