package Util;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import org.json.JSONObject;

public class Propiedades {

    public static Map<String, Object> simplificarExpresion(String[] tokens) {
        Deque<String> operandos = new ArrayDeque<>();
        List<String> leyesAplicadas = new ArrayList<>();
        boolean seAplicoLey = false;
        String expresionOriginal = String.join(" ", tokens);
    
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
                        if (!operando1.equals(tokens[i + 1])) {
                            seAplicoLey = true;
                        }
                        operandos.push(operando1);
                        break;
                    case "U":
                        String resultadoUnion = aplicarPropiedadesUnion(operando1, operando2, leyesAplicadas);
                        if (!resultadoUnion.equals(operando1 + " U " + operando2)) {
                            seAplicoLey = true;
                        }
                        operandos.push(resultadoUnion);
                        break;
                    case "&":
                        String resultadoInterseccion = aplicarPropiedadesInterseccion(operando1, operando2, leyesAplicadas);
                        if (!resultadoInterseccion.equals(operando1 + " & " + operando2)) {
                            seAplicoLey = true;
                        }
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
        System.out.println("Expresión simplificada antes de la conversión: " + expresionSimplificada);
    
        if (leyesAplicadas.isEmpty()) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensaje", "No se puede simplificar la operación");
            return resultado;
        }
    
        leyesAplicadas.add("Expresión simplificada: " + expresionSimplificada);
    
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("leyes", leyesAplicadas);
        resultado.put("conjunto_simplificado", convertirANotacionPolaca(expresionSimplificada));
    
