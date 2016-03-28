package local.ss;

import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

public class ExpressionCalculator {

    public ExpressionCalculator(Stack<IntegerValue> values, Stack<IntegerBinaryOperation> operations,
            Set<CellAddress> dependencies) {
        this.values = values;
        this.operations = operations;
        this.dependencies = dependencies;
    }

    public Integer calculate(Hashtable<CellAddress, Object> vars) throws SimpleSpreadSheetException {

        while (!operations.isEmpty()) {
            if (values.size() < 2) {
                throw new IllegalArgumentException("Not enough values in passed argument.");
            }
            IntegerBinaryOperation op = operations.pop();

            IntegerValue leftOp = values.pop();
            IntegerValue rightOp = values.pop();
            values.push(new IntegerConstantValue(op.apply(leftOp.getValue(vars), rightOp.getValue(vars))));
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("Too many values in passed argument.");
        }

        return values.peek().getValue(vars);
    }

    @Override
    public String toString() {
        return values.toString() + operations.toString() + dependencies.toString();
    }

    public Set<CellAddress> getDependencies() {
        return dependencies;
    }

    private Stack<IntegerValue> values;
    private Stack<IntegerBinaryOperation> operations;
    private Set<CellAddress> dependencies;

}
