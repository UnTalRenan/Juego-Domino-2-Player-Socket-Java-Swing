package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import java.io.*;
import java.net.*;

public class SubprocesoEmisor extends Thread {

    private final Socket socketCliente;
    Socket socket2;
    private final String mensajeAEnviar;

    String tipo = "";
    String archivo = "";
    //Principal prin;

    public SubprocesoEmisor(Socket socket, String nombreUsuario, String para, String mensaje) {
        super("SubprocesoEmisor: " + socket);
        socketCliente = socket;
        socket2 = socket;
//		this.prin=prin;
        mensajeAEnviar = nombreUsuario + Constantes.SEPARADOR_MENSAJE + para + Constantes.SEPARADOR_MENSAJE + mensaje;
        this.tipo = tipo;
        archivo = mensaje;
        //	System.out.println ("ENVIANDO "+mensaje);

    }

//	File file;
//	 long  tam=0;
    public void run() {
        try {

            try {
                PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream());
                escritor.println(mensajeAEnviar);
                //Obligar a enviar los datos inmediatamente y vaciar el buffer
                escritor.flush();
            } catch (Exception ex) {

//					prin.log.con=false;
            }

        } catch (Exception excepcionES) {
            excepcionES.printStackTrace();
        }
    }
}
