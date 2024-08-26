package Util;

import java.util.*;

public class Propiedades {

    public static List<String> simplificarExpresion(String[] tokens) {
        Stack<String> operadores = new Stack<>();
        Deque<String> operandos = new ArrayDeque<>();
        List<String> leyesAplicadas = new ArrayList<>();

        for (String token : tokens) {
            if (esOperador(token)) {
                operadores.push(token);
            } else {
                operandos.addFirst(token);
            }
        }

        while (!operadores.isEmpty()) {
            String operador = operadores.pop();
            String operando1 = operandos.poll();
            String operando2 = operandos.poll();

            switch (operador) {
                case "^":
                    // Aplicar ley del doble complemento
                    if (operando1.startsWith("^")) {
                        operandos.addFirst(operando1.substring(1));
                        leyesAplicadas.add("Ley del doble complemento: ^^" + operando1.substring(1) + " = " + operando1.substring(1));
                    } else {
                        operandos.addFirst("^" + operando1);
                    }
                    break;
                case "U":
                    // Propiedades idempotentes
                    if (operando1.equals(operando2)) {
                        operandos.addFirst(operando1);
                        leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " U " + operando1 + " = " + operando1);
                    } 
                    // Leyes de De Morgan
                    else if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
                        operandos.addFirst("U");
                        leyesAplicadas.add("Leyes de DeMorgan: " + operando1 + " U " + operando2 + " = U");
                    } 
                    // Propiedades conmutativas
                    else {
                        operandos.addFirst(operando1 + " U " + operando2);
                    }
                    break;
                case "&":
                    // Propiedades idempotentes
                    if (operando1.equals(operando2)) {
                        operandos.addFirst(operando1);
                        leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " & " + operando1 + " = " + operando1);
                    } 
                    // Leyes de De Morgan
                    else if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
                        operandos.addFirst("&");
                        leyesAplicadas.add("Leyes de DeMorgan: " + operando1 + " & " + operando2 + " = &");
                    } 
                    // Propiedades conmutativas
                    else {
                        operandos.addFirst(operando1 + " & " + operando2);
                    }
                    break;
                case "-":
                    operandos.addFirst(operando1 + " - " + operando2);
                    break;
            }
        }

        String expresionSimplificada = operandos.poll();
        leyesAplicadas.add("Expresión simplificada: " + expresionSimplificada);

        return leyesAplicadas;
    }

    private static boolean esOperador(String token) {
        return token.equals("U") || token.equals("&") || token.equals("-") || token.equals("^");
    }

    public static void main(String[] args) {
        String[] tokens = {"^", "^", "cA", "U", "cA", "U", "cB"};
        List<String> resultado = simplificarExpresion(tokens);
        for (String ley : resultado) {
            System.out.println(ley);
        }
    }
}