        return resultado;
    }

    private static boolean esOperador(String token) {
        return token.equals("U") || token.equals("&") || token.equals("-") || token.equals("^");
    }

    private static String aplicarLeyDobleComplemento(String operando, List<String> leyesAplicadas) {
        while (operando.contains("^^")) {
            int index = operando.indexOf("^^");
            String antes = operando.substring(0, index);
            String despues = operando.substring(index + 2);

            int endIndex = despues.length();
            for (int i = 0; i < despues.length(); i++) {
                char c = despues.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    endIndex = i;
                    break;
                }
            }

            String conjunto = despues.substring(0, endIndex);
            leyesAplicadas.add("Ley del doble complemento: ^^" + conjunto + " = " + conjunto);
            operando = antes + conjunto + despues.substring(conjunto.length());
        }
        return operando;
    }

    private static String aplicarPropiedadesUnion(String operando1, String operando2, List<String> leyesAplicadas) {
        // Propiedades idempotentes
        if (operando1.equals(operando2)) {
            leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " U " + operando1 + " = " + operando1);
            return operando1;
        }
        // Ley de complementos
        if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
            leyesAplicadas.add("Ley de complementos: " + operando1 + " U " + operando2 + " = universo");
            return "universo";
        }
    
        // Propiedades de absorción (aplicar conmutativas si es necesario)
        if ((operando1.contains(" & ") && operando1.startsWith(operando2)) || (operando2.contains(" & ") && operando2.endsWith(operando1))) {
            if (operando1.compareTo(operando2) > 0) {
                String temp = operando1;
                operando1 = operando2;
                operando2 = temp;
                leyesAplicadas.add("Propiedades conmutativas: " + operando2 + " U " + operando1 + " = " + operando1 + " U " + operando2);
            }
            // caso 1 (A & B) U A = A
            if (operando1.contains(" & ") && operando1.startsWith(operando2)) {
                leyesAplicadas.add("Propiedades de absorción: (" + operando1 + ") U (" + operando2 + ") = " + operando2);
                return operando2;
            }
            // caso 4 B U (A & B) = B
            if (operando2.contains(" & ") && operando2.endsWith(operando1)) {
                leyesAplicadas.add("Propiedades de absorción: (" + operando1 + ") U (" + operando2 + ") = " + operando1);
                return operando1;
            }
        }

        // caso 2 A U (A & B) = A
        if (operando2.contains(" & ") && operando2.startsWith(operando1)) {
            leyesAplicadas.add("Propiedades de absorción: (" + operando1 + ") U (" + operando2 + ") = " + operando1);
            return operando1;
        }

        // caso 3 (A & B) U B = B
        if (operando1.contains(" & ") && operando1.endsWith(operando2)) {
            leyesAplicadas.add("Propiedades de absorción: (" + operando1 + ") U (" + operando2 + ") = " + operando2);
            return operando2;
        }
        
    
        // Propiedades distributivas
        if (operando1.contains(" & ") && operando2.contains(" & ")) {
            leyesAplicadas.add("Propiedades distributivas: " + operando1 + " U (" + operando2 + ") = (" + operando1 + ") & (" + operando2 + ")");
            return operando1 + " & " + operando2;
        }
        // Propiedades asociativas
        if (operando1.contains(" U ") && operando2.contains(" U ")) {
            leyesAplicadas.add("Propiedades asociativas: " + operando1 + " U (" + operando2 + ") = (" + operando1 + ") U " + operando2);
            return operando1 + " U " + operando2;
        }
        return operando1 + " U " + operando2;
    }
    
    private static String aplicarPropiedadesInterseccion(String operando1, String operando2, List<String> leyesAplicadas) {
        // Propiedades idempotentes
        if (operando1.equals(operando2)) {
            leyesAplicadas.add("Propiedades idempotentes: " + operando1 + " & " + operando1 + " = " + operando1);
            return operando1;
        }
        // Ley de complementos
        if (operando1.equals("^" + operando2) || operando2.equals("^" + operando1)) {
            leyesAplicadas.add("Ley de complementos: " + operando1 + " & " + operando2 + " = vacío");
            return "vacío";
        }
    
        // Propiedades de absorción (aplicar conmutativas si es necesario)
        if ((operando1.contains(" U ") && operando1.startsWith(operando2)) || (operando2.contains(" U ") && operando2.endsWith(operando1))) {
            if (operando1.compareTo(operando2) > 0) {
                String temp = operando1;
                operando1 = operando2;
                operando2 = temp;
                leyesAplicadas.add("Propiedades conmutativas: " + operando2 + " & " + operando1 + " = " + operando1 + " & " + operando2);
            }
            // caso 1 (A U B) & A = A
            if (operando1.contains(" U ") && operando1.startsWith(operando2)) {
                leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando2);
                return operando2;
            }
            // caso 4 B & (A U B) = B
            if (operando2.contains(" U ") && operando2.endsWith(operando1)) {
                leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando1);
                return operando1;
            }
        }

        // caso 2 A & (A U B) = A
        if (operando2.contains(" U ") && operando2.startsWith(operando1)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando1);
            return operando1;
        }

        // caso 3 (A U B) & B = B
        if (operando1.contains(" U ") && operando1.endsWith(operando2)) {
            leyesAplicadas.add("Propiedades de absorción: " + operando1 + " & " + operando2 + " = " + operando2);
            return operando2;
        }
        

        // Propiedades distributivas
        if (operando1.contains(" U ") && operando2.contains(" U ")) {
            leyesAplicadas.add("Propiedades distributivas: " + operando1 + " & (" + operando2 + ") = (" + operando1 + ") U (" + operando2 + ")");
            return operando1 + " U " + operando2;
        }
        // Propiedades asociativas
        if (operando1.contains(" & ") && operando2.contains(" & ")) {
            leyesAplicadas.add("Propiedades asociativas: " + operando1 + " & (" + operando2 + ") = (" + operando1 + ") & " + operando2);
            return operando1 + " & " + operando2;
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
        return "^" + operando;
    }

    private static String convertirANotacionPolaca(String expresion) {
        Deque<String> stack = new ArrayDeque<>();
        StringBuilder resultado = new StringBuilder();
        String[] tokens = expresion.split(" ");
        
        for (String token : tokens) {
            if (esOperador(token)) {
                while (!stack.isEmpty() && precedencia(stack.peek()) >= precedencia(token)) {
                    resultado.append(stack.pop()).append(" ");
                }
                stack.push(token);
            } else {
                resultado.append(token).append(" ");
            }
        }
        
        while (!stack.isEmpty()) {
            resultado.insert(0, stack.pop() + " ");
        }
        
        return resultado.toString().trim();
    }
    
    private static String invertirOrden(String expresion) {
        String[] tokens = expresion.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = tokens.length - 1; i >= 0; i--) {
            resultado.append(tokens[i]).append(" ");
        }
        return resultado.toString().trim();
    }

    private static int precedencia(String operador) {
        switch (operador) {
            case "^":
                return 3;
            case "U":
                return 2;
            case "&":
                return 2;
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    public static void generarJsonSalida(Map<String, Map<String, Object>> resultados) {
        JSONObject json = new JSONObject(resultados);
        try (FileWriter file = new FileWriter("resultados.json")) {
            file.write(json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simplificarOperaciones() {
        Map<String, Map<String, Object>> resultados = new LinkedHashMap<>();
        List<Operacion> listaOperacionesOrdenada = new ArrayList<>(Utilidades.listaOperaciones);
        listaOperacionesOrdenada.sort(Comparator.comparing(Operacion::getNombre));

        for (Operacion operacion : listaOperacionesOrdenada) {
            String[] tokens = (String[]) operacion.getValor();
            Map<String, Object> resultado = simplificarExpresion(tokens);
            resultados.put(operacion.getNombre(), resultado);
        }
        generarJsonSalida(resultados);
        abrirArchivoJson("resultados.json");
    }

    public static void abrirArchivoJson(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                ProcessBuilder pb = new ProcessBuilder("notepad.exe", archivo.getAbsolutePath());
                pb.start();
            } else {
                System.out.println("El archivo no existe: " + rutaArchivo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}