package Foreman5;

import java.util.ArrayList;
import java.util.Collections;

//****************************************************************************
//****************************************************************************
//Class:        Generation
//Description:  Manages an ArrayList of population members, tracks fitness, 
//              and also keeps track of the current best solution.

public class Generation {

    public ArrayList<PopulationMember> population;
    public ArrayList<PopulationMember> crossoverArray;
    public int currentGeneration;
    public int populationSize;
    public double knapsackCapacity;
    public double crossoverRate;
    public double mutationRate;
    public char saveBestMember;
    public PopulationMember bestSolution;
    public double totalFitness;
    public double[] cumulativeNormArray;
    public ArrayList<double[]> bestSolutionItems;
    
//********************************************************************    
//Method:       Generation
//Description:  Constructor method for the generation class. 
//Parameters:   Various user inputs from the Foreman5 class. 
//Returns:      Nothing
//Calls:        Nothing

    public Generation(
        int initPopulationSize, double initKnapsackCapacity, 
        double initCrossoverRate, double initMutationRate, 
        char initSaveBestMember, String[] text) {

        population = new ArrayList();
        currentGeneration = 0;
        populationSize = initPopulationSize;
        knapsackCapacity = initKnapsackCapacity;
        crossoverRate = initCrossoverRate;
        mutationRate = initMutationRate;
        saveBestMember = initSaveBestMember;
        totalFitness = 0;

        for (int i = 0; i < populationSize; i++) {
            population.add(new PopulationMember(
                    populationSize, knapsackCapacity, text));

        }

    }
    
//********************************************************************    
//Method:       calculatePopulationMemberFitnesses
//Description:  Calculates and normalizes the fitness for each population member 
//Parameters:   None 
//Returns:      Nothing
//Calls:        population.calculateFitness


    public void calculatePopulationMemberFitnesses() {
        double totalInitialWeightDistance = 0;
        if (currentGeneration == 0) {

            for (int i = 0; i < population.size(); i++) {
                totalInitialWeightDistance
                        += population.get(i).initialWeightDistance;

            }
        }
        for (int i = 0; i < population.size(); i++) {
            if (currentGeneration == 0) {

            population.get(i).initialWeightDistance
                = Math.round((totalInitialWeightDistance / population.size())
                                * 100000D) / 100000D;
                population.get(i).calculateFitness(true);
            } else {
                population.get(i).calculateFitness(false);
            }

            totalFitness += population.get(i).shipmentFitness;

        }
        for (int i = 0; i < population.size(); i++) {
            if (currentGeneration == 0) {
                population.get(i).normalizedFitness = 
                        population.get(i).shipmentFitness * -1 / totalFitness;
            } else {
                population.get(i).normalizedFitness = 
                        population.get(i).shipmentFitness / totalFitness;
            }
        }

    }
    
//********************************************************************
//Method:       sortPopulation
//Description:  sorts the population ArrayList based on fitness. 
//Parameters:   None 
//Returns:      Nothing
//Calls:        Collections.sort

    public void sortPopulation() {
        Collections.sort(population);
       
    }
    
//********************************************************************    
//Method:       createCumulativeNormArray
//Description:  Prepares the population for crossover by creating the cumulative
//              normalized array from which population members will be 
//              probabilistically selected to be bred. The more fit a population 
//              member is, the larger a section of space it will take up in the
//              probabilistic selection space (0-1).
//Parameters:   None
//Returns:      Nothing
//Calls:        Nothing

    public void createCumulativeNormArray() {

        cumulativeNormArray = new double[population.size()];
        cumulativeNormArray[0] = population.get(0).normalizedFitness;
        for (int i = 1; i < cumulativeNormArray.length; i++) {
            cumulativeNormArray[i] = population.get(i).normalizedFitness
                    + cumulativeNormArray[i - 1];

        }
    }

//********************************************************************
//Method:       initializeCrossoverArray
//Description:  Probabilistically selects members from the cumulative normalized
//              array to be placed into the crossover array. 
//Parameters:   None 
//Returns:      Nothing
//Calls:        Nothing

    public void initializeCrossoverArray() {
        crossoverArray = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            double rand = Math.random();
            if (rand <= cumulativeNormArray[0]) {
                crossoverArray.add(population.get(0));
            } else {
                for (int j = 1; j < population.size(); j++) {
                    if (cumulativeNormArray[j - 1] < rand && 
                            rand <= cumulativeNormArray[j]) {
                        crossoverArray.add(population.get(j));
                    }

                }
            }
        }

    }

