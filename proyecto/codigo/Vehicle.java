import java.util.ArrayList;

/**
 * Representación de un vehículo.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @version 1
 * @see Node
 */
public class Vehicle
{
    public int VehId; // Número del vehículo.
    public ArrayList<Node> Route = new ArrayList<Node>(); // Ruta tomada por el vehículo actualmente.
    public double capacity; // Capacidad de la batería.
    public double carga; // Cantidad consumida de la batería.
    public int CurLoc; // Donde se encuentra actualmente el vehículo.
    public double tiempoRuta; // Tiempo gastado en la ruta actual.

    /**
     * Contructor del vehículo.
     *
     * @param id número del vehículo.
     * @param cap capacidad de la bateria.
     */
    public Vehicle(int id, double cap)
    {
        this.VehId = id;
        this.capacity = cap;
        this.carga = 0;
        this.CurLoc = 0; //En el deposito inicialmente.
        this.Route.clear();// Como empieza desde el deposito, no tiene ninguna ruta por el momento.
        this.tiempoRuta = 0;
    }

    /**
     * Añade un nodo a la ruta actual.
     *
     * @param Customer nodo a donde se va a mover el vehículo.
     */
    public void AddNode(Node Customer )
    {
        Route.add(Customer);
        this.CurLoc = Customer.NodeId; // Se cambia la posición actual.
    }

    /**
     * Miramos si podemos movernos a el siguiente nodo con la carga actual + lo que se necesita para llegar al otro nodo.
     * @param dem lo que necesitamos para llegar al próximo nodo.
     * @return verdadero si el auto no se descarga en el viaje, falso si el auto no tiene suficiente batería.
     */
    public boolean CheckIfFits(double dem)
    {
        return (this.carga + dem <= capacity);
    }

}