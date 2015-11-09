import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Scanner;


public class DataSet implements OptionHandler{
	protected Attributes attributes;
	protected Examples examples;
	protected String name;
	protected Random random;
	protected long seed;
	protected boolean isPartitioned = false;
	protected int[] partitionflag;
	Double [] p;
	boolean pIsCalculated = false;
	int index;
	
	public DataSet(Attributes attributes, String a) {
		this.attributes = attributes;
		this.name = a;
	}
	public DataSet(Attributes attributes) {
		this.attributes = attributes;
		this.examples = new Examples(attributes);
		index = this.attributes.getClassIndex();
	}

	public DataSet() {
		// TODO Auto-generated constructor stub
	}

	void add(DataSet dataset)
	{
		this.attributes = dataset.attributes;
		this.examples = dataset.examples;
		index = this.attributes.getClassIndex();
	}
	
	void add(Example example)
	{
		this.examples.add(example);
	}
	public Attributes getAttributes()
	{
		return this.attributes;
	}
	public Examples getExamples()
	{
		return this.examples;
	}
	public boolean getHasNominalAttributes()
	{
		for(int i=0;i<this.attributes.getSize();i++)
		{
			if(attributes.get(i).isNominal())
				return true;
		}
		return false;
	}
	public boolean getHasNumericAttributes()
	{
		for(int i=0;i<this.attributes.getSize();i++)
		{
			if(!attributes.get(i).isNominal())
				return true;
		}
		return false;
	}
	
	public void load(java.lang.String filename) throws java.lang.Exception

		{
		System.out.println(filename);
		File file = new File(filename);
		Scanner scan = new Scanner(new FileInputStream(file));	
		String datasetname="";		
		String s="";
		scan.useDelimiter("\n");
		while(scan.hasNext())
		{
				s=scan.next();
				if(s.contains("@dataset"))
				{
					
					String[] q =s.split(" ");
					datasetname = q[1];
				}
				if(s.contains("@attribute")) 
					{
					String parsing = "";
					while(s.contains("@attribute"))
					{
						parsing+=s+"\n";
						s=scan.next();
					}
				//	System.out.println(parsing);
					Scanner temp = new Scanner(parsing);
					this.attributes = new Attributes(temp);

					}
				else if(s.contains("@examples"))
				{
					String parsing="";
					while(scan.hasNext())
					{
						parsing+=scan.next()+"\n";
					}
					
					Scanner temp = new Scanner(parsing);
					this.examples = new Examples(this.attributes,temp);
				}
				//System.out.println(s);
		}
	//дdataset
		this.name = datasetname;
		index = this.attributes.getClassIndex();
	          }

	private void parse(java.util.Scanner scanner) throws java.lang.Exception
            {

            }
	public void addExamples(Examples examples)
	{
		this.examples = examples;
	}

	public java.lang.String toString()
	{
		String output = "@dataset "+name+"\n\n";

		output+=this.attributes.toString()+"\n";
		
		output+=this.examples.toString()+"\n\n\n";
		return output;
	}
	public void setRandom(java.util.Random random)
	{
		this.random = random;
	}
	
	public void setSeed(long seed)
	{
		this.seed = seed;
	}
	public long getSeed()
	{
		return this.seed;
	}
	public void Partition(int k)
	{
		random = new Random();
		//do not use seed in this project because the seed is a constant, will generate pseudo-random number
		//when implementing input a seed from command line, can use seed for generating.
		partitionflag = new int[this.examples.size()];
		for(int i=0;i<partitionflag.length;i++)
		{		
			partitionflag[i] = random.nextInt(k);
			//System.out.println(partitionflag[i]);
		}
		isPartitioned = true;
		
	}
	public TrainTestSets getCVSets(int p, int k)
	{
		//System.out.println(p+"===");
		TrainTestSets output = new TrainTestSets();
		DataSet newTrain = new DataSet(this.attributes);
		DataSet newTest = new DataSet(this.attributes);
		//int count1=0, count2=0;
		if(!isPartitioned)
			Partition(k);
		//build training set
		for(int i=0;i<this.examples.size();i++)
		{
			if(partitionflag[i]==p)
			{
				newTest.add(this.examples.get(i));
				//count1++;
			}
			else
			{

				Example temp = this.examples.get(i);
				newTrain.add(temp);
				//count2++;
			}
		}
		//System.out.println(count1+"   "+ count2);
		output.setTestingSet(newTest);
		output.setTrainingSet(newTrain);
		if(output.test.examples.size()<2||output.train.examples.size()<2)
		{
			System.out.println("There is not enough examples for each partition,"
					+ "please choose a new -x value and try again.");
			System.exit(0);
		}
		return output;
	}
	@Override
	public void setOptions(String[] args) {
		// TODO Auto-generated method stub
	}
	int getBestAttribute() throws Exception
	{
		index = this.attributes.getClassIndex();
		Double maxGainRatio=0.0; //the value must >0,if=0,means do not need to split
		int bestIndex=-1;
		for(int i=0;i<this.attributes.getSize();i++)
		{
			if(i==index) continue;
			
				if(getGainRatio(i)>maxGainRatio)
				{
					maxGainRatio = getGainRatio(i);
					//System.out.println(maxGainRatio+"DataSet::getBestAttribute");
					bestIndex = i;
				}
			
		}
		if(maxGainRatio==0.0) return -1;
		return bestIndex;
	}

