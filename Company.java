import java.util.*;
import java.io.*;

/*
* class representing a taxi in the simulation
*
*/
class Company{
   public String id;//serves as an location for each taxi/shop in the simulation.
   public String company;//serves as the company name for each taxi/shop
   /*
   * constructor initializes the taxi with an ID, company
   */
   public Company(String id, String company){
      this.id = id;
      this.company = company;
   }
   
   //Accessor and mutator methods for id and the company   
   public String getCompany(){return this.company;}
   public String getID(){return this.id;}
}