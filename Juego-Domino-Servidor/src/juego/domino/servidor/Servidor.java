/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package juego.domino.servidor;

/**
 *
 * @author Renan
 */
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.*;

public class Servidor extends JFrame implements EscuchaMensajes, ActionListener {
    //186.2.146.200 
    //172.20.10.2

    javax.swing.Timer reiniciaJuego = new javax.swing.Timer(1000, this);
    int tiempo = 5;

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (reiniciaJuego == evt.getSource()) {
            tiempo--;
            if (tiempo <= 0) {
                reiniciaJuego.stop();
                if (tab != null) {
                    tab.dispose();
                }
                tiempo = 5;
                cerrarTodosSocket();
            }
        }
    }

    Object ob[] = {};
    int indice = 0;

    public Servidor() {
        super("SERVIDOR CHAT");

        setSize(800, 600);
        setLocationRelativeTo(null);
        //	setVisible(true);
        iniciarServidor();
    }

    public void iniciarServidor() {
        try {
            //Se crea un objeto ServerSocket para recivir caonexiones
            ServerSocket socketServidor = new ServerSocket(Constantes.PUERTO_SERVIDOR, 100);
            System.out.println("Servidor escuchando en el puerto " + Constantes.PUERTO_SERVIDOR + "...");
            //Itera infinitamente esperando conexiones
            while (true) {
                //Crear el socket para el cliente
                Socket socketCliente = socketServidor.accept();
                //Inicia un hilo SubprocesoReceptor para cada cliente
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                String mensaje = entrada.readLine();
                //String mensaje=""+socketCliente.getInetAddress();
                System.out.println("LLEGO IP:" + mensaje);
                String tmp[] = mensaje.split(Constantes.SEPARA_DATOS);
                cerrarSocket(tmp[0]);
                ob = agrandar_Arreglo(ob);
                ob[indice] = new Object(socketCliente, "" + mensaje);
                indice++;

                new SubprocesoReceptor(this, mensaje, this, socketCliente).start();

                System.out.println("Conexion recibida de: " + ob[indice - 1].ip);
            }
        } catch (IOException excepcionES) {
            JOptionPane.showMessageDialog(null, excepcionES.getMessage() + "\nPUERTO:" + Constantes.PUERTO_SERVIDOR);
            excepcionES.printStackTrace();
        }
    }

    Tablero tab;

    @Override
    public void mensajeRecibido(String de, String para, String mensaje) {
        System.out.println("EScucho: de:" + de + " para:" + para + " mensaje:" + mensaje);

        if (para.equalsIgnoreCase(Constantes.NUEVO_JUGADOR)) {
            System.out.println("SE CONECTO NUEVO JUGADOR:  " + de);
            new SubprocesoEmisor(getSocket(de), para, de, Constantes.JUGADOR_ESPERA).start();

            System.out.println("JUGADORES CONECTADOS:  " + getDisponibles());
            if (getDisponibles() >= 2) {

                System.out.println("INICIANDO TABLERO");
                String datos[] = getInfo().split(";");
                tab = new Tablero(datos[0], datos[1], datos[2], datos[3], ob[0].ip, ob[1].ip);
                tab.setAdminMensajes(this);
                //new SubprocesoEmisor(getSocket(de),para,de,Constantes.JUEGO_LISTO).start();
                int turno = tab.turno;
                String fichas = tab.jugador[0].fichas.getFichas() + Constantes.SEPARA_DATOS + tab.jugador[1].fichas.getFichas();

                String sobrante = tab.fichasInicio.getFichas();

                String info = tab.jugador[0].getInfo() + Constantes.SEPARA_DATOS + tab.jugador[1].getInfo() + Constantes.SEPARA_DATOS + turno;

                new SubprocesoEmisor(getSocket(tab.jugador[0].ip), Constantes.RECIBE_INFO, de, info + Constantes.SEPARA_DATOS + "0").start();
                new SubprocesoEmisor(getSocket(tab.jugador[1].ip), Constantes.RECIBE_INFO, de, info + Constantes.SEPARA_DATOS + "1").start();
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {
                }
                new SubprocesoEmisor(getSocket(tab.jugador[0].ip), Constantes.RECIBE_FICHAS, de, fichas).start();
                new SubprocesoEmisor(getSocket(tab.jugador[1].ip), Constantes.RECIBE_FICHAS, de, fichas).start();
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {
                }
                new SubprocesoEmisor(getSocket(tab.jugador[0].ip), Constantes.RECIBE_SOBRANTES, de, sobrante).start();
                new SubprocesoEmisor(getSocket(tab.jugador[1].ip), Constantes.RECIBE_SOBRANTES, de, sobrante).start();
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {
                }
                new SubprocesoEmisor(getSocket(tab.jugador[0].ip), para, de, Constantes.JUEGO_LISTO).start();
                new SubprocesoEmisor(getSocket(tab.jugador[1].ip), para, de, Constantes.JUEGO_LISTO).start();

                System.out.println("JUEGO INICIADO");
                //JOptionPane.showMessageDialog(null,"")
            }

        } else if (para.equalsIgnoreCase(Constantes.EJECUTAR_JUGADA)) {

            String datos[] = mensaje.split(Constantes.SEPARA_DATOS);

            if (datos[0].equalsIgnoreCase("null")) {
                datos[0] = "-1";
            }

            System.out.println("posicionar " + datos[0] + " " + datos[1] + " " + datos[2]);
            int actual = Integer.parseInt(de);
            int padreId = Integer.parseInt(datos[0]);
            String lado1 = datos[1];
            String lado2 = datos[2];
            tab.posicionarRival(padreId, lado1, lado2);

            int actualTem = 0;
            if (actual == 0) {
                actualTem = 1;
            } else {
                actualTem = 0;
            }

            new SubprocesoEmisor(getSocket(tab.jugador[actualTem].ip), "" + actual, Constantes.EJECUTAR_JUGADA, mensaje).start();

        } else if (para.equalsIgnoreCase(Constantes.AGARRA_SOBRANTES)) {

            int actual = Integer.parseInt(de);
            tab.agarra_SobranteRival(mensaje);

            int actualTem = 0;
            if (actual == 0) {
                actualTem = 1;
            } else {
                actualTem = 0;
            }

            new SubprocesoEmisor(getSocket(tab.jugador[actualTem].ip), "" + actual, Constantes.AGARRA_SOBRANTES, mensaje).start();

        } else if (para.equalsIgnoreCase(Constantes.SALTO_TURNO)) {
            tab.turno();
            int actual = Integer.parseInt(de);

            int actualTem = 0;
            if (actual == 0) {
                actualTem = 1;
            } else {
                actualTem = 0;
            }
            new SubprocesoEmisor(getSocket(tab.jugador[actualTem].ip), "" + actual, Constantes.SALTO_TURNO, "VACIO").start();

        } else if (para.equalsIgnoreCase(Constantes.FIN_JUEGO)) {
            reiniciaJuego.start();
        }

    }

    public String getInfo() {

        String datos = "";
        for (int i = 0; i < ob.length; i++) {

            if (!ob[i].socketCliente.isClosed()) {
                datos = datos + ob[i].user + ";" + ob[i].avatar + ";";
            }

        }

        return datos;
    }

    public int getDisponibles() {

        int conta = 0;
        for (int i = 0; i < ob.length; i++) {

            if (!ob[i].socketCliente.isClosed()) {
                conta++;
            }

        }

        return conta;
    }

    public Socket getSocket(String ip) {

        for (int i = 0; i < ob.length; i++) {
            System.out.println("Buscando " + ob[i].user);
            if (ob[i].ip.equals("" + ip)) {
                System.out.println("Encontro " + ob[i].user);
                return ob[i].socketCliente;
            }

        }
        return null;

    }

    public void cerrarSocket(String ip) {

        for (int i = 0; i < ob.length; i++) {
            System.out.println("Buscando " + ob[i].ip);
            if (ob[i].ip.equals("" + ip)) {
                try {
                    ob[i].ip = "0.0.0.0";
                    ob[i].user = "ninguno";
                    ob[i].socketCliente.close();
                    System.out.println("Cerrado " + ip);
                } catch (Exception ex) {
                }

            }

        }

    }

    public void cerrarTodosSocket() {
        Object te[] = {};
        ob = te;
        indice = 0;
        //	for(int i=0;i<ob.length;i++)
        //	{
        //			try {
        //					ob[i].ip="0.0.0.0";
        //					ob[i].user="ninguno";
        //	 				
        //				}
        //				catch (Exception ex) {
        //				}
        //	}

    }

    public String capturarHora() {
        Date fechaActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return "[" + formato.format(fechaActual) + "]";
    }

    public String capturaFecha() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(d);
        return fecha;
    }

    public static Object[] agrandar_Arreglo(Object arr[]) {
        Object tmp[] = new Object[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }

        return tmp;
    }

    public static String[] agrandar_Arreglo(String arr[]) {
        String tmp[] = new String[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }

        return tmp;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        new Servidor();
    }

}
