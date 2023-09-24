package constants;

public enum StringOperators {
    IMPLICATION(String.valueOf('\u2192')),
    DOUBLE_IMPLICATION(String.valueOf('\u2194')),
    DISJUNCTION(String.valueOf('\u22C0')),
    CONJUNCTION(String.valueOf('\u22C1')),
    NOT(String.valueOf('\u223C'));

    private final String str;

    StringOperators(String str) {
        this.str = str;
    }

    public static boolean isOperator(String str) {
        StringOperators stringOperator = StringOperators.valueOf(str);

        switch (stringOperator) {
            case IMPLICATION:
            case DOUBLE_IMPLICATION:
            case DISJUNCTION:
            case CONJUNCTION:
            case NOT:
                return true;

            default:
                return false;
        }
    }

    public static int precedenceOfOperator(String operator) {
        StringOperators constant = StringOperators.valueOf(operator);
        switch (constant) {
            case NOT:
                return 1;

            case DISJUNCTION:
                return 2;

            case CONJUNCTION:
                return 3;

            case IMPLICATION:
                return 4;

            case DOUBLE_IMPLICATION:
                return 5;

            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        return this.str;
    }
}
