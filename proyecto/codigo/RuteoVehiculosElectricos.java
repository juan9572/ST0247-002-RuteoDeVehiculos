import java.io.*;

/**
 * Clase encargada de leer los datos y poner a funcionar el programa XD.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @see Pair
 * @see Digraph
 * @see Solution
 * @see Draw
 * @version 1
 */
public class RuteoVehiculosElectricos {
    int n, m, u, breaks;
    double r, speed, Tmax, Smax, st_customer, Q;
    Digraph mapa;
    short tipoEstacion[];
    float pendienteFuncionCarga[];
    String filename;
    static Pair<Float, Float>[] coordenadas;

    double tiempoSolucion;

    /**
     * Método constructor donde se leerán los datos del archivo pasado por parámetro.
     * @param filename nombre del archivo con los datos.
     */
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

    /**
     * Método para ver los atributos.
     * @return una cadena concatenada de los atributos.
     */
    @Override
    public String toString() {
        return "RuteoVehiculosElectricos{" + "r=" + r + ", speed=" + speed + ", Tmax=" + Tmax + ", Smax=" + Smax + ", st_customer=" + st_customer + ", Q=" + Q + ", tiempoSolucion=" + tiempoSolucion + '}';
    }


    /**
     * Método encargado de crear los datos necesarios y pasarlos a la clase que soluciona el problema.
     */
    public void solucionar() {

        //Número clientes, vehículos y capacidad de la batería.
        int numeroClientes = m;
        int numeroVehiculos = 1000;
        double capaVehiculo = Q;

        //Cordenadas deposito.
        float Depot_x = coordenadas[0].first;
        float Depot_y = coordenadas[0].second;

        //Parametro Tabu
        int TABU_Horizon = 10;

        //Inicializar
        Node[] Nodes = new Node[n];
        // Agregando deposito
        Nodes[0] = new Node(Depot_x, Depot_y);
        // Agregando los clientes
        for (int i = 1; i <= numeroClientes; i++) {
            Nodes[i] = new Node(i, 
                    coordenadas[i].first, 
                    coordenadas[i].second
            );
        }
        // Agregando las estaciones
        for (int i = numeroClientes + 1, j = 0; i < n ; i++, j++) {
            Nodes[i] = new Node(i,
                    coordenadas[i].first, 
                    coordenadas[i].second,
                    tipoEstacion[j],
                    this.pendienteFuncionCarga
                    
            );
        }

        // Matriz de distancia entre cada nodo
        double[][] distanceMatrix = new double[n][n];
        double Delta_x, Delta_y;
        for (int i = 0; i <= n-1; i++) {
            for (int j = i + 1; j <= n-1; j++)
            {

                Delta_x = (Nodes[i].Node_X - Nodes[j].Node_X);
                Delta_y = (Nodes[i].Node_Y - Nodes[j].Node_Y);

                // Distancia euclidiana
                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));
                
                distance = Math.round(distance*100.0)/100.0;

                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }


        System.out.println("Intentando resolver el problema de VRP para: "+numeroClientes+
                " Clientes y "+u+" Estaciones"+" con "+capaVehiculo + " Watts de capacidad \n");

        Solution s = new Solution(n-1, numeroVehiculos, capaVehiculo);
        s.GreedySolucion(Nodes, distanceMatrix, r, speed, Tmax);// Solución  greedy
        s.imprimirSolucion("Solución después de Greedy");

        Draw.drawRoutes(s, "GREEDY_Solución");

        s.TabuSearch(TABU_Horizon, distanceMatrix); // Solución  Tabu search

        s.imprimirSolucion("Solución después de Tabu Search");

        Draw.drawRoutes(s, "TABU_Solución");
    }


    /**
     * Método que ejecuta el programa.
     * @param args
     */
    public static void main(String[] args) {
        RuteoVehiculosElectricos problema1 = new RuteoVehiculosElectricos("D:\\Desktop\\Universidad\\Tercer Semestre\\Estructura Datos y Algoritos 2\\Proyecto\\src\\tc2c320s24cf0.txt");
        problema1.solucionar();
    }

}