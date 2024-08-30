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

  public class Utilidades {

    // Listas para almacenar los tokens y errores
    public static List<Tokens> tokens = new ArrayList<>();
    public static ArrayList<Errores> listaErrores = new ArrayList<>();

    // imprimir listas de conjuntos

    // Metodo para agregar tokens
    public static void agregarToken(String lexema, String tipoToken, int linea, int columna) {
      Tokens token = new Tokens(lexema, tipoToken, linea, columna);
      tokens.add(token);
    }

    // Metodo para borrar tokens
    public static void borrarTokens() {
      tokens.clear();
    }

    // Metodo para agregar errores
    public static void agregarError(String lexema, String descripcion, int linea, int columna) {
      Errores error = new Errores(lexema, descripcion, linea, columna);
      listaErrores.add(error);
      System.out.println(lexema);
    }

    // Metodo para borrar errores
    public static void borrarErrores() {
      listaErrores.clear();
    }

    public class GeneradorHTML {

      // Metodo para generar el reporte de tokens en HTML
      public static void generarTokensHTML() {
        try {
          limpiarArchivo("Tokens.html");
          BufferedWriter writer = new BufferedWriter(new FileWriter("Tokens.html"));

          writer.write("<html><head><title>Lista de Tokens</title></head><body style=\"background-color: #fc92a6;\">");
          writer.write("<h1 style=\"color: #4d000f;text-align:center; font-family: 'Impact';\">Lista de Tokens</h1>");

          writer.write("<table border=\"1\" style=\"border-collapse: collapse;margin: 0 auto;\">");
          writer.write(
              "<tr><th style=\"padding: 10px;background-color: #fa1d11;\">Lexema</th><th style=\"padding: 10px;background-color: #fa1d11;\">Token</th><th style=\"padding: 10px;background-color: #fa1d11;\">Línea</th><th style=\"padding: 10px;background-color: #fa1d11;\">Columna</th></tr>");

          for (Util.Tokens token : Utilidades.tokens) {
            writer.write("<tr>");
            writer.write("<td style=\"padding: 10px; background-color: #fc655d;\">" + token.getLexema() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fc655d;\">" + token.getTipoToken() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fc655d;\">" + token.getLinea() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fc655d;\">" + token.getColumna() + "</td>");
            writer.write("</tr>");
          }

          writer.write("</table>");

          writer.write("</body></html>");
          writer.close();
          abrirArchivo("Tokens.html");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Metodo para generar el reporte de errores en HTML
      public static void generarErroresHTML() {
        try {
          limpiarArchivo("Errores.html");
          BufferedWriter writer = new BufferedWriter(new FileWriter("Errores.html"));

          writer.write("<html><head><title>Lista de Errores</title></head><body style=\"background-color: #fc92a6;\">");
          writer.write("<h1 style=\"color: #4d000f;text-align:center; font-family: 'Impact';\">Lista de Errores</h1>");

          writer.write("<table border=\"1\" style=\"border-collapse: collapse;margin: 0 auto;\">");
          writer.write(
              "<tr><th style=\"padding: 10px;background-color: #ffcccc;\">Lexema</th><th style=\"padding: 10px;background-color: #ffcccc;\">Descripción</th><th style=\"padding: 10px;background-color: #ffcccc;\">Línea</th><th style=\"padding: 10px;background-color: #ffcccc;\">Columna</th></tr>");

          for (Util.Errores error : Utilidades.listaErrores) {
            writer.write("<tr>");
            writer.write("<td style=\"padding: 10px; background-color: #fa5ccb; \">" + error.getLexema() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fa5ccb; \">" + error.getDescripcion() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fa5ccb; \">" + error.getLinea() + "</td>");
            writer.write("<td style=\"padding: 10px; background-color: #fa5ccb; \">" + error.getColumna() + "</td>");
            writer.write("</tr>");
          }

          writer.write("</table>");

          writer.write("</body></html>");
          writer.close();
          abrirArchivo("Errores.html");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Metodo para abrir un archivo
      private static void abrirArchivo(String nombreArchivo) {
        try {
          Desktop.getDesktop().browse(new File(nombreArchivo).toURI());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Metodo para limpiar un archivo
      private static void limpiarArchivo(String nombreArchivo) {
        try {
          BufferedWriter clearWriter = new BufferedWriter(new FileWriter(nombreArchivo));
          clearWriter.write("");
          clearWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    public static ArrayList<Conjunto> listaConjuntos = new ArrayList<>(); // Lista de conjuntos
    public static ArrayList<Operacion> listaOperaciones = new ArrayList<>(); // Lista de operaciones
    public static ArrayList<Evaluacion> listaEvaluaciones = new ArrayList<>(); // Lista de evaluaciones

    // Metodo para agregar conjuntos
    public static void agregarConjunto(String nombre, String elementos) {
      // Verificar si el conjunto ya existe
      for (Conjunto conjunto : listaConjuntos) {
        if (conjunto.getNombre().equals(nombre)) {
          // Si existe, actualizar su valor
          conjunto.setValor(elementos);
          return;
        }
      }
      // Si no existe, agregar el nuevo conjunto
      Conjunto nuevoConjunto = new Conjunto(nombre, elementos);
      listaConjuntos.add(nuevoConjunto);
    }

    // Metodo para eliminar conjuntos
    public static void eliminarConjunto(String nombre) {
      for (Conjunto conjunto : listaConjuntos) {
        if (conjunto.getNombre().equals(nombre)) {
          listaConjuntos.remove(conjunto);
          break;
        }
      }
    }

    // Metodo para eliminar todos los conjuntos
    public static void eliminarTodosLosConjuntos() {
      listaConjuntos.clear();
    }

    // Metodo para obtener un conjunto por su nombre
    public static Conjunto obtenerConjuntoPorNombre(String nombre) {
      for (Conjunto conjunto : listaConjuntos) {
        if (conjunto.getNombre().equals(nombre)) {
          return conjunto;
        }
      }
      return null;
    }

    // Metodo para agregar operaciones
    public static void agregarOperacion(String nombre, String op) {
      // Verificar si la operación ya existe
      for (Operacion operacion : listaOperaciones) {
        if (operacion.getNombre().equals(nombre)) {
          // Si existe, actualizar su valor
          operacion.setValor(op);
          return;
        }
      }
      // Si no existe, agregar la nueva operación
      Operacion nuevaOperacion = new Operacion(nombre, op);
      listaOperaciones.add(nuevaOperacion);
    }

    // Metodo para eliminar operaciones
    public static void eliminarOperacion(String nombre) {
      for (Operacion operacion : listaOperaciones) {
        if (operacion.getNombre().equals(nombre)) {
          listaOperaciones.remove(operacion);
          break;
        }
      }
    }

    // Metodo para eliminar todas las operaciones
    public static void eliminarTodasLasOperaciones() {
      listaOperaciones.clear();
    }

    // Metodo para obtener una operacion por su nombre
    public static Operacion obtenerOperacion(String nombre) {
      for (Operacion operacion : listaOperaciones) {
        if (operacion.getNombre().equals(nombre)) {
          return operacion;
        }
      }
      return null;
    }

    // Metodo para agregar evaluaciones
    public static void agregarEvaluacion(String nombre, Object ev) {
      Evaluacion evaluacion = new Evaluacion(nombre, ev);
      listaEvaluaciones.add(evaluacion);
    }

    // Metodo para eliminar evaluaciones
    public static void eliminarEvaluacion(String nombre) {
      for (Evaluacion evaluacion : listaEvaluaciones) {
        if (evaluacion.getNombre().equals(nombre)) {
          listaEvaluaciones.remove(evaluacion);
          break;
        }
      }
    }

    // Metodo para obtener una evaluacion por su nombre
    public static Evaluacion obtenerEvaluacion(String nombre) {
      for (Evaluacion evaluacion : listaEvaluaciones) {
        if (evaluacion.getNombre().equals(nombre)) {
          return evaluacion;
        }
      }
      return null;
    }

    // Metodo para eliminar todas las evaluaciones
    public static void eliminarTodasLasEvaluaciones() {
      listaEvaluaciones.clear();
    }

    // Metodo para imprimir listas de conjuntos
    public static void imprimirConjuntos() {
      System.out.println("====================================");
      System.out.println("Lista de conjuntos");
      for (Conjunto conjunto : listaConjuntos) {
        splitConjunto(conjunto);
        System.out.println(conjunto.getNombre() + ": " + Arrays.toString((String[]) conjunto.getValor()));
      }
      System.out.println("====================================");
    }

    // Metodo para imprimir listas de operaciones
    public static void imprimirOperaciones() {
      System.out.println("====================================");
      System.out.println("Lista de operaciones");
      for (Operacion operacion : listaOperaciones) {
        splitOperacion(operacion);
        System.out.println(operacion.getNombre() + ": " + Arrays.toString((String[]) operacion.getValor()));
      }
      System.out.println("====================================");
    }

    // Metodo para imprimir listas de evaluaciones
    public static void imprimirEvaluaciones() {
      System.out.println("====================================");
      System.out.println("Lista de evaluaciones");
      for (Evaluacion evaluacion : listaEvaluaciones) {
        splitEvaluacion(evaluacion);
        System.out.println(evaluacion.getNombre() + ": " + Arrays.toString((String[]) evaluacion.getValor()));
      }
      System.out.println("====================================");
    }

    // Método auxiliar para eliminar espacios en blanco
    private static String eliminarEspacios(String str) {
      StringBuilder sb = new StringBuilder();
      for (char c : str.toCharArray()) {
          if (!Character.isWhitespace(c)) {
              sb.append(c);
          }
      }
      return sb.toString();
  }

  // Método auxiliar para eliminar corchetes y llaves
  private static String eliminarCorchetesYLlaves(String str) {
      StringBuilder sb = new StringBuilder();
      for (char c : str.toCharArray()) {
          if (c != '{' && c != '}' && c != '[' && c != ']') {
              sb.append(c);
          }
      }
      return sb.toString();
  }

  // Metodo para splitear el objeto de la lista de conjuntos
  public static void splitConjunto(Conjunto conjunto) {
      String valor = (String) conjunto.getValor(); // Obtener el valor del conjunto

      if (valor.contains("~")) { // si contiene un ~
          // Eliminar espacios alrededor del ~
          valor = eliminarEspacios(valor);
          valor = valor.replace("~", "~");

          // Es un intervalo
          String[] interval = valor.split("~"); // Separar el intervalo
          char start = interval[0].charAt(0); // Obtener el primer caracter
          char end = interval[1].charAt(0); // Obtener el segundo caracter
          ArrayList<String> elements = new ArrayList<>(); // Lista de elementos

          for (char c = start; c <= end; c++) { // Recorrer el intervalo
              elements.add(String.valueOf(c)); // Agregar el caracter a la lista
          }

          conjunto.setValor(elements.toArray(new String[0])); // Convertir la lista a un arreglo

      } else {
          // Es una lista de elementos
          valor = eliminarEspacios(valor);
          String[] elementos = valor.split(",");
          for (int i = 0; i < elementos.length; i++) {
              elementos[i] = elementos[i].trim(); // Eliminar espacios alrededor de cada elemento
          }
          conjunto.setValor(elementos); // Separar los elementos por coma
      }
  }

  // Metodo para splitear el objeto de la lista de operaciones
  public static void splitOperacion(Operacion operacion) {
      String valor = (String) operacion.getValor(); // Obtener el valor de la operación
      valor = eliminarCorchetesYLlaves(valor); // Remover corchetes y llaves
      valor = eliminarEspacios(valor); // Remover espacios innecesarios
      String[] elementos = valor.split(",");
      for (int i = 0; i < elementos.length; i++) {
          elementos[i] = elementos[i].trim(); // Eliminar espacios alrededor de cada elemento
      }
      operacion.setValor(elementos); // Separar los elementos por coma
  }

  // Metodo para splitear el objeto de la lista de evaluaciones
  public static void splitEvaluacion(Evaluacion evaluacion) {
      String valor = (String) evaluacion.getValor(); // Obtener el valor de la evaluación
      valor = eliminarCorchetesYLlaves(valor); // Remover corchetes y llaves
      valor = eliminarEspacios(valor); // Remover espacios innecesarios
      String[] elementos = valor.split(",");
      for (int i = 0; i < elementos.length; i++) {
          elementos[i] = elementos[i].trim(); // Eliminar espacios alrededor de cada elemento
      }
      evaluacion.setValor(elementos); // Separar los elementos por coma
  }

    

  }
