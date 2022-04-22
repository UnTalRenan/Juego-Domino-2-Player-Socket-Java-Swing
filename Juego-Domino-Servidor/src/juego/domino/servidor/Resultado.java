package juego.domino.servidor;

/**
 *
 * @author Renan
 */

import javax.swing.*;
import java.awt.*;

import java.awt.Font;
import java.lang.reflect.Method;
import java.awt.event.*;

public class Resultado extends JFrame implements ActionListener {

    Timer t = new Timer(1000, this);
    int tiempo = 5;

    public void actionPerformed(ActionEvent e) {
        tiempo--;
        if (tiempo <= 0) {
            t.stop();
            dispose();
        }
    }
    Tablero padre;

    public Resultado(String img, String nombre) {
        setLayout(null);

        JLabel tex = new JLabel(new ImageIcon(Constantes.path + "img/" + img));
        //tex.setFont(new Font("Verdana",Font.BOLD,40));
        tex.setBounds(0, 10, 400, 100);
        add(tex);

        tex = new JLabel(nombre);
        tex.setFont(new Font("Showcard Gothic", Font.BOLD, 40));
        tex.setForeground(Color.RED);
        tex.setBounds(0, 110, 400, 100);
        add(tex);

        setUndecorated(true);
        setVisible(true);
        setBounds(0, 0, 420, 250);
        setLocationRelativeTo(null);
        t.start();
        //Transparencia
        try {
            Class clazz = Class.forName("com.sun.awt.AWTUtilities");
            Method method = clazz.getMethod("setWindowOpaque", java.awt.Window.class, Boolean.TYPE);
            method.invoke(clazz, this, false);
        } catch (Exception e) {
        }

    }

}
