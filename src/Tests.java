import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Scanner;

import local.ss.CellAddress;
import local.ss.CellParser;
import local.ss.DataCell;
import local.ss.DfsSolver;
import local.ss.EmptyCell;
import local.ss.ExpressionCell;
import local.ss.SimpleSpreadSheet;
import local.ss.SimpleSpreadSheetException;

/**
 * This class contains test assertions that were used
 * during development.
 */

public class Tests {

    public static void runTests() {
        testCellAddress();
        testCellParser();
        testCellsAndCalculator();
        testSolver();
        testSS();
    }

    public static void testCellAddress() {
        try {
            new CellAddress("A0");
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress("A10");
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress("AA0");
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress("aa0");
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress(-1,0);
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress(26,0);
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress(25,9);
            assert(false);
        } catch (IllegalArgumentException e) {

        }
        try {
            new CellAddress(0,-1);
            assert(false);
        } catch (IllegalArgumentException e) {

        }

        CellAddress ca = new CellAddress("A1");
        CellAddress cb = new CellAddress(0,0);
        assert(ca.equals(cb));
        assert(ca.getColumn() == cb.getColumn());
        assert(ca.getRow() == cb.getRow());

        ca = new CellAddress("z9");
        cb = new CellAddress(25,8);
        assert(ca.equals(cb));
        assert(ca.getColumn() == cb.getColumn());
        assert(ca.getRow() == cb.getRow());

        ca = new CellAddress("B3");
        cb = new CellAddress(1,2);
        assert(ca.equals(cb));
        assert(ca.getColumn() == cb.getColumn());
        assert(ca.getRow() == cb.getRow());
    }

    public static void testCellParser() {

        //Empty cells
        assert(CellParser.parse("") instanceof EmptyCell);

        //Positive integer cells
        assert(CellParser.parse("123") instanceof DataCell);
        assert(CellParser.parse("9999999999999999999999") instanceof DataCell);

        //Invalid cells and negative integers
        assert(CellParser.parse("-123") instanceof DataCell);
        assert(CellParser.parse("A1") instanceof DataCell);

        //String cells
        assert(CellParser.parse("'#-123") instanceof DataCell);

        //Expression cells
        assert(CellParser.parse("=1") instanceof ExpressionCell);
        assert(!(CellParser.parse("=1") instanceof DataCell));
        assert(CellParser.parse("=A1") instanceof ExpressionCell);
        assert(CellParser.parse("=Z9") instanceof ExpressionCell);
        assert(CellParser.parse("=1*Z9+B3/0+25*F8") instanceof ExpressionCell);
        assert(CellParser.parse("=1*Z9+99999999999999999999999999999") instanceof ExpressionCell);

        assert(CellParser.parse("=") instanceof DataCell);
        assert(CellParser.parse("=1*") instanceof DataCell);
        assert(CellParser.parse("=+") instanceof DataCell);
        assert(CellParser.parse("=1*+2") instanceof DataCell);
        assert(CellParser.parse("=1+-2") instanceof DataCell);
        assert(CellParser.parse("=1 * 2") instanceof DataCell);
        assert(CellParser.parse("=1/") instanceof DataCell);
        assert(CellParser.parse("=1p2") instanceof DataCell);
    }

    public static void testCellsAndCalculator() {
        BigInteger bigInt = new BigInteger("999999999999999999999999");
        Hashtable<CellAddress, Object> vars = new Hashtable<>();
        vars.put(new CellAddress("A1"), "");
        vars.put(new CellAddress("Z9"), BigInteger.valueOf(10));
        vars.put(new CellAddress("B3"), BigInteger.valueOf(3));
        vars.put(new CellAddress("F8"), BigInteger.ZERO);
        vars.put(new CellAddress("A2"), "test");
        vars.put(new CellAddress("B7"), bigInt);

        //Empty cells
        assert(CellParser.parse("").calculateValue(vars).toString().equals(""));

        //Integer cells
        assert(((BigInteger)CellParser.parse("123").calculateValue(vars)).intValue() == 123);

        //String cells
        assert(CellParser.parse("'#-123").calculateValue(vars).toString().equals("#-123"));

        //Invalid cells
        assert(CellParser.parse("-123").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("12.3").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=12.3").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1+-1").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("A1").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse(bigInt.toString()).calculateValue(vars).equals(bigInt));

