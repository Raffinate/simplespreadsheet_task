package local.ss;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import local.ss.SimpleSpreadSheetException.ErrorType;

/**
 * ExpressionCellParser is a
 * deterministic finite state automata is used to
 * parse an expression to internal ExpressionCalculator representation.
 * finite state automata is used here because expression grammar is simple.
 * Order of evaluation is from left to right in expression.
 */

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
            binOp = getAdditionOperation();
            break;
        case '-':
            binOp = getSubtractionOperation();
            break;
        case '*':
            binOp = getMultiplicationOperation();
            break;
        case '/':
            binOp = getDivisionOperation();
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
            BigInteger value = BigInteger.valueOf(0);
            try {
                value = new BigInteger(numberString);
            } catch (NumberFormatException e) {
                assert(false);
            }
            values.push(new IntegerConstantValue(value));
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
        //We reverse stacks here because all operation priorities
        //are the same. This creates different results depending of
        //calculation order. Reversing stacks makes left's operation
        //priority higher (as in task example).
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

    private static IntegerBinaryOperation getAdditionOperation() {
        return new IntegerBinaryOperation() {
            @Override
            public BigInteger apply(BigInteger x, BigInteger y) throws SimpleSpreadSheetException {
                return x.add(y);
            }

            @Override
            public String toString() {
                return "+";
            }
        };
    }

    private static IntegerBinaryOperation getSubtractionOperation() {
        return new IntegerBinaryOperation() {
            @Override
            public BigInteger apply(BigInteger x, BigInteger y) throws SimpleSpreadSheetException {
                return x.subtract(y);
            }

            @Override
            public String toString() {
                return "-";
            }
        };
    }

    private static IntegerBinaryOperation getMultiplicationOperation() {
        return new IntegerBinaryOperation() {
            @Override
            public BigInteger apply(BigInteger x, BigInteger y) throws SimpleSpreadSheetException {
                return x.multiply(y);
            }

            @Override
            public String toString() {
                return "*";
            }
        };
    }

    private static IntegerBinaryOperation getDivisionOperation() {
        return new IntegerBinaryOperation() {
            @Override
            public BigInteger apply(BigInteger x, BigInteger y) throws SimpleSpreadSheetException {
                if (y.equals(BigInteger.ZERO)) {
                    throw new SimpleSpreadSheetException(ErrorType.DIVISION_ERROR, "Division by zero.");
                }
                return x.divide(y);
            }

            @Override
            public String toString() {
                return "/";
            }
        };
    }

    private ParserState state;
    private Stack<IntegerValue> values;
    private Stack<IntegerBinaryOperation> operations;
    private Set<CellAddress> dependencies;

    private static final Pattern numberRegEx = Pattern.compile("[0-9]+");

}
