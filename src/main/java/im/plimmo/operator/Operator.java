package im.plimmo.operator;

import java.util.Stack;

public interface Operator {
    int getArity();
    int getPrecedence();
    boolean isLeftAssociative();
    void execute(Stack<Double> stack);
}