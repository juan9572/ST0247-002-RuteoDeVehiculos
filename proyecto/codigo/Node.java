/**
 * Clase para representar los 3 tipos de nodos que se presentan en el mapa.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @see Pair
 * @version 1
 */
public class Node
{
    public int NodeId; // Número de nodo
    public float Node_X ,Node_Y; //Cordenadas del nodo.
    public double demand; //Consumo de watts/hora por kilometro que tiene viajar hacía otro nodo.
    public boolean IsRouted; // Si ya se ha visitado el nodo.
    public boolean IsDepot; // Si es el deposito.
    boolean IsStation; //Si es una estación de carga.
    int tipo; // Tipo de estación.
    float pendienteFuncionCarga[]; // La pendiente de la carga de cada estación.

    /**
     * Constructor para un deposito.
     * @param depot_x cordenada x para el deposito
     * @param depot_y cordenada y para el deposito
     */
    public Node(float depot_x,float depot_y)
    {
        this.NodeId = 0;
        this.Node_X = depot_x;
        this.Node_Y = depot_y;
        this.IsDepot = true;
        this.IsStation = false;
    }

    /**
     * Constructor para un cliente
     * @param id número del nodo
     * @param x cordenada x
     * @param y cordenada y
     */
    public Node(int id ,float x, float y)
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.demand =0;
        this.IsRouted = false;
        this.IsStation = false;
        this.IsDepot = false;
    }

    /**
     * Constructor para una Estación
     * @param id número del nodo
     * @param x cordenada x
     * @param y cordenada y
     * @param tipo tipo de estación
     * @param pendienteFuncionCarga La pendiente de carga de esta estación.
     */
    public Node(int id ,float x, float y, int tipo,float pendienteFuncionCarga[])
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.IsRouted = false;
        this.IsStation = true;
        this.tipo = tipo;
        this.pendienteFuncionCarga = pendienteFuncionCarga;
    }

    /**
     * Método para calcular la demanda que le lleva a un camión moverse hasta el próximo nodo.
     *
     * @param currPos posición actual del camión
     * @param distanceMatrix matriz con las ditancias de cada nodo que hay en el mapa.
     * @param r tasa de consumo de watts/hora * kilometro
     * @param speed velocidad del vehículo
     * @return pareja de doubles donde el primer elemento representa el consumo watts/hora * kilometro, mientras que el segundo
     * representa el tiempo gastado yendo hasta el nodo.
     */
    public Pair<Double,Double> caldemand(int currPos, double[][] distanceMatrix, double r, double speed){
        double distancia = distanceMatrix[currPos][NodeId];
        double tiempo = distancia/speed;
        double consumo = r * distancia;
        this.demand = consumo;
        return new Pair<Double, Double>(consumo,tiempo);
    }

    /**
     * Método encargado de representar la función de carga donde se calcula cuanta batería se cargará apartir del tiempo
     * tomado en la estación.
     *                                                                                                     Y2-Y1
     * Este método se diseño usando la formula para sacar una función apartir de dos puntos de esta:Y-Y1 = ----- * (X-X1)
     *                                                                                                     X2-X1
     * @param cantidadH Horas que el camión estará en la estación.
     * @return la cantidad de batería que cargará.
     */
    public double calcularTiempoRecarga(double cantidadH){
        return pendienteFuncionCarga[tipo]*cantidadH;
    }
}