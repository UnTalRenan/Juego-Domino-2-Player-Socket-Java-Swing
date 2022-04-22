package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import javax.swing.*;
//Esctructura de datos tipo lista

public class Lista {

    Nodo p;

    public Lista() {
        p = null;

    }

    public void ins_inicio(Nodo n) {
        if (estavacio()) {
            p = n;
        } else {
            n.sig = p;
            p = n;
        }
    }

    public void ins_final(Nodo n) {
        if (estavacio()) {
            ins_inicio(n);
        } else {
            Nodo tmp = p;
            while (tmp.sig != null) {
                tmp = tmp.sig;
            }
            tmp.sig = n;
        }
    }

    public void ins_pos(Nodo n, int pos) {
        if (pos == length() + 1) {
            ins_final(n);
        } else {
            if (pos == 1) {
                ins_inicio(n);
            } else {
                if (pos > length() + 1) {
                    return;
                } else {
                    Nodo ant = p;
                    Nodo tmp = p;
                    int cont = 1;
                    while (tmp != null) {
                        if (cont == pos) {
                            ant.sig = n;
                            n.sig = tmp;
                            return;
                        }
                        cont++;
                        tmp = tmp.sig;
                        ant = tmp;
                    }
                }
            }

        }
    }

    public void eliminar_inicio() {
        if (estavacio()) {
            JOptionPane.showMessageDialog(null, "Nose puede eliminar, esta vacio");
        } else {
            p = p.sig;
        }
    }

    public void eliminar_final() {
        if (estavacio()) {
            JOptionPane.showMessageDialog(null, "Nose puede eliminar, esta vacio");
        } else {

        }
    }

    public int fichasRestantes() {
        Nodo tmp = p;
        int cantidad = 0;
        while (tmp != null) {
            if (tmp.disponible) {
                cantidad++;
            }

            tmp = tmp.sig;
        }
        return cantidad;
    }

    public void imprimirFichasJ(JLabel panel, int inicio, int seperacion) {
        panel.removeAll();

        int x = inicio;
        int y = 0;
        Nodo tmp = p;
        int ancho = inicio;

        while (tmp != null) {
            if (tmp.disponible) {
                if (tmp.muestraContenido) {
                    tmp.tb.setIcon(new ImageIcon(tmp.imagen));
                }
                tmp.tb.setBounds(x, y, 30, 48);

                panel.add(tmp.tb);
                // System.out.println("este es mi id: "+tmp.id);
                x = x + seperacion;
                ancho = ancho + 50;
            }

            tmp = tmp.sig;

        }
        System.out.println("//////////////////////////////////////////////" + x);
        panel.repaint();
        //  

    }

    public void imprimirFichasSobrantes(JLabel panel, int inicio, int seperacion) {
        panel.removeAll();

        int x = inicio;
        int y = 10;
        Nodo tmp = p;
        while (tmp != null) {
            if (tmp.disponible) {
                if (tmp.muestraContenido) {
                    tmp.tb.setIcon(new ImageIcon(tmp.imagen));
                }

                tmp.tb.setBounds(x, y, 30, 48);
                panel.add(tmp.tb);

                if ((x + seperacion + tmp.tb.getWidth()) > panel.getWidth()) {
                    y = y + 55;
                    x = inicio;
                } else {
                    x = x + seperacion;
                }
            }
            // System.out.println("este es mi id: "+tmp.id);

            tmp = tmp.sig;
        }
        panel.repaint();
    }

    public void imprimirTablero(JLabel panel) {

        Nodo tmp = p;
        while (tmp != null) {

            tmp.tb.setBounds(tmp.getX(), tmp.getY(), tmp.getAncho(), tmp.getAlto());
            panel.add(tmp.tb);
            //System.out.println ("imp "+tmp.texto);
            tmp = tmp.sig;
        }

        panel.repaint();
    }

    public void imprimir() {
        Nodo tmp = p;
        while (tmp != null) {

            System.out.println("IMPRIME: " + tmp.texto);

            tmp = tmp.sig;
        }
    }

    public void elim_pos(int pos) {
        if (pos == 1) {
            eliminar_inicio();
            return;
        }
        if (pos >= length()) {
            eliminar_final();
            return;
        }
        int x = 1;
        Nodo act = p;
        Nodo ant = act;

        while (act != null) {
            if (x == pos) {
                break;
            }

            x++;
            ant = act;
            act = act.sig;
        }
        act = act.sig;
        ant.sig = act;
        act.ant = ant;

    }

    public boolean estavacio() {
        return (p == null) ? true : false;
    }

    public int length() {
        int cont = 1;
        Nodo tmp = p;
        while (tmp.sig != null) {
            cont++;
            tmp = tmp.sig;
        }
        return cont;
    }

    public Nodo primero() {
        //System.out.println ("Primero "+p.texto);
        return p;
    }

    public Nodo ultimo() {
        Nodo tmp = p;
        while (tmp.sig != null) {

            tmp = tmp.sig;
        }
        //System.out.println ("Ultimo "+tmp.texto);
        return tmp;
    }

    public Nodo penultimo() {
        Nodo tmp = p;
        while (tmp.sig != null) {
            if (tmp.sig == ultimo()) {
                return tmp;
            }

            tmp = tmp.sig;
        }
        //System.out.println ("Ultimo "+tmp.texto);
        return tmp;
    }
}
