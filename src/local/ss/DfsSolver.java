package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import local.ss.SimpleSpreadSheetException.ErrorType;

public class DfsSolver {

    public DfsSolver(int dimx, int dimy) {
        this.dimx = dimx;
        this.dimy = dimy;
        this.cells = new Hashtable<CellAddress, Cell>();
        this.result = new Hashtable<CellAddress, Object>();
    }

    public void addCells(Hashtable<CellAddress, String> input) {
        Set<Entry<CellAddress, String>> inputTable = input.entrySet();
        Set<CellAddress> calculatedVars = result.keySet();

        for (Entry<CellAddress, String> element : inputTable) {
            CellAddress addr = element.getKey();
            String data = element.getValue();
            if ((!data.isEmpty()) && (addressInRange(addr))) {
                Cell cell = CellParser.parse(data);
                Set<CellAddress> deps = cell.getDependencies();

                if ((!addressesInRange(deps)) || (calculatedVars.containsAll(deps))) {
                    //System.out.println("Fast adding: " + cell);
                    result.put(addr, cell.calculateValue(result));
                } else {
                    cells.put(element.getKey(), cell);
                }
            }
        }
    }

    public void solve() {
        while (!cells.isEmpty()) {
            CellAddress currentNode = cells.keySet().iterator().next();
            Set<CellAddress> openedNodes = new HashSet<CellAddress>();
            solveNode(openedNodes, currentNode);
        }
    }

    public Hashtable<CellAddress, Object> getResult() {
        return result;
    }

    private void solveNode(Set<CellAddress> openedNodes, CellAddress node) {

        openedNodes.add(node);

        Cell cell = cells.get(node);

        //        System.out.println("=====solveNode=====");
        //        System.out.println("Solver State: " + this);
        //        System.out.println("openedNodes: " + openedNodes);
        //        System.out.println("currentNode: " + node);
        //        System.out.println("-----solveNode-----");

        Set<CellAddress> deps = cell.getDependencies();

        for (CellAddress ca : deps) {

            if (result.containsKey(ca))
                continue;

            if (!openedNodes.add(ca)) {
                result.put(node, new SimpleSpreadSheetException(ErrorType.CYCLE_ERROR, ca.toString(),
                        "Cyclic reference."));
                cells.remove(node);
                openedNodes.remove(node);
                return;
            }

            solveNode(openedNodes, ca);
        }

        result.put(node, cells.get(node).calculateValue(result));
        cells.remove(node);
        openedNodes.remove(node);

        return;
    }

    private boolean addressInRange(CellAddress address) {
        return (address.getColumn() < dimx) && (address.getRow() < dimy);
    }

    private boolean addressesInRange(Set<CellAddress> addresses) {
        for (CellAddress addr : addresses) {
            if (!addressInRange(addr)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return cells.toString() + result.toString();
    }

    private int dimx;
    private int dimy;

    private Hashtable<CellAddress, Cell> cells;
    private Hashtable<CellAddress, Object> result;

}
