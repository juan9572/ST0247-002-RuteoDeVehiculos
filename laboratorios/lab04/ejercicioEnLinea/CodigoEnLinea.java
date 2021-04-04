package Lab2;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Clase encargada de resolver el problema de la flota de buses para obtener la cantidad minima de horas
 * para que un menor gasto en las horas extras.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon.
 */
public class CodigoEnLinea {
    int n; // Cantidad de n conductores de bus, n rutas de bus en la mañana y n rutas de bus en la tarde.
    int d;// La duración total de su ruta por día.
    int r;// Tarifa de r pesos por hora extra.
    int [] duracionRutasMañana; // Duración de las rutas de la mañana.
    int [] duracionRutasTarde; // Duración de las rutas de la tarde.

    /**
     * Método constructor encargado de leer los datos del archivo, cargarlo a las variables y mandarlo
     * al método para poder calcular el menor tiempo para las horas extra.
     * @param nombreArchivo Es la dirección donde se encuentra ekl archivo a leer.
     */
    public CodigoEnLinea(String nombreArchivo){
        BufferedReader lector;
        String linea;
        String lineaPartida[];
        try {
            lector = new BufferedReader(new FileReader(nombreArchivo));
            linea = lector.readLine();
            do{
                lineaPartida = linea.split(" "); // Se lee la primera línea que contiene las variables n,d,r
                n = Integer.parseInt(lineaPartida[0]); // Se agrega la n.
                d = Integer.parseInt(lineaPartida[1]); // Se agrega la d.
                r = Integer.parseInt(lineaPartida[2]); // Se agrega la r.
                linea = lector.readLine(); // Se lee la segunda que contiene las duraciones de la ruta de la mañana.
                lineaPartida = linea.split(" ");
                duracionRutasMañana = new int[lineaPartida.length];
                for (int i = 0; i < lineaPartida.length; i++) {
                    duracionRutasMañana[i] = Integer.parseInt(lineaPartida[i]); // Se agrega cada duración.
                }
                linea = lector.readLine();
                lineaPartida = linea.split(" "); // Se lee la tercera que contiene las duraciones de la ruta de la tarde.
                duracionRutasTarde = new int[lineaPartida.length];
                for (int i = 0; i < lineaPartida.length; i++) {
                    duracionRutasTarde[i] = Integer.parseInt(lineaPartida[i]); // Se agrega cada duración.
                }
                minimoValorPosible(); // Con las variables cargadas, se procede a calcular el valor mínimo en horas.
                linea = lector.readLine();
            }while (!linea.equals("0 0 0"));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Función auxiliar para calcular el costo minimo de las horas extra, en este método
     * se crean algunas variables auxiliares para pasarle al método que se encargará de calcularlo.
     */
    public void minimoValorPosible(){
        boolean visitadosM[] = new boolean[n];
        boolean visitadosT[] = new boolean[n];
        int cantidadHoras = 0;
        minimoValorPosible(cantidadHoras,visitadosM,visitadosT);
    }

    /**
     * Método encargado de imprimir la cantidad mínima generada por las horas extras,
     * para ello se busca la cantidad mínima de horas de cada ruta y se le va acumulando a un conductor,
     * después se le resta la duración total de la ruta y se bonifican las horas extra.
     *
     * @param cantidadHoras es la suma de horas de cada uno de los conductores.
     * @param visitadosM arreglo para saber si ya se ha visitado una de las rutas de la mañana, para evitar repetirlas.
     * @param visitadosT arreglo para saber si ya se ha visitado una de las rutas de la tarde, para evitar repetirlas.
     */
    public void minimoValorPosible(int cantidadHoras, boolean visitadosM[], boolean visitadosT[]){
        int conductores[] = new int[n]; // Se crean los conductores.
        while(haySinVisitar(visitadosM).first){
            int min = dondeEstaElMenor(duracionRutasMañana,visitadosM); // Se busca el valor menor de tiempo.
            int index = haySinVisitar(visitadosM).second;// Se busca el indice donde no se ha visitado.
            conductores[index] += min;
            visitadosM[index] = true;
        }
        conductores = invertirArreglo(conductores); // Se invierte el arreglo para distribuir mejor las horas.
        while(haySinVisitar(visitadosT).first){ // Se  hace lo mismo para las rutas de la tarde.
            int min = dondeEstaElMenor(duracionRutasTarde,visitadosT);
            int index = haySinVisitar(visitadosT).second;
            conductores[index] += min;
            visitadosT[index] = true;
        }
        for(int i = 0; i < conductores.length; i++){// Se recorre las horas de cada conductor.
            int candidato = (conductores[i]-d); // Se le resta la restricción de la duración total de su ruta por día.
            if(candidato >= 0){// Si es mayor es porque trabajo horas extra.
                cantidadHoras += candidato*r; // Se multiplica las horas extra por la tarifa de horas extra.
            }else{
                continue;
            }
        }
        System.out.println(cantidadHoras); // Se imprime el valor del costo de las horas extra.
    }

    /**
     *  Método encargado de invertir un arreglo.
     *
     * @param aInvertir se le da el arreglo que se quiere invertir.
     * @return se devuelve el arreglo invertido.
     */
    public int[] invertirArreglo(int [] aInvertir){
        int aux = 0;
        for (int i=0; i<aInvertir.length/2; i++) {
            aux = aInvertir[i];
            aInvertir[i] = aInvertir[aInvertir.length-1-i]; // El primero será el último.
            aInvertir[aInvertir.length-1-i] = aux; // El último será el primero.
        }
        return aInvertir;
    }

    /**
     * Método encargado de encontrar el valor mínimo de horas dentro de un ruta sin repetir valores.
     * @param duraciones el arreglo con las duraciones de las rutas.
     * @param visitados las rutas que ya se han visitado.
     * @return el valor mínimo que encuentre dentro de la ruta sin repetirlos.
     */
    public int dondeEstaElMenor(int [] duraciones, boolean[] visitados){
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < duraciones.length; i++){
            if(duraciones[i] < min && visitados[i] == false){
               min = duraciones[i];
            }
        }
        return min;
    }

    /**
     * Método encargado de buscar si hay rutas que no esten visitadas aún.
     *
     * @param visitados se le pasa las rutas.
     * @return una pareja donde el primero es un booleano que representa que aún faltan rutas por visitar, el segundo devuelve el indice donde la ruta no ha sido visitada.
     */
    public Pair<Boolean,Integer> haySinVisitar(boolean visitados[]){
        for(int i = 0; i < visitados.length; i++){
                if(visitados[i] == true){
                    continue;
                }else{
                    return new Pair<>(true,i);
                }
            }
        return new Pair<>(false,-1);
    }


    public static void main(String[] args) {
        CodigoEnLinea cel = new CodigoEnLinea("src\\Lab2\\Entrada.txt");
    }
}

/**
 * Contenedor para dos objetos de cualquier tipo. Basada en la implementacion de
 * Android.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon
 */
class Pair<F, S> {
    public final F first;
    public final S second;

    /**
     * Constructor de pares.
     *
     * @param first  primer objeto del par.
     * @param second segundo objeto del par.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
