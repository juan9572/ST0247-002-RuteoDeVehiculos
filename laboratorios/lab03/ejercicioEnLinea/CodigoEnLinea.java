package Lab01.CodigoEnLinea;

import Lab01.DigraphAM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Esta clase se encarga de hacer el problema de las n reinas con backtraking.
 * @version 1
 * @author Juan Pablo Rincon Usma | Julian Gómez Benitez
 */
public class CodigoEnLinea {
    DigraphAM grafo; // Grafo representado con matrices de adyacencia.
    int n,m; // La n y m del problema, donde n son los nodos y m las conexiones entre ellos.

    /**
     * Método encargado de leer los datos del archivo, primero se lee las variables n y m, para poder
     * inicializar el grafo, después de inicializar el grafo se procede a leer las conexiones m entre los nodos, y
     * se agregan al grafo ya inicializado.
     */
    public void leerNodos() {
        try{
            FileReader f  = new FileReader("src\\Lab01\\CodigoEnLinea\\DatosEnLine.txt");
            BufferedReader b = new BufferedReader(f);
            String line = b.readLine();
            String[] data = line.split(" ");
            n = Integer.parseInt(data[0]);
            m = Integer.parseInt(data[1]);
            grafo = new DigraphAM(n+1);
            for(int i = 0; i < m; i++){
                line = b.readLine();
                data = line.split(" ");
                grafo.addArc(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]));
            }
            b.close();
        }catch (IOException e) {
            System.out.println("Asegurece de tener el documento ''puentesColgantes''");
        }
    }

    int costoMin = Integer.MAX_VALUE; //
    ArrayList<Integer> caminoGlobal; //

    /**
     * Método encargado de encontrar la distancia más corta entre 2 puntos a, b dentro de un grafo, usando backtracking.
     * @param matriz El grafo representado como matrices de adyacencia.
     * @param source El nodo origen desde donde se empezará a buscar.
     * @param destination El nodo destino donde se debe llegar para terminar el recorrido.
     * @param costo El costo actual de dicho camino recorrido, se ira sumando en cada recursión.
     * @param camino El camino de los nodos actual.
     */
    public void caminoCorto(int [][] matriz,int source, int destination, int costo, ArrayList<Integer> camino){
        if(costo > costoMin){
            return;
        }
        if(source == destination){
            costoMin = Math.min(costo,costoMin);
            camino.add(destination);
            caminoGlobal = camino;
            return;
        }
        camino.add(source);
        ArrayList<Integer> sucesores = grafo.getSuccessors(source);
        for (Integer suce: sucesores){
            int costoTemp = costo + matriz[source][suce];
            ArrayList<Integer> caminoTemp = new ArrayList<>(camino);
            caminoCorto(matriz,suce,destination,costoTemp,caminoTemp);
        }
    }

    /**
     * Método encargado de ejecutar el programa.
     * @param args
     */
    public static void main(String[] args) {
        CodigoEnLinea cel = new CodigoEnLinea();
        cel.leerNodos();
        ArrayList<Integer> cam = new ArrayList<>();
        cel.caminoCorto(cel.grafo.grafo,1,cel.n,0,cam);
        System.out.println(cel.caminoGlobal);
        System.out.println(cel.costoMin);
    }

}
