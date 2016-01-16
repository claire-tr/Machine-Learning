//
// Yunyun Chen 
// yc555@georgetown.edu
// Platform: MacOS
// Language/Environment: java
//

public class DT extends Classifier implements OptionHandler{
	static int x = 10;//x-fold cross validation
	static boolean hasTrainingSet = false;
	static boolean hasTestSet = false;
	static DataSet train;
	public static int classIndex;
	static String[] options;
	Double z = 0.6925; //by default
	Node root = new Node();
	public static void main(String args[]) throws Exception
	{
		DT dt = new DT(args);
		TrainTestSets TTS = new TrainTestSets(options);
		Evaluator evaluator = new Evaluator(dt,TTS);
		if(hasTestSet)
		{
			evaluator.holdOut();
		}
		else{
			evaluator.kFold(x);
		}
		
		//System.out.println(TTS.toString());
	
		
	}
	DT(String[] args)
	{
		setOptions(args);
	}

	 public void train(DataSet dataset) throws Exception
	{	 
		train=dataset;
		classIndex = dataset.attributes.classIndex;
		root.setCount(train);
		build(root,train);
		Prune(root); //prune from root		
	}
	 void outputAttribute(Node node)
	 {
		 //just for testing this program 
		 System.out.println(node.attributeIndex);
		 for(int i =0;i<node.childnode.size();i++)
			 outputAttribute(node.childnode.get(i));
	 }
	void build(Node node, DataSet train) throws Exception
	{
		if(node.isHomogeneous||train.examples.size()<=1)
		{
			return;
		}
		int bestAttribute = train.getBestAttribute();
		
		if(bestAttribute == -1) return;//in the get BestAttribute function, if the biggest Gain Ratio
										//equals 0, return -1.
		//System.out.println("Best Attribute:  "+bestAttribute+" @DT::build");
		node.attributeIndex = bestAttribute;
		//System.out.println(node.attributeIndex);
		DataSet[] datasets = train.partitiononAttribute(bestAttribute);
		for(int i=0;i<train.attributes.get(bestAttribute).getSize();i++)
		{
			Node c = new Node();
			c.setCount(datasets[i]);
			if(datasets[i].examples.size()==0)
				c.setClassLable(node.getClassLable());// if there are no corresponding example for this value
									// use the majority class label of the parent
			node.addChild(c);	
			build(c,datasets[i]);
		}
	}
	
	void Prune(Node root)
	{
		TraversePrune(root);
	}

	Boolean TraversePrune(Node node)// if is leaf, return true
	{
		Boolean allLeafChild = true;
		if(node.childnode.size()==0) 
		{
			return true;
		}
		
		for(int i=0;i<node.childnode.size();i++)
		{
			if(!TraversePrune(node.childnode.get(i)))// if one of the child is not leaf
				{
				allLeafChild = false;
				//return false;
				}
		}
		if(!allLeafChild) return false;
			
		//all child didn't return false, means all leaf node
		//whether to prune
		if(needPrune(node))
		{
			PruneNode(node);	
			//System.out.println("yes");
			return true; // become a new leaf node
		}
		//didn't prune
		else
			return false;
	}
	void PruneNode(Node node)
	{
		//System.out.println("prune node   "+node.attributeIndex);
		node.getClassLable();
		//remove all childnode fails, so just use a new arraylist to replace
		node.resetChild();
		//System.out.println(node.childnode.size());
	}
	Boolean needPrune(Node node)
	{
		Double npParent=0.0,npChild=0.0;
		//System.out.println("need prune  "+node.attributeIndex);
		npParent = node.solveforP(z)*node.getTotalNumber();

		//System.out.println(node.childnode.size());
		for(int i=0;i<node.childnode.size();i++)
		{
			Node child = node.childnode.get(i);
			npChild += child.solveforP(z)*child.getTotalNumber();
		//	System.out.println(child.solveforP(z));
		}
		//System.out.println(npParent+"  "+ npChild);
		if(npParent <= npChild) return true;
		else 
			return false;
	}
	int classify(Example e)
	{
		Node node = root;
		//System.out.println("root: "+root.attributeIndex);
		int valueIndex;
		while(node.childnode.size()>0)
		{
			valueIndex = e.get(node.attributeIndex).intValue();
			//System.out.println(valueIndex+"   "+node.childnode.size());
			//System.out.println("attribute index  "+node.attributeIndex);
			node = node.childnode.get(valueIndex);
		}
		return node.getClassLable();
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
