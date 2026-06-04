package im.plimmo.operator;

import java.util.Stack;

public class SumOperation implements Operator {

    @Override
    public int getArity() {
        return -1;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }

    @Override
    public void execute(Stack<Double> stack) {
        double sum = 0.0;
        while (!stack.isEmpty()) {
            sum += stack.pop();
        }
        stack.push(sum);
    }
}
