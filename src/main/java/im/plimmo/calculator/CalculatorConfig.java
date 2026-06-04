package im.plimmo.calculator;

import im.plimmo.parser.ExpressionParser;
import im.plimmo.tokenizer.Tokenizer;

public class CalculatorConfig {

    private final Tokenizer tokenizer;
    private final ExpressionParser parser;

    public CalculatorConfig(Tokenizer tokenizer, ExpressionParser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public ExpressionParser getParser() {
        return parser;
    }
}
