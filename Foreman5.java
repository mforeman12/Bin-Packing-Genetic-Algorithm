package Foreman5;

import java.util.ArrayList;
import java.util.Collections;

//Program: Foreman5
//Course: Current Topics
//Description: Implements a GA to solve the bin packing problem, given user
//specified file and parameters. Shipments are evaluated by the fitness function
//r^(sqrt(1-(a/b)^2)), where r = shipment value, a = the distance from the
//shipment's weight to the knapsack capacity, and b = the average value of "a" 
//for all initial random shipments (essentially, it is an indicator of 
//progress). This function causes an exponantial growth in fitness gained with 
//respect to increased revenue and an exponential decay in fitness gained with
//respect to decreased difference between weight and the knapsack capacity as
//the shipment's weight approaches the knapsack capacity.
//Author: Matt Foreman
//Revised: 12/5/15
//Language: Java
//IDE: NetBeans 8.0.2
//****************************************************************************
//****************************************************************************
//Class: Foreman3
//Description: Runs the program, initializes the Generation class, which in
//turn initializes x PopulationMember classes, where x is the user given number
//of population members. This class also handles calls to ImageConstruction
//for histogram display.

public class Foreman5 {

public int populationSize;
public double knapsackCapacity;
public double crossoverRate;
public double mutationRate;
public char saveBestMember;
public double targetFitness;
public double targetValue;
public int numberOfRuns;
public char showDetails;
public int numberOfGenerations;
public char changeParameters;
public Generation generation;
public ArrayList<Generation> runResults;

//********************************************************************
//Method:      Foreman5
//Description: Constructs the Foreman5 class. Initializes the program with
//             user values.
//Parameters:  User values for each program parameter.
//Returns:     Nothing
//Calls:       Nothing


public Foreman5(int populationSize1, double knapsackCapacity1,
        double crossoverRate1, double mutationRate1, char saveBestMember1,
        double targetFitness1, int targetValue1, int numberOfRuns1,
        int numberOfGenerations1) {

    populationSize = populationSize1;
    knapsackCapacity = knapsackCapacity1;
    crossoverRate = crossoverRate1;
    mutationRate = mutationRate1;
    saveBestMember = saveBestMember1;
    targetFitness = targetFitness1;
    targetValue = targetValue1;
    numberOfRuns = numberOfRuns1;
    numberOfGenerations = numberOfGenerations1;

}

//********************************************************************
//Method:      main
//Description: Drives the program.
//Parameters:  None 
//Returns:     Nothing
//Calls:       Foreman5 constructor
//             run
//             Various ImageConstruction methods

public static void main(String[] args) {
    KeyboardInputClass keyboardInput = new KeyboardInputClass();
    TextFileClass text = new TextFileClass();

    text.getFileName("Specify the text file to be read:\n");
    text.getFileContents();

    int populationSize = keyboardInput.getInteger(
            false, 20, 0, 0, "Population size? (default = 20)");
    double knapsackCapacity = keyboardInput.getDouble(
            false, 200, 0, 0, "Knapsack capacity? (default = 200)");
    double crossoverRate = keyboardInput.getDouble(
            true, .5, 0, 1, "Crossover rate? (0-1: default = .5)");
    double mutationRate = keyboardInput.getDouble(
            true, .5, 0, 1, "Mutation rate? (0-1: default = .5)");
    char saveBestMember = keyboardInput.getCharacter(
            true, 'Y', "Y,N", 1, "Save best solution from generation "
            + "to generation? (Y/N: default = Y)");
    double targetFitness = keyboardInput.getDouble(
            true, 1, 0, 1, "Target fitness? (0-1: default = 1)");
    int targetValue = keyboardInput.getInteger(
            false, 1000, 0, 0, "Target value? (default = 1000)");
    int numberOfRuns = keyboardInput.getInteger(
            false, 1, 0, 0, "Number of runs? (default = 1)");
    int numberOfGens = 1;
    if (numberOfRuns > 1) {
        numberOfGens = keyboardInput.getInteger(
            false, 10000, 0, 0, "Number of generations? (default = 10000)");
    }

    int y = 0;
    Foreman5 foreman = new Foreman5(populationSize, knapsackCapacity,
            crossoverRate, mutationRate, saveBestMember, targetFitness,
            targetValue, numberOfRuns, numberOfGens);

    if (foreman.numberOfRuns > 1) {
        foreman.runResults = new ArrayList<>();
    }

    while (y < foreman.numberOfRuns) {

        int x = 0;
        boolean run = true;
        int generationCounter = 0;
        while (run == true) {

            if (x != generationCounter) {
                if (foreman.run(foreman, text.text, false, false, 
                        keyboardInput)) {
                    //   run = false;
                    if (y == 1) {
                        x = generationCounter;
                    }
                }
            } else {
                if (generationCounter == 0) {
                    if (foreman.run(foreman, text.text, true, true, 
                            keyboardInput)) {
                        //    run = false;
                    }
                } else {
                    if (foreman.run(foreman, text.text, false, true, 
                            keyboardInput)) {
                        //         run = false;
                    }
                }
            }

            if (x == generationCounter) {

                if (foreman.numberOfRuns == 1) {

                    int numberOfGenerations = keyboardInput.getInteger(
                            false, 10000, 0, 0, "Number of generations? "
                            + "(default = 10000; exit = 0)");
                    if (numberOfGenerations == 0) {
                        System.exit(0);
                    }
                    generationCounter += numberOfGenerations;
                    if (numberOfGenerations == 0) {
                        run = false;
                    }
                    if (keyboardInput.getCharacter(
                            true, 'N', "Y,N", 1, "Change any parameters? "
                                    + "(Y/N: default = N)") == 'Y') {

                foreman.knapsackCapacity = keyboardInput.getDouble(
                    false, 200, 0, 0, "Knapsack capacity? (default = 200)");
                foreman.crossoverRate = keyboardInput.getDouble(
                    true, .5, 0, 1, "Crossover rate? (0-1: default = .5)");
                foreman.mutationRate = keyboardInput.getDouble(
                    true, .5, 0, 1, "Mutation rate? (0-1: default = .5)");
                foreman.saveBestMember = keyboardInput.getCharacter(
                    true, 'Y', "Y,N", 1, "Save best solution from generation"
                        + " to generation? (Y/N: default = Y)");
                foreman.targetFitness = keyboardInput.getDouble(
                    true, 1, 0, 1, "Target fitness? (0-1: default = 1)");
                foreman.targetValue = keyboardInput.getInteger(
                    false, 1000, 0, 0, "Target value? (default = 1000)");
                    }
} else {
if (x == foreman.numberOfGenerations || 
        (foreman.generation.bestSolution.shipmentRevenue
    >= foreman.targetValue && foreman.generation.bestSolution.shipmentWeight 
    <= foreman.knapsackCapacity) || 
    (foreman.generation.bestSolution.normalizedFitness
    >= foreman.targetFitness && 
        foreman.generation.bestSolution.shipmentWeight <= 
        foreman.knapsackCapacity)) {

        foreman.runResults.add(foreman.generation);
        System.out.println("Run # " + (y + 1) + ", Generation " + 
            foreman.generation.currentGeneration + ": Best fitness="
            + foreman.generation.bestSolution.normalizedFitness
            + "; Weight=" + foreman.generation.bestSolution.shipmentWeight 
            + "; value=" + foreman.generation.bestSolution.shipmentRevenue);                   
                System.out.println("Best Genome:");
                System.out.println(foreman.generation.bestSolution.shipment);

                        run = false;
                    } else {
                        generationCounter++;
                    }
                }
            }
            x++;
        }
        y++;
    }
    if (foreman.numberOfRuns > 1) {
        System.out.println("Generation Count Summary");
        System.out.println("------------------------");

        ArrayList<Integer> sortGenerationCount = new ArrayList<>();
        int average = 0;

        for (int i = 0; i < foreman.runResults.size(); i++) {
            System.out.println(foreman.runResults.get(i).currentGeneration);
            sortGenerationCount.add(foreman.runResults.get(i).currentGeneration);
        }

        Collections.sort(sortGenerationCount);

        System.out.println("Sorted Generation Count Summary");
        System.out.println("-------------------------------");

        for (int i = 0; i < sortGenerationCount.size(); i++) {
            System.out.println(sortGenerationCount.get(i));
            average += sortGenerationCount.get(i);

        }
        average = average / foreman.numberOfRuns;
        System.out.println("Average number of generations = " + average);
        int generationRange = sortGenerationCount.get(
                sortGenerationCount.size() - 1) - sortGenerationCount.get(0);
        double histBarRange = generationRange / 10;
        double[][] runsPerBar = new double[10][2];
        for (int i = 0; i < 10; i++) {
            runsPerBar[i][0] = sortGenerationCount.get(0) + (histBarRange * i);

        }
        for (int i = 0; i < 10; i++) {
            if (i != 9) {
                for (int j = 0; j < sortGenerationCount.size(); j++) {
                    if (sortGenerationCount.get(j) >= runsPerBar[i][0]
                        && sortGenerationCount.get(j) < runsPerBar[i + 1][0]) {
                        runsPerBar[i][1]++;
                    }
                }
            } else {
                for (int j = 0; j < sortGenerationCount.size(); j++) {
                    if (sortGenerationCount.get(j) >= runsPerBar[i][0]) {
                        runsPerBar[9][1]++;
                    }
                }
            }
        }

        ImageConstruction myImage = new ImageConstruction(
                1000, 1000, -500, 500, -500, 500, 1);
        myImage.displayImage(true, "Image", true);
        myImage.imageOut.text = new String[31];
        myImage.imageOut.textLineCount = 31;
        myImage.imageOut.textPosition = new int[31][2];
        for (int i = 0; i < 10; i++) {

        myImage.insertBox(-300 + (i * 80), -300, -220 + (i * 80), Math.round((
            runsPerBar[i][1] / foreman.numberOfRuns) * 800) - 300,
                255, 255, 255, false);
        myImage.insertText(
                -315 + (i * 80), -320, String.valueOf(runsPerBar[i][0]), i);
        myImage.insertText(
                -270 + (i * 80), -295,
                String.valueOf((int) runsPerBar[i][1]), i + 10);

        }
        myImage.insertText(0, -390, "Number of Runs vs Generation Ranges", 20);
        myImage.insertText(0, -410, "Average generations for all runs = " + 
                String.valueOf(average), 21);

        myImage.insertText(-450, -360, "Population size = " + 
                String.valueOf(foreman.populationSize), 22);
        myImage.insertText(-450, -370, "Knapsack capacity = " + 
                String.valueOf(foreman.knapsackCapacity), 23);
        myImage.insertText(-450, -380, "Crossover rate = " + 
                String.valueOf(foreman.crossoverRate), 24);
        myImage.insertText(-450, -390, "Mutation rate = " +
                String.valueOf(foreman.mutationRate), 25);
        myImage.insertText(-450, -400, "Save best member = " +
                String.valueOf(foreman.saveBestMember), 26);
        myImage.insertText(-450, -410, "Target fitness = " + 
                String.valueOf(foreman.targetFitness), 27);
        myImage.insertText(-450, -420, "Target value = " + 
                String.valueOf(foreman.targetValue), 28);
        myImage.insertText(-450, -430, "Number of runs = " + 
                String.valueOf(foreman.numberOfRuns), 29);
        myImage.insertText(-450, -440, "Number of generations = " + 
                String.valueOf(foreman.numberOfGenerations), 30);

    }
    System.out.println("Program will terminate when the histogram is closed...");
}

//********************************************************************
//Method:      run
//Description: runs the GA, and manages the Generation class instance.
//Parameters:  Foreman5 foreman
//             String[] text
//             boolean firstGeneration
//             boolean askForDetails (true when we have run the user specified 
//             number of generations or when we have found the target 
//             value/fitness)
//             KeyboardInputClass keyboardInput
//Returns:     boolean targetFitnessOrRevenueReached (so we know to terminate)      
//Calls:       createInitialGeneration
//             generation.calculatePopulationMemberFitnesses
//             generation.sortPopulation
//             generation.copyPopulationMember
//             printGenerationValues
//             showDetails
//             generation.createCumulativeNormArray
//             generation.initializeCrossoverArray
//             generation.performCrossover


public boolean run(Foreman5 foreman, String[] text, boolean firstGeneration,
        boolean askForDetails, KeyboardInputClass keyboardInput) {

    if (firstGeneration == true) {
        foreman.createInitialGeneration(text);
    } else {

        if (foreman.knapsackCapacity != foreman.generation.knapsackCapacity) {
            foreman.generation.knapsackCapacity = foreman.knapsackCapacity;
            for (int i = 0; i < foreman.generation.population.size(); i++) {
                foreman.generation.population.get(i).knapsackCapacity
                        = foreman.knapsackCapacity;
                foreman.generation.population.get(i).initialWeightDistance
                    = Math.abs(foreman.knapsackCapacity - 
                        foreman.generation.population.get(i).shipmentWeight);
            }

        }

        foreman.generation.crossoverRate = foreman.crossoverRate;
        foreman.generation.mutationRate = foreman.mutationRate;
        foreman.generation.saveBestMember = foreman.saveBestMember;

        foreman.generation.currentGeneration++;
        foreman.generation.totalFitness = 0;
        foreman.generation.calculatePopulationMemberFitnesses();
    }
    foreman.generation.sortPopulation();

    if (firstGeneration == true) {
        foreman.generation.copyPopulationMember();
    } else if (foreman.generation.population.get(0).shipmentFitness
            >= foreman.generation.bestSolution.shipmentFitness) {
        foreman.generation.copyPopulationMember();
    }

    boolean targetFitnessOrRevenueReached = false;

    if (foreman.generation.bestSolution.normalizedFitness
        >= foreman.targetFitness || 
        foreman.generation.bestSolution.shipmentRevenue >= foreman.targetValue){
        targetFitnessOrRevenueReached = true;
    }

    if (foreman.numberOfRuns == 1) {
        if ((askForDetails || targetFitnessOrRevenueReached)) {
            printGenerationValues(foreman);
        }
    }

    if (foreman.numberOfRuns == 1) {
        if ((askForDetails || targetFitnessOrRevenueReached) && keyboardInput.
                getCharacter(true, 'N', "Y,N", 1, "Show details? (Y/N: default "
                        + "= N):") == 'Y') {
            foreman.showDetails(foreman, keyboardInput);
        }
    }

    foreman.generation.createCumulativeNormArray();
    foreman.generation.initializeCrossoverArray();
    foreman.generation.performCrossover();

    return targetFitnessOrRevenueReached;
}

//********************************************************************
//Method:      createInitialGeneration
//Description: Initializes the generation class
//Parameters:  Foreman5 foreman
//             String[] text
//Returns:     Nothing      
//Calls:       Generation class constructor
//             Generation.calculatePopulationMemberFitnesses


public void createInitialGeneration(String[] text) {
    generation = new Generation(
            populationSize, knapsackCapacity, crossoverRate, mutationRate,
            saveBestMember, text);

    generation.calculatePopulationMemberFitnesses();
}

//********************************************************************
//Method:      PrintGenerationValues
//Description: Prints the best fitness, weight, and value for each generation
//Parameters:  Foreman5 foreman
//Returns:     Nothing      
//Calls:       Nothing

public void printGenerationValues(Foreman5 foreman) {
    System.out.println("Generation " + foreman.generation.currentGeneration
        + ": Best fitness=" + foreman.generation.bestSolution.normalizedFitness
        + "; Weight=" + foreman.generation.bestSolution.shipmentWeight + 
            "; Value=" + foreman.generation.bestSolution.shipmentRevenue);
}

//********************************************************************
//Method: showDetails
//Description: prints the details for a generation or a population
//Parameters: Foreman5 foreman
//            KeyboardInputClass keyboardInput
//Returns:    Nothing      
//Calls:      Nothing

public void showDetails(Foreman5 foreman, KeyboardInputClass keyboardInput) {
    System.out.println("Details of best solution so "
            + "far as (item, weight, value) triples:");
    for (int i = 0; i < foreman.generation.bestSolutionItems.size(); i++) {
        System.out.print(
            "(" + (int) foreman.generation.bestSolutionItems.get(i)[0] + ", ");
        System.out.print(foreman.generation.bestSolutionItems.get(i)[1] + ", ");
        System.out.println(foreman.generation.bestSolutionItems.get(i)[2] + ")");

    }
    System.out.println(foreman.generation.bestSolutionItems.size()
            + " items selected (with weight="
            + foreman.generation.bestSolution.shipmentWeight + " and value="
            + foreman.generation.bestSolution.shipmentRevenue + ") out of "
            + foreman.generation.bestSolution.items.length + " total items");

    char showPopulationDetails = keyboardInput.getCharacter(
            true, 'N', "Y,N", 1, "Show population details? (Y/N: default = N)");
    if (showPopulationDetails == 'Y') {
        System.out.println(
                "Population members and attributes (fitness, weight, value):");
    for (int i = 0; i < foreman.populationSize; i++) {
        System.out.println(foreman.generation.population.get(i).shipment);
        System.out.print("(");
        System.out.printf(
                "%.3f", foreman.generation.population.get(i).normalizedFitness);
        System.out.println(
           ", " + foreman.generation.population.get(i).shipmentWeight
           + ", " + foreman.generation.population.get(i).shipmentRevenue + ")");

        }
    }
}

}
