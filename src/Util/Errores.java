package Util;

public class Errores {
  private String lexema;
  private String descripcion;
  private int linea;
  private int columna;

  public Errores(String lexema, String descripcion, int linea, int columna) {
    this.lexema = lexema;
    this.descripcion = descripcion;
    this.linea = linea;
    this.columna = columna;
  }

  public String getLexema() {
    return lexema;
  }

  public void setLexema(String lexema) {
    this.lexema = lexema;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public int getLinea() {
    return linea;
  }

  public void setLinea(int linea) {
    this.linea = linea;
  }

  public int getColumna() {
    return columna;
  }

  public void setColumna(int columna) {
    this.columna = columna;
  }

}
