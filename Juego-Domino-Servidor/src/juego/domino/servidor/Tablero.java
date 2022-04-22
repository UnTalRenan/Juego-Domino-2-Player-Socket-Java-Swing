package juego.domino.servidor;

/**
 *
 * @author Renan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Tablero extends JFrame implements ActionListener {

    Archivo a = new Archivo();

    JLabel fon = new JLabel(new ImageIcon(Constantes.path + "img/x.jpg"));
    public Lista fichasInicio = new Lista();

    JPanel p;

    JLabel panel1, panel2, panel3, panel4, panel5, principal;

    Lista sobrante = new Lista();
    String selec1 = "";
    String selec2 = "";
    String ladoCoincidio = "";
    Timer t = new Timer(50, this);

    public void actionPerformed(ActionEvent e) {

    }

    public void agarra_SobranteRival(String texto) {
        jugador[turno].fichas.ins_final(new Nodo(0, this));
        modificarEstado(texto);
        jugador[turno].fichas.ultimo().setFicha(texto);

        reImprime();
        selec1 = "";
        selec2 = "";

        verificarExistencia();
    }

    public void modificarEstado(String texto) {

        Nodo tmpSobra = fichasInicio.p;

        while (tmpSobra != null) {
            if (tmpSobra.texto.equalsIgnoreCase(texto)) {

                tmpSobra.disponible = false;
            }

            tmpSobra = tmpSobra.sig;
        }
    }

    // AdministradorMensajes administradorMensajes;
    Servidor server;
    public Jugador jugador[] = {};
    JButton luz[] = new JButton[4];

    public int turno = 0;
    JLabel etiNombre1 = new JLabel();
    JLabel foto1 = new JLabel();

    JLabel etiNombre2 = new JLabel();
    JLabel foto2 = new JLabel();

    JScrollPane scrollPrinc;

    public Tablero(String n1, String ava1, String n2, String ava2, String ip1, String ip2) {
        super("DOMINO- SERVIDOR");
        setLayout(null);

        //  int cantidad=Integer.parseInt(JOptionPane.showInputDialog("Cantidad de Jugadores"));
        jugador = new Jugador[2];
        jugador[0] = new Jugador(n1, ava1, ip1);
        jugador[1] = new Jugador(n2, ava2, ip2);
        for (int i = 0; i < 7; i++) {
            jugador[0].fichas.ins_final(new Nodo(i, this));
            jugador[1].fichas.ins_final(new Nodo(i, this));
        }

        for (int i = 0; i < 28; i++) {
            fichasInicio.ins_final(new Nodo(i, this));
        }
        panel1 = new JLabel();
        panel1.setLayout(null);
        panel1.setBorder(LineBorder.createGrayLineBorder());
        panel1.setBounds(220, 0, 360, 65);
        add(panel1);
        luz[0] = new JButton();
        luz[0].setBounds(panel1.getX() - 20, panel1.getY() + 45, 20, 20);
        luz[0].setBackground(Color.RED);
        add(luz[0]);

        panel2 = new JLabel();
        panel2.setLayout(null);
        panel2.setBorder(LineBorder.createGrayLineBorder());
        panel2.setBounds(580, 0, 360, 65);
        add(panel2);
        luz[1] = new JButton();
        luz[1].setBounds(panel2.getX() + panel2.getWidth(), panel2.getY() + 45, 20, 20);
        luz[1].setBackground(Color.RED);
        add(luz[1]);

        ///////SOBRANTES    
        panel5 = new JLabel();
        panel5.setLayout(null);
        panel5.setBorder(LineBorder.createGrayLineBorder());
        panel5.setBounds(5, 300, 160, 240);
        add(panel5);

        /////////////////////////////
        principal = new JLabel();
        scrollPrinc = new JScrollPane(principal, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        principal.setLayout(null);
        principal.setBorder(LineBorder.createGrayLineBorder());
        //principal.setBounds(165,72,830,408);
        //scrollPrinc.setOpaque(true);
        scrollPrinc.setBounds(165, 120, 830, 408);
        principal.setPreferredSize(new Dimension(1600, 408));
        principal.setIcon(new ImageIcon(Constantes.path + "img/tablero.png"));

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                }

                scrollPrinc.getHorizontalScrollBar().setValue(430);

            }
        });

        etiNombre1.setText("" + n1);
        etiNombre1.setBounds(185, 82, 300, 30);
        etiNombre1.setFont(new Font("Verdana", Font.BOLD, 30));

        add(etiNombre1);

        foto1.setBorder(LineBorder.createGrayLineBorder());
        foto1.setBounds(162, 2, 60, 68);
        add(foto1);
        setFoto(foto1, "avatar/" + ava1 + ".gif");

        int ta = (n2.length() * 24);

        etiNombre2.setText("" + n2);
        etiNombre2.setBounds(995 - ta, 82, ta, 30);
        etiNombre2.setFont(new Font("Verdana", Font.BOLD, 30));
        add(etiNombre2);

        foto2.setBorder(LineBorder.createGrayLineBorder());
        foto2.setBounds(940, 2, 60, 68);
        add(foto2);
        setFoto(foto2, "avatar/" + ava2 + ".gif");

        add(scrollPrinc);

        primerTurno();

        //   l.imprimir(p);
        //  p.add(txt);
        cargar();
        //  fichasInicio.imprimir();

        fon.setBounds(0, 0, 1005, 568);
        add(fon);

        repartir();

        t.start();
        setResizable(false);
        setSize(1005, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    //Ajustar/escalar imagen a Label
    public void setFoto(JLabel l, String p) {
        ImageIcon icon = new ImageIcon(Constantes.path + p);
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(l.getWidth(), l.getHeight(), java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        l.setIcon(newIcon);
        l.setSize(l.getWidth(), l.getHeight());
        //return l;
    }

    public void setAdminMensajes(Servidor admin) {
        server = admin;
    }

    public void repartir() {

        Nodo tem = fichasInicio.p;
        //int cont=0;
        for (int i = 0; i < jugador.length; i++) {

            Nodo tmp = jugador[i].fichas.p;
            while (tmp != null) {
                tmp.setFicha(tem.texto);
                tmp.tb.addActionListener(this);
                tem.disponible = false;
                tmp = tmp.sig;
                tem = tem.sig;
            }

        }
        jugador[0].fichas.imprimirFichasJ(panel1, 20, 40);
        jugador[1].fichas.imprimirFichasJ(panel2, 310, -40);

        fichasInicio.imprimirFichasSobrantes(panel5, 5, 40);

    }

    ArbolGeneral tablero = new ArbolGeneral();

    public void posicionarRival(int padreID, String lad1, String lad2) {
        selec1 = lad1;
        selec2 = lad2;
        NodoG padre = null;
        if (tablero.raiz != null) {
            padre = tablero.traer(padreID);
        }

        Lista lisTem;
        if (turno == 0) {
            lisTem = jugador[0].fichas;
        } else {
            lisTem = jugador[1].fichas;
        }

        if (tablero.raiz == null) {
            tablero.ingresar(null, selec1, selec2, selec1 + "-" + selec2, -1, 800, 180, Rotaciones.Inicial, principal, this);
            System.out.println(tablero.traer(0).x + " " + tablero.traer(0).y);
            remover(selec1 + "-" + selec2, lisTem);
            turno();
            selec1 = "";
            selec2 = "";
        } else {

            int id = padre.id;
            String act = padre.L_actual;
            String act2 = padre.L_actual2;
            int x = 0;
            int y = 0;
            String NuevoActual = "";
            double nuevaRotacion = 0.0;
            int var = -1;
            System.out.println("padre " + padre.x + " " + padre.y);
            if (act.equalsIgnoreCase(selec1) || act.equalsIgnoreCase(selec2) || act2.equalsIgnoreCase(selec1) || act2.equalsIgnoreCase(selec2)) {

                if (act2.equalsIgnoreCase(selec1)) {

                    NuevoActual = selec2;
                    var = 1;///La 1 a la derecha
                }

                if (act2.equalsIgnoreCase(selec2)) {

                    NuevoActual = selec1;
                    var = 0;//Lado 1 a la izquierda
                }
                if (act.equalsIgnoreCase(selec1)) {

                    NuevoActual = selec2;
                    var = 1;///La 1 a la derecha
                }

                if (act.equalsIgnoreCase(selec2)) {

                    NuevoActual = selec1;
                    var = 0;//Lado 1 a la izquierda
                }

                if (((selec1.equalsIgnoreCase(act) && selec2.equalsIgnoreCase(act)) || (selec1.equalsIgnoreCase(act2) && selec2.equalsIgnoreCase(act2))) && (padre.rotacion == Rotaciones.L1_derecha || padre.rotacion == Rotaciones.L1_izq)) {
                    nuevaRotacion = Rotaciones.Inicial;
                    var = 5;
                }

                if (padre.hij_izk == null) {
                    if (padre.rotacion == Rotaciones.Inicial) {
                        if (padre.x <= 800) {
                            x = padre.x - 40;
                            if (var == 0) {
                                nuevaRotacion = Rotaciones.L1_izq;
                            }
                            if (var == 1) {
                                nuevaRotacion = Rotaciones.L1_derecha;
                            }
                        } else {

                            x = padre.x + 40;
                            if (var == 1) {
                                nuevaRotacion = Rotaciones.L1_izq;
                            }
                            if (var == 0) {
                                nuevaRotacion = Rotaciones.L1_derecha;
                            }
                        }

                    } else {
                        if (padre.x <= 800) {

                            if (var == 5) {
                                x = padre.x - 40;
                            } else {
                                x = padre.x - 50;
                            }

                            if (var == 0) {
                                nuevaRotacion = Rotaciones.L1_izq;
                            }
                            if (var == 1) {
                                nuevaRotacion = Rotaciones.L1_derecha;
                            }
                            System.out.println("va para la izquierda");
                        } else {

                            if (var == 5) {
                                x = padre.x + 40;
                            } else {
                                x = padre.x + 50;
                            }
                            if (var == 1) {
                                nuevaRotacion = Rotaciones.L1_izq;
                            }
                            if (var == 0) {
                                nuevaRotacion = Rotaciones.L1_derecha;
                            }
                            System.out.println("va para la derecha");
                        }

                        //  x=padre.x-50;
                    }

                } else {

                    if (padre.rotacion == Rotaciones.Inicial) {
                        x = padre.x + 40;
                    } else {
                        x = padre.x + 50;
                    }
                    System.out.println("pimera que va para la derecha");

                    if (var == 1) {
                        nuevaRotacion = Rotaciones.L1_izq;
                    }
                    if (var == 0) {
                        nuevaRotacion = Rotaciones.L1_derecha;
                    }

                    NodoG tempo = tablero.traer(id);
                    tempo.disponible = false;

                }

                NodoG tempo = tablero.traer(id);

                if (tempo.L_actual2.isEmpty()) {
                    tempo.disponible = false;
                }

                y = padre.y;
                tablero.ingresar(tablero.traer(id), NuevoActual, "", selec1 + "-" + selec2, id, x, y, nuevaRotacion, principal, this);
                System.out.println("/////////AGREGO " + x + " " + y + " " + NuevoActual);
                turno();
                remover(selec1 + "-" + selec2, lisTem);

                selec1 = "";
                selec2 = "";

            } else {
                JOptionPane.showMessageDialog(null, "Esta  ficha no coincide!");
            }
        }
        ganador();
        // verificarExistencia();
    }

    public void ganador() {
        int j1 = jugador[0].fichas.fichasRestantes();
        int j2 = jugador[1].fichas.fichasRestantes();

        if (j1 <= 0) {
            //  JOptionPane.showMessageDialog(null,"Gano Jugador 1");
            new Resultado("Ganaj1.png", jugador[0].nombre);
            return;
        } else if (j2 <= 0) {
            //JOptionPane.showMessageDialog(null,"Gano Jugador 2");
            new Resultado("Ganaj2.png", jugador[1].nombre);
            return;
        }
        verificarExistencia();

    }

    public void verificarExistencia() {

        if (fichasInicio.fichasRestantes() > 0) {
            return;
        }

        int id = 0;
        if (tablero.raiz == null) {
            return;
        }

        NodoG nG = tablero.traer(id);
        String lin = "";
        do {
            System.out.println("comparando");
            if (nG.disponible) {
                lin = lin + ";" + nG.L_actual;
            }
            id++;
            nG = tablero.traer(id);

        } while (nG != null);

        String tem[] = lin.split(";");
        boolean cerrado = true;

        ///////JUGADOR 1
        Nodo tmp = jugador[0].fichas.p;
        while (tmp != null) {
            if (tmp.disponible) {
                for (int i = 0; i < tem.length; i++) {

                    if (tem[i].equalsIgnoreCase(tmp.lado1) || tem[i].equalsIgnoreCase(tmp.lado2)) {
                        cerrado = false;
                    }
                }
            }

            tmp = tmp.sig;

        }

        ///////JUGADOR 2
        tmp = jugador[1].fichas.p;
        while (tmp != null) {

            if (tmp.disponible) {
                for (int i = 0; i < tem.length; i++) {

                    if (tem[i].equalsIgnoreCase(tmp.lado1) || tem[i].equalsIgnoreCase(tmp.lado2)) {
                        cerrado = false;
                    }
                }
            }

            tmp = tmp.sig;

        }

        /// si esta cerrado a contar los puntos de las fichas
        if (cerrado) {

            int suma1 = jugador[0].fichas.cuentaPuntos();
            int suma2 = jugador[1].fichas.cuentaPuntos();
            int variable = -1;
            if (suma1 > suma2) {
                variable = 1;
                new Resultado("Ganaj2.png", jugador[1].nombre);

            } else if (suma2 > suma1) {
                variable = 0;
                new Resultado("Ganaj1.png", jugador[0].nombre);
            } else {
                variable = 5;
                new Resultado("empate.png", "");
            }

            //  administradorMensajes.enviarMensaje(jugador[0].ip,Constantes.GANE_DEFAULT,variable+"");
            //  administradorMensajes.enviarMensaje(jugador[1].ip,Constantes.GANE_DEFAULT,variable+"");
            new SubprocesoEmisor(server.getSocket(jugador[0].ip), "", Constantes.GANE_DEFAULT, variable + "").start();
            new SubprocesoEmisor(server.getSocket(jugador[1].ip), "", Constantes.GANE_DEFAULT, variable + "").start();
            //  server.mensajeRecibido("NADA",Constantes.FIN_JUEGO,"NADA");

        }

    }

    String Ar[] = a.traeLineas(Constantes.path + "Archivos/fichas.txt");

    public void cargar() {

        Nodo tmp = fichasInicio.p;

        while (tmp != null) {
            boolean repetido = false;
            do {
                repetido = false;
                int n = (int) (Math.random() * 28) + 0;
                String dato = Ar[n].trim();
                Nodo tmp2 = fichasInicio.p;
                while (tmp2 != null) {
                    if (tmp2.texto.equalsIgnoreCase("" + dato)) {
                        repetido = true;
                        //System.out.println ("repetido.."+dato);
                    }
                    tmp2 = tmp2.sig;
                }

                tmp.setFicha(dato);

            } while (repetido);

            tmp = tmp.sig;
        }

    }

    public void turno() {
        luz[turno].setBackground(Color.RED);
        turno++;
        if (turno == jugador.length) {
            turno = 0;
        }

        luz[turno].setBackground(Color.GREEN);

        verificarExistencia();
    }

    public void primerTurno() {
        int r = (int) (Math.random() * jugador.length);
        turno = r;
        //  JOptionPane.showMessageDialog(null,"El turno es del jugador "+(r+1)+"\n"+jugador[turno].nombre);
        System.out.println("TURNO JUGADOR " + turno + 1);

        luz[turno].setBackground(Color.GREEN);
    }

    public void remover(String texto, Lista lis) {
        Nodo tmp = lis.p;
        while (tmp != null) {
            if (tmp.texto.equalsIgnoreCase(texto)) {
                tmp.disponible = false;
            }

            tmp = tmp.sig;
        }

        reImprime();
    }

    public void reImprime() {
        jugador[0].fichas.imprimirFichasJ(panel1, 20, 40);
        jugador[1].fichas.imprimirFichasJ(panel2, 310, -40);

        fichasInicio.imprimirFichasSobrantes(panel5, 5, 40);
    }

}
