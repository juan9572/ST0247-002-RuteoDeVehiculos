package Lab02;

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
    /**
     * Método constructor para poder representar el mapa.
     */
    public LectorDatos(){
        LinkedList<Integer> nodos = leerNodos();
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

    public int[][] convertirAMatriz(DigraphAL grafo){
        int[][] matriz = new int[grafo.size()][grafo.size()];
        int postion = 0;
        for(int i = 0; i < grafo.size(); i++){
            while(grafo.grafo.get(i).size()-1 != 0){
                Pair <Integer, Integer> parejita = grafo.grafo.get(i).poll();
                for(int j = 0; j < grafo.grafo.size(); j++){
                    if(grafo.grafo.get(j).contains(new Pair(parejita.first,0))){
                        postion = j;
                        break;
                    }
                }
                if(grafo.grafo.get(i).size() == 0){
                    matriz[i][i] = 0;
                    grafo.grafo.get(i).add(new Pair(parejita.first,0));
                }else{
                    matriz[i][postion] = parejita.second;
                }
            }
        }
        return matriz;
    }

    public int tsp(int[][] graph, boolean[] v, int currPos, int n, int count, int cost, int ans){
        if (count == n && graph[currPos][0] > 0){
            ans = Math.min(ans, cost + graph[currPos][0]);
            return ans;
        }
        for (int i = 0; i < n; i++){
            if (v[i] == false && graph[currPos][i] > 0){
                v[i] = true;
                ans = tsp(graph, v, i, n, count + 1, cost + graph[currPos][i], ans);
                v[i] = false;
            }
        }
        return ans;
    }
}
