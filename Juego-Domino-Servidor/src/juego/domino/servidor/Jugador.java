package juego.domino.servidor;

/**
 *
 * @author Renan
 */
import javax.swing.*;

public class Jugador extends JLabel {

    public String nombre;
    public String avatar;
    String ip;
    public Lista fichas = new Lista();

    public Jugador(String nombre, String avatar, String ip) {

        this.nombre = nombre;
        this.avatar = avatar;
        this.ip = ip;

    }

    public String getInfo() {

        return nombre + ";" + avatar;
    }

}
