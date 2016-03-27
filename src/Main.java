import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import local.ss.Cell;
import local.ss.CellAddress;
import local.ss.CellParser;
import local.ss.ExpressionCalculator;
import local.ss.ExpressionCellParser;
import local.ss.RecursiveSolver;
import local.ss.SimpleSpreadSheetException;

public class Main {

	public static void main(String[] args) throws SimpleSpreadSheetException {
//
//		Cell cell0 = CellParser.parse("");
//		Cell cell1 = CellParser.parse("-123");
//		Cell cell2 = CellParser.parse("=0-A1+2/2-1*5");
//		Cell cell3 = CellParser.parse("124a3");
//		Cell cell4 = CellParser.parse("=0-A11+2/2-1*5");
//		Cell cell5 = CellParser.parse("'");
//		Cell cell6 = CellParser.parse("'asdq332fr&");
//		Cell cell7 = CellParser.parse("+3");
//		Cell cell8 = CellParser.parse("=b2");
//		Cell cell9 = CellParser.parse("=a1");
//		Cell cell10 = CellParser.parse("=c3");
//		Cell cell11 = CellParser.parse("=Z3");
//		
//		Cell cell = cell11;
//		
//		//System.out.println("Cell: " + cell.toString());
//
//		Hashtable<CellAddress, Object> vars = new Hashtable<>();
//		
//		vars.put(new CellAddress("A1"), -11);
//		vars.put(new CellAddress("b2"), "asd");
//		vars.put(new CellAddress("c3"), "=a1");
//
//		//System.out.println("Result: " + cell.calculateValue(vars).toString());
//		
//		Hashtable<CellAddress, String> input = new Hashtable<>();
//		RecursiveSolver solver = new RecursiveSolver(3, 3);
//		input.put(new CellAddress("A3"), "=A2");
//		input.put(new CellAddress("A2"), "=A1");
//		
//		input.put(new CellAddress("A1"), "=A3");
//		input.put(new CellAddress("B5"), "=3");
//		
//		solver.addCells(input);
//		solver.solve();
		
//		System.out.println(solver);
		
		testA();

	}
	
	private static void testA() {
		Hashtable<CellAddress, String> input = new Hashtable<>();
		RecursiveSolver solver = new RecursiveSolver(4, 3);
		
		putInput(input, "A1", "12");
		putInput(input, "B1", "=C2");
		putInput(input, "C1", "3");
		putInput(input, "D1", "'Sample");
		putInput(input, "A2", "=A1+B1*C1/5");
		putInput(input, "B2", "=A2*B1");
		putInput(input, "C2", "=B3-C3");
		putInput(input, "D2", "'Spread");
		putInput(input, "A3", "'Test");
		putInput(input, "B3", "=4-3");
		putInput(input, "C3", "5");
		putInput(input, "D3", "'Sheet");
		

		
		solver.addCells(input);
		solver.solve();
		
		System.out.println("Result: " + solver);
		
	}
	
	private static void putInput(Hashtable<CellAddress, String> input, String addr, String data) {
		input.put(new CellAddress(addr), data);
	}

}
