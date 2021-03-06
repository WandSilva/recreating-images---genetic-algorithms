package geneticAlgorithm;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Int;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;
import jmetal.util.wrapper.XReal;
import model.Images;
import utils.Colors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyProblem extends Problem {
    private Images images;
    private double[] mutationProbability_;
    private double[] mutationInterval_;

    public MyProblem(Integer numberOfVariables, String solutionType, String imagePath) throws IOException {
        this.images = new Images(imagePath);
        String[] path = imagePath.split("/");
        problemName_ = path[path.length-1];

        configProblem(numberOfVariables, solutionType);
    }

    public Images getImages() {
        return images;
    }

    public double[] getMutationInterval_() {
        return mutationInterval_;
    }

    public double[] getMutationProbability_() {
        return mutationProbability_;
    }

    @Override
    public void evaluate(Solution solution) throws JMException {

        XInt chromossome = new XInt(solution);

        int[] genotype = new int[numberOfVariables_];

        for (int i = 0; i < numberOfVariables_; i++) {
            genotype[i] = chromossome.getValue(i);
        }


        double fitness = this.images.getFitness(genotype);


        //setting the fitness of the solution
        solution.setObjective(0, fitness);
        solution.setFitness(fitness);

    }

    private void configProblem(Integer numberOfVariables, String solutionType) {
        numberOfVariables_ = numberOfVariables;//number of features (chromosome size).
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
        mutationProbability_ = new double[numberOfVariables_];
        mutationInterval_ = new double[numberOfVariables_];


        for (int i = 0; i < numberOfVariables_; i += Images.numFeatures) {
            lowerLimit_[i] = 0;
            lowerLimit_[1 + i] = 0;
            lowerLimit_[2 + i] = 0;
            lowerLimit_[3 + i] = 0;
            lowerLimit_[4 + i] = 0;
            lowerLimit_[5 + i] = 0;


            upperLimit_[i] = this.images.getWidth();
            upperLimit_[1 + i] = this.images.getHeight();
            upperLimit_[2 + i] = Math.max(this.images.getWidth(), this.images.getHeight());
            upperLimit_[3 + i] = 255;
            upperLimit_[4 + i] = numberOfVariables / Images.numFeatures;
            upperLimit_[5 + i] = 1;

            mutationInterval_[i] = 0.20;
            mutationInterval_[1 + i] = 0.20;
            mutationInterval_[2 + i] = 0.20;
            mutationInterval_[3 + i] = 0.20;
            mutationInterval_[4 + i] = 0.20;
            mutationInterval_[5 + i] = 1;
        }


        if (solutionType.compareTo("Real") == 0)
            solutionType_ = new RealSolutionType(this);
        else if (solutionType.compareTo("ArrayReal") == 0)
            solutionType_ = new ArrayRealSolutionType(this);
        else if (solutionType.compareTo("Integer") == 0)
            solutionType_ = new IntSolutionType(this);
        else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }
}
