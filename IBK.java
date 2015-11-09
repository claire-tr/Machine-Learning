//
// Yunyun Chen 
// yc555@georgetown.edu
// Platform: MacOS
// Language/Environment: java
//

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class IBK extends Classifier implements OptionHandler{
	public static int k = 3;
	static int x = 10;
	static boolean hasTrainingSet = false;
	static boolean hasTestSet = false;
	static DataSet train;
	DataSet test;
	public static int classIndex;
	static int sizeofclass;
	static int numofexamples;
	static String[] options;
	public static void main(String args[]) throws Exception
	{
		IBK ibk = new IBK(args);
		TrainTestSets TTS = new TrainTestSets(options);
		Evaluator evaluator = new Evaluator(ibk,TTS);
		if(hasTestSet)
		{
			evaluator.holdOut();
		}
		else{
			evaluator.kFold(x);
		}
		
		//System.out.println(TTS.toString());
	
		
	}
	IBK(String[] args)
	{
		setOptions(args);
	}

	 public void train(DataSet dataset) throws Exception
	{
		train=dataset;
		classIndex = dataset.attributes.classIndex;
		sizeofclass = dataset.attributes.get(classIndex).getSize();
		numofexamples = dataset.examples.size();
		int size = dataset.attributes.getSize();
		ArrayList<Scaler> scalers = new ArrayList<Scaler> (size);
		for(int i=0;i<size;i++)
		{
			scalers.add(new Scaler());
		}
		for(int i =0;i<size;i++)
		{
			if(dataset.attributes.get(i).isNumeric())
				{
				// new specific scaler 
				scalers.set(i, new Scaler(dataset.attributes));
				dataset.examples = scalers.get(i).scale(dataset.examples, i);//scale the ith attributes
				}
		}
	}
	
	int classify(Example e)
	{
		Double Distribution[] = getDistribution(e);
		Double max =0.0;
		Double maxindex=0.0;
		for(int i=0;i<sizeofclass;i++)
		{
			if(Distribution[i]>max)
			{
				max = Distribution[i];
				maxindex = (double) i;
			}
		}
		return maxindex.intValue();
	}
	
	Performance classify(DataSet dataset)
	{
		int[] result = new int[dataset.examples.size()];
		for(int i=0;i<dataset.examples.size();i++)
		{
			result[i] = classify(dataset.examples.get(i));
		}
		Performance p= new Performance(dataset, classIndex, result);
	//	System.out.println(p.getAccuracy());
		return p;
	}
	static Double calculateDistance(Example e1, Example e2)
	{
		//System.out.println("calculating distance...");
		Double dist=0.0;
		for(int i=0;i<train.attributes.getSize();i++)
		{
			if(i!=classIndex)
			{
				if(train.attributes.get(i).isNominal())
			{
				if(e1.get(i).compareTo(e2.get(i))!=0)
					{
					dist+=1;
					//System.out.println(e1.get(i)+"   "+e2.get(i));
					//System.out.println("distance  "+dist);
					}

			}
			else if(train.attributes.get(i).isNumeric())
			{
				if(e1.get(i)>=e2.get(i))
					dist+=e1.get(i)-e2.get(i);
					else
						dist+=e2.get(i)-e1.get(i);					
			}
			}
		}
		return dist;
	}
	Double[] getDistribution(Example e)
	{
		Double Distribution[] = new Double[sizeofclass];
		for(int i=0;i<sizeofclass;i++)
			Distribution[i]=(double) (1/numofexamples);
	    Comparator<Neighbour> cmp;
	    cmp = new Comparator<Neighbour>() {
	      public int compare(Neighbour n1, Neighbour n2) {
	    	  Double temp = (n1.distance - n2.distance);
	        return temp.intValue();
	      }
	    };
	    // a priority queue with a length of k
		PriorityQueue<Neighbour> KNN = new PriorityQueue<Neighbour>(k,cmp);
		for(int i=0;i<train.examples.size();i++)
		{
			double distance = calculateDistance(e,train.examples.get(i));
			Neighbour tem = new Neighbour(train.examples.get(i).get(classIndex),distance);
			KNN.add(tem);

		}
		for(int i=0;i<k;i++)
		{
			Neighbour nei = KNN.poll();
			//System.out.println(nei.classindex+"   "+nei.distance);
			int index = nei.classindex.intValue();
			Distribution[index]+=1;
		}
	
		//normalization
		double add=0;
		for(int i=0;i<sizeofclass;i++)
		{
			add+=Distribution[i];
		}
		for(int i=0;i<sizeofclass;i++)
		{
			Distribution[i]/=add;
		}

		
		return Distribution;
	}
	static void setx(String a)
	{
		x = Integer.valueOf(a);
	}
	static void setk(String a)
	{
		k = Integer.valueOf(a);
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
			if(args[i].compareTo("-k")==0)
			{
			setk(args[i+1]);
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
