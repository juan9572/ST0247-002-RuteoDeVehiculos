import java.util.ArrayList;

/**
 * Implementación de un grafo.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @version 1
 */
public abstract class Digraph {

	protected int size; // número de vertices.

    /**
     * Constructor para el grafo.
     * @param vertices el numero de vertices que tendra el grafo.
     */
	public Digraph(int vertices) {
		size = vertices;
	}

    /**
     * Metodo para añadir un arco nuevo, donde se representa cada nodo con un entero
     * y se le asigna un peso a la longitud entre un nodo fuente y uno destino.
     *
     * @param source desde donde se hara el arco
     * @param destination hacia donde va el arco
     * @param weight el peso de la longitud entre source y destination
     */
	public abstract void addArc(int source, int destination, double weight);

    /**
     * Metodo para obtener una lista de hijos desde un nodo, es decir todos los nodos
     * asociados al nodo pasado como argumento
     * @param vertex nodo al cual se le busca los asociados o hijos
     * @return todos los asociados o hijos del nodo vertex, listados en una ArrayList.
     */
	public abstract ArrayList<Integer> getSuccessors(int vertex);

    /**
     * Metodo para obtener el peso o longitud entre dos nodos
     *
     * @param source desde donde inicia el arco
     * @param destination  donde termina el arco
     * @return un double con dicho peso
     */
	public abstract double getWeight(int source, int destination);
	public int size() {
		return size;
	}
}