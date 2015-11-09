import java.util.ArrayList;
import java.util.Arrays;

//
// Yunyun Chen 
// yc555@georgetown.edu
// Platform: MacOS
// Language/Environment: java
//
// In accordance with the class policies and Georgetown's Honor Code,
// I certify that, with the exceptions of the class resources and those
// items noted below, I have neither given nor received any assistance
// on this project.
//


public class Perceptron extends Classifier implements OptionHandler{
	static int x = 10;//x-fold cross validation
	static boolean hasTrainingSet = false;
	static boolean hasTestSet = false;
	static DataSet train;
	public static int classIndex;
	static String[] options;
	int index_of_pos = 0,index_of_neg =1;
	Double[] w;
	Boolean converged = false;
	Double eta = 0.5;
	public static void main(String args[]) throws Exception
	{
		Perceptron p = new Perceptron(args);
		TrainTestSets TTS = new TrainTestSets(options);
		Evaluator evaluator = new Evaluator(p,TTS);
		//System.out.println(TTS);

		if(hasTestSet)
		{
			evaluator.holdOut();
		}
		else{
			evaluator.kFold(x);
		}
	
	}
	Perceptron(String[] args)
	{
		setOptions(args);
	}
	 public void train(DataSet dataset) throws Exception
	{	 
		train=dataset;
		classIndex = dataset.attributes.classIndex;
		Initialize_w(dataset.attributes.classIndex);//initialize w = 0, other initializations of weight
					   //vector are possible, so keep it as a function.
		converged = false;
		Double y_i = 0.0;
		while(!converged)
		{
			//System.out.println("========while=========");
			converged = true;
			for(int i = 0;i<train.examples.size();i++)
			{
				y_i = getActualSign(train.examples.get(i));
				//System.out.println("y_i=  "+y_i);
				// get y_i
				if(y_i*dotProduct(w,train.examples.get(i))<=0)
				{
					add_w(y_i,train.examples.get(i));
					converged = false;
					//System.out.println("false");
					//System.out.println("=====================");
						
				}

				
			}
			
		}
		
	}
	 void add_w(Double y_i,Example e)
	 {
		 for(int i=0;i<w.length;i++)
		 {
			 if(i == classIndex)// add 1 dimension
			 {
				 w[i] += eta*y_i*1;
			 }
			 if(i != classIndex)
				 w[i] += eta*y_i*e.get(i);
		 }
		// System.out.println(new ArrayList<Double>(Arrays.asList(w)));
	 }
	 Double dotProduct(Double[] w, Example e)
	 {
		 //get w dot x
		 Double result = 0.0;
		 for(int i=0;i<e.size();i++)
		 {
			 if(i == classIndex)
			 {
				 result += w[i];
			 }
			 if(i != classIndex)
				 {
				 result += w[i]*e.get(i);
				 }
			 
		 }
		// System.out.println(result);
		 return result;
	 }
	 
	 Double getActualSign(Example e)
	 {
		 //get y_i
		 classIndex = train.attributes.classIndex;
		 int index = e.get(classIndex).intValue();
		 if(index == index_of_pos)
			 return +1.0;
		 else
			 return -1.0;
		 // ok
	 }

	 void Initialize_w(int length)
	 {
		 //initialize w = 0
		 w = new Double[length+1];
		 for(int i =0;i<w.length;i++)
			 w[i] = 0.0;
	 }
	int classify(Example e) throws Exception
	{
		Double result = dotProduct(w,e);
		//System.out.println(index_of_neg);
		if(result>0)
			return index_of_pos;
		else
			return index_of_neg;
	}
	
	Performance classify(DataSet dataset) throws Exception
	{
		classIndex = dataset.attributes.classIndex;
		int[] result = new int[dataset.examples.size()];
		for(int i=0;i<dataset.examples.size();i++)
		{
			result[i] = classify(dataset.examples.get(i));
		}
		Performance p= new Performance(dataset, classIndex, result);
	//	System.out.println(p.getAccuracy());
		return p;
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
			if(args[i].compareTo("-eta")==0)
			{
				eta = Double.valueOf(args[i+1]);
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
		//	System.out.println(options[i]);
			}
	}
}
