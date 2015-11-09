
abstract public class Classifier {


	
	int classify(Example e) throws Exception
	{	return 0;	}
	
	abstract Performance classify(DataSet dataset) throws Exception;
	
	Double[] getDistribution(Example e) throws Exception
	{
		Double[] a= {0.0};
		return a;
	}

	public void train(DataSet train) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
