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


public class KernelPerceptron extends Classifier implements OptionHandler{
	static int x = 10;//x-fold cross validation
	static boolean hasTrainingSet = false;
	static boolean hasTestSet = false;
	static DataSet train;
	public static int classIndex;
	static String[] options;
	int index_of_pos = 0,index_of_neg = 1;
	Double[] alpha;
	Boolean converged = false;
	Double eta = 0.5;
	Boolean pKernel = false, GKernel = true;
	Double d = 2.0,c = 1.0,sigma = 1.0 ;
	public static void main(String args[]) throws Exception
	{
		KernelPerceptron p = new KernelPerceptron(args);
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
	KernelPerceptron(String[] args)
	{

		setOptions(args);
	}

	 public void train(DataSet dataset) throws Exception
	{	 

		 //+1 dimension
		train=dataset;
		classIndex = dataset.attributes.classIndex;	
		Initialize_alpha(train.examples.size());//initialize alpha = 0 for all examples
												//, other initializations of weight
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
				Double temp = 0.0;
				Double result = 0.0;
				for(int j =0;j<train.examples.size();j++)
				{
					Double y_j = getActualSign(train.examples.get(j));
					if(pKernel)
					{
						temp = polynomialKernel(train.examples.get(i),train.examples.get(j));
					}
					if(GKernel)
					{
						temp = GaussianKernel(train.examples.get(i),train.examples.get(j));
					}
					result += temp*y_j*alpha[j];
				}
				result *= y_i;
				if(result<=0)
				{
					add_alpha(i,1);
					converged = false;
					//System.out.println(new ArrayList<Double>(Arrays.asList(alpha)));
					//System.out.println("false");
					//System.out.println("=====================");
						//for(int k=0;k<alpha.length;k++)
						//	System.out.print(alpha[k]+" ");
					//	System.out.println("=====================");
				}
				
			}

		}
	}

	 Double polynomialKernel(Example x1, Example x2)
	 {
		 Double result = 0.0;
		 for(int i =0;i<x1.size();i++)
		 {	
			 if(i!=classIndex){
				 result += x1.get(i)*x2.get(i);
			 }
			 else{
				 // add one dimension when calculating
				 result +=1;
			 }
		 }
		 result += c;
		 return Math.pow(result, d);
	 }
	 Double GaussianKernel(Example x1, Example x2)
	 {
		 Double dist = 0.0, temp = 0.0;
		 for(int i =0;i<x1.size();i++)
		 {
			 if(i!=classIndex){
				 temp = x1.get(i)-x2.get(i);
				 dist += Math.pow(temp, 2);
			 }
			 //add one dimension when calculating
		 }
		 temp =(-2)*Math.pow(sigma, 2);
		 return Math.pow(Math.E, dist/temp);
	 }
	 
	 void add_alpha(int i, int temp)
	 {
			 alpha[i] += 1;
	 }

	 
	 Double getActualSign(Example e)
	 {
		 classIndex = train.attributes.classIndex;
		 int index = e.get(classIndex).intValue();
		 if(index == index_of_pos)
			 return +1.0;
		 else
			 return -1.0;
		 // ok
	 }

	 void Initialize_alpha(int length)
	 {
		 //initialize w = 0
		 alpha = new Double[length];
		 for(int i =0;i<alpha.length;i++)
			 alpha[i] = 0.0;
	 }
	int classify(Example e) throws Exception
	{
		//没写
		//=0 属于哪一类？
		Double result = 0.0;//dotProduct(alpha,e);
		for(int i=0;i<train.examples.size();i++)
		{
			Double y_i = getActualSign(train.examples.get(i));
			if(pKernel)
			{
				result += alpha[i]*y_i*polynomialKernel(e,train.examples.get(i));
			}
			if(GKernel)
			{
				result += alpha[i]*y_i*GaussianKernel(e,train.examples.get(i));
			}
		}
		//System.out.println(e);
		//System.out.println(result);
		//System.out.println(index_of_neg);
		if(result>0)
			return index_of_pos;
		else
			return index_of_neg;
	}
	
	Performance classify(DataSet dataset) throws Exception
	{
		//System.out.println("classifying");
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
			if(args[i].compareTo("-d")==0)
			{
				d = Double.valueOf(args[i+1]);			
			}
			if(args[i].compareTo("-sigma")==0)
			{
				sigma = Double.valueOf(args[i+1]);			
			}
			if(args[i].compareTo("-c")==0)
			{
				c = Double.valueOf(args[i+1]);			
			}
			if(args[i].compareTo("-kernel")==0)
			{

				boolean flag = true;
				//select kernel function by -kernel 
				// default:Gaussian kernel
				if(args[i+1].compareToIgnoreCase("Gaussian")==0)
				{
					GKernel = true;
					pKernel = false;
					flag = false;
				}
				else if(args[i+1].compareToIgnoreCase("polynomial")==0)
				{

					pKernel = true;
					GKernel = false;
					flag = false;
				}
				if(flag)
				{
					System.out.println("Invalid kernel choice");
					System.exit(-1);
				}
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
