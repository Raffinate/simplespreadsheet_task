package local.ss;

import java.util.Hashtable;
import java.util.Set;

/**
 * Interface for table cell.
 * It is used to represent parsed, but not evaluated cell.
 */

public interface Cell {

    public Object calculateValue(final Hashtable<CellAddress, Object> vars);

    public Set<CellAddress> getDependencies();

}
