package constants;

public class Brackets {
    public final static String ROUND_OPENING = "(";
    public final static String ROUND_CLOSING = ")";
    public final static String SQUARE_OPENING = "[";
    public final static String SQUARE_CLOSING = "]";
    public final static String CURLY_OPENING = "{";
    public final static String CURLY_CLOSING = "}";

    public static boolean isOpeningBracket(String str) {
        switch (str) {
            case ROUND_OPENING:
            case SQUARE_OPENING:
            case CURLY_OPENING:
                return true;

            default:
                return false;
        }
    }

    public static String correspondingClosingBracket(String bracket) {
        switch (bracket) {
            case ROUND_OPENING:
                return ROUND_CLOSING;
            case CURLY_OPENING:
                return CURLY_CLOSING;
            case SQUARE_OPENING:
                return SQUARE_CLOSING;
        }

        return null;
    }
}
