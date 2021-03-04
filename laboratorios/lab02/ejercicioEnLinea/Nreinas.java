package Lab01;

import java.util.*;
/**
 * Clase en la cual se implementan los metodos del Taller 3
 *
 * @author Juan Pablo Rincon Usma, Julian Gomez Benitez
 */
public class Nreinas {
    /**
     * Metodo que verifica si es posible poner las reinas hasta la columna c
     *
     * @param  c hasta esta columna revisa
     * @param  tablero el tablero
     * @return true si es posible, false de lo contrario
     */
    private static boolean puedoPonerReina(int c, int[] tablero) {
        for(int i = 0; i< c; i++){
            if(tablero[i]==tablero[c]){
                return false;
            }else if(Math.abs(tablero[i]-tablero[c])==Math.abs(i-c)){
                return false;
            }
        }
        return true;
    }
    /**
     * Metodo auxiliar para llamar el metodo posterior
     *
     * @param  n numero de reinas
     * @return numero de soluciones
     */
    public static int nReinas(int n, String[][] big) {
        if(n<4) return 0;
        return nReinas(0, n, new int[n], big);
    }
    /**
     * Metodo devuelve el numero de soluciones que tiene el problema
     *
     * @param  c columna
     * @param  n numero de reinas
     * @return numero de soluciones
     */
    private static int nReinas(int c, int n, int[] tablero, String[][] big) {
        int result = 0;
        if(c == n)return 1;
        for(int i = 0;i<n; i++){
            if(big[i][c].equals("*")){
                continue;
            }else{
                tablero[c]=i;
                if(puedoPonerReina(c,tablero)){
                    result = result +nReinas(c+1, n,tablero, big);
                }
            }
        }
        return result;
    }
    public static void imprimirTablero(int[] tablero) {
        int n = tablero.length;
        System.out.print("    ");
        for (int i = 0; i < n; ++i)
            System.out.print(i + " ");
        System.out.println("\n");
        for (int i = 0; i < n; ++i) {
            System.out.print(i + "   ");
            for (int j = 0; j < n; ++j)
                System.out.print((tablero[i] == j ? "Q" : "#") + " ");
            System.out.println();
        }
        System.out.println();
    }
    public static void main(String[] args){
        String[][] primero = new String[8][8];
        for(int i = 0; i<8; i++){
            for(int z = 0; z<8; z++){
                primero[i][z]=".";
            }
        }
        System.out.println("Case 1: "+nReinas(8,primero));
        String[][] segundo = new String[4][4];
        for(int y = 0; y<4; y++){
            for(int w = 0; w<4; w++){
                segundo[y][w]=".";
            }
        }
        segundo[0][1] = "*";
        System.out.println("Case 2: "+nReinas(4,segundo));
    }
}