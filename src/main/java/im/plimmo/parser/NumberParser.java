package im.plimmo.parser;

import im.plimmo.exception.CalculatorException;

public class NumberParser {

    private NumberParser() {}

    public static double parse(String s) {
        if (s == null || s.isEmpty()) {
            throw new CalculatorException("Cannot parse empty string as number");
        }

        int start = 0;
        boolean negative = false;

        if (s.charAt(0) == '-') {
            negative = true;
            start = 1;
            if (s.length() == 1) {
                throw new CalculatorException("Cannot parse '-' as number");
            }
        } else if (s.charAt(0) == '+') {
            start = 1;
            if (s.length() == 1) {
                throw new CalculatorException("Cannot parse '+' as number");
            }
        }

        long intPart = 0;
        long fracPart = 0;
        long fracDivisor = 1;
        boolean hasDot = false;
        boolean hasDigits = false;

        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.' || c == ',') {
                if (hasDot) {
                    throw new CalculatorException("Invalid number format: multiple dots in '" + s + "'");
                }
                hasDot = true;
            } else if (c >= '0' && c <= '9') {
                hasDigits = true;
                int digit = c - '0';
                if (hasDot) {
                    fracPart = fracPart * 10 + digit;
                    fracDivisor *= 10;
                } else {
                    intPart = intPart * 10 + digit;
                }
            } else {
                throw new CalculatorException("Invalid character '" + c + "' in number '" + s + "'");
            }
        }

        if (!hasDigits) {
            throw new CalculatorException("No digits found in '" + s + "'");
        }

        double result = intPart + (double) fracPart / fracDivisor;
        return negative ? -result : result;
    }

    public static boolean isNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            parse(s);
            return true;
        } catch (CalculatorException e) {
            return false;
        }
    }
}
