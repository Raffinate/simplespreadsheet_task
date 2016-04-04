package local.ss;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Reader is used to read spreadsheet line by line.
 */

public class Reader {


    public Reader(Scanner lineScanner, int dimx) {
        this.lineScanner = lineScanner;
        this.dimx = dimx;
        currentLine = -1;
    }

    public Hashtable<CellAddress, String> processNextLine() {
        int currentColumn = 0;

        Hashtable<CellAddress, String> result = new Hashtable<CellAddress,String>();

        for (String s : readNextLine()) {
            //System.out.println(s);
            result.put(new CellAddress(currentColumn, currentLine), s);
            currentColumn++;
            if (currentColumn >= dimx) {
                break;
            }
        }
        if (currentColumn < dimx) {
            throw new NoSuchElementException("Not enough columns in table");
        }

        return result;
    }

    private String[] readNextLine() {
        String line = lineScanner.nextLine();
        currentLine++;
        return splitLine(line);
    }

    private String[] splitLine(String line) {
        return line.split("\t",-1);
    }

    // private Pattern columnDelimiter = Pattern.compile("\t");
    private Scanner lineScanner;
    private int dimx;
    private int currentLine;
}
