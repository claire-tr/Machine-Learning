//
// Yunyun Chen 
// yc555@georgetown.edu
// Platform: MacOS 
// Language/Environment: java
//

import java.util.ArrayList;

public class NaiveBayes extends Classifier{
	int x = 10;
	boolean hasTrainingSet = false;
	boolean hasTestSet = false;
	DataSet train;
	Attributes attributes;
	int classIndex;
	ArrayList<Estimator> estimators = new ArrayList<Estimator>();
	String[] options;
	Double weight[];

	NaiveBayes(String[] args, Double w[])
	{
		setOptions(args);
		this.weight = w;
	}

	public void train(DataSet dataset) throws Exception
	{
		//System.out.println(dataset.attributes.get(dataset.attributes.classIndex));		
		int numofExamples = dataset.examples.size();
		int numofAttributes = dataset.attributes.getSize();
		classIndex = dataset.attributes.getClassIndex();
		this.attributes = dataset.attributes;
		for(int i =0;i<numofAttributes;i++)
		{
			{
				if(dataset.attributes.get(i).isNominal())
				{
					CategoricalEstimator e = new CategoricalEstimator(dataset,i,classIndex,this.weight);//joint estimator for ith attribute and class
					estimators.add(e);
				}
				else 
				{
					NormalEstimator e = new NormalEstimator(dataset,i,classIndex,this.weight);
					estimators.add(e);
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
			//获得每个类的概率
			Double prior[] = new Double[attributes.get(classIndex).getSize()];
			Double posterior[] = new Double[attributes.get(classIndex).getSize()];
			for(int i=0;i<attributes.get(classIndex).getSize();i++)
			{
				posterior[i]=1.0;
			}
			Double prob[] = new Double[attributes.get(classIndex).getSize()];
			for(int i=0;i<attributes.get(classIndex).getSize();i++)
			{
				//System.out.println(classIndex);
	
				//calculate  prob for ith class in the attribute
				if(i!=classIndex)
				{
					//calculate posterior
					for(int j=0;j<attributes.getSize();j++)
					{
						if(j!=classIndex)//skip classIndex
						{
						//j is the index of attribute
						int indexofClass=e.get(j).intValue();
						if(attributes.get(j).isNominal())
						{
							posterior[i]*=estimators.get(j).getProbability(indexofClass, i);
							//System.out.println(posterior[i]);
						}
						if(attributes.get(j).isNumeric())
							{
							NormalEstimator temp = (NormalEstimator)estimators.get(j);
							posterior[i]*=temp.getProbability((double)e.get(j),i);
							//System.out.println(e.get(j));
							//System.out.println(posterior[i]);
							}
						
						//System.out.println("jth attribute "+j +"  post  i  "+ i+"  "+posterior[i]);
					}
					
				}
				prior[i]=estimators.get(classIndex).getProbability(i);//prior
				//System.out.println(prior[i]);
				prob[i]=prior[i]*posterior[i];
				//System.out.println(posterior[i]);
				//System.out.println("i  "+i+"  "+prob[i]);
			}
				if(i == classIndex) prob[i] =0.0;
			}


	return prob;
		}
	 
		
	

	void setx(String a)
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
				}
			if(args[i].compareTo("-T")==0)
			{
				temp[i] = args[i];
				temp[i+1] = args[i+1];
			count = i+1;
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
