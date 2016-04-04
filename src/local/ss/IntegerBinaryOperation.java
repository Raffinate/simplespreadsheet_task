package local.ss;

import java.math.BigInteger;

/**
 * Interface for internal binary operations on integers.
 */

public interface IntegerBinaryOperation {
    public BigInteger apply(BigInteger x, BigInteger y) throws SimpleSpreadSheetException;
}
