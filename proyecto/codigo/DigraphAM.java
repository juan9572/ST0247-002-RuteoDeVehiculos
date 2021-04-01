import java.util.ArrayList;

/**
 * Implementacion de un grafo dirigido usando matrices de adyacencia.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @see Digraph
 * @version 1
 */
public class DigraphAM extends Digraph {
	double[][] matrix; // matriz del grafo.

    /**
     * Constructor para el grafo dirigido.
     * @param size el numero de vertices que tendra el grafo dirigido
     */
	public DigraphAM(int size) {
		super(size);
		matrix = new double[size][size];
	}

    /**
     * Metodo para añadir un arco nuevo, donde se representa cada nodo con un entero
     * y se le asigna un peso a la longitud entre un nodo fuente y uno destino
     * @param source desde donde se hara el arco
     * @param destination hacia donde va el arco
     * @param weight el peso de la longitud entre source y destination
     */
	public void addArc(int source, int destination, double weight) {
		matrix[source][destination] = weight;
	}

    /**
     * Metodo para obtener una lista de hijos desde un nodo, es decir todos los nodos
     * asociados al nodo pasado como argumento
     * @param vertex nodo al cual se le busca los asociados o hijos
     * @return todos los asociados o hijos del nodo vertex, listados en una ArrayList
     */
	public ArrayList<Integer> getSuccessors(int vertex) {
		ArrayList<Integer> s = new ArrayList<>();
		for (int i = 0; i < size; ++i)
			if (matrix[vertex][i] != 0)
				s.add(i);
		return s.size() == 0 ? null : s;
	}

    /**
     * Metodo para obtener el peso o longitud entre dos nodos
     *
     * @param source desde donde inicia el arco
     * @param destination  donde termina el arco
     * @return un double con dicho peso
     */
	public double getWeight(int source, int destination) {
		return matrix[source][destination];
	}

}