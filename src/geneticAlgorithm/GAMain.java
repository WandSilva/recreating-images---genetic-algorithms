package geneticAlgorithm;

import jmetal.core.*;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;
import model.Images;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String imagePath;
    private String mutationSize;

    public GAMain() {
        this.configFileName = "GAConfig.properties";
        readConfiguration();
    }

    public void run() throws IOException {
        MyProblem problem;         // The problem to solve
        Algorithm algorithm;         // The algorithm to use
        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        HashMap parameters; // Operator parameters

        problem = new MyProblem(numberFeatures, solutionType, imagePath);
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
            //crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);
            crossover = new SinglePointCircleCrossover(parameters);

            /*Mutation*/
            parameters = new HashMap();
            parameters.put("mutationSize", Double.parseDouble(mutationSize));
            parameters.put("probability", Double.parseDouble(mutationProbability));
            parameters.put("mutatedValue", Double.parseDouble(mutatedValue));
            mutation = new MutationCircle(parameters, problem.getMutationProbability_(), problem.getMutationInterval_());
            //mutation = new BitFlipMutation(parameters);
            // mutation = new MutationCreep(parameters);
            //((MutationCreep) mutation).setImage(problem.getImages());

            /* Selection Operator */
            parameters = new HashMap();
            //tem outras tipos de seleção, temos que achar o melhor
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            /* Add the operators to the algorithm*/
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            //exec the algorithm
            SolutionSet population = algorithm.execute(); //gets the final population
            Solution s = population.best(Comparator.comparing(Solution::getFitness));
            XInt chromossome = new XInt(s);

            int[] genotype = new int[numberFeatures];
            for (int i = 0; i < numberFeatures; i++) {
                genotype[i] = chromossome.getValue(i);
            }

            int[][] matrix = problem.getImages().fromGenotype(genotype).getEvolutiveMatrix();
            Images.render(matrix);

        } catch (JMException | ClassNotFoundException e) {
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
            imagePath = configFile.getProperty("IMAGE_PATH");
            mutationSize = configFile.getProperty("MUTATION_SIZE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {

        LocalDateTime time = LocalDateTime.now();
        String startTime = time.format(DateTimeFormatter.ISO_LOCAL_TIME);

        new GAMain().run();

        time = LocalDateTime.now();
        String endTime = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.println("\nStart time: " + startTime);
        System.out.println("End time: " + endTime);
    }
}

