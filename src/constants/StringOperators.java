package constants;

public class StringOperators {
    public final static String IMPLICATION = String.valueOf('\u2192');
    public final static String DOUBLE_IMPLICATION = String.valueOf('\u2194');
    public final static String OR = String.valueOf('\u22C1');
    public final static String AND = String.valueOf('\u22C0');
    public final static String NEGATION = String.valueOf('\u223C');

    public static boolean isOperator(String str) {
        if(str.equals(IMPLICATION)) {
            return true;
        } else if(str.equals(DOUBLE_IMPLICATION)) {
            return true;
        } else if(str.equals(OR)) {
            return true;
        } else if(str.equals(AND)) {
            return true;
        } else if(str.equals(NEGATION)) {
            return true;
        }
        return false;
    }

    public static int precedenceOfOperator(String operator) {
        if(operator.equals(NEGATION)) {
            return 1;
        } else if(operator.equals(OR)) {
            return 2;
        } else if(operator.equals(AND)) {
            return 3;
        } else if(operator.equals(IMPLICATION)) {
            return 4;
        } else if(operator.equals(DOUBLE_IMPLICATION)) {
            return 5;
        }
        return -1;
    }
}
