/**
 *
 * @author ljpalaciom
 */
import java.io.*;
import java.util.*;


public class RuteoVehiculosElectricos {
    int n, m, u, breaks;
    double r, speed, Tmax, Smax, st_customer, Q;
    Digraph mapa;
    short tipoEstacion[];
    float pendienteFuncionCarga[];
    String filename;
    static Pair<Float, Float>[] coordenadas;

    double tiempoSolucion;

    public RuteoVehiculosElectricos(String filename) {
        this.filename = filename;
        BufferedReader lector;
        String linea;
        String lineaPartida[];
        try {
            lector = new BufferedReader(new FileReader(filename));
            double[] valores = new double[10];
            for (int i = 0; i < 10; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                valores[i] = Float.parseFloat(lineaPartida[2]);
            }

            n = (int) valores[0];
            m = (int) valores[1];
            u = (int) valores[2];
            breaks = (int) valores[3];
            r = valores[4];
            speed = valores[5];
            Tmax = valores[6];
            Smax = valores[7];
            st_customer = valores[8];
            Q = valores[9];

            lector.readLine();
            lector.readLine();
            lector.readLine();

            coordenadas = new Pair[n];
            mapa = new DigraphAM(n);
            for (int i = 0; i <= m; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
            }
            tipoEstacion = new short[u];
            for (int i = 0; i < u; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
                tipoEstacion[i] = Short.parseShort(lineaPartida[5]);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mapa.addArc(i, j, Math.sqrt(
                            Math.pow(coordenadas[i].first - coordenadas[j].first,
                                    2)
                            + Math.pow(coordenadas[i].second - coordenadas[j].second, 2)
                    )
                    );
                }
            }

            pendienteFuncionCarga = new float[3];
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]);
            }
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]) / pendienteFuncionCarga[i];
            }
            tiempoSolucion = Double.MAX_VALUE;
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public String toString() {
        return "RuteoVehiculosElectricos{" + "r=" + r + ", speed=" + speed + ", Tmax=" + Tmax + ", Smax=" + Smax + ", st_customer=" + st_customer + ", Q=" + Q + ", tiempoSolucion=" + tiempoSolucion + '}';
    }

    public void exportarPuntosCSV() {
        try {
            PrintStream escribirCoordenadas = new PrintStream(new File("Coordenadas.csv"));
            escribirCoordenadas.println("X,Y");
            for (Pair<Float, Float> coordenada : coordenadas) {
                escribirCoordenadas.println(coordenada.first + "," + coordenada.second);
            }
            escribirCoordenadas.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void exportarRutasCSV(ArrayList<ArrayList<Integer>> rutas) {
        try {
            int numRuta = 0;
            for (ArrayList<Integer> ruta : rutas) {
                PrintStream escribirCoordenadas = new PrintStream(new File("ruta" + numRuta + ".csv"));
                escribirCoordenadas.println("X,Y");
                for (Integer verticeActual : ruta) {
                    escribirCoordenadas.println(coordenadas[verticeActual].first + "," + coordenadas[verticeActual].second);
                }
                escribirCoordenadas.close();
                numRuta++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void solucionar() {
        Random ran = new Random(151190);

        //Problem Parameters
        int NoOfCustomers = m;
        int NoOfVehicles = 5;
        double VehicleCap = Q;

        //Depot Coordinates
        float Depot_x = coordenadas[0].first;
        float Depot_y = coordenadas[0].second;

        //Tabu Parameter
        int TABU_Horizon = 10;

        //Initialise
        //Create Random Customers
        Node[] Nodes = new Node[n];
        Node depot = new Node(Depot_x, Depot_y);

        Nodes[0] = depot;
        for (int i = 1; i <= NoOfCustomers; i++) {
            Nodes[i] = new Node(i, 
                    coordenadas[i].first, 
                    coordenadas[i].second
            );
        }
        for (int i = NoOfCustomers + 1, j = 0; i < n ; i++, j++) {
            Nodes[i] = new Node(i,
                    coordenadas[i].first, 
                    coordenadas[i].second,
                    tipoEstacion[j]
                    
            );
        }    

        double[][] distanceMatrix = new double[n][n];
        double Delta_x, Delta_y;
        for (int i = 0; i <= n-1; i++) {
            for (int j = i + 1; j <= n-1; j++) //The table is summetric to the first diagonal
            {                                      //Use this to compute distances in O(n/2)

                Delta_x = (Nodes[i].Node_X - Nodes[j].Node_X);
                Delta_y = (Nodes[i].Node_Y - Nodes[j].Node_Y);

                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));
                
                distance = Math.round(distance*100.0)/100.0; //Distance in double

                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }
        int printMatrix = 1; //If we want to print diastance matrix

        if (printMatrix == 1){
            for (int i = 0; i <= n-1; i++) {
                for (int j = 0; j <= n-1; j++) {
                    System.out.print(distanceMatrix[i][j] + "  ");
                }
                System.out.println();
            }
        }

        //Compute the greedy Solution
        System.out.println("Attempting to resolve Vehicle Routing Problem (VRP) for "+NoOfCustomers+
                " Customers and "+u+" Stations"+" with "+VehicleCap + " Watts of capacity \n");

        Solution s = new Solution(n-1, NoOfVehicles, VehicleCap);
        s.GreedySolution(Nodes, distanceMatrix, r, speed, Tmax, pendienteFuncionCarga);
        s.SolutionPrint("Solution After Tabu Search");

        draw.drawRoutes(s, "GREEDY_Solution");
        
        s.GreedySolution(Nodes, distanceMatrix, r, speed, Tmax, pendienteFuncionCarga);

        s.TabuSearch(TABU_Horizon, distanceMatrix);

        s.SolutionPrint("Solution After Tabu Search");

        draw.drawRoutes(s, "TABU_Solution");
    }

    /**
     * Este metodo es un test para verificar que la solucion es correcta. 
     * @param rutas Es un contenedor de rutas representadas por un arraylist de parejas donde el primer elemento indica el nodo
     * y el segundo elemento el tiempo que se quedo en ese nodo
     * @return Verdadero si el tiempo de solucion expresado concuerda y si la bateria nunca esta por debajo de 0.
     */
    public boolean comprobarSolucion( ArrayList<ArrayList<Pair<Integer, Integer>>> rutas){
       return false;
      }
    
   

    public static void main(String[] args) {
        //File f = new File("prueba.txt");
        //ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        RuteoVehiculosElectricos problema1 = new RuteoVehiculosElectricos("prueba.txt");
        problema1.solucionar();
        problema1.exportarPuntosCSV();

    }

}