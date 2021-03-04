package Lab01;

import java.util.ArrayList;
/**
 * Implementacion de un grafo dirigido usando matrices de adyacencia
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 */
public class DigraphAM extends Digraph {

    public int grafo[][];

    /**
     * Constructor para el grafo dirigido
     * @param size el numero de vertices que tendra el grafo dirigido
     *
     */
    public DigraphAM(int size) {
        super(size);
        grafo = new int[size][size];
    }

    /**
     * Metodo para añadir un arco nuevo, donde se representa cada nodo con un entero
     * y se le asigna un peso a la longitud entre un nodo fuente y uno destino
     * @param source desde donde se hara el arco
     * @param destination hacia donde va el arco
     * @param weight el peso de la longitud entre source y destination
     */
    public void addArc(int source, int destination, int weight) {
        grafo[source][destination] = weight;
    }

    /**
     * Metodo para obtener una lista de hijos desde un nodo, es decir todos los nodos
     * asociados al nodo pasado como argumento
     * @param vertex nodo al cual se le busca los asociados o hijos
     * @return todos los asociados o hijos del nodo vertex, listados en una ArrayList
     * Para más información de las clases:
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html"> Ver documentacion ArrayList </a>
     */
    public ArrayList<Integer> getSuccessors(int vertex) {
        ArrayList<Integer> sucesoresDelGrafo = new ArrayList<>();
        for(int i = 0; i < grafo.length;i++){
            if(grafo[vertex][i] != 0){
                sucesoresDelGrafo.add(i);
            }
        }
        if(sucesoresDelGrafo.size() == 0){
            return null;
        }
        return sucesoresDelGrafo;
    }

    /**
     * Metodo para obtener el peso o longitud entre dos nodos
     *
     * @param source desde donde inicia el arco
     * @param destination  donde termina el arco
     * @return un entero con dicho peso
     */
    public int getWeight(int source, int destination) {
        return grafo[source][destination];
    }

}