import java.io.*;
import java.util.*;

/**
* Class that accept as input a file containing data on roads, shop locations and incoming calls
* and calculates abd output details for the best(shotrest trip) for each call.
*
* @author Nikita Martin
* @version 1.0
* @since 25-03-2024
*/
public class SimulatorTwo{
   private List<Company> shopLocations, taxiLocations;
   private List<String> clientLocations;
   
   /**
   * Default constructor for SimulatorOne.
   * Initializes the client and shop locations
   */
   public SimulatorTwo(){
      shopLocations = new ArrayList<>();//array reading in all the shops
      clientLocations = new ArrayList<>();//array reading in all the client locations
      taxiLocations = new ArrayList<>();//array reading in all the taxi locations
   }
   
   /**
   * Loads data into a graph, including nodes, edges, shop locations and client locations
   *
   * @param scanner The scanner object used to read data from the file
   * @param graph The graph object to which the data will be added.
   **/
   public void loadFromFile(Scanner scanner, Graph graph){
      // Read the number of nodes from the file
      int numNodes = Integer.parseInt(scanner.nextLine().trim());
      
      // Loop through each node
      for(int i=0; i<numNodes; i++){
         // Read data for edges from the current node
         String[] edgeData = scanner.nextLine().split(" ");//Format:source node, destination node, weight
         String sourceNode =  Integer.toString(i);// each node is indexed from 0
         graph.addVertex(sourceNode);//add the node to the graph
         
         //loop through each destination node and its corresponding cost
         for (int j = 1; j<edgeData.length; j+=2){
            int destNode = Integer.parseInt(edgeData[j]); //extract destination node
            double cost = Double.parseDouble(edgeData[j+1]); //extract edge cost
            graph.addEdge(sourceNode, Integer.toString(destNode), cost); //add edge to the graph
         }
      }
      
      // Read the number of taxis from the file
      int numTaxis = Integer.parseInt(scanner.nextLine().trim());
      // Read the data for taxis from the next line and split it into an array
      String[] taxiData = scanner.nextLine().split(" ");//obtain taxi data
      
      // Iterate over each element of the taxiData array
      for(String taxiInfo: taxiData){
         // Split each element of the taxiData array into parts based on the colon (:) separator
         String[] taxiInfoParts = taxiInfo.split(":");
         // Extract the taxi ID and taxi company from the split parts
         String taxiId = taxiInfoParts[0];
         String taxiCompany = taxiInfoParts[1];
         // Create a new Company object with the extracted taxi ID and company, then add it to the taxiLocations list
         taxiLocations.add(new Company(taxiId, taxiCompany));
      }
      
      //read the number of shops
      int numShops = Integer.parseInt(scanner.nextLine().trim());
      String[] shopData = scanner.nextLine().split(" ");//read shop locations 
      for(String shopInfo: shopData){
         // Split each element of the shopData array into parts based on the colon (:) separator
         String[] shopInfoParts = shopInfo.split(":");
         // Extract the shop ID and shop company from the split parts
         String shopId = shopInfoParts[0];
         String shopCompany = shopInfoParts[1];
         // Create a new Company object with the extracted shop ID and company, then add it to the shopLocations list
         shopLocations.add(new Company(shopId, shopCompany));
      }
      
      //read the number of clients
      int numClients = Integer.parseInt(scanner.nextLine().trim());
      // Read the data for clients from the next line and split it into an array
      String[] clientData = scanner.nextLine().split(" ");
      
      // Iterate over each element of the clientData array and add it to the clientLocations list
      for(String client: clientData){
         clientLocations.add(client);
      }
      
      scanner.close();
   }
   
   private String findClosestShop(String client, Graph graph){
      // Initialize the minimum shop cost to the maximum possible value
      double minShopCost = Double.MAX_VALUE;
      // Initialize the ID of the closest shop to an empty string
      String closestShop = "";
      
      // Iterate over each shop location in the list of shopLocations
      for (Company shop : shopLocations) {
         // Get the shortest distance from the client to the current shop using the graph
         double shopCost = graph.getShortestDistance(client, shop.getID());
         // Check if the current shop has a shorter distance than the minimum shop cost and is not the client itself
         if (shopCost < minShopCost && shopCost != 0.0) {
            // Update the minimum shop cost and the ID of the closest shop
            minShopCost = shopCost;
            closestShop = shop.getID();
         }
      }
      // Return the ID of the closest shop to the client
      return closestShop;
   }
   
