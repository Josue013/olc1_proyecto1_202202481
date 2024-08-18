package Util;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Operacion {
  String nombre;
  Object valor;

  public Operacion(String nombre, String valor) {
      this.nombre = nombre;
      this.valor = valor;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Object getValor() {
    return valor;
  }

  public void setValor(Object valor) {
    this.valor = valor;
  }

  @Override
  public String toString() {
      return nombre + ", " + valor;
  }
  
}
