import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;

/**
 * Clase encargada de encontrar las rutas para los vehículos.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @see Vehicle
 * @see Node
 * @version 1
 */
public class Solution
{
    int numeroVehiculos; // Número de vehículos.
    int numeroClientes; // Número de clientes.
    Vehicle[] Vehiculos; // Vehículos para la solución.
    double Costo; // Costo total.

    //Tabu Variables
    public Vehicle[] VehiculosParaLaMejorSolucion;
    double MejorSolucionCosto; // La mejor solución del tabu.

    public ArrayList<Double> SolucionPasada; // Las posibles soluciones que dio el tabu.

    /**
     * Método constructor encargado de inicializar los atributos del problema.
     * @param ClienNum números de clientes.
     * @param VechNum números de vehículos.
     * @param VechCap capacidad de la batería del vehículo.
     */
    public Solution(int ClienNum, int VechNum , double VechCap)
    {
        this.numeroVehiculos = VechNum;
        this.numeroClientes = ClienNum;
        this.Costo = 0;
        Vehiculos = new Vehicle[numeroVehiculos];
        VehiculosParaLaMejorSolucion =  new Vehicle[numeroVehiculos];
        SolucionPasada = new ArrayList<>();

        for (int i = 0; i < numeroVehiculos; i++)
        {
            Vehiculos[i] = new Vehicle(i+1,VechCap);
            VehiculosParaLaMejorSolucion[i] = new Vehicle(i+1,VechCap);
        }
    }

    /**
     * Método encargado de comprobar que el nodo no ha sido visitado.
     * @param Nodes nodos del mapa.
     * @return verdadero si existe uno que no este visitado, falso si ya estan visitados.
     */
    public boolean hayAlgunNodoSinRutear(Node[] Nodes)
    {
        for (int i = 1; i < Nodes.length; i++)
        {
            if (!Nodes[i].IsRouted)
                return true;
        }
        return false;
    }

