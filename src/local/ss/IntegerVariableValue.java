package local.ss;

import java.math.BigInteger;
import java.util.Hashtable;

import local.ss.SimpleSpreadSheetException.ErrorType;

/**
 * IntegerVariableValue is used to store value that in not known at parse
 * time. It extracts value from variable table.
 */

public class IntegerVariableValue implements IntegerValue {

    public IntegerVariableValue(final CellAddress cell) {
        this.cell = cell;
    }

    @Override
    public BigInteger getValue(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException {
        if (!vars.containsKey(cell)) {
            throw new SimpleSpreadSheetException(ErrorType.TYPE_ERROR, cell.toString(),
                    "Variable doesn't exist.");
        }

        Object value = vars.get(cell);

        if (!(value instanceof BigInteger)) {
            throw new SimpleSpreadSheetException(ErrorType.TYPE_ERROR, cell.toString(), "Variable is not an integer.");
        }

        return (BigInteger) value;
    }

    @Override
    public String toString() {
        return cell.toString();
    }

    private CellAddress cell;

}
