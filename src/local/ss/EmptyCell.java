package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * EmptyCell represent empty cells.
 */

public class EmptyCell implements Cell {

    static public EmptyCell emptyCell() {
        return EMPTY;
    }

    private EmptyCell() {
    }

    @Override
    public Set<CellAddress> getDependencies() {
        return new HashSet<CellAddress>();
    }

    @Override
    public String calculateValue(final Hashtable<CellAddress, Object> vars) {
        return "";
    }

    @Override
    public String toString() {
        return "<EMPTY>";
    }

    public static final EmptyCell EMPTY = new EmptyCell();
}
