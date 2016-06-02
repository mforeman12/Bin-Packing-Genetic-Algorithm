
package Foreman5;

//****************************************************************************
//****************************************************************************
//Class:        PopulationMember
//Description:  An instance of an individual member of a population. This class
//              tracks a member's shipment, fitness, weight, revenue, and
//              initial distance from the target weight.

public class PopulationMember implements Comparable<PopulationMember> {

public double[][] items;    
public double knapsackCapacity;
public int populationSize;
public double shipmentWeight;
public double shipmentRevenue;
public String shipment;
public double shipmentFitness;
public double normalizedFitness;
public int numberOfItems;
public double initialWeightDistance;

//********************************************************************
//Method:       PopulationMember
//Description:  Initial constructor for the population. This is only called once
//Parameters:   int pSize
//              double kCap
//              String text[]
//Returns:      Nothing
//Calls:        setWeightAndRevenue

public PopulationMember(int pSize, double kCap, String text[]){
    populationSize = pSize;
    knapsackCapacity = kCap;
    shipmentFitness = 0;
    shipment = "";
    numberOfItems = Integer.parseInt(text[0]);
    
    
    
    for (int i = 0; i < numberOfItems; i++) {
        int temp = (int)Math.round(Math.random());
        if(temp == 1){
            shipment = shipment.concat("1");
        }
        else{
            shipment = shipment.concat("0");
        }   
    }
    
    items = new double[numberOfItems][2];
    int x = 1;
    for (int i = 1; i < numberOfItems + 1; i++) {
        for (int j = 0; j < 2; j++) {
            items[i - 1][j] = Double.parseDouble(text[x]);
            
            x++;
        }
        
    }    
       
    setWeightAndRevenue();
    initialWeightDistance = Math.round((shipmentWeight - 
            knapsackCapacity)*100000D)/100000D;
    
    
}

//********************************************************************
//Method:       PopulationMember
//Description:  This constructor is used to create the bestSolution in each
//              generation.
//Parameters:   All of the values from the most fit member of the population
//              ArrayList
//Returns:      Nothing
//Calls:        Nothing

public PopulationMember(double[][] initItems, double initKnapsackCapacity,
int initPopulationSize, double initShipmentWeight, double initShipmentRevenue,
String initShipment, double initShipmentFitness, int initNumberOfItems, 
double initNormalizedFitness, double initInitialWeightDistance
        ){
    
    items = initItems;
    knapsackCapacity = initKnapsackCapacity;
    populationSize = initPopulationSize;
    shipmentWeight = initShipmentWeight;
    shipmentRevenue = initShipmentRevenue;
    shipment = initShipment;
    shipmentFitness = initShipmentFitness;
    numberOfItems = initNumberOfItems;
    normalizedFitness = initNormalizedFitness;
    initialWeightDistance = initInitialWeightDistance;
    
    
    
    
}

//********************************************************************
//Method:       setWeightAndRevenue
//Description:  Calculates the current revenue and weight of the shipment
//Parameters:   None
//Returns:      Nothing
//Calls:        Nothing

public void setWeightAndRevenue(){
    shipmentWeight = 0;
    shipmentRevenue = 0;
    for (int i = 0; i < shipment.length(); i++) {
        if(shipment.charAt(i) == '1'){
        shipmentWeight += items[i][0];
        shipmentRevenue += items[i][1];}    
    }
}

//********************************************************************
//Method:       calculateFitness
//Description:  implements the fitness function described in the program
//              description.
//Parameters:   boolean firstGen
//Returns:      Nothing
//Calls:        Nothing
    
public void calculateFitness(boolean firstGen){
    
    if(firstGen){
        shipmentFitness = -1*shipmentWeight;
    } else{
    
    
    double distanceFromTargetWeight = 
        Math.round(Math.abs(shipmentWeight - knapsackCapacity)*100000D)/100000D;
    
    
    
    double a = 0;
    if(initialWeightDistance != 0){
    a = Math.round((
            distanceFromTargetWeight/initialWeightDistance)*100000D)/100000D;}
    else{a = 0;}
    a = Math.round(Math.pow(a, 2)*100000D)/100000D;
    a = Math.round(Math.sqrt(1-a)*100000D)/100000D;
    double b = 0;
    if(shipmentWeight > knapsackCapacity){
        b = Math.round((shipmentRevenue*.05)*100000D)/100000D;
        shipmentFitness = Math.round(Math.pow(b, a)*100000D)/100000D;
    }
    
    else {shipmentFitness = shipmentRevenue + 
            (distanceFromTargetWeight/initialWeightDistance);}
    
    
    }
    
    
}

//********************************************************************
//Method:       evolve
//Description:  Resets the populationMember with a new shipment after each
//              generation.
//Parameters:   String initShipment (the new shipment)
//Returns:      Nothing
//Calls:        setWeightAndRevenue

public void evolve(String initShipment){
    shipment = initShipment;
    setWeightAndRevenue();
    shipmentFitness = 0;
    normalizedFitness = 0;
    
    
}

//********************************************************************
//Method:       compareTo
//Description:  Sorts PopulationMembers by fitness
//Parameters:   PopulationMember member
//Returns:      int (for sorting)
//Calls:        Nothing

@Override
public int compareTo(PopulationMember member){

    return Double.compare(member.shipmentFitness, shipmentFitness);
 
  
}
    


}
