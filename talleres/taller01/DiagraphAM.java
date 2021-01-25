package Taller12;

import java.util.ArrayList;
import java.util.LinkedList;

public class DiagraphAM extends Graph {
    //Grafo con matriz
    int[][] mat;
    public DiagraphAM(int size){
        super(size);
        mat = new int[size][size];

        for(int i=0; i<=size; i++){
            mat[i][0] = i;
            mat[0][i] = i;
        }
    }

    public void addArc(int source, int destination, int weight){
        mat[source][destination] = weight;
    }

    public int getWeight(int source, int destination){
        return mat[source][destination];
    }

    public ArrayList<Integer> getSuccessors(int vertex) {
        ArrayList<Integer> np= new ArrayList<>();
        for(int i=1; i<=size; i++){
            if(mat[vertex][i]!=0){
                np.add(mat[0][i]);
            }
        }
        return np;
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
        boolean[] visitados = new boolean[g.size()];
        return isThereARoadDFS(origin, destination, visitados);
    }
    /**
     * Método para recorrer por DFS y buscar si se puede llegar desde un grafo hasta a otro.
     *
     * @param origin donde se empieza a buscar.
     * @param destination donde se quiere llegar.
     * @return si esta el destino entonces se devuelve true.
     */
    public boolean isThereARoadDFS(int origin, int destination, boolean[] visitados){
        visitados[origin] = true;
        if(origin == destination){
            return true;
        }
            for(Integer s : getSuccessors(origin)){
                if(!visitados[s]){
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
        boolean visitados[] = new boolean[g.size()];
        visitados[origin] = true;
        aVisitar.add(origin);
        if(origin == destination){
            return true;
        }
        while(!aVisitar.isEmpty()){
           int temp = aVisitar.pop();
           if(temp == destination){
               return true;
           }
           visitados[temp] = true;
           for(Integer sigueinte: g.getSuccessors(temp)){
               if(!visitados[sigueinte]){
                   aVisitar.add(sigueinte);
               }
           }
        }
        return false;
    }
}
