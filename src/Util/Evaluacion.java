
package Util;


public class Evaluacion {
  String nombre;
  Object valor;

  public Evaluacion(String nombre, Object valor) {
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
