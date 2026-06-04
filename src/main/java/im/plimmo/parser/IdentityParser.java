package im.plimmo.parser;

import im.plimmo.operator.Operator;
import im.plimmo.token.Token;
import java.util.List;
import java.util.Map;

public class IdentityParser implements ExpressionParser {
    @Override
    public List<Token> parse(List<Token> tokens, Map<String, Operator> registry) {
        return tokens;
    }
}
