package local.ss;

import java.math.BigInteger;
import java.util.Hashtable;

/**
 * Interface for integers that are used ExpressionCalculator.
 */

public interface IntegerValue {

    public BigInteger getValue(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException;

}
