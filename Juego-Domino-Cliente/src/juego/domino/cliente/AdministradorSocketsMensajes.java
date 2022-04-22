package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import java.net.*;
import java.io.*;

public class AdministradorSocketsMensajes implements AdministradorMensajes {
    //Declarar objetos y variables de la clase

    private Socket socketCliente;
    private final String direccionServidor;
    private SubprocesoReceptor subprocesoReceptor;
    private boolean conectado = false;
    //constructor de AdministradorSocketsMensajes

    public AdministradorSocketsMensajes(String direccion) {
        direccionServidor = direccion;
    }
    //Sobrecargar el metodo conectar

    @Override
    public void conectar(EscuchaMensajes escucha, String ipclient) {
        //	if(conectado)
        //		return;
        try {
            System.out.println("Conectando...");
            //Crear el socket del cliente
            socketCliente = new Socket(InetAddress.getByName(direccionServidor), Constantes.PUERTO_SERVIDOR);

            PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream());
            escritor.println("" + ipclient);
            //Obligar a enviar los datos inmediatamente y vaciar el buffer
            escritor.flush();

            //Crear un hilo para recivir paquetes del servidor
            subprocesoReceptor = new SubprocesoReceptor(escucha, socketCliente);//SubprocesoReceptorPaquetes(escucha);
            subprocesoReceptor.start();
            //Establecer la bandera booleana como 'true'
            conectado = true;
            System.out.println("Conectado");
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }
    //Sobrecargar el metodo conectar

    public void desconectar(EscuchaMensajes escucha) {
        if (!conectado) {
            return;
        }
        try {
            //crear el hilo que envia los paquetes al servidor
            Thread desconectarSubproceso = new SubprocesoEmisor(socketCliente, "", "", Constantes.CADENA_DESCONEXION);
            desconectarSubproceso.start();
            desconectarSubproceso.join(10000);
            subprocesoReceptor.dejarDeEscuchar();
            //Cerrar el socket del cliente
            socketCliente.close();
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        } catch (InterruptedException excepcionInterrupcion) {
            excepcionInterrupcion.printStackTrace();
        }
        //Establecer estado de conexion en false
        conectado = false;
        System.exit(0);
    }

    public void desconectar() {
        if (!conectado) {
            return;
        }
        try {

            subprocesoReceptor.dejarDeEscuchar();
            //Cerrar el socket del cliente
            socketCliente.close();
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }

        //Establecer la bandera de conexi�n en false
        conectado = false;
    }

    public void enviarMensaje(String de, String para, String mensaje) {
        if (!conectado) {
            return;
        }
        //enviar paquetes al Servidor
        new SubprocesoEmisor(socketCliente, de, para, mensaje).start();
    }

}
