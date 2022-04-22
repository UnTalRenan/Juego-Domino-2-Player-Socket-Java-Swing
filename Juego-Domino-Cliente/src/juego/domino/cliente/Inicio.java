package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Inicio extends JFrame implements ActionListener, EscuchaMensajes {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (bt[0] == e.getSource()) {
            conectar();
            actualizar();
        }
        if (bt[1] == e.getSource()) {
            System.exit(0);
        }

    }
    AdministradorMensajes administradorMensajes;
    Tablero princ;

    JLabel eti;
    JTextField txt[] = new JTextField[3];

    ImageIcon[] ImgAvatar;
    String[] VctAvatar = {"default", "abuelo", "arabe", "diablo", "general", "gringo", "guardia", "hombre", "jefe", "juez", "mexicano", "mujer", "profesor", "rey", "usuario"};
    JComboBox CbAvatar1;

    JTabbedPane pest;
    JPanel panel;
    JButton bt[] = new JButton[2];
    Archivo a = new Archivo();

    Tablero tab;
    int J_actual;
    EsperaOtro esperaO;

    public Inicio() {
        super("Conexion");
        setLayout(null);

        pest = new JTabbedPane();
        pest.addTab("Datos Personales", panel = new JPanel());
        pest.setBounds(10, 50, 300, 200);
        add(pest);
        panel.setLayout(null);

        eti = new JLabel("NOMBRE:");
        eti.setBounds(10, 20, 100, 25);
        panel.add(eti);

        txt[0] = new JTextField();
        txt[0].setBounds(eti.getX() + 80, eti.getY(), 150, 25);
        panel.add(txt[0]);

        ////COMBO
        ImgAvatar = new ImageIcon[VctAvatar.length];
        Integer[] intArray1 = new Integer[VctAvatar.length];

        for (int i = 0; i < VctAvatar.length; i++) {
            intArray1[i] = i;
            ImgAvatar[i] = cargarImagen(Constantes.path + "avatar/" + VctAvatar[i] + ".gif");
            if (ImgAvatar[i] != null) {
                ImgAvatar[i].setDescription(VctAvatar[i]);
            }
        }

        eti = new JLabel("AVATAR:");
        eti.setBounds(10, txt[0].getY() + 70, 50, 25);
        panel.add(eti);

        CbAvatar1 = new JComboBox(intArray1);
        ComboBoxRenderer renderer1 = new ComboBoxRenderer();
        renderer1.setPreferredSize(new Dimension(100, 130));
        CbAvatar1.setRenderer(renderer1);
        CbAvatar1.setMaximumRowCount(3);
        CbAvatar1.setBounds(eti.getX() + 80, txt[0].getY() + 50, 150, 70);
        panel.add(CbAvatar1);

        ///COMBO FIN
        pest = new JTabbedPane();
        pest.addTab("Datos Servidor", panel = new JPanel());
        pest.setBounds(10, 260, 300, 100);
        add(pest);
        panel.setLayout(null);

        eti = new JLabel("IP SERVIDOR:");
        eti.setBounds(10, 20, 100, 25);
        panel.add(eti);

        txt[1] = new JTextField();
        txt[1].setBounds(eti.getX() + 80, eti.getY(), 150, 25);
        panel.add(txt[1]);

        bt[0] = new JButton("CONECTAR");
        bt[0].setBounds(40, pest.getY() + pest.getHeight() + 10, 100, 40);
        bt[0].addActionListener(this);
        add(bt[0]);

        bt[1] = new JButton("SALIR");
        bt[1].setBounds(bt[0].getX() + bt[0].getWidth() + 20, bt[0].getY(), 100, 40);
        bt[1].addActionListener(this);
        add(bt[1]);
        datos();
        administradorMensajes = new AdministradorSocketsMensajes(Constantes.DIRECCION_SERVIDOR);

        /*setUndecorated(false);
    	com.sun.awt.AWTUtilities.setWindowOpacity(this,0.5f);
         */
        esperaO = new EsperaOtro();
        esperaO.setVisible(false);
        
        setResizable(false);
        setSize(340, 500);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public String getIP() {
        String ip = "0.0.0.0";

        try {

            InetAddress addr = InetAddress.getLocalHost();

            ip = addr.getHostAddress();

        } catch (Exception e) {

            e.printStackTrace();
        }

        //return ip;//
        return txt[0].getText();
    }

    public void conectar() {

        administradorMensajes.conectar(this, getIP() + Constantes.SEPARA_DATOS + txt[0].getText() + Constantes.SEPARA_DATOS + VctAvatar[CbAvatar1.getSelectedIndex()]);
        administradorMensajes.enviarMensaje(txt[0].getText(), Constantes.NUEVO_JUGADOR, "VACIO");

    }

    public void desconectar() {
        administradorMensajes.enviarMensaje(txt[0].getText(), "SERVER", Constantes.CADENA_DESCONEXION);
        administradorMensajes.desconectar(this);
    }

    String jugador2Nombre;
    String jugador2Avatar;

    String jugador1Nombre;
    String jugador1Avatar;

    String jugador1Fichas[] = {};
    String jugador2Fichas[] = {};

    String num;
    String fichasSobrantes[] = {};
    String turno;

    public void mensajeRecibido(String de, String para, String mensaje) {
        System.out.println(de + " Escucho " + mensaje);

        if (mensaje.equalsIgnoreCase(Constantes.JUGADOR_ESPERA)) {
            System.out.println("Esperando al otro jugador");

            dispose();
            esperaO.setVisible(true);

        } else if (de.equalsIgnoreCase(Constantes.RECIBE_INFO)) {
            String J[] = mensaje.split(Constantes.SEPARA_DATOS);
            String datos1[] = J[0].split(";");
            String datos2[] = J[1].split(";");
            turno = J[2];
            num = J[3];

            jugador1Nombre = datos1[0];
            jugador1Avatar = datos1[1];

            jugador2Nombre = datos2[0];
            jugador2Avatar = datos2[1];
        } else if (de.equalsIgnoreCase(Constantes.RECIBE_FICHAS)) {
            String J[] = mensaje.split(Constantes.SEPARA_DATOS);
            String datos1[] = J[0].split(";");
            String datos2[] = J[1].split(";");

            jugador1Fichas = datos1;
            jugador2Fichas = datos2;
            /*	for (int i = 0; i<jugador1Fichas.length; i++) 
                    {
                            System.out.println ("FICHAS 1 "+jugador1Fichas[i]);
                    }*/

        } else if (de.equalsIgnoreCase(Constantes.RECIBE_SOBRANTES)) {
            String S[] = mensaje.split(";");

            fichasSobrantes = S;
            /*	for (int i = 0; i<fichasSobrantes.length; i++) 
                    {
                            System.out.println ("SOBRANTE "+fichasSobrantes[i]);
                    }*/
        } else if (mensaje.equalsIgnoreCase(Constantes.JUEGO_LISTO)) {
            //JUEGO LISTO	
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
            esperaO.setVisible(false);
            tab = new Tablero(jugador1Nombre, jugador1Avatar, jugador2Nombre, jugador2Avatar, turno, num);

            tab.setAdminMensajes(administradorMensajes);

            tab.repartir(-1, fichasSobrantes);
            tab.repartir(0, jugador1Fichas);
            tab.repartir(1, jugador2Fichas);

        } else if (para.equalsIgnoreCase(Constantes.EJECUTAR_JUGADA)) {

            String datos[] = mensaje.split(Constantes.SEPARA_DATOS);

            if (datos[0].equalsIgnoreCase("null")) {
                datos[0] = "-1";
            }

            int actual = Integer.parseInt(de);
            int padreId = Integer.parseInt(datos[0]);
            String lado1 = datos[1];
            String lado2 = datos[2];
            tab.posicionarRival(padreId, lado1, lado2);

            /* int actualTem=0;
                if(actual==0)
                 actualTem=1;
                 else
                    actualTem=0;
             */
            //new SubprocesoEmisor(getSocket(tab.jugador[actualTem].nombre),""+actual,Constantes.EJECUTAR_JUGADA,mensaje).start();
            return;
        } else if (para.equalsIgnoreCase(Constantes.AGARRA_SOBRANTES)) {

            tab.agarra_SobranteRival(mensaje);
        } else if (para.equalsIgnoreCase(Constantes.SALTO_TURNO)) {

            tab.turno();
        } else if (para.equalsIgnoreCase(Constantes.GANE_DEFAULT)) {

            tab.ganeDefault(Integer.parseInt(mensaje));
        }

    }

    public void datos() {
        String dat[] = a.traeLineas(Constantes.path + "Archivos/servidor.txt");
        txt[1].setText(dat[0]);

    }

    public void actualizar() {
        String dat[] = a.traeLineas(Constantes.path + "Archivos/servidor.txt");
        dat[0] = txt[1].getText();
        a.escribeLineas(Constantes.path + "Archivos/servidor.txt", dat);

    }

    public ImageIcon cargarImagen(String path) {
        //java.net.URL imgURL = Inicio.class.getResource(path);
        if (!path.isEmpty()) /*(imgURL != null) */ {
            return new ImageIcon(path);
        } else {
            System.err.println("No se pudo cargar la imagen : " + path);
            return null;
        }
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            int selectedIndex = ((Integer) value);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ImageIcon icon1 = ImgAvatar[selectedIndex];
            String avat = VctAvatar[selectedIndex];
            setIcon(icon1);

            if (icon1 != null) {
                setText(avat);
                setFont(list.getFont());
            } else {
                setUhOhText(avat + " (imagen no valida)", list.getFont());
            }
            return this;
        }

        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) {
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }

    public static void main(String[] args) {
        new Inicio();
    }
}
