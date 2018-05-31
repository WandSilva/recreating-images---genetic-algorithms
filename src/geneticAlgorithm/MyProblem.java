package geneticAlgorithm;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

public class MyProblem extends Problem {

    public MyProblem(Integer numberOfVariables, String solutionType) {
        configProblem(numberOfVariables, solutionType);
    }

    @Override
    public void evaluate(Solution solution) throws JMException {

        double fitness = 0;

        /* Esse XReal meio q permite trabalhar com uma solução como se fosse uma lista,
         * Em cada posição fica um gene
         * Cada gene será um dos parametros da nossa imagem
         * Pra pegar usa o .getValue(index)
         */
        XReal chromosome = new XReal(solution);

        /* ---------------------------------------------------------------------
         Aqui devemos pegar os valores do cromossomo e tentar recriar a imagem.

         fitnesse = funcaoDeCriacao(chromossome);  tipo isso kkkkkk
         ---------------------------------------------------------------------*/

        //setting the fitness of the solution
        solution.setObjective(0, fitness);
        solution.setFitness(fitness);

    }

    private void configProblem(Integer numberOfVariables, String solutionType) {
        numberOfVariables_ = numberOfVariables;//number of features (chromosome size).
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;
        problemName_ = "ImageProblem";

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        //defining a min and max value for each gene of the chromosome
        for (int i = 0; i < numberOfVariables_; i++) {
            lowerLimit_[i] = 0.0;
            upperLimit_[i] = 1.0;
        }

        if (solutionType.compareTo("Real") == 0)
            solutionType_ = new RealSolutionType(this);
        else if (solutionType.compareTo("ArrayReal") == 0)
            solutionType_ = new ArrayRealSolutionType(this);
        else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }
}
