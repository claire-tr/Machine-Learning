
public class Scaler {
	public double max =Double.NEGATIVE_INFINITY;
	public double min =Double.POSITIVE_INFINITY;
	Attributes attributes;
	Scaler(Attributes att)
	{
		this.attributes = att;
	}
public Scaler() {
		// TODO Auto-generated constructor stub
	}
public Example scale(Example e,int index)
{
	if(e.get(index)<min)
		{
		e.set(index, 0.0);
		return e;
		}
	if(e.get(index)>max)
		{
		e.set(index, 1.0);
		return e;
		}
	e.set(index,e.get(index)-min/(max-min));
	return e;
}
public Examples scale(Examples es, int index)
{
	for(int j=0;j<es.size();j++)
	{
		if(es.get(j).get(index)>max)
			max = es.get(j).get(index);
			else if(es.get(j).get(index)<min)
				min = es.get(j).get(index);
	}
	//scale for each example
	for(int j=0;j<es.size();j++)
		es.set(j,scale(es.get(j),index));
	
	return es;
}

}
