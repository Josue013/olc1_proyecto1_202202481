package Util;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

public class Propiedades {

    public static Map<String, Object> simplificarExpresion(String[] tokens) {
        Deque<String> operandos = new ArrayDeque<>();
        List<String> leyesAplicadas = new ArrayList<>();

        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            if (esOperador(token)) {
                if (operandos.size() < 2 && !token.equals("^")) {
                    throw new IllegalArgumentException("Operando insuficiente para la operación: " + token);
                }
                String operando1 = operandos.pop();
                String operando2 = token.equals("^") ? null : operandos.pop();

                switch (token) {
                    case "^":
                        operando1 = aplicarLeyDeMorgan(operando1, leyesAplicadas);
                        operando1 = aplicarLeyDobleComplemento(operando1, leyesAplicadas);
                        operandos.push(operando1);
                        break;
                    case "U":
                        String resultadoUnion = aplicarPropiedadesUnion(operando1, operando2, leyesAplicadas);
                        operandos.push(resultadoUnion);
                        break;
                    case "&":
                        String resultadoInterseccion = aplicarPropiedadesInterseccion(operando1, operando2, leyesAplicadas);
                        operandos.push(resultadoInterseccion);
                        break;
                    case "-":
                        operandos.push(operando1 + " - " + operando2);
                        break;
                }
            } else {
                operandos.push(token);
            }
        }

        if (operandos.size() != 1) {
            throw new IllegalArgumentException("Expresión inválida. Verifique la expresión de entrada.");
        }

        String expresionSimplificada = operandos.pop();
        leyesAplicadas.add("Expresión simplificada: " + expresionSimplificada);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("leyes", leyesAplicadas);
        resultado.put("conjunto_simplificado", expresionSimplificada);

        return resultado;
    }

    private static boolean esOperador(String token) {
        return token.equals("U") || token.equals("&") || token.equals("-") || token.equals("^");
    }

    private static String aplicarLeyDobleComplemento(String operando, List<String> leyesAplicadas) {
        if (operando.startsWith("^^")) {
            leyesAplicadas.add("Ley del doble complemento: ^^" + operando.substring(2) + " = " + operando.substring(2));
            return operando.substring(2);
        } else if (operando.startsWith("^")) {
            return "^" + operando.substring(1);
        }
        return operando;
    }

    private static String aplicarPropiedadesUnion(String operando1, String operando2, List<String> leyesAplicadas) {
        if (operando1.equals(operando2)) {
            leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " U " + operando1 + " = " + operando1);
            return operando1;
        } else if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
            leyesAplicadas.add("Ley de complementos: " + operando1 + " U " + operando2 + " = universo");
            return "universo";
        } else if (operando1.contains(" & ") && operando1.startsWith(operando2)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " U " + operando2 + " = " + operando2);
            return operando2;
        } else if (operando2.contains(" & ") && operando2.startsWith(operando1)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " U " + operando2 + " = " + operando1);
            return operando1;
        }
        return operando1 + " U " + operando2;
    }

    private static String aplicarPropiedadesInterseccion(String operando1, String operando2, List<String> leyesAplicadas) {
        if (operando1.equals(operando2)) {
            leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " & " + operando1 + " = " + operando1);
            return operando1;
        } else if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
            leyesAplicadas.add("Ley de complementos: " + operando1 + " & " + operando2 + " = vacío");
            return "vacío";
        } else if (operando1.contains(" U ") && operando1.startsWith(operando2)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando2);
            return operando2;
        } else if (operando2.contains(" U ") && operando2.startsWith(operando1)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando1);
            return operando1;
        }
        return operando1 + " & " + operando2;
    }

    private static String aplicarLeyDeMorgan(String operando, List<String> leyesAplicadas) {
        if (operando.contains("U")) {
            String[] partes = operando.split("U");
            if (partes.length == 2) {
                leyesAplicadas.add("Ley de De Morgan: ^(" + partes[0].trim() + " U " + partes[1].trim() + ") = ^" + partes[0].trim() + " & ^" + partes[1].trim());
                return "^" + partes[0].trim() + " & ^" + partes[1].trim();
            }
        } else if (operando.contains("&")) {
            String[] partes = operando.split("&");
            if (partes.length == 2) {
                leyesAplicadas.add("Ley de De Morgan: ^(" + partes[0].trim() + " & " + partes[1].trim() + ") = ^" + partes[0].trim() + " U ^" + partes[1].trim());
                return "^" + partes[0].trim() + " U ^" + partes[1].trim();
            }
        }
        return operando;
    }

    public static void generarJsonSalida(Map<String, Map<String, Object>> resultados) {
        JSONObject json = new JSONObject(resultados);
        try (FileWriter file = new FileWriter("src/JsonSalida/resultados.json")) {
            file.write(json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simplificarOperaciones() {
        Map<String, Map<String, Object>> resultados = new HashMap<>();
        for (Operacion operacion : Utilidades.listaOperaciones) {
            String[] tokens = (String[]) operacion.getValor();
            Map<String, Object> resultado = simplificarExpresion(tokens);
            resultados.put(operacion.getNombre(), resultado);
        }
        generarJsonSalida(resultados);
    }
}
