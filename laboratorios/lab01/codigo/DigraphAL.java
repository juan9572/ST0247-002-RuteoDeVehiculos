package Lab01;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementacion de un grafo dirigido usando listas de adyacencia
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 */
public class DigraphAL extends Digraph {

    public ArrayList<LinkedList<Pair<Integer,Integer>>> grafo;
    /**
     * Constructor para el grafo dirigido
     * @param size el numero de vertices que tendra el grafo dirigido
     *
     */
    public DigraphAL(int size) {
        super(size);
        grafo = new ArrayList<>(size);
        for(int i = 0; i < size; i++){
            grafo.add(new LinkedList<Pair<Integer,Integer>>());
        }
    }

    /**
     * Metodo para añadir un arco nuevo, donde se representa cada nodo con un entero
     * y se le asigna un peso a la longitud entre un nodo fuente y uno destino
     * @param source desde donde se hara el arco
     * @param destination hacia donde va el arco
     * @param weight el peso de la longitud entre source y destination
     */
    public void addArc(int source, int destination, int weight) {
        grafo.get(source).addFirst(new Pair<>(destination,weight));
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
        ArrayList<Integer> sucecesoresDelGrafo = new ArrayList<>();
        for(int i = 0; i < grafo.get(vertex).size(); i++){
            sucecesoresDelGrafo.add(grafo.get(vertex).get(i).first);
        }
        if(sucecesoresDelGrafo.size() == 0){
            return null;
        }
        return sucecesoresDelGrafo;
    }

    /**
     * Metodo para obtener el peso o longitud entre dos nodos
     *
     * @param source desde donde inicia el arco
     * @param destination  donde termina el arco
     * @return un entero con dicho peso
     */
    public int getWeight(int source, int destination) {
        int result = 0;
        for (Pair<Integer, Integer> integerIntegerPair : grafo.get(source)) {
            if (integerIntegerPair.first == destination) result = integerIntegerPair.second;
        }
        return result;
    }

}