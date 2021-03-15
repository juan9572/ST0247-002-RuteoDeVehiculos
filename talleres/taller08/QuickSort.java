package Taller08;

/**
 * Clase para representar el QuickSort.
 *  @author Julian Gomez Benitez, Juan Pablo Rincon
 */
public class QuickSort
{
    /**
     * Esta función toma el último elemento como pivote,
     * coloca el elemento pivote en su posición correcta
     * posición en la matriz ordenada, y coloca todos los elementos
     * menores (más pequeños que el pivote) a la izquierda del
     * pivote y todos los elementos mayores a la derecha del pivote.
     * @param arreglo el arreglo de n posciones.
     * @param low posición más baja donde empieza el index.
     * @param high posición más alta donde termina el index.
     * @return el index donde se debe hacer la partición.
     */
    public int partition(int arreglo[], int low, int high) {
        int pivot = arreglo[high];
        int i = (low-1); // index del elemento más pequeño
         for (int j=low; j<high; j++)
        {
            //Si el elemento acutal es más pequeño o igual que el pivote
            if (arreglo[j] <= pivot)
            {
                i++;
                // se hace el swap en el arreglo[i] y arreglo[j]
                int temp = arreglo[i];
                arreglo[i] = arreglo[j];
                arreglo[j] = temp;
            }
        }

        //Se hace el swap en el arreglo[i+1] y arreglo[high] (o el pivote).
        int temp = arreglo[i+1];
        arreglo[i+1] = arreglo[high];
        arreglo[high] = temp;
        return i+1;
    }

    /**
     * Método auxilira para el quicksort
     * @param arreglo
     */
    public void sort(int [] arreglo){
        sort(arreglo,0,arreglo.length-1);
    }

    /**
     * Método encargado de ordenar el arreglo usando quicksort
     * @param arr arreglo de n posiciones.
     * @param low posición más baja donde empieza el index.
     * @param high posición más alta donde termina el index.
     */
    public void sort(int arr[], int low, int high) {
        if (low < high){
            int pi = partition(arr, low, high); // Partición.
            sort(arr, low, pi-1);// Ordenar los elementos recursivos antes del pivote.
            sort(arr, pi+1, high);// Ordenar el pivote y los elementos después de este.
        }
    }

    /**
     * Funcion para iniciar el programa.
     * @param args
     */
    public static void main(String args[]) {
        int n = 100000; // Poner aqui el valor de n con el que quieran probar el codigo
        int arr[] = new int[n];
        for(int i = 0; i < arr.length; i++) {
            int m = (int) (Math.random() * 100000 + 1);
            arr[i] = m;
        }
        long inicio = System.currentTimeMillis();
        QuickSort ob = new QuickSort();
        ob.sort(arr);
        long terminar = System.currentTimeMillis();
        long total = terminar - inicio;
        System.out.println("Para longitud " + arr.length + " tiempo: " + total + " milisegundos");
    }
}