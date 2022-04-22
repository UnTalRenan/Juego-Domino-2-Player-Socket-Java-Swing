package juego.domino.cliente;

/**
 *
 * @author Renan
 */
public interface AdministradorMensajes {

    public void conectar(EscuchaMensajes escucha, String ipclient);

    public void desconectar(EscuchaMensajes escucha);

    public void desconectar();

    public void enviarMensaje(String de, String para, String mensaje);
}
