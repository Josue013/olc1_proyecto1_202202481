/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Analizadores;

import INTERFAZ.Interfaz;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import Util.Operacion;
import Util.Utilidades;
import Util.Propiedades;


/**
 *
 * @author PC
 */
public class ConjAnalyzer {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            // Instanciar la clase Scanner de JFlex
            System.out.println("Generando analizador léxico...");
            String ruta = "src/Analizadores/";
            String[] opJFlex = {ruta + "Lexico.jflex", "-d", ruta};
            jflex.Main.generate(opJFlex);

            // Instanciar la clase Parser de CUP
            System.out.println("Generando analizador sintáctico...");
            String[] opCup = {"-destdir", ruta, "-parser", "Parser", ruta + "Sintactico.cup"};
            java_cup.Main.main(opCup);

            // Mensaje de éxito
            System.out.println("Generación exitosa de analizador léxico y sintáctico.");
            
            //Inicializar la interfaz
            Interfaz interfaz = new Interfaz();
            interfaz.setLocationRelativeTo(null); // Centrar ventana
            interfaz.setVisible(true); // mostrar interfaz
            System.out.println("Mostrando Interfaz...");
           
            /* 
            // codigo de prueba
            System.out.println("====================================");
            System.out.println("Ejemplo de prueba");
            String entrada = "{\r\n" + //
                                "  #Definición de conjuntos\r\n" + //
                                "  CONJ : conjuntoA -> 1,2,3,a,b ;\r\n" + //
                                "  CONJ : conjuntoB -> a~z;\r\n" + //
                                "  CONJ : conjuntoC -> 0~9;\r\n" + //
                                "  #Definición de operaciones\r\n" + //
                                "  OPERA : operacion1 -> & {conjuntoA} {conjuntoB};\r\n" + //
                                "  OPERA : operacion2 -> & U {conjuntoB} {conjuntoC} {conjuntoA};\r\n" + //
                                "  #Evaluamos conjuntos de datos\r\n" + //
                                "  EVALUAR ( {a, b, c} , operacion1 );\r\n" + //
                                "  EVALUAR ( {1, b} , operacion1 );\r\n" + //
                                "}\r\n" + //
                                ""; 
            
            Lexico lex = new Lexico(new BufferedReader(new StringReader(entrada))); // Instanciar el analizador léxico
            Parser parser = new Parser(lex); // Instanciar el parser

            try {
                parser.parse(); // Realizar el análisis sintáctico 
                System.out.println("Analisis completo exitoso: ");
                //System.out.println(entrada);
            } catch (Exception e) {
                System.out.println("Error fatal en compilación de entrada.");
                System.out.println("Causa: " + e.getCause());
                System.out.println("Mensaje: " + e.getMessage());
            }
            System.out.println("====================================");
            */

        } catch (Exception e) {
            // Manejo de excepciones
            e.printStackTrace(); // Imprimir el stack trace para depuración
        }

        
        

    }
    


}
