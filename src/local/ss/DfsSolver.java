package local.ss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import local.ss.SimpleSpreadSheetException.ErrorType;

/*
 * Since there can be a lot of empty cells, empty cells that
 * are passed as input are ignored and every cells that is not
 * in remaining input is considered as empty implicitly.
 * Such behavior doesn't affect the result according to current
 * calculation rules.
 */

public class DfsSolver {

    public DfsSolver() {
        this.cells = new Hashtable<CellAddress, Cell>();
        this.result = new Hashtable<CellAddress, Object>();
    }

    public void addCells(Hashtable<CellAddress, String> input) {
        Set<Entry<CellAddress, String>> inputTable = input.entrySet();
        Set<CellAddress> calculatedVars = result.keySet();

        for (Entry<CellAddress, String> element : inputTable) {
            CellAddress addr = element.getKey();
            String data = element.getValue();
            if ((!data.isEmpty())){ //&& (addressInRange(addr))) {
                Cell cell = CellParser.parse(data);
                Set<CellAddress> deps = cell.getDependencies();

                if (calculatedVars.containsAll(deps)) {
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

        assert(cells.containsKey(node));
        Cell cell = cells.get(node);

        //        System.out.println("=====solveNode=====");
        //        System.out.println("Solver State: " + this);
        //        System.out.println("openedNodes: " + openedNodes);
        //        System.out.println("currentNode: " + node);
        //        System.out.println("-----solveNode-----");

        Set<CellAddress> deps = cell.getDependencies();

        for (CellAddress ca : deps) {

            if (!cells.containsKey(ca))
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

    @Override
    public String toString() {
        return cells.toString() + result.toString();
    }

    private Hashtable<CellAddress, Cell> cells;
    private Hashtable<CellAddress, Object> result;

}
