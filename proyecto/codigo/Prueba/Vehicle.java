import java.util.*;
class Vehicle
{
    public int VehId;
    public ArrayList<Node> Route = new ArrayList<Node>();
    public double capacity;
    public double carga;
    public int CurLoc;
    public boolean Closed;

    public Vehicle(int id, double cap)
    {
        this.VehId = id;
        this.capacity = cap;
        this.carga = 0;
        this.CurLoc = 0; //In depot Initially
        this.Closed = false;
        this.Route.clear();
    }

    public void AddNode(Node Customer )//Add Customer to Vehicle Route
    {
        Route.add(Customer);
        this.carga +=  Customer.demand;
        this.CurLoc = Customer.NodeId;
    }

    public boolean CheckIfFits(double dem) //Check if we have Capacity Violation
    {
        carga += dem;
        return (carga <= capacity);
    }

}