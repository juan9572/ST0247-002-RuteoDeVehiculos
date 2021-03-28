/**
 * Clase que representa nodos cliente y nodos deposito
 * @author ljpalaciom
 */
public class Node
{
    public int NodeId;
    public float Node_X ,Node_Y; //Node Coordinates
    public double demand; //Node Demand if Customer
    public boolean IsRouted;
    public boolean IsDepot;
    boolean IsStation; //True if it Depot Node
    int tipo;
    float pendienteFuncionCarga[];

    public Node(float depot_x,float depot_y) //Cunstructor for depot
    {
        this.NodeId = 0;
        this.Node_X = depot_x;
        this.Node_Y = depot_y;
        this.IsDepot = true;
        this.IsStation = false;
    }

    public Node(int id ,float x, float y) //Cunstructor for Customers
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.demand =0;
        //this.IsRouted = false;
        this.IsStation = false;
        this.IsDepot = false;
    }
    public Node(int id ,float x, float y, int tipo,float pendienteFuncionCarga[]) //Cunstructor for Stations
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.IsRouted = false;
        this.IsStation = true;
        this.tipo = tipo;
        this.pendienteFuncionCarga = pendienteFuncionCarga;
    }
    public Pair<Double,Double> caldemand(int currPos, double[][] distanceMatrix, double r, double speed){
        double distancia = distanceMatrix[currPos][NodeId];
        double tiempo = distancia/speed;
        double consumo = r * distancia;
        this.demand = consumo;
        return new Pair<Double, Double>(consumo,tiempo);
    }
    public double calcularTiempoRecarga(double cantidadH){
        return pendienteFuncionCarga[tipo]*cantidadH;
    }
}