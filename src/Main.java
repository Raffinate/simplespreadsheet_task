import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import local.ss.SimpleSpreadSheet;
import local.ss.SimpleSpreadSheetException;

public class Main {

    public static void main(String[] args) throws SimpleSpreadSheetException {

        Tests.runTests();

        SimpleSpreadSheet ss = new SimpleSpreadSheet();

        try {
            //InputStream is = new FileInputStream(new File("./tests/t10.txt"));
            ss.solve(new Scanner(System.in));
            //ss.solve(new Scanner(is));
            ss.write(new PrintWriter(System.out));
        } catch (InputMismatchException e) {
            System.out.println("Need valid table dimensions.");
        } catch (NoSuchElementException e) {
            System.out.println("Not enough data in input table.");
        } catch (IllegalArgumentException e) {
            System.out.println("Table is too large for english alphabet [A-Z] or digit [1-9]. ");
        }// catch (FileNotFoundException e) {
        //  System.out.println("Where is my Spreadsheet?.");
        //}

    }

}
