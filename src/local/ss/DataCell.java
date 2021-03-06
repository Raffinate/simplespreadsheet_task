package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * DataCell stores data that is not an expression.
 * Since it is not an expression and doesn't need to be calculated
 * DataCell stores data that will later be placed in result table.
 */

public class DataCell implements Cell {

    public DataCell(final Object data) {
        this.data = data;
    }

    @Override
    public Object calculateValue(final Hashtable<CellAddress, Object> vars) {

        return data;
    }

    @Override
    public Set<CellAddress> getDependencies() {
        return new HashSet<CellAddress>();
    }

    @Override
    public String toString() {
        return data.toString();
    }

    private Object data;

}
