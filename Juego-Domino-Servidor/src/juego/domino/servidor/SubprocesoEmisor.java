package juego.domino.servidor;

/**
 *
 * @author Renan
 */
import java.io.*;
import java.net.*;

public class SubprocesoEmisor extends Thread {

    private final Socket socketCliente;
    private final String mensajeAEnviar;
   
    String nombreU;
    String para;
    String soloMsj;
    
    //String nom="";
    //Date d = new Date();
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //String fecha = sdf.format(d);

    public SubprocesoEmisor(Socket socket, String nombreUsuario, String para, String mensaje) {
        super("SubprocesoEmisor: " + socket);

        socketCliente = socket;
        this.nombreU = nombreUsuario;
        this.para = para;
        this.soloMsj = mensaje;
        mensajeAEnviar = nombreUsuario + Constantes.SEPARADOR_MENSAJE + para + Constantes.SEPARADOR_MENSAJE + mensaje;
        
        /*
        if (socket == null) {

            //
            return;
        }*/

    }

    public void run() {
        try {
            try {

                //Enviar los paquetes desde el cliente al servidor/o lo contrario
                PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream());
                escritor.println(mensajeAEnviar);
                //Obligar a enviar los datos inmediatamente y vaciar el buffer
                escritor.flush();
            } catch (Exception ex) {
                System.out.println("No Enviado");
            }

            //	System.out.println ("Enviado\n//////////////////////////////\n");
        } catch (Exception excepcionES) {
            System.out.println("No Enviado");
            excepcionES.printStackTrace();
        }
    }

    public String extension(String full) {
        int dot = full.lastIndexOf('.');
        return full.substring(dot + 1);
    }
}
