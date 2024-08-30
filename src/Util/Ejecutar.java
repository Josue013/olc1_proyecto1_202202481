package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import Util.Operacion;
import Util.Conjunto;
import Util.Evaluacion;
import INTERFAZ.Interfaz;
import Util.Tokens;
import Util.Utilidades;
import Util.Propiedades;
import Util.PanelGrafica;
import INTERFAZ.Interfaz;
public class Ejecutar {

    // Map para almacenar los resultados de las operaciones
    private static Map<String, Set<String>> resultadosOperaciones = new HashMap<>();

    public static void Ejecutar() {
        Util.Utilidades.imprimirConjuntos(); // Los imprime ya spliteados
        Util.Utilidades.imprimirOperaciones(); // Los imprime ya spliteados
        Util.Utilidades.imprimirEvaluaciones(); // Los imprime ya spliteados
        RealizarOperaciones();
        //Util.PanelGrafica.graficarOperaciones(jPanel1);

        System.out.println("====================================");
        System.out.println("EJECUTAR");
        System.out.println("====================================");

    }

    public static String SalidaConsola() {

        StringBuilder tabla = new StringBuilder();

        for (Evaluacion eval : Util.Utilidades.listaEvaluaciones) {
            tabla.append("==============================\n");
            tabla.append("Evaluar: ").append(eval.getNombre()).append("\n");
            tabla.append("==============================\n");
            Set<String> resultadoOperacion = resultadosOperaciones.get(eval.getNombre());
            if (resultadoOperacion != null) {
                String[] evs = (String[]) eval.getValor();
                for (String ev : evs) {
                    if (resultadoOperacion.contains(ev)) {
                        tabla.append(ev).append(" -> exitoso\n");
                    } else {
                        tabla.append(ev).append(" -> fallo\n");
                    }
                }
            } else {
                tabla.append("No se encontró la operacion o esta tenia conjuntos que no existen\n");
            }


        }
        return tabla.toString();
    }

    // Realizar operaciones de conjuntos
    public static void RealizarOperaciones() {
        for (int i = 0; i < Utilidades.listaOperaciones.size(); i++) {
            Operacion operacion = Utilidades.listaOperaciones.get(i);
            Stack<Set<String>> pila = new Stack<>();
            boolean conjuntosExisten = true;

            String[] tokens = (String[]) operacion.getValor();
            System.out.println("Procesando operación: " + operacion.getNombre());
            System.out.println("Tokens: " + Arrays.toString(tokens));
            
            for (int j = tokens.length - 1; j >= 0; j--) {
                String token = tokens[j];
                System.out.println("Procesando token: " + token);
                
                if (esOperador(token)) {
                    if (token.equals("^")) {
                        if (pila.isEmpty()) {
                            System.out.println("Error: Pila vacía al intentar aplicar operador " + token);
                            conjuntosExisten = false;
                            break;
                        }
                        Set<String> conjunto = pila.pop();
                        Set<String> universo = new HashSet<>();
                        for (int k = 33; k <= 126; k++) {
                            universo.add(Character.toString((char) k));
                        }
                        Set<String> complemento = new HashSet<>(universo);
                        complemento.removeAll(conjunto);
                        pila.push(complemento);
                        System.out.println("Resultado del complemento: " + setToString(complemento));
                    } else {
                        if (pila.size() < 2) {
                            System.out.println("Error: Pila con menos de dos elementos al intentar realizar operación binaria");
                            conjuntosExisten = false;
                            break;
                        }
                        Set<String> conjunto1 = pila.pop();
                        Set<String> conjunto2 = pila.pop();
                        Set<String> resultado = new HashSet<>();
                        switch (token) {
                            case "U":
                                resultado.addAll(conjunto1);
                                resultado.addAll(conjunto2);
                                System.out.println("Resultado de la unión: " + setToString(resultado));
                                break;
                            case "&":
                                System.out.println("Conjunto1: " + setToString(conjunto1));
                                System.out.println("Conjunto2: " + setToString(conjunto2));
                                for (String elemento : conjunto1) {
                                    if (conjunto2.contains(elemento)) {
                                        resultado.add(elemento);
                                    }
                                }
                                System.out.println("Resultado de la intersección: " + setToString(resultado));
                                break;
                            case "-":
                                resultado.addAll(conjunto1);
                                resultado.removeAll(conjunto2);
                                System.out.println("Resultado de la diferencia: " + setToString(resultado));
                                break;
                        }
                        pila.push(resultado);
                    }
                } else {
                    Conjunto conjunto = Utilidades.obtenerConjuntoPorNombre(token.trim());
                    if (conjunto != null) {
                        Set<String> elementos = new HashSet<>(Arrays.asList((String[]) conjunto.getValor()));
                        pila.push(elementos);
                        System.out.println("Conjunto apilado: " + setToString(elementos));
                    } else {
                        System.out.println("Conjunto no encontrado: " + token);
                        conjuntosExisten = false;
                        break;
                    }
                }
            }

            if (!conjuntosExisten) {
                System.out.println("Operación eliminada debido a conjuntos faltantes: " + operacion.getNombre());
                Utilidades.eliminarOperacion(operacion.getNombre());
                i--; // Ajustar el índice después de eliminar la operación
                continue;
            }

            if (!pila.isEmpty()) {
                Set<String> resultadoFinal = pila.pop();
                resultadosOperaciones.put(operacion.getNombre(), resultadoFinal);
                System.out.println("Resultado final de la operación (" + operacion.getNombre() + ") : " + setToString(resultadoFinal));
            }
        }
    }

    private static boolean esOperador(String token) {
        return token.equals("U") || token.equals("&") || token.equals("-") || token.equals("^");
    }

    // Método auxiliar para convertir un conjunto a una cadena
    private static String setToString(Set<String> set) {
        return set.toString();
    }

    

}