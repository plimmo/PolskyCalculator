package im.plimmo.operator;

import java.util.Stack;

public class MultiplicationOperation implements Operator {

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
        stack.push(a * b);
    }
}
