package Lab03;

import java.util.ArrayList;
/**
 * Esta clase se encarga de ejecutar el programa.
 * @version 1
 * @author Juan Pablo Rincon Usma | Julian Gómez Benitez
 */
public class Main {
    /**
     * Main desde donde se ejecuta los métodos para buscar el camino más corto entre 2 nodos.
     * @param args
     */
    public static void main(String[] args) {
        LectorDatos ld = new LectorDatos();
        ArrayList<Integer> cam = new ArrayList<>();
        ld.caminoMasCorto(ld.Mapa,10000,4,0,cam);
        System.out.println(ld.caminoGlobal);
        System.out.println(ld.costoMin);
    }

}