    /**
     * Método greedy encargado de encontrar el conjunto de rutas para los vehículos.
     * @param Nodes nodos del mapa.
     * @param CostMatrix matriz con los pesos de los nodos.
     * @param r consumo de watts/hora * kilometro.
     * @param speed velocidad de los vehículos.
     * @param Tmax tiempo usado durante la ruta.
     */
    public void GreedySolucion(Node[] Nodes , double[][] CostMatrix, double r, double speed, double Tmax) {

        double CostoCandidato,CostoFinal,tiempoRuta = 0,demanda = 0;
        int VehIndex = 0;
        while (hayAlgunNodoSinRutear(Nodes)) {
            int CustIndex = 0;
            Node Candidato = null;
            double minCost = (float) Double.MAX_VALUE;

            if (Vehiculos[VehIndex].Route.isEmpty())
            {
                Vehiculos[VehIndex].AddNode(Nodes[0]); //Se acabo de iniciar una ruta por eso se pone el nodo deposito.
            }
            double[][] auxCostMatrix = CostMatrix;

            for (int i = 1; i <= numeroClientes; i++) { // Empezamos en 1 porque 0 es el deposito
                if (Nodes[i].IsRouted == false) {
                    if (Vehiculos[VehIndex].CheckIfFits(Nodes[i].caldemand(Vehiculos[VehIndex].CurLoc, auxCostMatrix, r, speed).first)) { // Se revisa si se puede ir al siguiente nodo con las condiciones actuales
                        CostoCandidato = CostMatrix[Vehiculos[VehIndex].CurLoc][i];
                        if (minCost > CostoCandidato) { // Si se encuentra una mejor solución.
                            minCost = CostoCandidato;
                            tiempoRuta = Nodes[i].caldemand(Vehiculos[VehIndex].CurLoc, auxCostMatrix, r, speed).second;
                            demanda = Nodes[i].caldemand(Vehiculos[VehIndex].CurLoc, auxCostMatrix, r, speed).first;
                            CustIndex = i;
                            Candidato = Nodes[i];
                        }
                    }
                }
            }

            if ( Candidato  == null)
            {
                //Si no hay un nodo al que se pueda ir
                if ( VehIndex+1 < Vehiculos.length ) //y todavia hay más vehículos.
                {
                    if (Vehiculos[VehIndex].CurLoc != 0) {//Se termina la ruta de este vehículo.
                        CostoFinal = CostMatrix[Vehiculos[VehIndex].CurLoc][0];
                        Vehiculos[VehIndex].AddNode(Nodes[0]);
                        this.Costo +=  CostoFinal;
                    }
                    VehIndex = VehIndex+1; //Se mueve al siguiente vehículo.
                }
                else //Si no hay más vehículos, es porque no se puede resolver el problema con estas condiciones.
                {
                    System.out.println("\nEl resto de clientes no encaja con un vehículo\n" +
                            "No se puede resolver el problema con estas condiciones.");
                    System.exit(0);
                }
            }
            else
            {
                if(!Candidato.IsStation && !Candidato.IsDepot){ // Si el vehículo esta en donde un cliente.
                    tiempoRuta += 0.5;
                }
                if(Nodes[Vehiculos[VehIndex].CurLoc].IsStation == true){ // Si el vehículo esta en una estación.
                    if(Candidato.IsStation){ // Si el candidato a moverse es una estación entonces recargue lo minimo para llegar hasta ella.
                        double cantidadH = calcularHoras(demanda,Nodes[CustIndex].tipo,Nodes[CustIndex].pendienteFuncionCarga);
                        Vehiculos[VehIndex].tiempoRuta += cantidadH;
                        Vehiculos[VehIndex].carga -= Nodes[CustIndex].calcularTiempoRecarga(cantidadH);
                    }else{ // Si no recargue toda la batería.
                        double cantidadH = calcularHoras(Vehiculos[VehIndex].carga+demanda,Nodes[Vehiculos[VehIndex].CurLoc].tipo,Nodes[Vehiculos[VehIndex].CurLoc].pendienteFuncionCarga);
                        Vehiculos[VehIndex].tiempoRuta += cantidadH;
                        Vehiculos[VehIndex].carga -= Nodes[Vehiculos[VehIndex].CurLoc].calcularTiempoRecarga(cantidadH);
                    }
                }
                if(Vehiculos[VehIndex].tiempoRuta + tiempoRuta > Tmax){ // Si el vehículo esta acercandose al limite de tiempo, entonces finalice la ruta.
                    Vehiculos[VehIndex].carga += Nodes[0].caldemand(Vehiculos[VehIndex].CurLoc, auxCostMatrix, r, speed).first;
                    Vehiculos[VehIndex].tiempoRuta += Nodes[0].caldemand(Vehiculos[VehIndex].CurLoc, auxCostMatrix, r, speed).second;
                    CostoFinal = CostMatrix[Vehiculos[VehIndex].CurLoc][0];
                    Vehiculos[VehIndex].AddNode(Nodes[0]);
                    this.Costo +=  CostoFinal;
                    if ( VehIndex+1 < Vehiculos.length ){ //Si todavia hay más vehículos.
                        VehIndex = VehIndex+1; //Se mueve al siguiente vehículo.
                    }
                }else{//Si hay un candidato.
                    Vehiculos[VehIndex].AddNode(Candidato);
                    Vehiculos[VehIndex].carga += demanda;
                    Vehiculos[VehIndex].tiempoRuta += tiempoRuta;
                    Nodes[CustIndex].IsRouted = true;
                    this.Costo += minCost;
                }
            }
        }
        // Se finaliza la ruta.
        CostoFinal = CostMatrix[Vehiculos[VehIndex].CurLoc][0];
        Vehiculos[VehIndex].AddNode(Nodes[0]);
        this.Costo +=  CostoFinal;
    }

    /**
     * Método encargado de decirme las horas necesarias para cargar lo mínimo para llegar hasta el próximo nodo.
     *
     * @param carga la carga necesaria para llegar al siguiente nodo considerando la actual.
     * @param tipoEstacion el tipo de estación de carga.
     * @param pendienteCarga la pendiente de carga de la estación.
     * @return devuelve las horas que necesita para cargar lo mínimo para moverse hacía el próximo nodo.
     */
    public double calcularHoras(double carga, int tipoEstacion, float[] pendienteCarga) {
        return carga/pendienteCarga[tipoEstacion];
    }

