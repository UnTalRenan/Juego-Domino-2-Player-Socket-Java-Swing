package juego.domino.servidor;

/**
 *
 * @author Renan
 */

import java.net.*;

public class Object {

    Socket socketCliente;
    String user;
    String ip;
    String avatar;

    public Object(Socket socket, String i) {
        System.out.println("i:" + i);
        String tmp[] = i.split(Constantes.SEPARA_DATOS);

        socketCliente = socket;
        ip = tmp[0];
        user = tmp[1];
        this.avatar = tmp[2];

    }

}
