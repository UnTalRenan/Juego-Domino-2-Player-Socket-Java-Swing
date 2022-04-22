package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Tablero extends JFrame implements ActionListener {

    JLabel fon = new JLabel(new ImageIcon(Constantes.path + "img/x.jpg"));
    Lista fichasInicio = new Lista();

    JPanel p;

    JLabel panel1, panel2, panel3, panel4, panel5, principal;

    Lista sobrante = new Lista();
    String selec1 = "";
    String selec2 = "";
    String ladoCoincidio = "";
    Timer tiempo = new Timer(50, this);
    Timer timerEfecto = new Timer(1000, this);

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!selec1.isEmpty() && !selec2.isEmpty()) {
            int id = 0;
            if (tablero.raiz == null) {
                return;
            }

            NodoG nG = tablero.traer(id);

            do {
                System.out.println("comparando");
                if (nG == e.getSource()) {
                    if (nG.disponible) {
                        System.out.println("////////////////PRESIONO " + nG.id + "  " + nG.L_actual + " " + nG.L_actual2 + "  x=" + nG.x + " y=" + nG.y);
                        posicionar(nG);
                    } else {
                        JOptionPane.showMessageDialog(null, "No puede poner la ficha aqui");
                    }

                    return;
                }
                id++;
                nG = tablero.traer(id);

            } while (nG != null);

        }

        /*	for (int i = 0; i<jugador.length; i++) 
    	    {
    	    	if(turno==i)
    	    	{
    	    		Nodo tmp=jugador[i].fichas.p;
					while(tmp!=null)
					{
						if(tmp.tb==e.getSource())
						{
							selec1=tmp.lado1;
							selec2=tmp.lado2;
							System.out.println ("Selecciono:"+selec1+"_"+selec2);
							//posicionar();
						}
						
						tmp=tmp.sig;
						
					}
    	    	}*/
        if (turno == actual) {
            Nodo tmp = jugador[actual].fichas.p;
            while (tmp != null) {
                if (tmp.tb == e.getSource()) {
                    selec1 = tmp.lado1;
                    selec2 = tmp.lado2;
                    System.out.println("Selecciono:" + selec1 + "_" + selec2);
                    //posicionar();
                }

                tmp = tmp.sig;

            }

            Nodo tmpSobra = fichasInicio.p;

            while (tmpSobra != null) {
                if (tmpSobra.tb == e.getSource()) {

                    agarra_Sobrante(tmpSobra);
                }

                tmpSobra = tmpSobra.sig;
            }

        }

        if (tiempo == e.getSource()) {
            if (turno != actual) {
                esperaTexto.setVisible(true);
                //	esperaT.setVisible(true);
            } else {
                esperaTexto.setVisible(false);
                //	esperaT.setVisible(false);
            }

        }
        if (NuevoJuego == e.getSource()) {
            dispose();
            esperaTexto.dispose();
            new Inicio();

        }
        if (saltaTurno == e.getSource()) {
            administradorMensajes.enviarMensaje(actual + "", Constantes.SALTO_TURNO, "VACIO");
            turno();
        }
        if (timerEfecto == e.getSource()) {
            if (parpadeo) {
                saltaTurno.setBackground(Color.RED);
                parpadeo = false;
            } else {
                saltaTurno.setBackground(Color.GREEN);
                parpadeo = true;
            }
        }

    }

    boolean parpadeo = true;

    public void agarra_Sobrante(Nodo selecciono) {
        jugador[turno].fichas.ins_final(new Nodo(selecciono.id, this));
        selecciono.disponible = false;
        jugador[turno].fichas.ultimo().setFicha(selecciono.texto);
        administradorMensajes.enviarMensaje("" + actual, Constantes.AGARRA_SOBRANTES, selecciono.texto);

        reImprime();
        selec1 = "";
        selec2 = "";
        verificarExistencia();
    }

    public void agarra_SobranteRival(String texto) {
        jugador[turno].fichas.ins_final(new Nodo(0, this));
        modificarEstado(texto);
        jugador[turno].fichas.ultimo().muestraContenido = false;
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
    Jugador jugador[] = {};
    JButton luz[] = new JButton[4];

    int turno = 0;
    int actual;
    AdministradorMensajes administradorMensajes;
    JScrollPane jugaScroll;
    JScrollPane jugaScroll2;
    JScrollPane scrollPrinc;
    EsperaTurno esperaT;
    EsperaTurno esperaTexto;

    JButton NuevoJuego = new JButton("NUEVO JUEGO");
    JButton saltaTurno = new JButton("SALTAR TURNO");

    public Tablero(String n1, String ava1, String n2, String ava2, String turno, String actu) {
        super("DOMINO " + actu);
        setLayout(null);

        this.turno = Integer.parseInt(turno);
        actual = Integer.parseInt(actu);

        //	int cantidad=Integer.parseInt(JOptionPane.showInputDialog("Cantidad de Jugadores"));
        jugador = new Jugador[2];
        jugador[0] = new Jugador(n1, ava1);
        jugador[1] = new Jugador(n2, ava2);
        for (int i = 0; i < 7; i++) {
            jugador[0].fichas.ins_final(new Nodo(i, this));
            jugador[1].fichas.ins_final(new Nodo(i, this));
        }

        /*	jugador=new Jugador[cantidad];
    		
    		for (int i = 0; i<jugador.length; i++) 
    		{
    			
    			String nombre=JOptionPane.showInputDialog("Ingrese Nombre Jugador # "+(i+1));
    			String clave="";//JOptionPane.showInputDialog("Ingrese Clave Jugador # "+(i+1));
    			jugador[i]=new Jugador(nombre,clave);
    				for (int x = 0; x<7; x++) 
					{
						jugador[i].fichas.ins_final(new Nodo(x));
					}
    			
			}*/
        for (int i = 0; i < 14; i++) {
            fichasInicio.ins_final(new Nodo(i, this));
        }

        panel1 = new JLabel();
        jugaScroll = new JScrollPane(panel1, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel1.setLayout(null);
        //panel1.setBorder(LineBorder.createGrayLineBorder());	

        jugaScroll.setBounds(220, 0, 360, 65);
        panel1.setBounds(0, 0, 360, 65);
        add(jugaScroll);
        panel1.setPreferredSize(new Dimension(900, 65));

        luz[0] = new JButton();
        luz[0].setBounds(jugaScroll.getX() - 20, jugaScroll.getY() + 45, 20, 20);
        luz[0].setBackground(Color.RED);
        add(luz[0]);

        panel2 = new JLabel();
        jugaScroll2 = new JScrollPane(panel2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel2.setLayout(null);
        panel2.setBorder(LineBorder.createGrayLineBorder());
        jugaScroll2.setBounds(580, 0, 360, 65);
        panel2.setBounds(0, 0, 360, 65);

        add(jugaScroll2);
        panel2.setPreferredSize(new Dimension(900, 65));

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (Exception ex) {
                }
                jugaScroll2.getHorizontalScrollBar().setValue(900);

            }
        });

        luz[1] = new JButton();
        luz[1].setBounds(jugaScroll2.getX() + jugaScroll2.getWidth(), panel2.getY() + 45, 20, 20);
        luz[1].setBackground(Color.RED);
        add(luz[1]);

        luz[this.turno].setBackground(Color.GREEN);

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
        principal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                System.out.println("inicial");
                if ((selec1.isEmpty() || selec2.isEmpty()) && tablero.raiz == null) {
                    JOptionPane.showMessageDialog(null, "Seleccione una ficha");
                    return;
                }
                if (tablero.raiz == null) {
                    posicionar(null);
                }
            }
        });
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                }

                scrollPrinc.getHorizontalScrollBar().setValue(430);

            }
        });

        JLabel etiNombre = new JLabel("" + n1);
        etiNombre.setBounds(185, 82, 300, 30);
        etiNombre.setFont(new Font("Verdana", Font.BOLD, 30));

        add(etiNombre);

        JLabel foto = new JLabel();
        foto.setBorder(LineBorder.createGrayLineBorder());
        foto.setBounds(162, 2, 60, 68);
        add(foto);
        setFoto(foto, "avatar/" + ava1 + ".gif");

        int ta = (n2.length() * 24);

        etiNombre = new JLabel("" + n2);
        etiNombre.setBounds(995 - ta, 82, ta, 30);
        etiNombre.setFont(new Font("Verdana", Font.BOLD, 30));
        add(etiNombre);

        foto = new JLabel();
        foto.setBorder(LineBorder.createGrayLineBorder());
        foto.setBounds(940, 2, 60, 68);
        add(foto);
        setFoto(foto, "avatar/" + ava2 + ".gif");

        add(scrollPrinc);
        //	   primerTurno(); 

        NuevoJuego.setBounds(10, 50, 145, 40);
        NuevoJuego.addActionListener(this);
        NuevoJuego.setVisible(false);
        add(NuevoJuego);

        saltaTurno.setBounds(10, 200, 145, 40);
        saltaTurno.addActionListener(this);
        saltaTurno.setBackground(Color.GREEN);
        saltaTurno.setVisible(false);
        add(saltaTurno);

        //   l.imprimir(p);
        //	p.add(txt);
        //	cargar();
        //	fichasInicio.imprimir();
        fon.setBounds(0, 0, 1005, 568);
        add(fon);

        esperaTexto = new EsperaTurno(false);
        //esperaT=new EsperaTurno(true);
        tiempo.start();
        //esperaT.setModal(true);
        timerEfecto.start();
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

    public void setAdminMensajes(AdministradorMensajes admin) {
        administradorMensajes = admin;
    }

    public void repartir(int juga, String fic[]) {
        System.out.println("JUGA :" + juga);
        if (juga == -1) {
            Nodo tem = fichasInicio.p;
            for (int i = 0; i < fic.length; i++) {
                if (actual != juga) {
                    tem.muestraContenido = false;
                }

                tem.setFicha(fic[i]);

                tem = tem.sig;
            }
        } else {
            Nodo tem = jugador[juga].fichas.p;
            for (int i = 0; i < fic.length; i++) {
                //System.out.println ("SETFICHA:"+fic[i]);
                if (actual != juga) {
                    tem.muestraContenido = false;
                }
                tem.setFicha(fic[i]);
                tem = tem.sig;
            }
        }

        reImprime();

    }

    ArbolGeneral tablero = new ArbolGeneral();

    public void posicionar(NodoG padre) {

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

            administradorMensajes.enviarMensaje("" + actual, Constantes.EJECUTAR_JUGADA, "null" + Constantes.SEPARA_DATOS + selec1 + Constantes.SEPARA_DATOS + selec2);
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
            String uso = "";
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

                        //	x=padre.x-50;
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
                administradorMensajes.enviarMensaje("" + actual, Constantes.EJECUTAR_JUGADA, id + Constantes.SEPARA_DATOS + selec1 + Constantes.SEPARA_DATOS + selec2);

                turno();
                remover(selec1 + "-" + selec2, lisTem);

                selec1 = "";
                selec2 = "";
            } else {
                JOptionPane.showMessageDialog(null, "Esta  ficha no coincide!");
            }
        }
        ganador();
    }

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

                        //	x=padre.x-50;
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
    }

    public void ganador() {
        int j1 = jugador[0].fichas.fichasRestantes();
        int j2 = jugador[1].fichas.fichasRestantes();

        if (j1 <= 0) {
            //	JOptionPane.showMessageDialog(null,"Gano Jugador 1");
            administradorMensajes.enviarMensaje("" + actual, Constantes.FIN_JUEGO, "VACIO");
            administradorMensajes.enviarMensaje("" + actual, Constantes.CADENA_DESCONEXION, Constantes.CADENA_DESCONEXION);
            turno = actual;
            new Resultado("Ganaj1.png", jugador[0].nombre);
            NuevoJuego.setVisible(true);

        } else if (j2 <= 0) {
            administradorMensajes.enviarMensaje("" + actual, Constantes.FIN_JUEGO, "VACIO");
            administradorMensajes.enviarMensaje("" + actual, Constantes.CADENA_DESCONEXION, Constantes.CADENA_DESCONEXION);
            //JOptionPane.showMessageDialog(null,"Gano Jugador 2");
            turno = actual;
            new Resultado("Ganaj2.png", jugador[1].nombre);
            NuevoJuego.setVisible(true);
        }
    }

    public void verificarExistencia() {

        if (turno == actual) {

            if (fichasInicio.fichasRestantes() <= 0) {
                saltaTurno.setVisible(true);
            }

        } else {
            saltaTurno.setVisible(false);
        }

    }

    public void ganeDefault(int jugad) {

        administradorMensajes.enviarMensaje("" + actual, Constantes.FIN_JUEGO, "VACIO");
        administradorMensajes.enviarMensaje("" + actual, Constantes.CADENA_DESCONEXION, Constantes.CADENA_DESCONEXION);
        turno = actual;

        saltaTurno.setVisible(false);
        NuevoJuego.setVisible(true);
        if (jugad == 0) {

            new Resultado("Ganaj1.png", jugador[0].nombre);

        } else if (jugad == 1) {

            new Resultado("Ganaj2.png", jugador[1].nombre);
        } else {
            new Resultado("empate.png", "");
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
        JOptionPane.showMessageDialog(null, "El turno es del jugador " + (r + 1) + "\n" + jugador[turno].nombre);
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
        jugador[1].fichas.imprimirFichasJ(panel2, 850, -40);

        fichasInicio.imprimirFichasSobrantes(panel5, 5, 40);
    }

}
