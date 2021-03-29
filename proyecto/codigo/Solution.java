import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
public class Solution
{
    int NoOfVehicles;
    int NoOfCustomers;
    Vehicle[] Vehicles;
    double Cost;

    //Tabu Variables
    public Vehicle[] VehiclesForBestSolution;
    double BestSolutionCost;

    public ArrayList<Double> PastSolutions;

    public Solution(int CustNum, int VechNum , double VechCap)
    {
        this.NoOfVehicles = VechNum;
        this.NoOfCustomers = CustNum;
        this.Cost = 0;
        Vehicles = new Vehicle[NoOfVehicles];
        VehiclesForBestSolution =  new Vehicle[NoOfVehicles];
        PastSolutions = new ArrayList<>();

        for (int i = 0 ; i < NoOfVehicles; i++)
        {
            Vehicles[i] = new Vehicle(i+1,VechCap);
            VehiclesForBestSolution[i] = new Vehicle(i+1,VechCap);
        }
    }

    public boolean UnassignedCustomerExists(Node[] Nodes)
    {
        for (int i = 1; i < Nodes.length; i++)
        {
            if (!Nodes[i].IsRouted)
                return true;
        }
        return false;
    }

    public void GreedySolution(Node[] Nodes , double[][] CostMatrix, double r, double speed, double Tmax) {

        double CandCost,EndCost,tiempoRuta = 0,demand = 0;
        int VehIndex = 0;
        while (UnassignedCustomerExists(Nodes)) {
            int CustIndex = 0;
            Node Candidate = null;
            double minCost = (float) Double.MAX_VALUE;

            if (Vehicles[VehIndex].Route.isEmpty())
            {
                Vehicles[VehIndex].AddNode(Nodes[0]);
            }
            double[][] pCostMatrix = CostMatrix;

            for (int i = 1; i <= NoOfCustomers; i++) {
                if (Nodes[i].IsRouted == false) {
                    if (Vehicles[VehIndex].CheckIfFits(Nodes[i].caldemand(Vehicles[VehIndex].CurLoc, pCostMatrix, r, speed).first)) {
                        CandCost = CostMatrix[Vehicles[VehIndex].CurLoc][i];
                        if (minCost > CandCost) {
                            minCost = CandCost;
                            tiempoRuta = Nodes[i].caldemand(Vehicles[VehIndex].CurLoc, pCostMatrix, r, speed).second;
                            demand = Nodes[i].caldemand(Vehicles[VehIndex].CurLoc, pCostMatrix, r, speed).first;
                            CustIndex = i;
                            Candidate = Nodes[i];
                        }
                    }
                }
            }

            if ( Candidate  == null)
            {
                //Not a single Customer Fits
                if ( VehIndex+1 < Vehicles.length ) //We have more vehicles to assign
                {
                    if (Vehicles[VehIndex].CurLoc != 0) {//End this route
                        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
                        Vehicles[VehIndex].AddNode(Nodes[0]);
                        this.Cost +=  EndCost;
                    }
                    VehIndex = VehIndex+1; //Go to next Vehicle
                }
                else //We DO NOT have any more vehicle to assign. The problem is unsolved under these parameters
                {
                    System.out.println("\nThe rest customers do not fit in any Vehicle\n" +
                            "The problem cannot be resolved under these constrains");
                    System.exit(0);
                }
            }
            else
            {
                if(!Candidate.IsStation && !Candidate.IsDepot){
                    tiempoRuta += 0.5;
                }
                if(Nodes[Vehicles[VehIndex].CurLoc].IsStation == true){
                    if(Candidate.IsStation){
                        double cantidadH = calcularHoras(demand,Nodes[CustIndex].tipo,Nodes[CustIndex].pendienteFuncionCarga);
                        Vehicles[VehIndex].tiempoRuta += cantidadH;
                        Vehicles[VehIndex].carga -= Nodes[CustIndex].calcularTiempoRecarga(cantidadH);
                    }else{
                        double cantidadH = calcularHoras(Vehicles[VehIndex].carga+demand,Nodes[Vehicles[VehIndex].CurLoc].tipo,Nodes[Vehicles[VehIndex].CurLoc].pendienteFuncionCarga);
                        Vehicles[VehIndex].tiempoRuta += cantidadH;
                        Vehicles[VehIndex].carga -= Nodes[Vehicles[VehIndex].CurLoc].calcularTiempoRecarga(cantidadH);
                    }
                }
                if(Vehicles[VehIndex].tiempoRuta + tiempoRuta > Tmax){
                    Vehicles[VehIndex].carga += Nodes[0].caldemand(Vehicles[VehIndex].CurLoc, pCostMatrix, r, speed).first;
                    Vehicles[VehIndex].tiempoRuta += Nodes[0].caldemand(Vehicles[VehIndex].CurLoc, pCostMatrix, r, speed).second;
                    EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
                    Vehicles[VehIndex].AddNode(Nodes[0]);
                    this.Cost +=  EndCost;
                    if ( VehIndex+1 < Vehicles.length ){
                        VehIndex = VehIndex+1;
                    }
                }else{
                    Vehicles[VehIndex].AddNode(Candidate);//If a fitting Customer is Found
                    Vehicles[VehIndex].carga += demand;
                    Vehicles[VehIndex].tiempoRuta += tiempoRuta;
                    Nodes[CustIndex].IsRouted = true;
                    this.Cost += minCost;
                }
            }
        }
        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
        Vehicles[VehIndex].AddNode(Nodes[0]);
        this.Cost +=  EndCost;
        System.out.println(Vehicles[VehIndex].tiempoRuta);
    }

