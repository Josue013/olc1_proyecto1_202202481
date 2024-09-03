package Util;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.Desktop;
import org.json.JSONObject;

public class Propiedades {

    public static Map<String, Object> simplificarExpresion(String[] tokens) {
        Deque<String> operandos = new ArrayDeque<>();
        List<String> leyesAplicadas = new ArrayList<>();
        boolean seAplicoLey = false;

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
                        seAplicoLey = true;
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

        if (!seAplicoLey) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensaje", "No se puede simplificar la operación");
            return resultado;
        }

        String expresionSimplificada = operandos.pop();
        leyesAplicadas.add("Expresión simplificada: " + expresionSimplificada);

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("leyes", leyesAplicadas);
        resultado.put("conjunto_simplificado", expresionSimplificada);

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

            // Encontrar el primer carácter no alfanumérico en 'despues'
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

        // Propiedades conmutativas (solo si es necesario)
        /* 
        String resultadoConmutativo = operando2 + " U " + operando1;
        if (puedeSimplificarMas(resultadoConmutativo)) {
            leyesAplicadas.add("Propiedades conmutativas: " + operando1 + " U " + operando2 + " = " + resultadoConmutativo);
            return resultadoConmutativo;
        }
        */
        // Propiedades asociativas
        if (operando1.contains(" U ") && operando2.contains(" U ")) {
            leyesAplicadas.add("Propiedades asociativas: " + operando1 + " U (" + operando2 + ") = (" + operando1 + ") U " + operando2);
        }

        // Propiedades distributivas
        if (operando1.contains(" & ") && operando2.contains(" & ")) {
            leyesAplicadas.add("Propiedades distributivas: " + operando1 + " U (" + operando2 + ") = (" + operando1 + ") & (" + operando2 + ")");
        }

        return operando1 + " U " + operando2;
    }

    private static String aplicarPropiedadesInterseccion(String operando1, String operando2, List<String> leyesAplicadas) {
        // Propiedades idempotentes
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

        // Propiedades conmutativas (solo si es necesario)
        /* 
        String resultadoConmutativo = operando2 + " & " + operando1;
        if (puedeSimplificarMas(resultadoConmutativo)) {
            leyesAplicadas.add("Propiedades conmutativas: " + operando1 + " & " + operando2 + " = " + resultadoConmutativo);
            return resultadoConmutativo;
        }
        */
        // Propiedades asociativas
        if (operando1.contains(" & ") && operando2.contains(" & ")) {
            leyesAplicadas.add("Propiedades asociativas: " + operando1 + " & (" + operando2 + ") = (" + operando1 + ") & " + operando2);
        }

        // Propiedades distributivas
        if (operando1.contains(" U ") && operando2.contains(" U ")) {
            leyesAplicadas.add("Propiedades distributivas: " + operando1 + " & (" + operando2 + ") = (" + operando1 + ") U (" + operando2 + ")");
        }

        return operando1 + " & " + operando2;
    }
    /* 
    private static boolean puedeSimplificarMas(String expresion) {
        // Aplicar la propiedad conmutativa a la expresión
        String[] partes = expresion.split(" ");
        if (partes.length == 3 && (partes[1].equals("U") || partes[1].equals("&"))) {
            String operando1 = partes[0];
            String operador = partes[1];
            String operando2 = partes[2];
            String resultadoConmutativo = operando2 + " " + operador + " " + operando1;
    
            // Verificar si la nueva expresión simplificada permite aplicar alguna otra propiedad de simplificación
            List<String> leyesAplicadas = new ArrayList<>();
            if (operador.equals("U")) {
                aplicarPropiedadesUnion(operando2, operando1, leyesAplicadas);
            } else if (operador.equals("&")) {
                aplicarPropiedadesInterseccion(operando2, operando1, leyesAplicadas);
            }
    
            // Retornar true si se puede simplificar más, de lo contrario, retornar false
            return !leyesAplicadas.isEmpty();
        }
    
        return false;
    }
    */
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
                // Abrir el archivo con el Bloc de notas
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