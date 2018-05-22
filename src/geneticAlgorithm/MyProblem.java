package geneticAlgorithm;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class MyProblem extends Problem {

    public MyProblem(Integer numberOfVariables, String solutionType) {
        configProblem(numberOfVariables, solutionType);
    }

    @Override
    public void evaluate(Solution solution) throws JMException {

    }

    private void configProblem(Integer numberOfVariables, String solutionType) {
        numberOfVariables_ = numberOfVariables;//number of features
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;
        problemName_ = "CredPloblem";

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        for (int i = 0; i < numberOfVariables_; i++) {
            lowerLimit_[i] = 0.0;
            upperLimit_[i] = 1.0;
        } // for

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
