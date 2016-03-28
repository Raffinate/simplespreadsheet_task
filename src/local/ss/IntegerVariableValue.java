package local.ss;

import java.util.Hashtable;

import local.ss.SimpleSpreadSheetException.ErrorType;

public class IntegerVariableValue implements IntegerValue {

    public IntegerVariableValue(final CellAddress cell) {
        this.cell = cell;
    }

    @Override
    public Integer getValue(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException {
        if (!vars.containsKey(cell)) {
            throw new SimpleSpreadSheetException(ErrorType.TYPE_ERROR, cell.toString(),
                    "Variable is not an integer or doesn't exist.");
        }

        Object value = vars.get(cell);

        if (!(value instanceof Integer)) {
            throw new SimpleSpreadSheetException(ErrorType.TYPE_ERROR, cell.toString(), "Variable is not an integer.");
        }

        return (Integer) value;
    }

    @Override
    public String toString() {
        return cell.toString();
    }

    private CellAddress cell;

}