//********************************************************************
//Method:       performCrossover
//Description:  Crosses over the population at the user specified crossover 
//              rate. Also calls the mutation method, saves the best member
//              of the population (if that mode is on), and resets the
//              population with new values
//Parameters:   None 
//Returns:      Nothing
//Calls:        performMutation
//              createNextPopulationMember
//              saveBestMember
    
    public void performCrossover() {

        for (int i = 0; i < crossoverArray.size(); i += 2) {
            if (Math.random() <= crossoverRate) {
                int crossoverPosition = 0;
                int x = 1;
                while (x == 1) {

                    crossoverPosition = (int) (Math.random() * population.get(
                            0).numberOfItems);
                    if (crossoverPosition != 0 && crossoverPosition != 1
                        && crossoverPosition != population.get(x).numberOfItems
                        && crossoverPosition != population.get(x).numberOfItems
                            - 1) {
                        x = 0;
                    }

                }
                String firstRightChunk;
            firstRightChunk = 
                crossoverArray.get(i).shipment.substring(crossoverPosition);
            String firstLeftChunk;
            firstLeftChunk = 
                crossoverArray.get(i).shipment.substring(0, crossoverPosition);
            String secondRightChunk;
            secondRightChunk = 
                crossoverArray.get(i + 1).shipment.substring(crossoverPosition);
            String secondLeftChunk;
            secondLeftChunk = 
                crossoverArray.get(i + 1).shipment.substring(0, crossoverPosition);

            String newFirst = firstLeftChunk.concat(secondRightChunk);
            String newSecond = secondLeftChunk.concat(firstRightChunk);

            newFirst = performMutation(newFirst);
            newSecond = performMutation(newSecond);

            createNextPopulationMember(i, newFirst);
            createNextPopulationMember(i + 1, newSecond);

            }//end of if

            createNextPopulationMember(i, performMutation(
                    crossoverArray.get(i).shipment));
            createNextPopulationMember(i + 1, performMutation(
                    crossoverArray.get(i).shipment));

        }
        if (saveBestMember == 'Y') {
            saveBestMember();
        }
    }
    
//********************************************************************    
//Method:       performMutation
//Description:  Mutates the population at the user specified mutation rate.
//Parameters:   String shipment (the new shipment to be mutated) 
//Returns:      String (the mutated string)
//Calls:        Nothing

    public String performMutation(String shipment) {
        String newShipment = "";
        if (Math.random() <= mutationRate) {
            int mutationIndex = 
                (int) (Math.random() * (population.get(0).numberOfItems));
            if (shipment.charAt(mutationIndex) == '0') {
                if (mutationIndex != 0 && mutationIndex != 
                        population.get(0).numberOfItems - 1) {
                    newShipment = shipment.substring(0, mutationIndex) + '1'
                            + shipment.substring(mutationIndex + 1);
                } else if (mutationIndex == 0) {
                    newShipment = '1' + shipment.substring(1);
                } else if (mutationIndex == population.get(0).numberOfItems - 1)
                {  newShipment = shipment.substring(0, mutationIndex) + '1';
                }
            } else {
                if (mutationIndex != 0 && mutationIndex != 
                        population.get(0).numberOfItems - 1) {
                    newShipment = shipment.substring(0, mutationIndex) + '0'
                            + shipment.substring(mutationIndex + 1);
                } else if (mutationIndex == 0) {
                    newShipment = '0' + shipment.substring(1);
                } else if (mutationIndex == population.get(0).numberOfItems - 1) 
                {    newShipment = shipment.substring(0, mutationIndex) + '0';
                }
            }
        } else {
            newShipment = shipment;
        }
        return newShipment;
    }

//********************************************************************
//Method:       createNextPopulationMember
//Description:  resets the population ArrayList with new values after crossover
//              and mutation
//Parameters:   int index (index in the population ArrayList of the member to
//              reset)
//              String shipment
//Returns:      Nothing
//Calls:        Nothing
    
    public void createNextPopulationMember(int index, String shipment) {
        population.get(index).evolve(shipment);
    }
    
//********************************************************************
//Method:       copyPopulationMember
//Description:  creates a new population member that stores the values of the
//              best solution from this generation. This method is only called
//              when a new overall best is found.
//Parameters:   None
//Returns:      Nothing
//Calls:        PopulationMember constructor

    public void copyPopulationMember() {
        bestSolution = new PopulationMember(
                population.get(0).items,
                population.get(0).knapsackCapacity,
                population.get(0).populationSize,
                population.get(0).shipmentWeight,
                population.get(0).shipmentRevenue,
                population.get(0).shipment,
                population.get(0).shipmentFitness,
                population.get(0).numberOfItems,
                population.get(0).normalizedFitness,
                population.get(0).initialWeightDistance
        );

        bestSolutionItems = new ArrayList<>();
        for (int i = 0; i < bestSolution.shipment.length(); i++) {
            if (bestSolution.shipment.charAt(i) == '1') {
                double[] item = new double[3];
                item[0] = i;
                item[1] = bestSolution.items[i][0];
                item[2] = bestSolution.items[i][1];
                bestSolutionItems.add(item);
            }

        }
    }
    
//********************************************************************
//Method:       saveBestMember
//Description:  removes a random member from the population and replaces it
//              with the current best solution
//Parameters:   None 
//Returns:      Nothing
//Calls:        Nothing


    public void saveBestMember() {
        int overwriteIndex = (int) (Math.random() * (population.size()));

        population.add(overwriteIndex, bestSolution);
        population.remove(overwriteIndex + 1);

    }

}
