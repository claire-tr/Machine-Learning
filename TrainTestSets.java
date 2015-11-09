import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;


public class TrainTestSets implements OptionHandler{

		protected DataSet test;
		protected DataSet train;
		protected String[] options;
		public boolean hasTrain = false;
		public boolean hasTest = false;
		
		TrainTestSets()		{}
		TrainTestSets(DataSet train, DataSet test)
		{
			this.test = test;
			this.train = train;
			hasTrain = true;
			hasTest = true;
		}
		
		TrainTestSets(java.lang.String[] options) throws Exception
		{
			boolean flag=false;

			if(options.length==2)
			{
				if(options[0].compareTo("-t")==0)
					{
					train = buildSet(options[1]);
					//System.out.println(train.toString());
					hasTrain = true;
					flag=true;
					}
				
				if(options[0].compareTo("-T")==0)
				{
				test = buildSet(options[1]);
				//System.out.println(test.toString());
				hasTest = true;
				flag=true;
				}

			}
			else if(options.length==4)
			{
				for(int i =0;i<options.length;i=i+2)
				{
					if(options[i].compareTo("-t")==0)
					{
					train = buildSet(options[i+1]);
					//System.out.println(train.toString());
					hasTrain = true;
					flag=true;
					}
					else if(options[i].compareTo("-T")==0)
					{
					test = buildSet(options[i+1]);
					//System.out.println(test.toString());
					hasTest = true;
					flag=true;
					}
				}
			}
			if(!flag)
				System.out.println("Invalid Arguments(Options)!");
			if((!hasTest)&&!(hasTrain)) System.out.println("Invalid file format!");
		}
		public DataSet buildSet(String filename) throws Exception
		{
			File file = new File(filename);
			Attributes attributeset = new Attributes();
		    Scanner scan = new Scanner(new FileInputStream(file));	
		    String datasetname="";
		    Examples exampleset = new Examples(attributeset);
		
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
					attributeset = new Attributes(temp);

					}
				else if(s.contains("@examples"))
				{
					String parsing="";
					while(scan.hasNext())
					{
						parsing+=scan.next()+"\n";
					}
					
					Scanner temp = new Scanner(parsing);
					exampleset = new Examples(attributeset,temp);
				}
				//System.out.println(s);
		}
	//dataset
		DataSet trainningset = new DataSet(attributeset, datasetname);
		trainningset.addExamples(exampleset);
		return trainningset;
	}
		
		DataSet getTestingSet()
		{
			return test;
		}
		DataSet getTrainingSet()
		{
			return train;
		}
		public void setOptions(String[] options)
		{
			this.options = options;
		}
		void setTestingSet(DataSet test)
		{
			this.test = test;
		}
		void setTrainingSet(DataSet train)
		{
			this.train = train;
		}
		public java.lang.String toString()
		{
			String output ="";
			if(hasTrain)
				output+=this.train.toString()+"\n\n";
			if(hasTest)
				output+=this.test.toString()+"\n";
			return output;
		}
}
