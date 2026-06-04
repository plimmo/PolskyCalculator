package im.plimmo.operator;

import java.util.Stack;

import im.plimmo.exception.CalculatorException;

public class ModOperation implements Operator {

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }

    @Override
    public void execute(Stack<Double> stack) {
        double b = stack.pop();
        double a = stack.pop();
        if (b == 0.0) {
            throw new CalculatorException("Modulo by zero");
        }
        stack.push(a % b);
    }
}
