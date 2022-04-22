package juego.domino.servidor;

/**
 *
 * @author Renan
 */

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class SubprocesoReceptor extends Thread {

    private BufferedReader entrada;
    FileInputStream entrada2 = null;//ENTRADA
    private EscuchaMensajes escuchaMensajes;
    private boolean seguirEscuchando = true;
    private Socket socketC;
    Servidor server;
    String user;

    public SubprocesoReceptor(Servidor server, String user, EscuchaMensajes escucha, Socket socketCliente) {
        super("SubprocesoReceptor: " + socketCliente);
        escuchaMensajes = escucha;
        this.server = server;
        this.user = user;
        socketC = socketCliente;
        try {
            //Establecer tiempo fuera del socket
            socketC.setSoTimeout(15000);
            //Establecer flujos de entrada

            entrada = new BufferedReader(new InputStreamReader(socketC.getInputStream()));

        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }
    int con = 0;
    boolean b = true;

    public void run() {
        String mensaje;
        //Iterar infinitamente escuchando mensajes

        while (seguirEscuchando) {
            try {
                if (socketC.isClosed()) {
                    return;
                }
                //Leer mensaje del cliente
                mensaje = entrada.readLine();

            } catch (InterruptedIOException excepcionInterrupcion) {
                //Reestablece el ciclo para seguir escuchando
                continue;
            } catch (IOException excepcionES) {

                //server.desconectar(user);
                System.out.println("Usuario " + user + ": se desconecto de forma inesperada");
                server.reiniciaJuego.start();
                //	excepcionES.printStackTrace();

                break;

            }

            if (mensaje != null) {

                String tmp[] = mensaje.split(Constantes.SEPARADOR_MENSAJE);
                System.out.println("RECEPTOR:" + mensaje + "\n//////////////LENGTH " + tmp.length + "////////////");

                if (tmp.length == 3) {
                    try {
                        escuchaMensajes.mensajeRecibido(tmp[0], tmp[1], tmp[2]);
                    } catch (Exception ex) {
                    }

                } else if (mensaje.equalsIgnoreCase(Constantes.SEPARADOR_MENSAJE + Constantes.CADENA_DESCONEXION)) {
                    //escuchaMensajes.mensajeRecibido(tmp[0],tmp[1],tmp[2]);
                    dejarDeEscuchar();
                }

            }
        }
        try {
            //Cerrar el flujo de entrada
            entrada.close();
            //entra.close();

        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }
    //Cancela el bucle While, para que no escuche ms mensajes

    public void dejarDeEscuchar() {
        seguirEscuchando = false;
    }

}
