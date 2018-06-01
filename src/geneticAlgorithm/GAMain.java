package geneticAlgorithm;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.SwapMutation;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

public class GAMain {

    private String configFileName;
    private int numberCopyBest;
    private String solutionType;
    private int numberFeatures;
    private String crossoverProbability;
    private String mutationProbability;
    private String mutatedValue;
    private String populationSize;
    private String maxEvaluation;
    private String alpha;
    private double convergence;

    public GAMain() {
        this.configFileName = "GAConfig.properties";
        readConfiguration();
    }

    public void run() {
        Problem problem;         // The problem to solve
        Algorithm algorithm;         // The algorithm to use
        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        HashMap parameters; // Operator parameters

        problem = new MyProblem(numberFeatures, solutionType);
        algorithm = new MyAlgorithm(problem, numberCopyBest, convergence);

        /* Algorithm parameters*/
        algorithm.setInputParameter("populationSize", Integer.parseInt(populationSize));
        algorithm.setInputParameter("maxEvaluations", Integer.parseInt(maxEvaluation));

        try {// Mutation and Crossover for Real codification
            /* Crossover */
            parameters = new HashMap();
            parameters.put("probability", Double.parseDouble(crossoverProbability));
            double alphaValue = Double.parseDouble(alpha);
            parameters.put("alpha", alphaValue);
            parameters.put("comparator", Comparator.comparing(Solution::getFitness).reversed());
            //tem outras tipos de crossover, temos que achar o melhor
            crossover = CrossoverFactory.getCrossoverOperator("BLXAlphaCrossover", parameters);

            /*Mutation*/
            parameters = new HashMap();
            parameters.put("probability", Double.parseDouble(mutationProbability));
            parameters.put("mutatedValue", Double.parseDouble(mutatedValue));
            mutation = new SwapMutation(parameters); //tem outras tipos de mutação, temos que achar o melhor

            /* Selection Operator */
            parameters = new HashMap();
            parameters.put("comparator", Comparator.comparing(Solution::getFitness).reversed());
            //tem outras tipos de seleção, temos que achar o melhor
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            /* Add the operators to the algorithm*/
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            //exec the algorithm
            algorithm.execute();

        } catch (JMException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readConfiguration() {
        Properties configFile = new Properties();
        try {
            configFile.load(new FileInputStream(configFileName));
            numberCopyBest = Integer.parseInt(configFile.getProperty("NUMBERCOPYBESTSOLUTION"));
            solutionType = configFile.getProperty("SOLUTION_TYPE");
            numberFeatures = Integer.parseInt(configFile.getProperty("NUMBER_FEATURES"));
            crossoverProbability = configFile.getProperty("CROSSOVER_PROBABILITY");
            mutationProbability = configFile.getProperty("MUTATION_PROBABILITY");
            mutatedValue = configFile.getProperty("MUTATION_INTERVAL");
            populationSize = configFile.getProperty("POPULATION_SIZE");
            maxEvaluation = configFile.getProperty("MAXEVALUATIONS");
            alpha = configFile.getProperty("ALPHA");
            convergence = Double.parseDouble(configFile.getProperty("CONVERGENCE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

