package geneticAlgorithm;

import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

public class MyAlgorithm extends Algorithm {

    private int copyBest;
    private double minConvergence;
    private BufferedWriter avgFitnessOutput;
    private BufferedWriter bestFitnessOutput;


    MyAlgorithm(Problem problem, int numberCopyBest, double minConvergence) {
        super(problem);
        this.copyBest = numberCopyBest;
        this.minConvergence = minConvergence;
        try {

            avgFitnessOutput = Files.newBufferedWriter(Paths.get(problem.getName() + "_avg_fitness_" + System.currentTimeMillis() + ".txt"));
            bestFitnessOutput = Files.newBufferedWriter(Paths.get(problem.getName() + "_best_fitness_" + System.currentTimeMillis() + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFitness(double avgFitness, double bestFitness) throws JMException {
        try {
            avgFitnessOutput.write(avgFitness + "\n");
            bestFitnessOutput.write(bestFitness + "\n");
        } catch (IOException e) {
            throw new JMException("Failed on writing fitness");
        }
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations = 0;
        double convergence;
        int iteration = 0;

        SolutionSet population;
        SolutionSet offspringPopulation;

        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;

        Comparator comparator;

        comparator = new FitnessComparator(); // Single objective comparator

        // Read the params
        populationSize = (Integer) this.getInputParameter("populationSize");
        maxEvaluations = (Integer) this.getInputParameter("maxEvaluations");
        // Initialize the variables
        population = new SolutionSet(populationSize);
        offspringPopulation = new SolutionSet(populationSize);


        // Read the operators
        mutationOperator = this.operators_.get("mutation");
        crossoverOperator = this.operators_.get("crossover");
        selectionOperator = this.operators_.get("selection");

        // Create the initial population
        Solution newIndividual;

        for (int i = 0; i < populationSize; i++) {
            newIndividual = new Solution(problem_);
            System.out.println("\nEvaluating solution " + i);
            problem_.evaluate(newIndividual);
            System.out.println("Fitness: " + newIndividual.getFitness());
            evaluations++;
            population.add(newIndividual);
        }

        // Sort population
        population.sort(comparator);
        double avgFitness = 0;
        double currentFitness = population.get(0).getFitness();

        double lastFitness;
        Solution bestIndividual = population.get(0);
        while (evaluations < maxEvaluations) {
            lastFitness = currentFitness;

            for (int i = 0; i < populationSize; i++) {
                avgFitness = population.get(i).getFitness();
            }

            avgFitness /= populationSize;

            this.writeFitness(avgFitness, currentFitness);

            // Copy the bests individuals to the offspring population
            int bestSize = copyBest;//number even
            for (int x = 0; x < bestSize; x++) {
                //offspringPopulation.add(new Solution(population.get((populationSize - 1) - x)));
                offspringPopulation.add(new Solution(population.get(x)));
            }

            // Reproductive cycle
            System.out.println("\ninitiating the reproductive cycle");
            for (int i = 0; i < ((populationSize - bestSize) / 2); i++) {

                // Selection
                Solution[] parents = new Solution[2];
                System.out.println("\n\nDoing selection...");
                parents[0] = (Solution) selectionOperator.execute(population);
                System.out.println("Fitness of the first selected: " + parents[0].getFitness());
                parents[1] = (Solution) selectionOperator.execute(population);
                System.out.println("Fitness of the second selected: " + parents[1].getFitness());

                // Crossover
                System.out.println("Doing crossover and mutation...");
                Solution[] offspring = (Solution[]) crossoverOperator.execute(parents);

                // Mutation
                mutationOperator.execute(offspring[0]);
                mutationOperator.execute(offspring[1]);

                // Evaluation of the new individual
                System.out.println("Evaluating the new offspring solution [1/2]");
                problem_.evaluate(offspring[0]);
                System.out.println("Evaluating the new offspring solution [2/2]");
                problem_.evaluate(offspring[1]);
                System.out.println("offspring 0 fitness: " + offspring[0].getFitness());
                System.out.println("offspring 1 fitness: " + offspring[1].getFitness());

                evaluations += 2;

                // Replacement: the two new individuals are inserted in the offspring population
                offspringPopulation.add(offspring[0]);
                offspringPopulation.add(offspring[1]);

            } // for
            System.out.println("\nReproductive cycle done");


            // The offspring population becomes the new current population
            population.clear();
            for (int i = 0; i < populationSize; i++) {
                population.add(offspringPopulation.get(i));
            }

            population.sort(comparator);
            currentFitness = population.get(0).getFitness();

            if (population.get(0).getFitness() < bestIndividual.getFitness()) {
                bestIndividual = population.get(0);
            }

            convergence = Math.abs(currentFitness - lastFitness);
            System.out.println("convergência: " + convergence + " min " + minConvergence);
            offspringPopulation.clear();

            for (int i = 0; i < population.size(); i++)
                System.out.println("fit " + i + ": " + population.get(i).getFitness());

            iteration++;
        } // while
        // Return a population with the best individual
        population.sort(comparator);

        System.out.println("Evaluations: " + evaluations);
        System.out.println("Iterations: " + iteration);
        System.out.println("Best: " + population.get(0));
        System.out.println("Best of all: " + bestIndividual);
        System.out.println("Worst: " + population.get(population.size() - 1));

        try {
            this.bestFitnessOutput.close();
            this.avgFitnessOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //population.remove(populationSize-1);
        //population.add(bestIndividual);
        population.sort(comparator);

        return population;
    }
}
