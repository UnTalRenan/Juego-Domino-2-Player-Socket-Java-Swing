package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class NodoG extends JButton {

    String L_actual = "";
    String L_actual2 = "";
    int id;
    int idpadre;
    NodoG hij_izk, he_der, padre;

    int x;
    int y;

    String text = "";
    //JButton ficha;  

    double rotacion = 0.0;
    ImageIcon icono = null;
    boolean disponible = true;

    public NodoG() {

        //	ficha=new JButton();
        //	ficha.setContentAreaFilled(false);
        this.setContentAreaFilled(false);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform tx = AffineTransform.getRotateInstance(rotacion, icono.getIconWidth() / 2, icono.getIconHeight() / 2);
        g2d.drawImage(icono.getImage(), tx, this);
    }

    public double getRotacion() {
        return this.rotacion;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

}
