package im.plimmo.tokenizer;

import im.plimmo.exception.CalculatorException;
import im.plimmo.parser.NumberParser;
import im.plimmo.token.Token;
import im.plimmo.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class PostfixTokenizer implements Tokenizer {

    @Override
    public List<Token> tokenize(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new CalculatorException("Expression cannot be empty");
        }

        List<Token> tokens = new ArrayList<>();
        String[] parts = expression.trim().split("\\s+");

        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (NumberParser.isNumber(part)) {
                double value = NumberParser.parse(part);
                tokens.add(new Token(part, TokenType.NUMBER, value));
            } else {
                tokens.add(new Token(part, TokenType.OPERATOR));
            }
        }

        return tokens;
    }
}