    public double calcularHoras(double carga, int tipoEstacion, float[] pendienteCarga) {
        return carga/pendienteCarga[tipoEstacion];
    }

    public int findStation(Node[] nodes, double[][] CostMatrix, int CurLoc){
        int index = 0;
        double costoMin = Double.MAX_VALUE;
        double distancia;
        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].IsStation && CurLoc != i){
                distancia = CostMatrix[CurLoc][i];
                if(distancia < costoMin){
                    costoMin = distancia;
                    index = i;
                }
            }
        }
        return index;
    }


    public void TabuSearch(int TABU_Horizon, double[][] CostMatrix) {

        //We use 1-0 exchange move
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

        BestSolutionCost = this.Cost; //Initial Solution Cost

        boolean Termination = false;

        while (!Termination)
        {
            iteration_number++;
            BestNCost = Double.MAX_VALUE;

            for (VehIndexFrom = 0;  VehIndexFrom <  this.Vehicles.length;  VehIndexFrom++) {
                RouteFrom =  this.Vehicles[VehIndexFrom].Route;
                int RoutFromLength = RouteFrom.size();
                for (int i = 1; i < RoutFromLength - 1; i++) { //Not possible to move depot!

                    for (VehIndexTo = 0; VehIndexTo <  this.Vehicles.length; VehIndexTo++) {
                        RouteTo =   this.Vehicles[VehIndexTo].Route;
                        int RouteTolength = RouteTo.size();
                        for (int j = 0; (j < RouteTolength - 1); j++) {//Not possible to move after last Depot!

                            MovingNodeDemand = RouteFrom.get(i).demand;

                            if ((VehIndexFrom == VehIndexTo) ||  this.Vehicles[VehIndexTo].CheckIfFits(MovingNodeDemand)) {
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

            RouteFrom =  this.Vehicles[SwapRouteFrom].Route;
            RouteTo =  this.Vehicles[SwapRouteTo].Route;
            this.Vehicles[SwapRouteFrom].Route = null;
            this.Vehicles[SwapRouteTo].Route = null;

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


            this.Vehicles[SwapRouteFrom].Route = RouteFrom;
            this.Vehicles[SwapRouteFrom].carga -= MovingNodeDemand;

            this.Vehicles[SwapRouteTo].Route = RouteTo;
            this.Vehicles[SwapRouteTo].carga += MovingNodeDemand;

            PastSolutions.add(this.Cost);

            this.Cost  += BestNCost;

            if (this.Cost <   BestSolutionCost)
            {
                SaveBestSolution();
            }

            if (iteration_number == MAX_ITERATIONS)
            {
                Termination = true;
            }
        }

        this.Vehicles = VehiclesForBestSolution;
        this.Cost = BestSolutionCost;

        try{
            PrintWriter writer = new PrintWriter("PastSolutionsTabu.txt", "UTF-8");
            writer.println("Solutions"+"\t");
            for  (int i = 0; i< PastSolutions.size(); i++){
                writer.println(PastSolutions.get(i)+"\t");
            }
            writer.close();
        } catch (Exception e) {}
    }

    public void SaveBestSolution()
    {
        BestSolutionCost = Cost;
        for (int j=0 ; j < NoOfVehicles ; j++)
        {
            VehiclesForBestSolution[j].Route.clear();
            if (! Vehicles[j].Route.isEmpty())
            {
                int RoutSize = Vehicles[j].Route.size();
                for (int k = 0; k < RoutSize ; k++) {
                    Node n = Vehicles[j].Route.get(k);
                    VehiclesForBestSolution[j].Route.add(n);
                }
            }
        }
    }

    public void SolutionPrint(String Solution_Label)//Print Solution In console
    {
        System.out.println("=========================================================");
        System.out.println(Solution_Label+"\n");

        for (int j=0 ; j < NoOfVehicles ; j++)
        {
            if (!Vehicles[j].Route.isEmpty())
            {   System.out.print("Vehicle " + j + ":");
                int RoutSize = Vehicles[j].Route.size();
                for (int k = 0; k < RoutSize ; k++) {
                    if (k == RoutSize-1)
                    { System.out.print(Vehicles[j].Route.get(k).NodeId );  }
                    else
                    { System.out.print(Vehicles[j].Route.get(k).NodeId+ "->"); }
                }
                System.out.println();
            }
        }
        System.out.println("\nSolution Cost "+this.Cost+"\n");
    }
}
