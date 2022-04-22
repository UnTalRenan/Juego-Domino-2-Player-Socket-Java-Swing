package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import java.io.*;
import java.net.*;

public class SubprocesoReceptor extends Thread {

    private BufferedReader entrada;
    private EscuchaMensajes escuchaMensajes;
    private boolean seguirEscuchando = true;
    Socket socket;
    //Constructor de SubprocesoReceptor

    public SubprocesoReceptor(EscuchaMensajes escucha, Socket socketCliente) {
        super("SubprocesoReceptor: " + socketCliente);
        escuchaMensajes = escucha;
        socket = socketCliente;
        try {
            //Establecer tiempo fuera del socket
            socket.setSoTimeout(15000);
            //Establecer flujos de entrada
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }

    public void run() {
        String mensaje;
        //Iterar infinitamente escuchando mensajes
        while (seguirEscuchando) {
            try {
                //	if(socket.isClosed())
                //	return;
                //System.out.println("Esperando msj");
                //Leer mensaje del cliente
                mensaje = entrada.readLine();
                //System.out.println (" msj"+mensaje);
            } catch (InterruptedIOException excepcionInterrupcion) {
                //Reestablece el ciclo para seguir escuchando
                continue;
            } catch (IOException excepcionES) {
                //	excepcionES.printStackTrace();
                break;
            }
            //Si el mensaje es valido enviarselo al servidor

            if (mensaje != null && mensaje.length() > 0 && !mensaje.isEmpty()) {
                System.out.println("RECIBIDO SubProcesoR: " + mensaje + "\n////////////////////////////////\n");
                //	System.out.println ("partiendo:"+mensaje);
                //	System.out.println ("tan:"+mensaje.length());
                String tm[] = mensaje.split(Constantes.SEPARADOR_MENSAJE);

                String de = tm[0];
                String para = tm[1];
                String msg = tm[2];

                if (msg.equalsIgnoreCase(Constantes.CADENA_DESCONEXION)) {
                    dejarDeEscuchar();
                } else {
                    escuchaMensajes.mensajeRecibido(de, para, msg);
                }

            }

        }

        try {
            //Cerrar el flujo de entrada
            entrada.close();
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }
    //Cancela el bucle While, para que no escuche m�s mensajes

    public void dejarDeEscuchar() {
        seguirEscuchando = false;
    }


}
