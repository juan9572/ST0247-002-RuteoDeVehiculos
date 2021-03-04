package Lab01;

public class Main {

    public static void main(String[] args) {
        LectorDatos ld = new LectorDatos();
        int [][] matriz = ld.convertirAMatriz(ld.Mapa);
        boolean[] v = new boolean[matriz.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = false;
        }
        v[0] = true;
        int ans = Integer.MAX_VALUE;
        ans = ld.tsp(matriz,v,0,matriz.length,1,0,ans);
        System.out.println(ans);
    }

}
