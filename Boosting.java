

import java.util.ArrayList;

public class Boosting extends Classifier{
	static int x = 10;
	static boolean hasTrainingSet = false;
	static boolean hasTestSet = false;
	DataSet train,test;
	static Attributes attributes;
	static int classIndex;
	ArrayList<Classifier> models = new ArrayList<Classifier>();
	static String[] options;
	static String[] arguments;
	Double weight[];
	int t = 10; //default value of ensemble size
	ArrayList<Double> alpha = new ArrayList<Double>(); 

	public static void main(String args[]) throws Exception
	{
		Boosting b = new Boosting(args);
		TrainTestSets TTS = new TrainTestSets(options);
		//System.out.println(TTS.toString());
		attributes = TTS.train.attributes;
		Evaluator evaluator = new Evaluator(b,TTS);
		arguments = args;
		if(hasTestSet)
		{
			evaluator.holdOut();
		}
		else{
			evaluator.kFold(x);
		}
	
	}
	
	
	public Boosting(String[] args) {
		setOptions(args);
	}
	public void train(DataSet dataset) throws Exception
	{

		int classIndex = dataset.attributes.classIndex;
		Double error;
		weight = new Double[dataset.examples.size()];
		for(int i =0;i<weight.length;i++) weight[i] = 1/(0.0 + dataset.examples.size());//initialize equal weight
		if(t==1)
		{
			NaiveBayes nb = new NaiveBayes(arguments,weight);
			nb.train(dataset);
			models.add(nb);
			alpha.add(1.0);
			return;
		}		
		for(int i = 0;i<t;i++)
		{
			error = 0.0;
			NaiveBayes nb = new NaiveBayes(arguments,weight);
			nb.train(dataset);	
			int[] actualclass = new int[dataset.examples.size()];
			int[] result = new int[dataset.examples.size()];			
			for(int j=0;j<dataset.examples.size();j++)
				{
					actualclass[j] = dataset.examples.get(j).get(classIndex).intValue();
					result[j] = nb.classify(dataset.examples.get(j));
					if(result[j]!=actualclass[j])
					{
						//System.out.println(weight[j]);
						error += weight[j];
					}				
				}
			//System.out.println(error);
			if(error>0.5)
				{
				t = i;
				break;
				}
			models.add(nb);
			if(error==0)
			{
				t = i;
				break;
			}
			Double temp = (1-error)/error;
			alpha.add(0.5*Math.log(temp));
			for(int j = 0;j<weight.length;j++)
			{
				if(result[j]==actualclass[j])
				{
					weight[j] = weight[j]/(2*(1-error));
				}
				if(result[j]!=actualclass[j])
				{
					weight[j] = weight[j]/(2*error);
				}						
			}	

		}
	
	}

	int classify(Example e) throws Exception
	{
		Double prob[] = getDistribution(e);
		double maxprob=0;
		int maxclass=0;
		for(int i=0;i<prob.length;i++)
			if(prob[i]>maxprob)
				{
				maxprob=prob[i];
				maxclass=i;
				}
		return maxclass;
	}
	
	 Performance classify(DataSet dataset) throws Exception
	{
		 int[] result = new int[dataset.examples.size()];
			for(int i=0;i<dataset.examples.size();i++)
			{
				result[i] = classify(dataset.examples.get(i));
			}
			Performance performance= new Performance(dataset, classIndex, result);
			return performance;
	}

	 Double[] getDistribution(Example e) throws Exception
		{
		 classIndex = attributes.getClassIndex();
		 Double[] temp;
		 Double[] prob = new Double[attributes.get(classIndex).getSize()];
		 for(int i=0;i<prob.length;i++) prob[i] = 0.0;
		 for(int i=0;i<t;i++)
		 {
			 temp = models.get(i).getDistribution(e);
			 for(int j=0;j<temp.length;j++)
			 {
				 prob[j] += temp[j]*alpha.get(i);
			 }
		 }

		 return prob;
		}
		static void setx(String a)
		{
			x = Integer.valueOf(a);
		}

		public void setOptions(String args[])
		{

			int count = 0;
			String temp[] = new String[args.length];
			for(int i = 0 ; i< args.length-1; i+=2)
			{
				if(args[i].compareTo("-t")==0)
					{
					temp[i] = args[i];
					temp[i+1] = args[i+1];
					count = i+1;
					hasTrainingSet = true;
					}
				if(args[i].compareTo("-T")==0)
				{
					temp[i] = args[i];
					temp[i+1] = args[i+1];
					count = i+1;
					hasTestSet = true;
				}
				if(args[i].compareTo("-size")==0)
				{
					t = Integer.valueOf(args[i+1]);
				}
				if(args[i].compareTo("-x")==0)
				{
				setx(args[i+1]);
				}
				
			}
			options = new String[count+1];
			for(int i=0;i<count+1;i++)
				{
				options[i]=temp[i];
				}
		}

}


