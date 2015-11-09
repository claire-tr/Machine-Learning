import java.util.ArrayList;


public class Node {
	ArrayList<Node> childnode  = new ArrayList<Node>();	
	int attributeIndex=-1;//if =-1 by default, leaf node
	int count[] = {0};// count for each class index domain value
	int classIndex;
	int classLable = -1;//by majority
	boolean hasSetCount = false;
	boolean isHomogeneous = false;

	void addChild(Node c)
	{
		this.childnode.add(c);
	}
	void resetChild()
	{
		this.childnode = new ArrayList<Node>();
	}
	public void setCount(DataSet set) throws Exception
	{
		classIndex = set.attributes.classIndex;
		count = new int[set.attributes.getClassAttribute().domain.size()];
		for(int i = 0;i<set.examples.size();i++)
		{
			int temp = set.examples.get(i).get(classIndex).intValue();
			count[temp]++;
		}
		hasSetCount = true;
		
		//isHomogeneous
		int c =0;
		for(int i=0;i<count.length;i++)
		{
			if(count[i]>0) c++;
		}
		if(c>1)
			isHomogeneous = false;
		else isHomogeneous = true;
		getClassLable();
	}
	boolean isHomogeneous()
	{
		if(!hasSetCount)//when building each non-root node, will set count immediately.
						//so if hasSetCount = false, means it is the root node
		{
			return false;
		}
		int c =0;
		for(int i=0;i<count.length;i++)
		{
			if(count[i]>0) c++;
		}
		if(c>1)
			return false;
		else return true;
	}
	int getClassLable()
	{
		int maxcount = 0;
		for(int i=0;i<count.length;i++)
		{
			if(count[i]>maxcount)
			{
				maxcount = count[i];
				classLable = i;
			}
		}
		//System.out.println(classLable);
		return classLable;
	}
	void setClassLable(int a)
	{
		this.classLable = a;
	}
	int getTotalNumber() //get n
	{
		int amount=0;
		for(int i=0;i<count.length;i++)
		{
			amount += count[i];
		}
		return amount;
	}
	int getX()
	{
		int x = 0;
		for(int i=0;i<count.length;i++)
		{
			if(i==classLable) continue;
			else
				x += count[i];
		}
		return x;
	}
	Double solveforP(Double z)
	{
		Double p = 0.0;
		Double up1,up2,down;
		int n = getTotalNumber();
		if(n == 0) return 0.0;
		int x = getX();
		up1 = x + 0.5 + z*z/2;
		up2 = (x+0.5)*(1-(x+0.5)/(double)n)+z*z/4;
		up2 *= z*z;
		up2 = java.lang.Math.sqrt(up2);
		down = n+z*z;
		p = (up1+up2)/down;
	//	System.out.println("solving   "+up2+"  "+down);
		return p;		
	}
	
	Double getAllChildP(Double z)
	{
		Double p = 0.0;
		if(this.childnode.size()==0)
			return Double.NEGATIVE_INFINITY;//if leaf node, child p always smaller, means don't prune
		else
			for(int i=0;i<this.childnode.size();i++)
				p += this.childnode.get(i).solveforP(z);		
		return p;
			
	}
}
