package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Nodo {

    String lado1, lado2;
    int i;
    Nodo sig;
    Nodo ant;
    // ImageIcon img;
    JButton tb;
    String imagen = "";
    String texto = "";
    int x, ancho;
    int y, alto;

    String izquierda, derecha, arriba, abajo;
    int id;
    boolean disponible = false;
    boolean muestraContenido = true;

    public Nodo(int id, ActionListener action) {

        this.id = id;

        tb = new JButton();
        tb.addActionListener(action);
        tb.setBackground(Color.WHITE);
        sig = null;

    }

    public Nodo(int id, String dato, int x, int y, int ancho, int alto, String iz, String der, String arr, String aba) {

        this.id = id;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.izquierda = iz;
        this.derecha = der;
        this.arriba = arr;
        this.abajo = aba;

        tb = new JButton();
        tb.setBackground(Color.WHITE);

        String tem[] = dato.split("-");
        this.lado1 = iz;//tem[0];
        this.lado2 = der;//tem[1];
        //	System.out.println (lado1+"-"+lado2);
        imagen = Constantes.path + "img/" + tem[0] + "-" + tem[1] + ".png";
        this.texto = lado1 + "-" + lado2;
        tb.setIcon(new ImageIcon(imagen));

        sig = null;

    }

    public void setFicha(String dato) {
        String tem[] = dato.split("-");
        this.lado1 = tem[0];
        this.lado2 = tem[1];
        //	System.out.println (lado1+"-"+lado2);
        imagen = Constantes.path + "img/" + lado1 + "-" + lado2 + ".png";
        this.texto = lado1 + "-" + lado2;

        disponible = true;

    }

    public Nodo(String info, Nodo otro) {
        info = info;
        sig = otro;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String toString() {
        return lado1 + "-" + lado2;
    }

}
