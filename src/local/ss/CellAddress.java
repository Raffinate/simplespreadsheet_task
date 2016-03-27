package local.ss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CellAddress {

	public CellAddress (final String cell) {
		if (!isCellString(cell))
			throw new IllegalArgumentException("Invalid cell string: " + cell);
		
		this.cell = cell.toUpperCase();
		column = charToIndex(cell.charAt(0));
		row = Integer.parseInt(cell.substring(1)) - 1;
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

	public String toString() {
		return cell;
	}
	
    public static boolean isCellString(String cell) {
        return cellPositionRegExp.matcher(cell.toUpperCase()).matches();
    }
    
    public boolean equals(Object other) {
    	if (this == other)
    		return true;
    	if (!(other instanceof CellAddress))
    		return false;
    	return cell.equals(((CellAddress)other).cell);
    }
    
    public int hashCode() {
    	return cell.hashCode();
    }
    
    private static int charToIndex(char ch) {
    	return Character.getNumericValue(ch) - charShift;
    }
    
    private static char indexToChar(int idx) {
    	return (char)(idx + charShift);
    	
    }
    
    private String cell;
    private int column;
    private int row;
    private static final Pattern cellPositionRegExp = Pattern.compile("[a-zA-Z][0-9]");
    private static final int charShift = Character.getNumericValue('A');

}
