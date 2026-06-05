package im.plimmo.operator;

import java.util.Stack;

public class UnaryMinusOperation implements Operator {

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public boolean isLeftAssociative() {
        return false;
    }

    @Override
    public void execute(Stack<Double> stack) {
        double a = stack.pop();
        stack.push(-a);
    }
}
