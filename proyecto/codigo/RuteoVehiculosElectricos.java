/**
 * version 1.0
 * @author
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class RuteoVehiculosElectricos {
    /**
     * Donde numeroTotalNodos es n,
     * numeroClientes es m,
     * numeroEstacionesCarga es u
     * puntosSoporteCargaBateria es breaks,
     * tasaConsumoWatts es r,
     * velocidad es speed,
     * tiempoMaxDeRuta es Tmax,
     * tiempoMaxCarga es Smax,
     * tiempoVisita es St_customer,
     * capacidadBateria es Q.
     */
    int numeroTotalNodos, numeroClientes, numeroEstacionesCarga, puntosSoporteCargaBateria;
    double tasaConsumoWatts, velocidad, tiempoMaxDeRuta, tiempoMaxCarga, tiempoVisita, capacidadBateria;
    Digraph mapa;
    short tipoEstacion[];
    float pendienteFuncionCarga[];
    String filename;
    Pair<Float, Float>[] coordenadas;

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

            numeroTotalNodos = (int) valores[0];
            numeroClientes = (int) valores[1];
            numeroEstacionesCarga = (int) valores[2];
            puntosSoporteCargaBateria = (int) valores[3];
            tasaConsumoWatts = valores[4];
            velocidad = valores[5];
            tiempoMaxDeRuta = valores[6];
            tiempoMaxCarga = valores[7];
            tiempoVisita = valores[8];
            capacidadBateria = valores[9];

            lector.readLine();
            lector.readLine();
            lector.readLine();

            coordenadas = new Pair[numeroTotalNodos];
            mapa = new DigraphAM(numeroTotalNodos,velocidad);
            for (int i = 0; i <= numeroClientes; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
            }
            tipoEstacion = new short[numeroEstacionesCarga];
            for (int i = 0; i < numeroEstacionesCarga; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
                tipoEstacion[i] = Short.parseShort(lineaPartida[5]);
            }

            for (int i = 0; i < numeroTotalNodos; i++) {
                for (int j = 0; j < numeroTotalNodos; j++) {
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
        return "RuteoVehiculosElectricos{" + "r=" + tasaConsumoWatts + ", speed=" + velocidad + ", Tmax=" + tiempoMaxDeRuta + ", Smax=" + tiempoMaxCarga + ", st_customer=" + tiempoVisita + ", Q=" + capacidadBateria + ", tiempoSolucion=" + tiempoSolucion + '}';
    }

    public void exportarPuntosCSV() {
        try {
            PrintStream escribirCoordenadas = new PrintStream(new File("ArchivosGenerados\\Coordenadas.csv"));
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
                PrintStream escribirCoordenadas = new PrintStream(new File("ArchivosGenerados\\ruta" + numRuta + ".csv"));
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

    }

    /**
     * Este metodo es un test para verificar que la solucion es correcta. 
     * @param rutas Es un contenedor de rutas representadas por un arraylist de parejas donde el primer elemento indica el nodo
     * y el segundo elemento el tiempo que se quedo en ese nodo
     * @return Verdadero si el tiempo de solucion expresado concuerda y si la bateria nunca esta por debajo de 0.
     */
    public boolean comprobarSolucion( ArrayList<ArrayList<Pair<Integer, Integer>>> rutas){

    }


    public static void main(String[] args) {
        RuteoVehiculosElectricos problema1 = new RuteoVehiculosElectricos("D:\\Desktop\\Universidad\\Tercer Semestre\\Estructura Datos y Algoritos 2\\Proyecto\\src\\prueba.txt");
        BestFirstSearch bfs = new BestFirstSearch(problema1.numeroTotalNodos);
        problema1.solucionar();
        problema1.exportarPuntosCSV();
    }

}
