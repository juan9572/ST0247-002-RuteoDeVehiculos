import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Esta clase se encarga de leer el archivo txt con las cordenadas y distancias
 * @version 1
 * @author Juan Pablo Rincon Usma | Julian Gómez Benitez
 */
public class LectorDatos
{
    Digraph Mapa; // Lugar donde se guardan las posiciones del mapa

    /**
     * Método constructor para poder representar el mapa.
     */
    public LectorDatos(){
        LinkedList<Integer> nodos = leerNodos();
        this.Mapa = new DigraphAL(nodos.size());
        leerArcos();
    } 
    /**
     * Método que se encarga de leer los lugares del archivo txt y devolverlos como una estructura de datos
     * @return Devuelve una LinkedList de lugares, decidimos usar LinkedList porque para insertar elementos es O(1)
     */
    public LinkedList<Integer> leerNodos() {
        try{
            FileReader f  = new FileReader("puentesColgantes.txt");
            BufferedReader b = new BufferedReader(f);
            String line;
            LinkedList<Integer> nodos = new LinkedList<>();
            int lineCount = 0;
            while((line = b.readLine()) != null) {
                if(lineCount != 0) {
                    String[] data = line.split(" ");
                    int id = Integer.parseInt(data[0]);
                    nodos.add(id);
                }
                lineCount++;
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
            FileReader f  = new FileReader("puentesColgantes.txt");
            BufferedReader b = new BufferedReader(f);
            String line;
            LinkedList<Integer> distancias = new LinkedList<>();
            int lineCount = 0;
            while((line = b.readLine()) != null) {
                if(lineCount != 0) {
                    String[] data = line.split(" ");
                    int inicio = Integer.parseInt(data[0]);
                    int llegada = Integer.parseInt(data[1]);
                    int distancia = Integer.parseInt(data[2]);
                    Mapa.addArc(inicio,llegada,distancia);
                }
                lineCount++;
            }
            b.close();
        }catch (IOException e) {
            System.out.println("Asegurece de tener el documento ''puentesColgantes''");
        }
        return;
    }

    public int[][] convertirAMatriz(DigraphAL grafo){
      int[][] matriz = new int[grafo.size()][grafo.size()];
      for(int i = 0; i < grafo.size(); i++){
        while(grafo.grafo.get(i).size() != 0){
          Pair <Integer, Integer> parejita= grafo.grafo.get(i).poll();
          matriz [i][parejita.first] = parejita.second;
        }
      }
      return matriz;
    }
    
  //  public int tsp(){
//
    //}
}
