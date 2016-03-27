package local.ss;

public class CellParser {
	
	static public Cell parse(final String data) {
		if (data.isEmpty()) {
			return EmptyCell.emptyCell();
		}
		
		if (data.charAt(0) == '\'') {
			return parseString(data);
		}
		
		if (Character.isDigit(data.charAt(0))) {
			return parseInteger(data);
		}
		
		if (data.charAt(0) == '=') {
			return parseExpression(data);
		}
		
		return new DataCell(new SimpleSpreadSheetException("Cell data is invalid.", "EPRS"));
		
	}
	
	private static DataCell parseString(final String data) {
		
		return new DataCell(data.substring(1));
	}
	
	private static Cell parseInteger(final String data) {
		int result = 0;
		try {
			result = Integer.parseInt(data);
		} catch (NumberFormatException e) {
			return new DataCell(new SimpleSpreadSheetException("Not a valid positive number: " + data + ".", "EINT"));
		}
		
		return new DataCell(new Integer(result));
		
	}
	
	private static Cell parseExpression(final String data) {
		ExpressionCellParser parser = new ExpressionCellParser();
		try {
			ExpressionCalculator calc = parser.parse(data);
			return new ExpressionCell(calc);
		} catch (SimpleSpreadSheetException e) {
			return new DataCell(e);
		}
	}
}
