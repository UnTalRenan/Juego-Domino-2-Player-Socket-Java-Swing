package juego.domino.cliente;

/**
 *
 * @author Renan
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author
 *
 */
//Estructura de dato arbol general
public class ArbolGeneral {

    NodoG raiz, buscar;
    int cont, numNodos, contarG = 0;

    public ArbolGeneral() {
        raiz = null;
        cont = 0;
    }

    public void ingresar(NodoG padre, String actual, String actual2, String texto, int idpadre, int x, int y, double rotacion, JLabel panel, ActionListener AC) {
        if (raiz == null) {
            raiz = new NodoG();
            raiz.L_actual = actual;
            raiz.L_actual2 = actual2;
            raiz.idpadre = idpadre;
            raiz.text = texto;
            raiz.id = cont;
            raiz.x = x;
            raiz.y = y;
            raiz.rotacion = rotacion;
            raiz.icono = new ImageIcon(Constantes.path + "img/" + texto + ".png");
            cont += 1;
            mostrar(panel, raiz, AC);
            return;
        }
        if (padre.hij_izk == null) {
            padre.hij_izk = new NodoG();
            padre.hij_izk.L_actual = actual;
            padre.hij_izk.L_actual = actual;
            padre.hij_izk.idpadre = idpadre;
            padre.hij_izk.text = texto;
            padre.hij_izk.id = cont;
            padre.hij_izk.x = x;
            padre.hij_izk.y = y;
            padre.hij_izk.rotacion = rotacion;
            padre.hij_izk.icono = new ImageIcon(Constantes.path + "img/" + texto + ".png");
            mostrar(panel, padre.hij_izk, AC);
        } else {
            NodoG act = padre.hij_izk;
            while (act.he_der != null) {
                act = act.he_der;
            }
            act.he_der = new NodoG();
            act.he_der.idpadre = idpadre;
            act.he_der.L_actual = actual;
            act.he_der.L_actual = actual;
            act.he_der.text = texto;
            act.he_der.id = cont;
            act.he_der.x = x;
            act.he_der.y = y;
            act.he_der.rotacion = rotacion;
            act.he_der.icono = new ImageIcon(Constantes.path + "img/" + texto + ".png");
            mostrar(panel, act.he_der, AC);
        }

        cont++;
    }

    public void mostrar(JLabel panel, NodoG nodo, ActionListener AC) {
        nodo.setBounds(nodo.x, nodo.y, 50, 50);
        nodo.setIcon(nodo.icono);

        nodo.addActionListener(AC);
        panel.add(nodo);
        nodo.setRotacion(nodo.rotacion);
        nodo.repaint();
        panel.repaint();
    }

    public void preFijo(NodoG padre) {
        while (padre != null) {
            //System.out.print(padre.info);
            preFijo(padre.hij_izk);
            padre = padre.he_der;
        }

    }

    public void postFijo(NodoG padre) {
        while (padre != null) {
            postFijo(padre.hij_izk);
            // 		System.out.print(padre.info);
            padre = padre.he_der;
        }

    }

    public NodoG traer(int id) {
        buscar = null;
        busqueda(raiz, id);
        return buscar;

    }

    public void busqueda(NodoG padre, int id) {
        if (buscar != null) {
            return;
        }
        if (padre.id == id) {
            buscar = padre;
            return;
        }
        NodoG c = padre.hij_izk;
        while (c != null) {
            busqueda(c, id);
            c = c.he_der;
        }
    }

    public NodoG traerConXy(int x, int y) {
        buscar = null;
        busqueda(raiz, x, y);
        return buscar;

    }

    public void busqueda(NodoG padre, int x, int y) {
        if (buscar != null) {
            return;
        }
        if (padre.x == x && padre.y == y) {
            buscar = padre;
            return;
        }
        NodoG c = padre.hij_izk;
        while (c != null) {
            busqueda(c, x, y);
            c = c.he_der;
        }
    }

    public NodoG padre(int id) {
        buscar = null;
        busca_padre(raiz, id);
        return buscar;
    }

    public void busca_padre(NodoG padre, int id) {
        if (buscar != null) {
            return;
        }
        NodoG c = padre.hij_izk;
        while (c != null) {
            if (c.id == id) {
                buscar = padre;
                return;
            } else {
                busca_padre(c, id);
                c = c.he_der;
            }
        }
    }

    public void dibuja(Graphics g, NodoG act, int x, int y, int x2, int y2) {
        @SuppressWarnings("unused")
        int n = 0, h = 1, i = 0, id;
        while (act != null) {
            g.setColor(Color.WHITE);
            g.drawLine(x + 45, y, x2 + 45, y2 + 90);
            g.fillOval(x, y, 90, 90);
            g.setColor(Color.RED);
            g.drawString("" + act.text, x + 40, y + 37);
            general(act.id);
            n = contarG;
            if (act.id > 0) {
                if (act.id == tieneHijos(padre(act.id).id)) {
                    i = nie(padre(act.id).id);
                }
            }
            dibuja(g, act.hij_izk, x - 150 * (n / 2), y + 190, x, y);
            act = act.he_der;
            x += (150 + (150 * (i / 2)));
        }

    }

    public NodoG general(int id) {
        buscar = null;
        contarG = 0;
        contar_Hijos(raiz, id);
        return buscar;
    }

    public int nie(int id) {
        int num = 1, n = 1;
        general(id);// A general saca tamaÃ±o de cuantos hijos tiene
        num = contarG;
        for (int i = 1; i <= num; i++) {//
            general(idHijos(id, i).id);
            if (n <= contarG) {
                n = contarG;
            }
        }
        return n;
    }

    public NodoG idHijos(int id, int n_hijos) {
        buscar = null;
        buscarIdHijos(raiz, id, n_hijos);
        return buscar;
    }

    public void buscarIdHijos(NodoG padre, int id, int n_hijos) {
        int con_hijos = 1;
        while (padre != null) {
            if (padre.id == id) {
                buscar = padre.hij_izk;
                if (n_hijos == 1) {
                    return;
                }
                while (buscar.he_der != null) {
                    con_hijos++;
                    buscar = buscar.he_der;
                    if (con_hijos == n_hijos) {
                        return;
                    }
                }
            }
            buscarIdHijos(padre.hij_izk, id, n_hijos);
            padre = padre.he_der;
        }

    }

    public int tieneHijos(int id) {//
        int num = 1, n = 1, f = 0;
        general(id);// A general saca tamaÃ±o de cuantos hijos tiene
        num = contarG;
        for (int i = 1; i <= num; i++) {//
            general(idHijos(id, i).id);
            n = contarG;
            if (n >= 1) {
                f = idHijos(id, i - 1).id;
                return f;
            }
        }
        return f;
    }

    public int contar_Hijos(NodoG padre, int id) {
        while (padre != null) {
            if (padre.id == id) {
                if (padre.hij_izk == null) {
                    return contarG;
                }
                buscar = padre.hij_izk;
                contarG++;
                while (buscar.he_der != null) {
                    contarG++;
                    buscar = buscar.he_der;
                }
            }
            contar_Hijos(padre.hij_izk, id);
            padre = padre.he_der;
        }
        return contarG;
    }

}
