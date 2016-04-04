package local.ss;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Basic class that reads and solve spreadsheet,
 * accumulates the result and writes result
 * result of spreadsheet calculations.
 * Numbers are considered as BigIntegers, so
 * large numbers should not overflow.
 */

public class SimpleSpreadSheet {
    public SimpleSpreadSheet() {
        init();
    }

    private void init() {
        dimx = 0;
        dimy = 0;
        result = new Hashtable<>();
    }

    public void solve(Scanner s) {
        init();

        dimy = s.nextInt();
        dimx = s.nextInt();

        s.nextLine();

        if ((dimy <= 0) || (dimx <= 0)) {
            throw new InputMismatchException();
        }

        Reader reader = new Reader(s, dimx);
        DfsSolver solver = new DfsSolver();

        for (int i = 0; i < dimy; ++i) {
            Hashtable<CellAddress, String> line = reader.processNextLine();
            solver.addCells(line);
        }

        solver.solve();
        result = solver.getResult();
    }

    public void write(PrintWriter writer) {

        //writer.println("*******************");

        for (int y = 0; y < dimy; ++y) {
            for (int x = 0; x < dimx; ++x) {
                CellAddress cell = new CellAddress(x, y);
                if (result.containsKey(cell)) {
                    writer.print(result.get(cell));
                }
                if (x < dimx - 1)
                    writer.write('\t');
            }
            writer.println();
        }
        writer.flush();
    }

    private int dimx;
    private int dimy;
    private Hashtable<CellAddress, Object> result;
}
