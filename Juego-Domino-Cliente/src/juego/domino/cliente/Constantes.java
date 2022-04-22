package juego.domino.cliente;

/**
 *
 * @author Renan
 */
public interface Constantes {

    public static final String path = "resources/";
    //190.11.229.67
    Archivo a = new Archivo();
    String dat[] = a.traeLineas(path + "Archivos/servidor.txt");
    //Direccion donde se envian los paquetes, 
    public static final String DIRECCION_SERVIDOR = dat[0].trim();//"192.168.0.101";
    ////Puerto donde los clientes se conectan al servidor
    public static final int PUERTO_SERVIDOR = Integer.parseInt(dat[1].trim());//12346;
    //Cadena que indica la desconexion de un usuario
    public static final String CADENA_DESCONEXION = "_DESCONECTAR__";
    //Sirve para separar en los paquetes el usuario y el mensaje
    public static final String SEPARADOR_MENSAJE = ">>>";
    //Sirve para nuevo jugador
    public static final String NUEVO_JUGADOR = "NEW_G@M3R#";
    //Sirve para indicar que espere al 2do jugador
    public static final String JUGADOR_ESPERA = "ESP3R4";

    public static final String SEPARA_DATOS = "<<SEPARA<<";

    public static final String JUEGO_LISTO = "<<R3@D1<<";

    public static final String RECIBE_FICHAS = "<<F1CH4S<<";

    public static final String RECIBE_INFO = "<<1NF0_J<<";

    public static final String RECIBE_SOBRANTES = "<<S0bR@<<";

    public static final String EJECUTAR_JUGADA = "<<JUG@D4<<";

    public static final String AGARRA_SOBRANTES = "<<4G@RRA_S08R4<<";

    public static final String FIN_JUEGO = "<<G4M3_0V3R<<";

    public static final String SALTO_TURNO = "<<S@LT0<<";

    public static final String GANE_DEFAULT = "<<GAME_CERRADO<<";


}
