package im.plimmo.parser;

import im.plimmo.operator.Operator;
import im.plimmo.token.Token;
import java.util.List;
import java.util.Map;

public interface ExpressionParser {
    List<Token> parse(List<Token> tokens, Map<String, Operator> registry);
}
