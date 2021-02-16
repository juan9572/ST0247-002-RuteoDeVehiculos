
import java.util.ArrayList;
import java.util.Arrays;


public class DigraphAM extends Graph {

    //Grafo con matriz
    public int[][] mat;
    public int size;

    public DigraphAM(int size){
        super(size);
        this.size = size;
        mat = new int[size][size];

    }

    void imprimir() {
        for (int[] ints : mat) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public void addArc(int source, int destination, int weight){
        if (!(source == 0 && destination == 0)) {
            mat[source][destination] = weight;
        }
    }


    public int getWeight(int source, int destination){
        return mat[source][destination];
    }

    public ArrayList<Integer> getSuccessors(int vertex)
    {
        ArrayList<Integer> np= new ArrayList<>();
        for (int i = 1; i < mat.length; i++) {
            if (mat[vertex][i] != 0) {
                np.add(i);
            }
        }
        return np;
    }
}