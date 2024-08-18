/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package INTERFAZ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class Archivo {

  // Metodo para abrir un archivo
  public static String AbrirArchivo(File archivo) {
    String aux, texto = ""; //String para guardar el contenido del archivo
    if (archivo != null) { //Si el archivo abierto es diferente de nulo
      try {
        FileReader archivos = new FileReader(archivo); //FileReader para leer el archivo
        try (BufferedReader lee = new BufferedReader(archivos)) { //BufferedReader para leer el archivo
          while ((aux = lee.readLine()) != null) { //Mientras haya lineas que leer
            texto += aux + "\n"; //Se guarda la linea en el texto
          }
        }
        return texto; //Se retorna el texto
      } catch (IOException ex) {  
        System.err.println("Error al abrir el archivo" + ex.getMessage()); //Mensaje de error
      }
    }
    return null;
  }

  // Metodo para guardar un archivo
  public static void GuardarArchivo(String contenido,String path){ 
    File archivo = new File(path); //Se crea un nuevo archivo
    try {
            FileWriter archivos = new FileWriter(archivo); //FileWriter para escribir en el archivo
            try (BufferedWriter w = new BufferedWriter(archivos)) { //BufferedWriter para escribir en el archivo
                w.write(contenido); //Se escribe el contenido en el archivo
            }
        } catch (IOException ex) {
            System.err.println("Error en Archivo: " + ex.getMessage()); //Mensaje de error
        }
    }
    
    

}
