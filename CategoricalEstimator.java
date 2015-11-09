
public class CategoricalEstimator extends Estimator{
	public Double count[][];//attribute index & classindex
	public Double classcount[];//count of the class itself
	public int sizeofExamples=0;
	int numofClasses;
	Double weight[];
	DataSet dataset;
	int sizeofclass;
	CategoricalEstimator(DataSet dataset,int index, int classIndex, Double w[]) throws Exception
	{
		this.weight = w;
		int sizeofAttribute = dataset.attributes.get(index).getSize();
		sizeofclass = dataset.attributes.get(classIndex).getSize();
		this.dataset = dataset;
		sizeofExamples = dataset.examples.size()+sizeofclass;// add 1 smooth
		count = new Double[sizeofAttribute][sizeofclass];//store counts
		for(int i=0;i<sizeofAttribute;i++)
			for(int j=0;j<sizeofclass;j++)
				count[i][j]=1.0;// add 1 smooth
		classcount = new Double[sizeofclass];
		for(int i=0;i<sizeofclass;i++)
		{
			classcount[i]=1.0;//add 1 smooth for prior
		}
		for(int i=0;i<dataset.examples.size();i++)// go through examples
		{
			Example e = dataset.examples.get(i);
			int attributeindex = e.get(index).intValue();
			int classindex = e.get(classIndex).intValue();
			addValue(attributeindex,classindex,i);
		}

	
	}
	void addValue(int i, int j, int index)
	{
		count[i][j]+=weight[index]*dataset.examples.size();
		classcount[j]+=weight[index]*dataset.examples.size();
	}

	Double getProbability(int k)//prior
	{
		return classcount[k]/sizeofExamples;//already smoothed in construction method
	}
	public Double getProbability(int i, int j)//condition
	{
		//System.out.println("i j "+i+" "+j+"  "+count[i][j]/classcount[j]);
		return count[i][j]/(classcount[j]+sizeofclass+1);
	}

}
