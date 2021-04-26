package Taller10;

/**
 * Prueba la implementacion de los metodos en la clase Taller10.
 *
 * Ejecute esta clase para hacerse una idea de si su implementacion de los
 * ejercicios propuestos en el Taller de Clase #10 son correctos.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("Taller10 -> "+ convert(test()));
    }

    static boolean test(){
        String[] wordlist = { "hash", "quantum", "fever", "bench", "long"};
        int[][] answers = { {4,1,0,1,0},{1,7,0,1,1},{0,0,5,1,0},{1,1,1,5,1}};
        for(int i = 0; i < answers.length; i++){
            for (int j = 0; j < answers.length; j++) {
                String a = Taller10.lcsOpcional(wordlist[i],wordlist[j]);
                int r = Taller10.lcs(wordlist[i], wordlist[j]);
                if (r != answers[i][j]){
                    return false;
                }
                System.out.println("Palabra 1: " + wordlist[i]);
                System.out.println("Palabra 2: " + wordlist[j]);
                if(r == 0){
                    System.out.println("Letras iguales: " + r + " -> null");
                }else{
                    System.out.println("Letras iguales: " + r + " -> " + a);
                }
            }
        }
        return true;
    }

    static String convert(boolean b) {
        return b ? "correcta" : "incorrecta";
    }

}
