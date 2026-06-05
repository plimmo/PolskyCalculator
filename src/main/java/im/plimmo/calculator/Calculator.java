package im.plimmo.calculator;

import im.plimmo.exception.CalculatorException;
import im.plimmo.operator.Operator;
import im.plimmo.parser.IdentityParser;
import im.plimmo.token.Token;
import im.plimmo.tokenizer.PostfixTokenizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Calculator {

    private final Map<String, Operator> registry = new HashMap<>();
    private final CalculatorConfig config;

    public Calculator() {
        this(new CalculatorConfig(new PostfixTokenizer(), new IdentityParser()));
    }

    public Calculator(CalculatorConfig config) {
        this.config = config;
    }

    public void register(String symbol, Operator operator) {
        registry.put(symbol, operator);
    }

    public List<Token> tokenize(String expression) {
        return config.getTokenizer().tokenize(expression);
    }

    public List<Token> toPolishNotation(List<Token> tokens) {
        return config.getParser().parse(tokens, registry);
    }

    public double calculate(String expression) {
        List<Token> tokens = tokenize(expression);
        List<Token> polishTokens = toPolishNotation(tokens);
        return evaluate(polishTokens);
    }

    public double evaluate(List<Token> tokens) {
        Stack<Double> stack = new Stack<>();

        for (Token token : tokens) {
            if (token.isNumber()) {
                stack.push(token.getNumericValue());

            } else if (token.isOperator()) {
                String symbol = token.getValue();
                Operator op = registry.get(symbol);

                if (op == null) {
                    throw new CalculatorException("Unknown operator: '" + symbol + "'");
                }

                int arity = op.getArity();

                if (arity == -1) {
                    op.execute(stack);
                } else {
                    if (stack.size() < arity) {
                        throw new CalculatorException("Not enough operands for operator '" + symbol + "': need " + arity + ", got " + stack.size());
                    }
                    op.execute(stack);
                }

            } else {
                throw new CalculatorException("Unexpected token: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new CalculatorException("Invalid expression: stack has " + stack.size() + " elements after evaluation");
        }

        return stack.pop();
    }

}