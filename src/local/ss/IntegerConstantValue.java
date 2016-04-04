package local.ss;

import java.math.BigInteger;
import java.util.Hashtable;

/**
 * IntegerConstantValue stores integer constants for
 * expression evaluation.
 */

public class IntegerConstantValue implements IntegerValue {

    public IntegerConstantValue(final BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger getValue(Hashtable<CellAddress, Object> vars) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString(10);
    }

    private final BigInteger value;
}
