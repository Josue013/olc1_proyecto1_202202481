package Util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class PanelGrafica extends JPanel {
    private Operacion operacion;
    private Set<String> conjuntos;

    public PanelGrafica(Operacion operacion) {
        this.operacion = operacion;
        this.conjuntos = new HashSet<>();
        String[] tokens = (String[]) operacion.getValor();
        for (String token : tokens) {
            if (!esOperador(token)) {
                conjuntos.add(token);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Map<String, Area> areasConjuntos = new HashMap<>();
        int index = 0;
        for (String conjunto : conjuntos) {
            areasConjuntos.put(conjunto, crearAreaConjunto(index++));
        }

        Deque<Area> pila = new ArrayDeque<>();
        String[] tokens = (String[]) operacion.getValor();
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            if (esOperador(token)) {
                if (token.equals("^")) {
                    if (pila.isEmpty()) {
                        System.err.println("Error: Pila vacía al intentar aplicar operador " + token);
                        return;
                    }
                    Area operando1 = pila.pop();
                    Area resultado = aplicarOperacion(token, operando1, null);
                    pila.push(resultado);
                } else {
                    if (pila.size() < 2) {
                        System.err.println("Error: Pila vacía al intentar aplicar operador " + token);
                        return;
                    }
                    Area operando1 = pila.pop();
                    Area operando2 = pila.pop();
                    Area resultado = aplicarOperacion(token, operando1, operando2);
                    pila.push(resultado);
                }
            } else {
                Area areaConjunto = areasConjuntos.get(token);
                if (areaConjunto != null) {
                    pila.push(areaConjunto);
                } else {
                    System.err.println("Error: Conjunto no encontrado " + token);
                    return;
                }
            }
        }

        if (!pila.isEmpty()) {
            Area resultadoFinal = pila.pop();
            g2d.setColor(Color.magenta);
            g2d.fill(resultadoFinal);
        }

        drawOvals(g, conjuntos);
    }

    private Area crearAreaConjunto(int index) {
        switch (index) {
            case 0:
                return new Area(new Ellipse2D.Double(100, 100, 100, 100));
            case 1:
                return new Area(new Ellipse2D.Double(150, 100, 100, 100));
            case 2:
                return new Area(new Ellipse2D.Double(120, 150, 100, 100));
            case 3:
                return new Area(new Ellipse2D.Double(170, 150, 100, 100));
            default:
                return new Area();
        }
    }

    private Area aplicarOperacion(String token, Area operando1, Area operando2) {
        Area resultado = new Area(operando1);
        switch (token) {
            case "U":
                resultado.add(operando2);
                break;
            case "&":
                resultado.intersect(operando2);
                break;
            case "-":
                resultado.subtract(operando2);
                break;
            case "^":
                Area universo = new Area(new Ellipse2D.Double(50, 50, 300, 300));
                universo.subtract(operando1);
                resultado = universo;
                break;
        }
        return resultado;
    }

    private boolean esOperador(String token) {
        return token.equals("U") || token.equals("&") || token.equals("-") || token.equals("^");
    }

    private void drawOvals(Graphics g, Set<String> conjuntos) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.black);
        int index = 0;
        for (String conjunto : conjuntos) {
            switch (index) {
                case 0:
                    g2d.drawOval(100, 100, 100, 100);
                    g2d.drawString(conjunto, 80, 90); // Etiqueta bien colocada
                    break;
                case 1:
                    g2d.drawOval(150, 100, 100, 100);
                    g2d.drawString(conjunto, 260, 90); // Etiqueta bien colocada
                    break;
                case 2:
                    g2d.drawOval(120, 150, 100, 100);
                    g2d.drawString(conjunto, 100, 270); // Etiqueta bien colocada
                    break;
                case 3:
                    g2d.drawOval(170, 150, 100, 100);
                    g2d.drawString(conjunto, 280, 270); //Etiqueta bien colocada
                    break;
            }
            index++;
        }
    }

    public static void graficarOperaciones() {
        for (Operacion operacion : Utilidades.listaOperaciones) {
            JFrame frame = new JFrame("Operación: " + operacion.getNombre());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 400);
            PanelGrafica panel = new PanelGrafica(operacion);
            frame.add(panel);
            frame.setVisible(true);
        }
    }
}