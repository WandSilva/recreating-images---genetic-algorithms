package geneticAlgorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

public class MutationCreep extends Mutation {

	
	private Double mutationProbability_ = null;
	private Double mutatedValue_ = 1.0;
	private Properties configFile;
	double seed;
	/**
	* Valid solution types to apply this operator 
   	*/
	private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class, ArrayRealSolutionType.class) ;

	public MutationCreep(HashMap<String, Object> parameters) {
		super(parameters);
		 
		if (parameters.get("probability") != null){
	  		mutationProbability_ = (Double) parameters.get("probability") ;  
	  		  
		}
		if (parameters.get("mutatedValue") != null){
	  		mutatedValue_ = (Double) parameters.get("mutatedValue") ;  
	  		  
		}
	}

	/**
	 * Add random value for one position 
	 * @param probability
	 * @param solution
	 */
	private void doMutation(Double probability, Solution solution) {
		XReal chromosome = new XReal(solution) ;
		int position;
		int solutionLength = solution.numberOfVariables();
		if (PseudoRandom.randDouble() < probability) {
			 //Gets position that will mutate
			position = PseudoRandom.randInt(0,solutionLength-1);		
			//System.out.println("position: " + position);
			//Get value addition
			double mutation = PseudoRandom.randDouble(-mutatedValue_, mutatedValue_);
			//System.out.println("mutation: " + mutation);
			try {
				double getValuePosition = chromosome.getValue(position);
				double new_Gene = 0;
				//System.out.println("valor adicionado: " +mutation+ " valor na posição: "+ getValuePosition);
				new_Gene = getValuePosition + mutation;
				if(new_Gene < 0){
					new_Gene = 0;
				}
				
				chromosome.setValue(position, new_Gene);	
				//System.out.println("New Value Position: " +chromosome.getValue(position));
			} catch (JMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}
	
	@Override
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		
		if (!VALID_TYPES.contains(solution.getType().getClass())) {
		      Configuration.logger_.severe("UniformMutation.execute: the solution " +
		          "is not of the right type. The type should be 'Real', but " +
		          solution.getType() + " is obtained");

		      Class cls = java.lang.String.class;
		      String name = cls.getName(); 
		      throw new JMException("Exception in " + name + ".execute()") ;
		    } // if 

		
		  this.doMutation(mutationProbability_, solution);
		  return solution;
	}

	

}
