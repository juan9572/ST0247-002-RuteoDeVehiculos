package Lab01.CodigoEnLinea;
/**
 * Esta clase se encarga de hacer el problema de las n reinas con backtraking.
 * @version 1
 * @author Juan Pablo Rincon Usma | Julian Gómez Benitez
 */
public class NReinasBackTracking {
    final int Nreinas = 4; // N reinas.

    /**
     * Método para imprimir el tablero
     * @param tablero Se le pasa el tablero con las reinas.
     */
    void imprimirSolucion(int tablero[][]){
        for (int i = 0; i < Nreinas; i++) {
            for (int j = 0; j < Nreinas; j++)
                System.out.print(" " + tablero[i][j]
                        + " ");
            System.out.println();
        }
    }

    /**
     * Método encargado de revisar que se pueda poner una reina en una posición.
     *
     * @param tablero Se le pasa el tablero con las reinas.
     * @param fila la fila donde se pondrá.
     * @param col la columna donde se pondrá.
     * @return verdadero si se puede poner, falso si es atacada por otra reina.
     */
    boolean esSeguro(int tablero[][], int fila, int col){
        int i, j;

        // Mira la no este ocupada la fila donde se va a poner
        for (i = 0; i < col; i++)
            if (tablero[fila][i] == 1)
                return false;

        // Mira la diagonal superior izquierda
        for (i = fila, j = col; i >= 0 && j >= 0; i--, j--)
            if (tablero[i][j] == 1)
                return false;

        // Mira la diagonal inferior izquierda
        for (i = fila, j = col; j >= 0 && i < Nreinas; i++, j--)
            if (tablero[i][j] == 1)
                return false;

        return true;
    }

    /**
     * Método encargado de solucionar las n reinas, aquí se colocará cada una de las reinas en el tablero.
     * Además determina si el problema se puede solucionar o no.
     * @param tablero se le pasa el tablero donde se pondrán las reinas.
     * @param col se le pasa la columna desde donde se empezarán a poner reinas.
     * @return verdadero si hay una solución para el ejercicio, falso si no.
     */
    boolean solucionNReinas(int tablero[][], int col){
        if (col >= Nreinas)
            return true;
        for (int i = 0; i < Nreinas; i++) {
            if (esSeguro(tablero, i, col)) {
                tablero[i][col] = 1;
                if (solucionNReinas(tablero, col + 1) == true)
                    return true;

                tablero[i][col] = 0;
            }
        }

        return false;
    }

    /**
     * Método auxiliar encargado de realizar el problema de las n reinas.
     * @return verdadero y se imprime la solución que se le dio, en caso de ser falso, solo retorna el falso.
     */
    boolean solveNQ(){
        int tablero[][] = new int[Nreinas][Nreinas];
        for (int i = 0; i < Nreinas; i++) {
            for (int j = 0; j < Nreinas; j++) {
                tablero[i][j] = 0;
            }
        }
        if (solucionNReinas(tablero, 0) == false){
            System.out.print("No hay posibles souluciones");
            return false;
        }
        imprimirSolucion(tablero);
        return true;
    }

    /**
     * Método que corre el programa.
     * @param args
     */
    public static void main(String args[]){
        NReinasBackTracking Queen = new NReinasBackTracking();
        //long time1 = System.currentTimeMillis();
        Queen.solveNQ();
        //long time2 = System.currentTimeMillis();
        //System.out.println(time2-time1);
    }
}
