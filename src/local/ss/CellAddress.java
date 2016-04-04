package local.ss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CellAddress class checks and stores Spreadsheet cell address.
 * Address is either a [A-Za-z][1-9] string where
 * lower-case letters are the same as upper-case analogs,
 * or two integers (column and row), where column is in [0,25] range
 * and row is in [0,8] range. Letters correspond to columns.
 *
 */

public final class CellAddress {

    public CellAddress(final String cell) {
        if (!isCellString(cell))
            throw new IllegalArgumentException("Invalid cell string: " + cell);

        this.cell = cell.toUpperCase();

        column = charToIndex(this.cell.charAt(0));
        row = Integer.parseInt(this.cell.substring(1)) - 1;

        assert(isCellPosition(column, row));
    }

    public CellAddress(int column, int row) {

        if (!isCellPosition(column, row))
            throw new IllegalArgumentException("Invalid cell coordinates: (" + column + "," + row + ")");

        this.column = column;
        this.row = row;
        cell =  Character.toString(indexToChar(column)) + Integer.toString(row + 1);

        assert(isCellString(cell));
    }

    public static int cellAddressSubstringEnd(final String expressionString) {
        Matcher matcher = cellPositionRegExp.matcher(expressionString);
        if (!matcher.find()) {
            return 0;
        }
        if (matcher.start() != 0) {
            return 0;
        }
        return matcher.end();
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return cell;
    }

    public static boolean isCellString(String cell) {
        return cellPositionRegExp.matcher(cell.toUpperCase()).matches();
    }

    public static boolean isCellPosition(int col, int row) {
        return (col >= 0) && (row >= 0) && (col <= 'Z'-'A') && (row <= '9' - '1');
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof CellAddress))
            return false;
        return cell.equals(((CellAddress) other).cell);
    }

    @Override
    public int hashCode() {
        return cell.hashCode();
    }

    private static int charToIndex(char ch) {
        return (ch) - charShift;
    }

    private static char indexToChar(int idx) {
        return (char) (idx + charShift);

    }

    private String cell;
    private int column;
    private int row;
    private static final Pattern cellPositionRegExp = Pattern.compile("[a-zA-Z][1-9]");
    private static final int charShift = ('A');

}