   private String getCompanyForShop(String shopID) {
      // Iterate over each company in the list of shopLocations
      for (Company company : shopLocations) {
         // Check if the current company's ID matches the given shop ID
         if (company.getID().equals(shopID)) {
            // Return the company name associated with the shop ID
            return company.getCompany();
         }
      }
      // If the shop ID is not found, return an empty string (shouldn't happen in valid data)
      return "";   } 
   
   /**
   * Process client calls by finding the nearest taxis and shops for each client location
   *
   * @param graph The graph representing the network of taxis and shops
   **/
   private void processCalls(Graph graph){
      //check if the graph is empty
      if(graph.isEmpty()){
         System.out.println("Graph is Empty");
      }else{
         //Iterate over each client location
         for(String client: clientLocations){
            System.out.println("client " + client);
            
            //find the closest taxis to the client
            List<Company> closestTaxis = new ArrayList<>();
            String clientClosestShop = findClosestShop(client, graph);
            String clientShopCompany = getCompanyForShop(clientClosestShop);
            // Initialize shortestDistance to the maximum possible value 
            double minTaxiCost = Double.MAX_VALUE;
            
            //obtain the closest taxis to the clients
            for(Company taxi: taxiLocations){
               if(taxi.getCompany().equals(clientShopCompany)){
                  //obtain the shortest distance between the taxi and client
                  double distanceToClient = graph.getShortestDistance(taxi.getID(), client);
                  if(distanceToClient < minTaxiCost && distanceToClient!=0.0){
                     minTaxiCost = distanceToClient;//value updates to reflect actual shortest distance between the shop and client
                     closestTaxis.clear();//array cleared to only store the closest taxis to the client in the array
                     closestTaxis.add(taxi);
                  }
                  else if(distanceToClient == minTaxiCost && distanceToClient != 0.0){
                     //checks if there are multiple taxis close to the client with the same cost
                     closestTaxis.add(taxi);
                  }
               }
            }
            
            String closestShopID = findClosestShop(client, graph);//finds the closest shop to the specified client location within the given graph
            double minShopCost = graph.getShortestDistance(client, closestShopID);//retrieves the shortest distance between the client location and the closest shop location from the graph.
            
            //check if a client cannot be picked up or dropped off by checking if the shortest routes to the client and shop exists
            if(minTaxiCost == Double.MAX_VALUE || minTaxiCost == 0 || minShopCost == Double.MAX_VALUE || minShopCost == 0){
               System.out.println("cannot be helped");
               continue;
            }
            
            //process the closest shops to the client
            if(!closestTaxis.isEmpty()){
               Collections.sort(closestTaxis, Comparator.comparing(Company::getID));//sorts list in ascending order based on value of its elements.
               for (Company taxi : closestTaxis) {
                  //check if multiple paths from a taxi to a client with the same cost
                  List<String> nearestTaxi = graph.getShortestPath(taxi.id, client);
                  if(!nearestTaxi.isEmpty() && nearestTaxi.size() != 1){
                  
                     //check if multiple paths from a taxi to a client with the same cost
                     graph.dijkstra(taxi.getID());
                     //get the vertex of the client aka target node 
                     Vertex clientVertex = graph.getVertex(client);
                     System.out.println("destination " + taxi.getCompany());
                     System.out.println("taxi " + taxi.getID());
                     if(!clientVertex.hasMultipleSolutions){
                        //check of there are multiple paths going to it with the same shortest distance cost
                        for(String paths: nearestTaxi){
                           System.out.print(paths + " ");
                        }
                        System.out.println();
                     }
                     else{
                        System.out.println("multiple solutions cost " + (int)minTaxiCost + " to a nearest " + taxi.company);
                     }
                  }
               }
            }
            
            //get the path from the client to the shop
            List<String> nearestShop = graph.getShortestPath(client, closestShopID);
                  
            if(!nearestShop.isEmpty() && nearestShop.size() != 1){
               System.out.println("shop " + closestShopID);
               
               //checks if theres more than path to the shop
               graph.dijkstra(client);
               Vertex shopVertex = graph.getVertex(closestShopID);
               
               if (!shopVertex.hasMultipleSolutions) {
                  for(String paths: nearestShop){
                     System.out.print(paths + " ");
                  }
                  System.out.println();
               }else{
                   System.out.println("multiple solutions cost " + (int)minShopCost);
               }
            }
         }
      }
   }
         
   public static void main(String [] args){
      SimulatorTwo simTwo = new SimulatorTwo();
      Scanner scanner = new Scanner(System.in);
      Graph graph = new Graph();
      simTwo.loadFromFile(scanner, graph);
      simTwo.processCalls(graph);
   }
}