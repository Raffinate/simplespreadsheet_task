package local.ss;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import local.ss.SimpleSpreadSheetException.ErrorType;

public class ExpressionCellParser {

    private enum ParserState {
        BEGIN, PARSE_VALUE, PARSE_OPERATION, END
    }

    public ExpressionCellParser() {
        state = ParserState.BEGIN;
        init();
    }

    public ExpressionCalculator parse(final String expression) throws SimpleSpreadSheetException {
        String expr = expression;
        while (true) {
            switch (state) {
            case BEGIN:
                expr = parseBegin(expr);
                break;
            case PARSE_VALUE:
                expr = parseValue(expr);
                break;
            case PARSE_OPERATION:
                expr = parseOperation(expr);
                break;
            case END:
                return parseFinish();
            }
        }
    }

    private String parseBegin(final String expr) throws SimpleSpreadSheetException {

        char firstChar = expr.charAt(0);

        if (firstChar != '=') {
            throw new SimpleSpreadSheetException(ErrorType.EXPRESSION_ERROR, Character.toString(firstChar),
                    "Expression should start with =.");
        }

        state = ParserState.PARSE_VALUE;

        return expr.substring(1);
    }

    private String parseOperation(final String expr) throws SimpleSpreadSheetException {
        //        if (expr.isEmpty()) {
        //            throw new SimpleSpreadSheetException(ErrorType.EXPRESSION_ERROR, "END",
        //                    "Expression string ends unexpectedly.");
        //        }
        assert(!expr.isEmpty());

        char op = expr.charAt(0);
        IntegerBinaryOperation binOp = null;

        switch (op) {
        case '+':
            binOp = new IntegerBinaryOperation() {
                @Override
                public Integer apply(Integer x, Integer y) throws SimpleSpreadSheetException {
                    return x + y;
                }

                @Override
                public String toString() {
                    return "+";
                }
            };
            break;
        case '-':
            binOp = new IntegerBinaryOperation() {
                @Override
                public Integer apply(Integer x, Integer y) throws SimpleSpreadSheetException {
                    return x - y;
                }

                @Override
                public String toString() {
                    return "-";
                }

            };
            break;
        case '*':
            binOp = new IntegerBinaryOperation() {
                @Override
                public Integer apply(Integer x, Integer y) throws SimpleSpreadSheetException {
                    return x * y;
                }

                @Override
                public String toString() {
                    return "*";
                }

            };
            break;
        case '/':
            binOp = new IntegerBinaryOperation() {
                @Override
                public Integer apply(Integer x, Integer y) throws SimpleSpreadSheetException {
                    if (y == 0) {
                        throw new SimpleSpreadSheetException(ErrorType.DIVISION_ERROR, "Division by zero.");
                    }
                    return x / y;
                }

                @Override
                public String toString() {
                    return "/";
                }
            };
            break;
        default:
            throw new SimpleSpreadSheetException(ErrorType.EXPRESSION_ERROR, "\'" + expr.charAt(0) + "\'",
                    "Unexpected symbol in expression.");
        }

        operations.push(binOp);
        state = ParserState.PARSE_VALUE;

        return expr.substring(1);
    }

    private String parseValue(final String expr) throws SimpleSpreadSheetException {
        int substrPosition = 0;

        if (expr.isEmpty()) {
            throw new SimpleSpreadSheetException(ErrorType.EXPRESSION_ERROR, "END",
                    "Expression string ends unexpectedly.");
        }

        substrPosition = CellAddress.cellAddressSubstringEnd(expr);

        if (substrPosition != 0) {
            String address = expr.substring(0, substrPosition);
            CellAddress cell = new CellAddress(address);
            values.push(new IntegerVariableValue(cell));
            dependencies.add(cell);
        } else {
            Matcher numberMatcher = numberRegEx.matcher(expr);

            if ((!numberMatcher.find()) || (numberMatcher.start() != 0)) {
                throw new SimpleSpreadSheetException(ErrorType.EXPRESSION_ERROR, "\'" + expr.charAt(0) + "\'",
                        "Unexpected symbol in expression.");
            }

            substrPosition = numberMatcher.end();
            String numberString = expr.substring(0, substrPosition);
            values.push(new IntegerConstantValue(Integer.parseInt(numberString)));
        }

        String restExpr = expr.substring(substrPosition);

        if (restExpr.isEmpty()) {
            state = ParserState.END;
        } else {
            state = ParserState.PARSE_OPERATION;
        }

        return restExpr;
    }

    private ExpressionCalculator parseFinish() {
        Stack<IntegerValue> vals = reverseStack(values);
        Stack<IntegerBinaryOperation> ops = reverseStack(operations);

        ExpressionCalculator calc = new ExpressionCalculator(vals, ops, dependencies);
        init();
        return calc;
    }

    private void init() {
        state = ParserState.BEGIN;
        values = new Stack<IntegerValue>();
        operations = new Stack<IntegerBinaryOperation>();
        dependencies = new HashSet<CellAddress>();
    }

    private static <T> Stack<T> reverseStack(Stack<T> s) {
        Stack<T> ns = new Stack<T>();

        while (!s.isEmpty()) {
            ns.push(s.pop());
        }

        return ns;
    }

    private ParserState state;
    private Stack<IntegerValue> values;
    private Stack<IntegerBinaryOperation> operations;
    private Set<CellAddress> dependencies;

    private static final Pattern numberRegEx = Pattern.compile("[0-9]+");

}
