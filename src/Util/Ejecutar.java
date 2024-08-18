package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import Util.Operacion;
import Util.Conjunto;
import Util.Evaluacion;
import INTERFAZ.Interfaz;
import Util.Tokens;
import Util.Utilidades;

public class Ejecutar {

    public static void Ejecutar(){
      Util.Utilidades.imprimirConjuntos();      // Los imprime ya spliteados
      Util.Utilidades.imprimirOperaciones();    // Los imprime ya spliteados
      Util.Utilidades.imprimirEvaluaciones();   // Los imprime ya spliteados
      
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
        
      }

      return tabla.toString();
  }

}