	DataSet[] partitiononAttribute(int attributeIndex)//return DataSet[]
	{
		index = this.attributes.getClassIndex();
		DataSet[] DataSets = new DataSet[this.attributes.get(attributeIndex).domain.size()];
		for(int i=0;i<DataSets.length;i++)
		{
			DataSets[i] = new DataSet(this.attributes);
		}
		int temp =0;
		for(int i=0;i<this.examples.size();i++)
		{
			temp = this.examples.get(i).get(attributeIndex).intValue();
			DataSets[temp].add(this.examples.get(i));
		}
		return DataSets;
	}
	
	Double getGainRatio(int attributeIndex) throws Exception
	{
		if(getSplitInformation(attributeIndex)==0)
			return 0.0;
		//System.out.println(attributeIndex);
		//System.out.println(getGain(attributeIndex)/getSplitInformation(attributeIndex));
		return getGain(attributeIndex)/getSplitInformation(attributeIndex);
	}
	Double getGain(int attributeIndex) throws Exception
	{
		index = this.attributes.getClassIndex();
		Double entropy = getEntropy();
		Double gain = entropy;
		//System.out.println(entropy);
		//get sv/s
		Double [] count = new Double[this.attributes.get(attributeIndex).domain.size()];
		Double[] s = new Double[count.length];
		int temp = 0;
		for(int k=0;k<count.length;k++) count[k] =0.0;
		for(int i =0;i<this.examples.size();i++)
		{
			temp = examples.get(i).get(attributeIndex).intValue();
			count[temp] += 1;
		}
		for(int i=0;i<s.length;i++)
		{
			s[i] = (double) (count[i]/this.examples.size());
		}
		
		for(int i=0;i<this.attributes.get(attributeIndex).domain.size();i++) //ith attribute
		{
			Double all = 0.0;//define Double for the divide computation
			Double sEntropy = 0.0;
			Double[] counts = new Double[this.attributes.getClassAttribute().domain.size()];
			for(int k=0;k<counts.length;k++) counts[k] =0.0;
			for(int k =0;k<this.examples.size();k++)
			{
				if(examples.get(k).get(attributeIndex).intValue()==i)
				{
					temp = examples.get(k).get(index).intValue();
					counts[temp]+=1;
					all+=1;
				}
				
			}
			
			for(int j =0;j<this.attributes.getClassAttribute().domain.size();j++)//ith class label
			{
				if(all == 0||counts[j]==0)
					continue;
				sEntropy += -(counts[j]/all)*((Math.log((counts[j]/all))/Math.log(2))); 
				//System.out.println("counts[j]: "+counts[j]+" all: "+all+ " /: "+ counts[j]/all);
			}
			sEntropy *= s[i];
			//System.out.println(sEntropy);
			gain -= sEntropy;
			//System.out.println(gain);
		}

		return gain;
	}
	
	Double[] getP() throws Exception
	{
		index = this.attributes.getClassIndex();
		// class p
		if(pIsCalculated) 
			return p;
		Double [] count = new Double[this.attributes.getClassAttribute().domain.size()];
		for(int k=0;k<count.length;k++) count[k] =0.0;
		p = new Double[count.length];
		int temp = 0;
		for(int i =0;i<this.examples.size();i++)
		{
			temp = examples.get(i).get(index).intValue();
			count[temp] += 1;
		}
		for(int i=0;i<p.length;i++)
		{
			p[i] = (double) (count[i]/this.examples.size());
		}
		pIsCalculated = true;
		return p;
	}
	Double getEntropy() throws Exception
	{
		p = getP();
		Double entropy = 0.0;
		for(int i=0;i<p.length;i++)
		{
			if(p[i]==0) continue;
			entropy += -p[i]*(Math.log(p[i])/Math.log(2));
			//System.out.println(entropy);
		}
//	System.out.println(entropy);
	return entropy;	
	}
	Double getSplitInformation(int attributeIndex)
	{
		index = this.attributes.getClassIndex();
		Double splitInformation = 0.0;
		Double [] count = new Double[this.attributes.get(attributeIndex).domain.size()];
		for(int k=0;k<count.length;k++) count[k] =0.0;
		Double[] s = new Double[count.length];
		int temp = 0;
		for(int i =0;i<this.examples.size();i++)
		{
			temp = examples.get(i).get(attributeIndex).intValue();
			count[temp] += 1;
		}
		for(int i=0;i<s.length;i++)
		{

			s[i] = (double) (count[i]/this.examples.size());
			if(s[i]==0.0) continue;
			splitInformation += -s[i]*(Math.log(s[i])/Math.log(2));
			//System.out.println(Math.log(s[i])/Math.log(2));
		}
		//System.out.println("splitInformation  "+splitInformation);
		return splitInformation;
	}

}