    /**
     * Método encargado de optimizar la solución greedy, mediante el cambio de posición de los nodos de la solución greedy,
     * en otras palabras se van a variar el orden de la solución con el fin de encotrar una más optima de tomar.
     * @param TABU_Horizon Número que guía la solución.
     * @param CostMatrix La matriz con los costos de los nodos del mapa.
     */
    public void TabuSearch(int TABU_Horizon, double[][] CostMatrix) {

        ArrayList<Node> RouteFrom;
        ArrayList<Node> RouteTo;

        double MovingNodeDemand = 0;

        int VehIndexFrom,VehIndexTo;
        double BestNCost,NeigthboorCost;

        int SwapIndexA = -1, SwapIndexB = -1, SwapRouteFrom =-1, SwapRouteTo=-1;

        int MAX_ITERATIONS = 200;
        int iteration_number= 0;

        int DimensionCustomer = CostMatrix[1].length;
        int TABU_Matrix[][] = new int[DimensionCustomer+1][DimensionCustomer+1];

        MejorSolucionCosto = this.Costo; //Initial Solution Cost

        boolean Termination = false;

        while (!Termination)
        {
            iteration_number++;
            BestNCost = Double.MAX_VALUE;

            for (VehIndexFrom = 0; VehIndexFrom <  this.Vehiculos.length; VehIndexFrom++) {
                RouteFrom =  this.Vehiculos[VehIndexFrom].Route;
                int RoutFromLength = RouteFrom.size();
                for (int i = 1; i < RoutFromLength - 1; i++) { //Not possible to move depot!

                    for (VehIndexTo = 0; VehIndexTo <  this.Vehiculos.length; VehIndexTo++) {
                        RouteTo =   this.Vehiculos[VehIndexTo].Route;
                        int RouteTolength = RouteTo.size();
                        for (int j = 0; (j < RouteTolength - 1); j++) {//Not possible to move after last Depot!

                            MovingNodeDemand = RouteFrom.get(i).demand;

                            if ((VehIndexFrom == VehIndexTo) ||  this.Vehiculos[VehIndexTo].CheckIfFits(MovingNodeDemand)) {
                                //If we assign to a different route check capacity constrains
                                //if in the new route is the same no need to check for capacity

                                if (((VehIndexFrom == VehIndexTo) && ((j == i) || (j == i - 1))) == false)  // Not a move that Changes solution cost
                                {
                                    double MinusCost1 = CostMatrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i).NodeId];
                                    double MinusCost2 = CostMatrix[RouteFrom.get(i).NodeId][RouteFrom.get(i + 1).NodeId];
                                    double MinusCost3 = CostMatrix[RouteTo.get(j).NodeId][RouteTo.get(j + 1).NodeId];

                                    double AddedCost1 = CostMatrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i + 1).NodeId];
                                    double AddedCost2 = CostMatrix[RouteTo.get(j).NodeId][RouteFrom.get(i).NodeId];
                                    double AddedCost3 = CostMatrix[RouteFrom.get(i).NodeId][RouteTo.get(j + 1).NodeId];

                                    //Check if the move is a Tabu! - If it is Tabu break
                                    if ((TABU_Matrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i+1).NodeId] != 0)
                                            || (TABU_Matrix[RouteTo.get(j).NodeId][RouteFrom.get(i).NodeId] != 0)
                                            || (TABU_Matrix[RouteFrom.get(i).NodeId][RouteTo.get(j+1).NodeId] != 0)) {
                                        break;
                                    }

                                    NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3
                                            - MinusCost1 - MinusCost2 - MinusCost3;

                                    if (NeigthboorCost < BestNCost) {
                                        BestNCost = NeigthboorCost;
                                        SwapIndexA = i;
                                        SwapIndexB = j;
                                        SwapRouteFrom = VehIndexFrom;
                                        SwapRouteTo = VehIndexTo;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int o = 0; o < TABU_Matrix[0].length;  o++) {
                for (int p = 0; p < TABU_Matrix[0].length ; p++) {
                    if (TABU_Matrix[o][p] > 0)
                    { TABU_Matrix[o][p]--; }
                }
            }

            RouteFrom =  this.Vehiculos[SwapRouteFrom].Route;
            RouteTo =  this.Vehiculos[SwapRouteTo].Route;
            this.Vehiculos[SwapRouteFrom].Route = null;
            this.Vehiculos[SwapRouteTo].Route = null;

            Node SwapNode = RouteFrom.get(SwapIndexA);

            int NodeIDBefore = RouteFrom.get(SwapIndexA-1).NodeId;
            int NodeIDAfter = RouteFrom.get(SwapIndexA+1).NodeId;
            int NodeID_F = RouteTo.get(SwapIndexB).NodeId;
            int NodeID_G = RouteTo.get(SwapIndexB+1).NodeId;

            Random TabuRan = new Random();
            int RendomDelay1 = TabuRan.nextInt(5);
            int RendomDelay2 = TabuRan.nextInt(5);
            int RendomDelay3 = TabuRan.nextInt(5);

            TABU_Matrix[NodeIDBefore][SwapNode.NodeId] = TABU_Horizon + RendomDelay1;
            TABU_Matrix[SwapNode.NodeId][NodeIDAfter]  = TABU_Horizon + RendomDelay2 ;
            TABU_Matrix[NodeID_F][NodeID_G] = TABU_Horizon + RendomDelay3;

            RouteFrom.remove(SwapIndexA);

            if (SwapRouteFrom == SwapRouteTo) {
                if (SwapIndexA < SwapIndexB) {
                    RouteTo.add(SwapIndexB, SwapNode);
                } else {
                    RouteTo.add(SwapIndexB + 1, SwapNode);
                }
            }
            else
            {
                RouteTo.add(SwapIndexB+1, SwapNode);
            }


            this.Vehiculos[SwapRouteFrom].Route = RouteFrom;
            this.Vehiculos[SwapRouteFrom].carga -= MovingNodeDemand;

            this.Vehiculos[SwapRouteTo].Route = RouteTo;
            this.Vehiculos[SwapRouteTo].carga += MovingNodeDemand;

            SolucionPasada.add(this.Costo);

            this.Costo += BestNCost;

            if (this.Costo < MejorSolucionCosto)
            {
                guardarMejorSolucion();
            }

            if (iteration_number == MAX_ITERATIONS)
            {
                Termination = true;
            }
        }

        this.Vehiculos = VehiculosParaLaMejorSolucion;
        this.Costo = MejorSolucionCosto;

        try{
            PrintWriter writer = new PrintWriter("SolucionesPasadasTabu.txt", "UTF-8");
            writer.println("Soluciones"+"\t");
            for  (int i = 0; i< SolucionPasada.size(); i++){
                writer.println(SolucionPasada.get(i)+"\t");
            }
            writer.close();
        } catch (Exception e) {}
    }

