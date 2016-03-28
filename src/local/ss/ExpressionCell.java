package local.ss;

import java.util.Hashtable;
import java.util.Set;

public class ExpressionCell implements Cell {

    public ExpressionCell(ExpressionCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public Object calculateValue(final Hashtable<CellAddress, Object> vars) {
        Object result = null;
        try {
            result = calculator.calculate(vars);
        } catch (SimpleSpreadSheetException e) {
            result = e;
        }
        return result;
    }

    @Override
    public Set<CellAddress> getDependencies() {

        return calculator.getDependencies();
    }

    @Override
    public String toString() {
        return "<EXPR: " + calculator.toString() + ">";
    }

    private ExpressionCalculator calculator;

}