        //Expression cells
        assert(CellParser.parse("=1").calculateValue(vars).equals(BigInteger.valueOf(1)));
        //System.out.println(CellParser.parse("=Z9").calculateValue(vars).toString());
        assert(CellParser.parse("=Z9").calculateValue(vars).equals(BigInteger.valueOf(10)));
        assert(CellParser.parse("=1*Z9+B3/2+F8").calculateValue(vars).equals(BigInteger.valueOf(6)));
        assert(CellParser.parse("=B3-B3").calculateValue(vars).equals(BigInteger.ZERO));
        assert(CellParser.parse("=B7*B7").calculateValue(vars).equals(bigInt.multiply(bigInt)));
        assert(CellParser.parse("=B7+" + bigInt.toString()).calculateValue(vars).equals(bigInt.add(bigInt)));

        assert(CellParser.parse("=A1").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=A2 + 1").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1*Z9+B3/0+25*F8").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1*Z9+B3/F8+25*F8").calculateValue(vars) instanceof SimpleSpreadSheetException);

        assert(CellParser.parse("=1*").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=+").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1*+2").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1+-2").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1 * 2").calculateValue(vars) instanceof SimpleSpreadSheetException);
        assert(CellParser.parse("=1/").calculateValue(vars) instanceof SimpleSpreadSheetException);

        //System.out.println(CellParser.parse("=R2+R2").calculateValue(vars).toString());

    }

    public static void testSolver() {
        Hashtable<CellAddress, String> input = new Hashtable<>();
        DfsSolver solver = new DfsSolver();

        //Testing default input case
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

        assert(solver.getResult().get(new CellAddress("B2")).equals(BigInteger.valueOf(-16)));

        //Testing self recursion
        putInput(input, "A1", "=A1");
        solver = new DfsSolver();

        solver.addCells(input);
        solver.solve();
        //System.out.println(solver);
        assert(solver.getResult().get(new CellAddress("A1")) instanceof SimpleSpreadSheetException);
        assert(solver.getResult().get(new CellAddress("A2")) instanceof SimpleSpreadSheetException);
        assert(solver.getResult().get(new CellAddress("B2")) instanceof SimpleSpreadSheetException);

        putInput(input, "A1", "=b2");
        solver = new DfsSolver();

        solver.addCells(input);
        solver.solve();

        assert(solver.getResult().get(new CellAddress("A1")) instanceof SimpleSpreadSheetException);
        assert(solver.getResult().get(new CellAddress("A2")) instanceof SimpleSpreadSheetException);
        assert(solver.getResult().get(new CellAddress("B2")) instanceof SimpleSpreadSheetException);

        solver = new DfsSolver();
        input.clear();
        putInput(input, "A1", "=A2");
        solver.addCells(input);
        solver.solve();
        assert(solver.getResult().get(new CellAddress("A1")) instanceof SimpleSpreadSheetException);

        //pre-solving should solve this.
        solver = new DfsSolver();
        input.clear();
        putInput(input, "A1", "1");
        solver.addCells(input);
        input.clear();
        putInput(input, "A2", "=A1");
        solver.addCells(input);
        assert(solver.getResult().get(new CellAddress("A2")).equals(BigInteger.valueOf(1)));

    }

    public static void testSS() {
        String[] errinputs = {"", "\n\t\n", "1\n\t\n", "-1\n\t\n", "1 -1\n\t\n",
                "2 2\n\t\t\n", "0 1\n\t\n", "1 0\n\t\n", "1 1\n"};

        String[] inputs = {"1 1\n\n", "1 1\n\t", "1 1\n\t\n", "1 1\n1", "1 2\n1\t=A1",
                "3 2\n1\t=B2\n=B3\t2\n=A1+B1+A2+B2+B3\t1", "1 3  \n\t\t"};
        String[] results = {"\n", "\n", "\n", "1\n", "1\t1\n", "1\t2\n1\t2\n7\t1\n", "\t\t\n"};

        SimpleSpreadSheet ss = new SimpleSpreadSheet();

        for (String s : errinputs) {
            try {
                ss.solve(new Scanner(s));
                System.out.println(s);
                assert(false);
            } catch (Exception e) {

            }
        }
        int i = 0;
        for (String s : inputs) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(ba);
            ss.solve(new Scanner(s));
            ss.write(pw);
            //System.out.println(ba.toString());
            assert(ba.toString().equals(results[i]));
            ++i;
        }
    }

    private static void putInput(Hashtable<CellAddress, String> input, String addr, String data) {
        input.put(new CellAddress(addr), data);
    }
}
