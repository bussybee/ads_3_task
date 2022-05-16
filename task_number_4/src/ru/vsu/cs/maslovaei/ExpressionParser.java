package ru.vsu.cs.maslovaei;

import java.util.*;
import java.lang.*;

class ExpressionParser {
    private static final String operators = "+-*/";
    private static final String delimiters = "()" + operators;
    public static boolean flag = true;

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isOperator(String token) {
        if (token.equals("u-")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private static int getPriority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        return 4;
    }

    public static List<String> analyzeExpression(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String previous = "";
        String current = "";
        
        while (tokenizer.hasMoreTokens()) {
            current = tokenizer.nextToken();

            if (!tokenizer.hasMoreTokens() && isOperator(current)) {
                System.out.println("Некорректное выражение.");
                flag = false;
                return postfix;
            }

            if (current.equals(" ")) continue;
            else if (isDelimiter(current)) {
                if (current.equals("(")) {
                    stack.push(current);
                }
                else if (current.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            System.out.println("Скобки не согласованы.");
                            flag = false;
                            return postfix;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty()) {
                        postfix.add(stack.pop());
                    }
                } else {
                    if (current.equals("-") && (previous.equals("") || (isDelimiter(previous) && !previous.equals(")")))) {
                        current = "u-";
                    } else {
                        while (!stack.isEmpty() && (getPriority(current) <= getPriority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }

                    }
                    stack.push(current);
                }

            } else {
                postfix.add(current);
            }
            previous = current;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) {
                postfix.add(stack.pop());
            }
            else {
                System.out.println("Скобки не согласованы.");
                flag = false;
                return postfix;
            }
        }
        return postfix;
    }
}