    /**
     *
     */
    public void guardarMejorSolucion()
    {
        MejorSolucionCosto = Costo;
        for (int j = 0; j < numeroVehiculos; j++)
        {
            VehiculosParaLaMejorSolucion[j].Route.clear();
            if (! Vehiculos[j].Route.isEmpty())
            {
                int longitudRuta = Vehiculos[j].Route.size();
                for (int k = 0; k < longitudRuta ; k++) {
                    Node n = Vehiculos[j].Route.get(k);
                    VehiculosParaLaMejorSolucion[j].Route.add(n);
                }
            }
        }
    }

    /**
     * Imprimir la solución dada por la consola.
     * @param nombre nombre de la solución.
     */
    public void imprimirSolucion(String nombre)
    {
        System.out.println("=========================================================");
        System.out.println(nombre+"\n");

        for (int j = 0; j < numeroVehiculos; j++)
        {
            if (!Vehiculos[j].Route.isEmpty())
            {   System.out.print("Vehículo " + j + ":");
                int longitudRuta = Vehiculos[j].Route.size();
                for (int k = 0; k < longitudRuta ; k++) {
                    if (k == longitudRuta-1)
                    { System.out.print(Vehiculos[j].Route.get(k).NodeId );  }
                    else
                    { System.out.print(Vehiculos[j].Route.get(k).NodeId+ "->"); }
                }
                System.out.println();
            }
        }
        System.out.println("\nCosto solución: "+this.Costo +"\n");
    }
}
