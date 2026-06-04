package im.plimmo.parser;

import im.plimmo.exception.CalculatorException;
import im.plimmo.operator.Operator;
import im.plimmo.token.Token;
import im.plimmo.token.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ShuntingYardParser implements ExpressionParser {

    @Override
    public List<Token> parse(List<Token> tokens, Map<String, Operator> registry) {
        List<Token> output = new ArrayList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        for (Token token : tokens) {
            if (token.isNumber()) {
                output.add(token);

            } else if (token.isOperator()) {
                String val = token.getValue();
                Operator op = registry.get(val);

                if (op == null) {
                    throw new CalculatorException("Unknown operator: '" + val + "'");
                }

                if (op.getArity() != 2 && op.getArity() != 1) {
                    operatorStack.push(token);
                } else {
                    while (!operatorStack.isEmpty()) {
                        Token top = operatorStack.peek();
                        if (top.isOpenParen()) break;

                        Operator topOp = registry.get(top.getValue());
                        if (topOp == null) break;

                        boolean shouldPop = topOp.getPrecedence() > op.getPrecedence()
                                || (topOp.getPrecedence() == op.getPrecedence() && op.isLeftAssociative());

                        if (shouldPop) {
                            output.add(operatorStack.pop());
                        } else {
                            break;
                        }
                    }
                    operatorStack.push(token);
                }

            } else if (token.isOpenParen()) {
                operatorStack.push(token);

            } else if (token.isCloseParen()) {
                boolean foundOpen = false;
                while (!operatorStack.isEmpty()) {
                    Token top = operatorStack.pop();
                    if (top.isOpenParen()) {
                        foundOpen = true;
                        break;
                    }
                    output.add(top);
                }
                if (!foundOpen) {
                    throw new CalculatorException("Mismatched parentheses: missing '('");
                }
                if (!operatorStack.isEmpty() && operatorStack.peek().isOperator()) {
                    Token top = operatorStack.peek();
                    Operator topOp = registry.get(top.getValue());
                    if (topOp != null && topOp.getArity() != 2 && topOp.getArity() != 1) {
                        output.add(operatorStack.pop());
                    }
                }

            } else if (token.isComma()) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().isOpenParen()) {
                    output.add(operatorStack.pop());
                }
                if (operatorStack.isEmpty()) {
                    throw new CalculatorException("Mismatched parentheses or misplaced comma");
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            Token top = operatorStack.pop();
            if (top.isOpenParen() || top.isCloseParen()) {
                throw new CalculatorException("Mismatched parentheses");
            }
            output.add(top);
        }

        return output;
    }
}