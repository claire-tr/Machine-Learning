import java.math.BigDecimal;
public class Performance {
	DataSet dataset;
	int classIndex;
	int[] result;
	int[] actualclass;
	Double numofcorrect;
	Double numofwrong;
	Double[] accuracy;
	Double meanaccuracy;
	Double variance = 0.0;
	Double maxdiff=0.0;
	int k;//k-fold cross validation
	static int i;
	Performance(int k)
	{
		this.k = k;
		accuracy = new Double[k];
		i=0;
	}
	Performance(DataSet dataset, int classIndex,int[] result)
	{
		this.dataset = dataset;
		this.classIndex = classIndex;
		this.result = result;
		actualclass = new int[dataset.examples.size()];
		for(int i=0;i<dataset.examples.size();i++)
		{
			actualclass[i] = dataset.examples.get(i).get(classIndex).intValue();
		}
		numofcorrect = 0.0;
		numofwrong = 0.0;
		for(int i=0;i<actualclass.length;i++)
		{
			if(actualclass[i]==result[i])
				numofcorrect+=1;
			else
				numofwrong+=1;
		}
	}
	
	String getStringAccuracy()
	{
		Double acc = numofcorrect/(numofwrong+numofcorrect);
		String output = acc*100+"%";
		return output;
		
	}
	Double getDoubleAccuracy()
	{
		return numofcorrect/(numofwrong+numofcorrect);
		
	}
	
	public String toString()
	{
		return "";
	}
	public void addAccuracy(Double d1)
	{
		if(i<k)
		{
			this.accuracy[i] = d1;
			i++;
		}
	}
	public Double getDoubleMeanAccuracy()
	{
		double temp=0;
		for(int j=0;j<k;j++)
		{
			temp+=this.accuracy[j];
		}
		meanaccuracy = temp/k;
		return temp/k;
	}
	public String getStringMeanAccuracy()
	{

		getDoubleMeanAccuracy();
		for(int j=0;j<k;j++)
		{
			if(this.accuracy[j]-meanaccuracy>maxdiff)
				maxdiff = this.accuracy[j]-meanaccuracy;
			if(meanaccuracy-this.accuracy[j]>maxdiff)
				maxdiff = meanaccuracy-this.accuracy[j];
			//System.out.println(this.accuracy[j]);
		}
		BigDecimal bd = new BigDecimal(meanaccuracy*100);
		BigDecimal maxbd = new BigDecimal(maxdiff*100);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		maxbd = maxbd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd+"% ± "+maxbd+"%";
	}
	public Double getVariace()
	{
		double temp=0;
		for(int j=0;j<k;j++)
		{
			temp=Math.pow(this.accuracy[j]-meanaccuracy, 2);
			variance+=temp;
		}
		variance = variance/k;
		BigDecimal va = new BigDecimal(variance);
		//System.out.println(variance);
		va =  va.setScale(5, BigDecimal.ROUND_HALF_UP);
		return va.doubleValue();
	}

}
