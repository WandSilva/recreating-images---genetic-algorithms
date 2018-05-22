package geneticAlgorithm;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public class MyAlgorithm extends Algorithm {

    private int numberCopyBest;
    private double convergence;

    public MyAlgorithm(Problem problem, int numberCopyBest, double convergence) {
        super(problem);
        this.numberCopyBest = numberCopyBest;
        this.convergence = convergence;
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        return null;
    }
}
