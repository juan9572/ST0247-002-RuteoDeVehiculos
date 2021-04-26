package Taller10;

/**
 * Clase en la cual se implementan los metodos del Taller 10
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon.
 */
public class Taller10{
    /**
     * Metodo que calcula la longitud de la subsecuencia mas larga en comun entre dos cadenas
     * -primer punto-
     *
     * @param x cadena de caracteres
     * @param y cadena de caracteres
     *
     * @return catidad de letras comunes entre ambas cadenas.
     */
    public static int lcs(String x, String y) {
        if(x.equals(y)){
            return x.length();
        }
        if(x.length() == 0 || y.length() == 0){
            return 0;
        }
        int matriz[][] = new int[x.length()+1][y.length()+1];
        for(int i = 0; i <= x.length(); i++) {
            matriz[i][0] = 0;
        }
        for (int j = 0; j <= y.length(); j++) {
            matriz[0][j] = 0;
        }
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                if(x.charAt(i-1) == y.charAt(j-1)){
                    matriz[i][j] = 1 + matriz[i-1][j-1];
                }else{
                    matriz[i][j] = Math.max(matriz[i][j-1],matriz[i-1][j]);
                }
            }
        }
        return matriz[x.length()][y.length()];
    }

    /**
     * Metodo que calcula la longitud de la subsecuencia mas larga en comun entre dos cadenas
     * -segundo punto-
     *
     * @param x cadena de caracteres
     * @param y cadena de caracteres
     *
     * @return los caracteres iguales dentro de las secuencias
     */
    public static String lcsOpcional(String x, String y){
        return lcsOpcional(x,y,x.length(),y.length());
    }

    /**
     * Método auxiliar para calcular la longitud de la subsecuencia mas larga y devolverla como String
     * @param x cadena de caracteres
     * @param y cadena de caracteres
     * @param nX longitud de la cadena x
     * @param nY longitud de la cadena y
     *
     * @return los caracteres iguales dentro de las secuencias
     */
    private static String lcsOpcional(String x, String y, int nX, int nY) {
        if(x.equals(y)){
            return x;
        }
        if(x.length() == 0 || y.length() == 0){
            return "";
        }
        int matriz[][] = new int[nX+1][nY+1];
        for(int i = 0; i <= nX; i++) {
            matriz[i][0] = 0;
        }
        for (int j = 0; j <= nY; j++) {
            matriz[0][j] = 0;
        }
        for (int i = 1; i <= nX; i++) {
            for (int j = 1; j <= nY; j++) {
                if(x.charAt(i-1) == y.charAt(j-1)){
                    matriz[i][j] = 1 + matriz[i-1][j-1];
                }else{
                    matriz[i][j] = Math.max(matriz[i][j-1],matriz[i-1][j]);
                }
            }
        }
        String res = "";
        while (nX != 0 && nY!= 0){
            if (x.charAt(nX-1) == y.charAt(nY-1)){
                res = res + x.charAt(nX-1);
                nX = nX-1;
                nY = nY-1;
            }
            else {
                if (matriz[nX-1][nY] >= matriz[nX][nY-1])
                    nX = nX-1;
                else
                    nY = nY-1;
            }
        }
        return res;
    }
}