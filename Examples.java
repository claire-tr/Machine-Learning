import java.util.Scanner;


public class Examples extends java.util.ArrayList<Example>{
	private Attributes attributeset = new Attributes();
	Examples(Attributes attributes)
	{
		attributeset = attributes;
	}
	
	Examples(Attributes attributes, Scanner scan)
	{
		Double attributeindex;
		this.attributeset = attributes;
		scan.useDelimiter("\n");

		while(scan.hasNext())
		{
			Example ex = new Example();
			String forparse = scan.next();
			//System.out.println(forparse);
			String parse[]=forparse.split(" ");					
					for(int k=0;k<parse.length;k++)
					{
						//System.out.print(parse[k]+"."); //
							attributeindex=attributeset.get(k).getIndex(parse[k]);
							ex.add(attributeindex);
						//System.out.println(attributeindex); // value's index

					}
				
			//
			//System.out.println(ex);
			this.add(ex);
		}
	}

	public String toString()
	{
		String output="@examples \n\n";
		for( int i=0;i<this.size();i++ ) 			
			{
			for(int j=0;j<this.attributeset.getSize();j++)
			{
				if(this.attributeset.get(j).whatAttribute()=="Numeric")
					output+=this.get(i).get(j).toString()+" ";
				else
				{
					Double temp=this.get(i).get(j);
					int index = temp.intValue();
					output+=this.attributeset.get(j).getValue(index)+" ";
				}
					
			}
			output+="\n";
			}
		return output;
	}
}
