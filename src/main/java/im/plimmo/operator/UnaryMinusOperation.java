package im.plimmo.operator;

import java.util.Stack;

public class UnaryMinusOperation implements Operator {

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public int getPrecedence() {
        return 3; // выше * и /
    }

    @Override
    public boolean isLeftAssociative() {
        return false; // правоассоциативен
    }

    @Override
    public void execute(Stack<Double> stack) {
        double a = stack.pop();
        stack.push(-a);
    }
}
