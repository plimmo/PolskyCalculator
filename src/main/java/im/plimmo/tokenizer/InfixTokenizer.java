package im.plimmo.tokenizer;

import im.plimmo.exception.CalculatorException;
import im.plimmo.parser.NumberParser;
import im.plimmo.token.Token;
import im.plimmo.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class InfixTokenizer implements Tokenizer {

    @Override
    public List<Token> tokenize(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new CalculatorException("Expression cannot be empty");
        }

        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int len = expression.length();

        while (i < len) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '(') {
                tokens.add(new Token("(", TokenType.OPEN_PAREN));
                i++;
                continue;
            }

            if (c == ')') {
                tokens.add(new Token(")", TokenType.CLOSE_PAREN));
                i++;
                continue;
            }

            if (c == ',') {
                tokens.add(new Token(",", TokenType.COMMA));
                i++;
                continue;
            }

            if (Character.isDigit(c) || c == '.' ||
                    ((c == '-' || c == '+') && isUnaryContext(tokens) && i + 1 < len
                            && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.'))) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                i++;
                while (i < len && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                String numStr = sb.toString();
                double value = NumberParser.parse(numStr);
                tokens.add(new Token(numStr, TokenType.NUMBER, value));
                continue;
            }

            if (Character.isLetter(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < len && (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '_')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                tokens.add(new Token(sb.toString(), TokenType.OPERATOR));
                continue;
            }

            StringBuilder sb = new StringBuilder();
            while (i < len && isSymbolOperatorChar(expression.charAt(i))) {
                sb.append(expression.charAt(i));
                i++;
            }
            if (!sb.isEmpty()) {
                tokens.add(new Token(sb.toString(), TokenType.OPERATOR));
                continue;
            }

            throw new CalculatorException("Unknown character: '" + c + "' at position " + i);
        }

        return tokens;
    }

    private boolean isUnaryContext(List<Token> tokens) {
        if (tokens.isEmpty()) return true;
        Token last = tokens.get(tokens.size() - 1);
        return last.isOperator() || last.isOpenParen() || last.isComma();
    }

    private boolean isSymbolOperatorChar(char c) {
        return !Character.isLetterOrDigit(c) && !Character.isWhitespace(c)
                && c != '(' && c != ')' && c != ',' && c != '.';
    }
}