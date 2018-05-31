package geneticAlgorithm;

import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

public class MyAlgorithm extends Algorithm {

    private int copyBest;
    private double minConvergence;

    public MyAlgorithm(Problem problem, int numberCopyBest, double minConvergence) {
        super(problem);
        this.copyBest = numberCopyBest;
        this.minConvergence = minConvergence;
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations = 0;
        double currentFitness = 0;
        double lastFitness = 0;
        double convergence = 100;
        int generation = 0;
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
        generation++;

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
        currentFitness = population.get(populationSize - 1).getFitness();
        generation++;

        while ((convergence > minConvergence) && evaluations < maxEvaluations) {
            lastFitness = currentFitness;
            currentFitness = 0;

            // Copy the bests individuals to the offspring population
            int bestSize = copyBest;//number even
            for (int x = 0; x < bestSize; x++) {
                offspringPopulation.add(new Solution(population.get((populationSize - 1) - x)));
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
            currentFitness = population.get(populationSize - 1).getFitness();
            convergence = Math.abs(currentFitness - lastFitness);
            System.out.println("convergência: " + convergence + " min " + minConvergence);
            offspringPopulation.clear();

            for (int i = 0; i < population.size(); i++)
                System.out.println("fit " + i + ": " + population.get(i).getFitness());

            generation++;
            iteration++;
        } // while
        // Return a population with the best individual
        SolutionSet resultPopulation = new SolutionSet();
        population.sort(comparator);
        resultPopulation = population;

        System.out.println("Evaluations: " + evaluations);
        return resultPopulation;
    }
}
