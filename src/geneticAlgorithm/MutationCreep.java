package geneticAlgorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Real;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XInt;
import jmetal.util.wrapper.XReal;
import model.Images;

public class MutationCreep extends Mutation {


    private double mutationSize;
    private Double mutationProbability_ = null;
    private Double mutatedValue_ = 1.0;
    private Properties configFile;
    double seed;
    private Images images;

    /**
     * Valid solution types to apply this operator
     */
    public void setImage(Images images) {
        this.images = images;
    }

    private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class);

    public MutationCreep(HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");

        }
        if (parameters.get("mutatedValue") != null) {
            mutatedValue_ = (Double) parameters.get("mutatedValue");

        }
        if (parameters.get("mutationSize") != null) {
            this.mutationSize = (Double) parameters.get("mutationSize");

        }
    }

    /**
     * Add random value for one position
     *
     * @param probability
     * @param solution
     */
    private void doMutation(Double probability, Solution solution) {
        double dropProbability = 0.1;
        XReal chromosome = new XReal(solution);
        int position;
        int solutionLength = solution.numberOfVariables();
        int changed = (int) (this.mutationSize * solutionLength);
        try {
            for (int i = 0; i < changed; i++) {
                position = PseudoRandom.randInt(0, solutionLength - 1);
                double getValuePosition = chromosome.getValue(position);
                double new_Gene = getValuePosition;

                double drop = PseudoRandom.randDouble(0, 1);
                if (drop < dropProbability) {
                    new_Gene = 0;
                }
                if (PseudoRandom.randDouble() < probability) {
                    //Gets position that will mutate
                    //System.out.println("position: " + position);
                    //Get value addition
                    double mutation = PseudoRandom.randDouble(-mutatedValue_, mutatedValue_);
                    //System.out.println("mutation: " + mutation);
                    new_Gene = getValuePosition + mutation;
                    //System.out.println("valor adicionado: " +mutation+ " valor na posição: "+ getValuePosition);


                    if (new_Gene < 0) {
                        new_Gene = 0;
                    }

                    if (new_Gene > 1) {
                        new_Gene = mutation;
                    }
                }
                chromosome.setValue(position, new_Gene);


            }
        } catch (JMException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("UniformMutation.execute: the solution " +
                    "is not of the right type. The type should be 'Real', but " +
                    solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if


        this.doMutation(mutationProbability_, solution);
        return solution;
    }


}
