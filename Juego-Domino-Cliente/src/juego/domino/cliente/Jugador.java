package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;

public class Jugador extends JLabel {


    String nombre;
    String clave;
    Lista fichas = new Lista();

    public Jugador(String nombre, String clave) {

        this.nombre = nombre;
        this.clave = clave;

    }

}
