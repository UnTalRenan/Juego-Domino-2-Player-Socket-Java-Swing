package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;
import java.lang.reflect.Method;

public class EsperaTurno extends JDialog {

    Tablero padre;

    public EsperaTurno(Boolean modal) {
        super(new JFrame(), "ESPERA", modal);

        JLabel tex = new JLabel(new ImageIcon(Constantes.path + "img/espereturno.png"));
        //tex.setFont(new Font("Verdana",Font.BOLD,40));
        tex.setBounds(0, 50, 400, 100);
        add(tex);

        setUndecorated(true);
        setVisible(true);
        setBounds(0, 0, 400, 100);
        setLocationRelativeTo(null);

        try {
            Class clazz = Class.forName("com.sun.awt.AWTUtilities");
            Method method = clazz.getMethod("setWindowOpaque", java.awt.Window.class, Boolean.TYPE);
            method.invoke(clazz, this, false);
        } catch (Exception e) {
        }

    }

}
