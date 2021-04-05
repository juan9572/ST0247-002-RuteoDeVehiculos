package Lab03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Esta clase se encarga de leer el archivo txt con las cordenadas y distancias
 * @version 1
 * @author Juan Pablo Rincon Usma | Julian Gómez Benitez
 */
public class LectorDatos
{
    DigraphAL Mapa; // Lugar donde se guardan las posiciones del mapa
    LinkedList<Integer> nodos; // Aqui se almacenan las posiciones donde están los nodos.
    /**
     * Método constructor para poder representar el mapa.
     */
    public LectorDatos(){
        nodos = leerNodos();
        this.Mapa = new DigraphAL(nodos.size(), nodos);
        leerArcos();
    }
    /**
     * Método que se encarga de leer los lugares del archivo txt y devolverlos como una estructura de datos
     * @return Devuelve una LinkedList de lugares, decidimos usar LinkedList porque para insertar elementos es O(1)
     */
    public LinkedList<Integer> leerNodos() {
        try{
            FileReader f  = new FileReader("src\\Lab01\\puentesColgantes.txt");
            BufferedReader b = new BufferedReader(f);
            String line;
            LinkedList<Integer> nodos = new LinkedList<>();
            while((line = b.readLine()) != null) {
                String[] data = line.split(" ");
                if(data[0].equals("Arcos.")){
                    break;
                }
                if(data[0].matches("[0-9]+")){
                    int id = Integer.parseInt(data[0]);
                    nodos.add(id);
                }
            }
            b.close();
            return nodos;
        }catch (IOException e) {
            System.out.println("Asegurece de tener el documento ''puentesColgantes''");
        }
        return null;
    }
    /**
     * Método que se encarga de leer las calles del archivo txt y devolverlos como una estructura de datos
     * @return Devuelve una LinkedList de calles, decidimos usar LinkedList porque para insertar elementos es O(1)
     */
    public void leerArcos(){
        try{
            FileReader f  = new FileReader("src\\Lab01\\puentesColgantes.txt");
            BufferedReader b = new BufferedReader(f);
            String line;
            LinkedList<Integer> distancias = new LinkedList<>();
            boolean yaSePuedeAgregar = false;
            while((line = b.readLine()) != null) {
                String[] data = line.split(" ");
                if(data[0].equals("Arcos.")){
                    yaSePuedeAgregar = true;
                }
                if(data[0].matches("[0-9]+") && yaSePuedeAgregar) {
                    int inicio = Integer.parseInt(data[0]);
                    int llegada = Integer.parseInt(data[1]);
                    int distancia = Integer.parseInt(data[2].replace(".0",""));
                    Mapa.addArc(inicio,llegada,distancia);
                }
            }
            b.close();
        }catch (IOException e) {
            System.out.println("Asegurece de tener el documento ''puentesColgantes''");
        }
        return;
    }

    /**
     * Método encargado de devolver la posición en donde se encuentra un elemento en el grafo
     * así podemos solucionar que los grafos no empiezan desde 0, se busca en que posición fue asignado.
     * @param source es el nodo que se le busca en que posción esta asignado en el grafo.
     * @return la posición donde se encuentra el nodo source en el grafo Mapa.
     */
    public int devolverPos(int source){
        int pos = 0;
        for (int i = 0; i < nodos.size(); i++) {
            if(nodos.get(i) == source){
                pos = i;
                break;
            }
        }
        return pos;
    }

    int costoMin = Integer.MAX_VALUE; // Costo minimo en las recursiones para poder evaluar los caminos
    ArrayList<Integer> caminoGlobal; // Los nodos recorridos para poder llegar al de menor costo

    /**
     * Método encargado de encontrar el camino más corto entre 2 puntos de un grafo usando backtracking.
     * @param grafo el grafo dado como listas de adyacencia.
     * @param source el punto inicial a desde donde se empezará a recorrer.
     * @param destination el punto final b donde se debe llegar.
     * @param costo variable para poder ir contando el costo del camino actual.
     * @param camino es el camino que se esta recorriendo actualmente.
     */
    public void caminoMasCorto(DigraphAL grafo,int source, int destination, int costo, ArrayList<Integer> camino){
        if(costo > costoMin){ // Caso de backtracking.
            return;
        }
        if(source == destination){ // Caso de parada de la recursión.
            camino.add(destination);
            caminoGlobal = camino;
            costoMin = Math.min(costoMin, costo);
            return;
        }
        int pos = devolverPos(source);
        ArrayList<Integer> suce = grafo.getSuccessors(pos);
        camino.add(source);
        for (Integer a:suce){
            int i=0;
            if(a == 0){
                if(camino.contains(10000)){
                    continue;
                }
            }else{
                if(!camino.contains(a)){
                    int costoTemp = costo + grafo.grafo.get(pos).get(i).second;
                    ArrayList<Integer> caminoTemp = new ArrayList<>(camino);
                    caminoMasCorto(grafo,a,destination,costoTemp,caminoTemp);
                }
            }
            i++;
        }
    }
}
