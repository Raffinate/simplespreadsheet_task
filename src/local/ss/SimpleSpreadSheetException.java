package local.ss;

public class SimpleSpreadSheetException extends Exception {

    public enum ErrorType {
        EXPRESSION_ERROR,
        PARSE_ERROR,
        DIVISION_ERROR,
        TYPE_ERROR,
        CYCLE_ERROR
    }

    public SimpleSpreadSheetException(ErrorType type, String description) {
        super(description);
        this.type = type;
        shortMessage = "#" + typeToString(type);
    }

    public SimpleSpreadSheetException(ErrorType type, String data, String description) {
        super(description);
        this.type=type;
        shortMessage = "#" + typeToString(type) + ":" + data;
    }

    public static String typeToString(ErrorType err) {
        switch(err) {
        case EXPRESSION_ERROR:
            return "PARSER";
        case PARSE_ERROR:
            return "PARSER";
        case DIVISION_ERROR:
            return "DIVBY0";
        case TYPE_ERROR:
            return "NOTINT";
        case CYCLE_ERROR:
            return "RECUR";
        }

        return "ERR";
    }

    @Override
    public String toString() {
        return shortMessage;
    }

    private String shortMessage;
    ErrorType type;

    public static final long serialVersionUID = 1L;
}
