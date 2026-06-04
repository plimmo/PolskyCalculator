package im.plimmo.token;

public class Token {
    private final String value;
    private final TokenType type;
    private final double numericValue;

    public Token(String value, TokenType type, double numericValue) {
        this.value = value;
        this.type = type;
        this.numericValue = numericValue;
    }

    public Token(String value, TokenType type) {
        this(value, type, 0.0);
    }

    public String getValue() {
        return value;
    }

    public double getNumericValue() {
        return numericValue;
    }

    public boolean isNumber() {
        return type == TokenType.NUMBER;
    }

    public boolean isOperator() {
        return type == TokenType.OPERATOR;
    }

    public boolean isOpenParen() {
        return type == TokenType.OPEN_PAREN;
    }

    public boolean isCloseParen() {
        return type == TokenType.CLOSE_PAREN;
    }

    public boolean isComma() {
        return type == TokenType.COMMA;
    }

    @Override
    public String toString() {
        return "Token{value='" + value + "', type=" + type + "}";
    }
}
