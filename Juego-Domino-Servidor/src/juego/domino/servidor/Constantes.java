package juego.domino.servidor;

/**
 *
 * @author Renan
 */
public interface Constantes {

    public static final String path = "resources/";
    ////Puerto donde los clientes se conectan al servidor
    public static final int PUERTO_SERVIDOR = 12346;

    public static final String CADENA_DESCONEXION = "_DESCONECTAR__";
    //Sirve para separar las cadenas de texto envidas como mensajes
    public static final String SEPARADOR_MENSAJE = ">>>";
    //Sirve para identificar cuando se conecta un nuevo jugador
    public static final String NUEVO_JUGADOR = "NEW_G@M3R#";
    //Sirve para avisarle al jugador que esta en espera
    public static final String JUGADOR_ESPERA = "ESP3R4";
    //Sirve para indicar que espere al 2do jugador
    public static final String SEPARA_DATOS = "<<SEPARA<<";

    public static final String JUEGO_LISTO = "<<R3@D1<<";

    public static final String RECIBE_FICHAS = "<<F1CH4S<<";

    public static final String RECIBE_INFO = "<<1NF0_J<<";

    public static final String RECIBE_SOBRANTES = "<<S0bR@<<";

    public static final String EJECUTAR_JUGADA = "<<JUG@D4<<";

    public static final String AGARRA_SOBRANTES = "<<4G@RRA_S08R4<<";

    public static final String FIN_JUEGO = "<<G4M3_0V3R<<";

    public static final String ERROR_NOMBRE = "<<3RR0R_N0M8RE<<";

    public static final String SALTO_TURNO = "<<S@LT0<<";
    public static final String GANE_DEFAULT = "<<GAME_CERRADO<<";
}
