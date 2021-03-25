/**
 * Clase que representa nodos cliente y nodos deposito
 * @author ljpalaciom
 */
class Node
{
    public int NodeId;
    public float Node_X ,Node_Y; //Node Coordinates
    public double demand; //Node Demand if Customer
    public boolean IsRouted;
    private boolean IsDepot;
    boolean IsStation; //True if it Depot Node
    int tipo;

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
        this.IsRouted = false;
        this.IsStation = false;
        this.IsDepot = false;
    }
    public Node(int id ,float x, float y, int tipo) //Cunstructor for Stations
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.demand = calcularTiempoRecarga();
        this.IsStation = true;
        this.tipo = tipo;
    }
    public double caldemand(int currPos, double[][] distanceMatrix, double r, double speed){
        double distancia = distanceMatrix[currPos][this.NodeId];
        double tiempo = distancia/speed;
        double consumo = r * distancia;

        return consumo;
    } 
    public static double calcularTiempoRecarga(){
        
        }
}