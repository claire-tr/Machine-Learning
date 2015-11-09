import java.lang.Math;
public class NormalEstimator extends Estimator{
	double mean[],variance[];
	int count[];
	DataSet dataset;
	int index;
	int numofclasses;
	double temp[], temp2[],tem[] ;
	int classIndex;
	Double weight[];
	NormalEstimator(DataSet dataset, int index, int classIndex, Double w[]) throws Exception
	{
		this.weight = w;
		this.classIndex = classIndex;
		//System.out.println("build normal");
		this.dataset = dataset;
		this.index = index;
		numofclasses = dataset.attributes.get(classIndex).getSize();
		//System.out.println(numofclasses);
		mean = new double[numofclasses];
		variance = new double[numofclasses];
		count = new int[numofclasses];
		//mean
		
		double temp[] = new double[numofclasses];
		double temp2[] = new double[numofclasses];
		double tem[] = new double[numofclasses];
		for(int i=0;i<dataset.examples.size();i++)
		{
			//for each example
			for(int j=0;j<numofclasses;j++)
				if(dataset.examples.get(i).get(classIndex).intValue()==j)
			{
					temp[j] += dataset.examples.get(i).get(index)*weight[i]*dataset.examples.size();
					count[j]++;
			}
		}
		for(int i=0;i<numofclasses;i++)
		{
			mean[i] = temp[i]/count[i];
		}
		//variance
		for(int i=0;i<dataset.examples.size();i++)
		{
			for(int j=0;j<numofclasses;j++)
				if(dataset.examples.get(i).get(classIndex).intValue()==j)
			{
					tem[j]=dataset.examples.get(i).get(index)*weight[i]*dataset.examples.size()-mean[j];
					tem[j]=tem[j]*tem[j];
					temp2[j]+=tem[j];		
			}
		
		}
		for(int i=0;i<numofclasses;i++)
		variance[i]=temp2[i]/count[i];
		
	}
	void addValue(int k, int index)
	{
		//update statistics
		mean[index] = (mean[index]*count[index]+k)/(count[index]+1);
		tem[index]=0;
		temp[index]=0;
		for(int i=0;i<dataset.examples.size();i++)
		{
			tem[index]=dataset.examples.get(i).get(index)-mean[index];
			tem[index]=tem[index]*tem[index];
			temp[index]+=tem[index];			
		}
		variance[index]=temp[index]/count[index];
		
	}

	Double getProbability(double k,int index)
	{
		double output=0,expo=0,xishu=0;
		xishu=1/(Math.sqrt(variance[index]*2*Math.PI));
		expo=-(Math.pow(k-mean[index], 2))/(2*variance[index]);
		output=xishu*Math.pow(Math.E, expo);
		return output;
	}
}
