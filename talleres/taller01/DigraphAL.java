package Taller12;

import java.util.ArrayList;
import java.util.LinkedList;

//GRAFO CON LISTAS
/**
 * Implementacion de un grafo dirigido usando listas de adyacencia
 *
 * @author Juan Pablo Usma | Julian Gomez Benitez
 */
public class DigraphAL extends Graph
{
    private ArrayList<LinkedList<Pair<Integer,Integer>>> nodo;
    /**
     * Constructor para el grafo dirigido
     * @param size el numero de vertices que tendra el grafo dirigido
     *
     */
    public DigraphAL(int size) {
        super(size);
        nodo = new ArrayList<>();
        for (int i = 0; i < size + 1; i++) {
            nodo.add(new LinkedList<Pair<Integer,Integer>>());
        }
    }
    /**
     * Metodo para añadir un arco nuevo, donde se representa cada nodo con un entero
     * y se le asigna un peso a la longitud entre un nodo fuente y uno destino
     * @param source desde donde se hara el arco
     * @param destination hacia donde va el arco
     * @param weight el peso de la longitud entre source y destination
     */
    public void addArc(int source, int destination, int weight){
        nodo.get(source).add(new Pair<>(destination,weight));
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
        for (Pair<Integer, Integer> integerIntegerPair : nodo.get(source)) {
            if (integerIntegerPair.getKey() == destination) result = integerIntegerPair.getValue();
        }
        return result;
    }
    /**
     * Metodo para obtener una lista de hijos desde un nodo, es decir todos los nodos
     * asociados al nodo pasado como argumento
     * @param vertice nodo al cual se le busca los asociados o hijos
     * @return todos los asociados o hijos del nodo vertex, listados en una ArrayList
     * Para más información de las clases:
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html"> Ver documentacion ArrayList </a>
     */
    public ArrayList<Integer> getSuccessors(int vertice){
        ArrayList<Integer> n = new ArrayList<>();
        nodo.get(vertice).forEach(i -> n.add(i.getKey()));
        return n;
    }
    /**
     * Método para recorrer por DFS y buscar si se puede llegar desde un grafo hasta a otro.
     *
     * @param g Grafo con el nodo dado.
     * @param origin donde se empieza a buscar.
     * @param destination donde se quiere llegar.
     * @return si esta el destino entonces se devuelve true.
     */
    public boolean isThereARoadDFS(Graph g, int origin, int destination){
        LinkedList<Integer> visitados = new LinkedList<>();
        return isThereARoadDFS(origin, destination, visitados);
    }
    /**
     * Método para recorrer por DFS y buscar si se puede llegar desde un grafo hasta a otro.
     *
     * @param origin donde se empieza a buscar.
     * @param destination donde se quiere llegar.
     * @return si esta el destino entonces se devuelve true.
     */
    public boolean isThereARoadDFS(int origin, int destination, LinkedList<Integer> visitados){
        visitados.add(origin);
        if(origin == destination){
            return true;
        }
        for(Integer s : getSuccessors(origin)){
            if(!visitados.contains(s)){
                if(isThereARoadDFS(s,destination,visitados)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Método para recorrer por BFS y buscar si se puede llegar desde un grafo hasta a otro.
     *
     * @param origin donde se empieza a buscar.
     * @param destination donde se quiere llegar.
     * @return si esta el destino entonces se devuelve true.
     */
    public boolean isThereARoadBFS(Graph g, int origin, int destination){
        LinkedList<Integer> aVisitar = new LinkedList<>();
        for(Integer s: g.getSuccessors(origin)){
            aVisitar.add(s);
        }
        LinkedList<Integer> visitados = new LinkedList<>();
        visitados.add(origin);
        aVisitar.add(origin);
        if(origin == destination){
            return true;
        }
        while(!aVisitar.isEmpty()){
            int temp = aVisitar.pop();
            if(temp == destination){
                return true;
            }
            visitados.add(temp);
            for(Integer sigueinte: g.getSuccessors(temp)){
                if(!visitados.contains(sigueinte)){
                    aVisitar.add(sigueinte);
                }
            }
        }
        return false;
    }
}