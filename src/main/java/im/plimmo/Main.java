package im.plimmo;

import im.plimmo.calculator.Calculator;
import im.plimmo.calculator.CalculatorConfig;
import im.plimmo.operator.*;
import im.plimmo.parser.ShuntingYardParser;
import im.plimmo.token.Token;
import im.plimmo.tokenizer.InfixTokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Часть 1: Базовый RPN-калькулятор ===");
        basicRpnDemo();

        System.out.println();
        System.out.println("=== Часть 2: Программируемый польский калькулятор ===");
        programmableRpnDemo();

        System.out.println();
        System.out.println("=== Часть 3: Инфиксный калькулятор (алгоритм сортировочной станции) ===");
        infixDemo();

        System.out.println();
        System.out.println("=== Интерактивный режим (инфиксная нотация) ===");
        System.out.println("Введите выражение или 'exit' для выхода:");
        interactiveMode();
    }

    private static void basicRpnDemo() {
        Calculator calc = new Calculator();
        calc.register("+", new AdditionOperation());
        calc.register("-", new SubtractionOperation());
        calc.register("*", new MultiplicationOperation());
        calc.register("/", new DivisionOperation());

        int expected = (4 + 1) * 5;
        int actual = (int) calc.calculate("4 1 + 5 *");
        if (expected == actual) {
            System.out.println("Вычисления правильны, ответ: " + actual);
        } else {
            System.out.println("Ожидалось: " + expected + "; но получили " + actual);
        }

        double r2 = calc.calculate("10 2 / 3 -");
        printTest("10 / 2 - 3", 2.0, r2);

        double r3 = calc.calculate("-3 5 +");
        printTest("-3 + 5", 2.0, r3);
    }

    private static void programmableRpnDemo() {
        Calculator calc = new Calculator();
        calc.register("+", new AdditionOperation());
        calc.register("-", new SubtractionOperation());
        calc.register("*", new MultiplicationOperation());
        calc.register("/", new DivisionOperation());
        calc.register("%", new ModOperation());
        calc.register("m", new UnaryMinusOperation());  // унарный минус
        calc.register("sum", new SumOperation());

        double r1 = calc.calculate("6 8 + 3 %");
        printTest("(6 + 8) % 3", 2.0, r1);

        double r2 = calc.calculate("1 2 3 4 5 6 sum");
        printTest("sum(1,2,3,4,5,6)", 21.0, r2);

        double r3 = calc.calculate("1 2 3 sum 4 *");
        printTest("sum(1,2,3) * 4", 24.0, r3);

        double r4 = calc.calculate("1.5 2.5 +");
        printTest("1.5 + 2.5", 4.0, r4);

        double r5 = calc.calculate("5 m");
        printTest("m(5) = -5", -5.0, r5);

        double r6 = calc.calculate("3 m 7 +");
        printTest("m(3) + 7", 4.0, r6);
    }

    private static void infixDemo() {
        CalculatorConfig infixConfig = new CalculatorConfig(
                new InfixTokenizer(),
                new ShuntingYardParser()
        );
        Calculator calc = new Calculator(infixConfig);

        calc.register("+", new AdditionOperation());
        calc.register("-", new SubtractionOperation());
        calc.register("*", new MultiplicationOperation());
        calc.register("/", new DivisionOperation());
        calc.register("%", new ModOperation());
        calc.register("sum", new SumOperation());

        String eq1 = "sum(1, 2, 3, 4) / 5 + 9 % 2";
        List<Token> tokens1 = calc.tokenize(eq1);
        List<Token> polish1 = calc.toPolishNotation(tokens1);
        double result1 = calc.evaluate(polish1);
        System.out.println("Выражение: " + eq1);
        System.out.println("RPN:       " + tokensToString(polish1));
        printTest("sum(1,2,3,4)/5 + 9%2", 3.0, result1);

        String eq2 = "(3 + 4) * 2";
        double result2 = calc.calculate(eq2);
        printTest("(3 + 4) * 2", 14.0, result2);

        String eq3 = "10 - 2 * 3";
        double result3 = calc.calculate(eq3);
        printTest("10 - 2 * 3", 4.0, result3);

        String eq4 = "sum(1, 2, 3) * 4";
        double result4 = calc.calculate(eq4);
        printTest("sum(1,2,3) * 4", 24.0, result4);

        String eq5 = "7.5 / 2.5";
        double result5 = calc.calculate(eq5);
        printTest("7.5 / 2.5", 3.0, result5);
    }

    private static void interactiveMode() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Выберите режим:");
                System.out.println("  1 — Базовый польский калькулятор (+ - * /, например: 4 1 + 5 *)");
                System.out.println("  2 — Программируемый польский калькулятор (+ - * / % m sum, например: 1 2 3 sum)");
                System.out.println("  3 — Нормальный калькулятор (инфиксная нотация, например: (3 + 4) * 2)");
                System.out.println("  exit — выйти из программы");
                System.out.print("> ");

                String modeInput = reader.readLine();
                if (modeInput == null || modeInput.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Выход из программы.");
                    break;
                }

                Calculator calc;
                switch (modeInput.trim()) {
                    case "1" -> {
                        calc = new Calculator();
                        calc.register("+", new AdditionOperation());
                        calc.register("-", new SubtractionOperation());
                        calc.register("*", new MultiplicationOperation());
                        calc.register("/", new DivisionOperation());
                        System.out.println("Режим: базовый польский калькулятор");
                        System.out.println("Операторы: + - * /");
                        System.out.println("Примеры: '4 1 + 5 *', '10 2 / 3 -', '-3 5 +'");
                    }
                    case "2" -> {
                        calc = new Calculator();
                        calc.register("+", new AdditionOperation());
                        calc.register("-", new SubtractionOperation());
                        calc.register("*", new MultiplicationOperation());
                        calc.register("/", new DivisionOperation());
                        calc.register("%", new ModOperation());
                        calc.register("m", new UnaryMinusOperation());
                        calc.register("sum", new SumOperation());
                        System.out.println("Режим: программируемый польский калькулятор");
                        System.out.println("Операторы: + - * / % m sum");
                        System.out.println("Примеры: '6 8 + 3 %', '1 2 3 4 5 6 sum', '5 m', '1.5 2.5 +'");
                    }
                    case "3" -> {
                        CalculatorConfig infixConfig = new CalculatorConfig(new InfixTokenizer(), new ShuntingYardParser()
                        );
                        calc = new Calculator(infixConfig);
                        calc.register("+", new AdditionOperation());
                        calc.register("-", new SubtractionOperation());
                        calc.register("*", new MultiplicationOperation());
                        calc.register("/", new DivisionOperation());
                        calc.register("%", new ModOperation());
                        calc.register("sum", new SumOperation());
                        System.out.println("Режим: нормальный калькулятор (инфиксный)");
                        System.out.println("Операторы: + - * / % sum(...)");
                        System.out.println("Примеры: '(3 + 4) * 2', 'sum(1, 2, 3) * 4', '10 - 2 * 3'");
                    }
                    default -> {
                        System.out.println("Неверный выбор, попробуй снова.");
                        System.out.println();
                        continue;
                    }
                }

                System.out.println("Введите выражение. 'exit' — вернуться к выбору режима.");
                System.out.print("\n> ");

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equalsIgnoreCase("exit")) {
                        System.out.println("Возврат к выбору режима.");
                        System.out.println();
                        break;
                    }
                    if (line.trim().isEmpty()) {
                        System.out.print("> ");
                        continue;
                    }
                    try {
                        List<Token> tokens = calc.tokenize(line);
                        List<Token> polishTokens = calc.toPolishNotation(tokens);
                        double result = calc.evaluate(polishTokens);
                        System.out.println("RPN: " + tokensToString(polishTokens));
                        System.out.println("Результат: " + result);
                    } catch (Exception e) {
                        System.out.println("Ошибка в выражении: " + e.getMessage());
                    }
                    System.out.print("\n> ");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения ввода: " + e.getMessage());
        }
    }

    private static void printTest(String label, double expected, double actual) {
        String status = Math.abs(expected - actual) < 1e-9 ? "✓" : "✗";
        System.out.printf("  [%s] %s => ожидалось: %s, получили: %s%n",
                status, label, formatNum(expected), formatNum(actual));
    }

    private static String formatNum(double v) {
        if (v == Math.floor(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }

    private static String tokensToString(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(t.getValue());
        }
        return sb.toString();
    }
}