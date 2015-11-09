import java.util.ArrayList;
import java.util.Scanner;


public class Attributes {
	private java.util.ArrayList<Attribute>	attributes= new ArrayList<Attribute>();
	public int classIndex;//default
	private boolean hasNominalAttributes = false;
	private boolean hasNumericAttributes = false;
	
	public Attributes(Scanner temp) throws Exception {
		initialize();
		parse(temp);
	}
	public Attributes() {
		// TODO Auto-generated constructor stub
	}
	void add(Attribute attribute)
	{
		attributes.add(attribute);
		classIndex=this.attributes.size()-1;// update the classIndex everytime when add a new attribute
		if(attribute.whatAttribute().compareTo("Nominal")==0)
			hasNominalAttributes = true;
		else
			hasNumericAttributes = true;

		
	}
	void add(int index, Attribute attribute)
	{

			this.attributes.add(index, attribute);
			classIndex=this.attributes.size()-1;// update the classIndex everytime when add a new attribute

	}
	Attribute get(int i)
	{
		return this.attributes.get(i);
	}
	Attribute getClassAttribute() throws Exception
	{
		//
		return this.get(classIndex);
	}
	int getClassIndex()
	{
		return classIndex;
	}
	int getSize()
	{
		return attributes.size();
	}
	int getIndex(String name)
	{
		//写
		for(int i=0;i<attributes.size();i++)
			{
			if(attributes.get(i).name.compareTo(name)==0)
				return i;
			}
		return -1;
				
	}
	private void initialize()
	{
		
	}
	
	private void parse(Scanner scanner) throws Exception
	{
		//Parses the attribute declarations in the specified scanner.
		scanner.useDelimiter("\n");
		Attribute tem = new Attribute();
		while(scanner.hasNext())
		{
			String s = scanner.next();
			Scanner temp = new Scanner(s);
			tem=AttributeFactory.make(temp);
			this.attributes.add(tem);
		}
		classIndex=this.attributes.size()-1;
	}
	public String toString()
	{
		String output="";
		for( int i=0;i<attributes.size();i++ ) 			
			{
				output+=attributes.get(i).toString();
			}
		return output;
		
	}
	public void setClassIndex(int classIndex)
            throws java.lang.Exception
            {
				this.classIndex=classIndex;
            }
	public boolean getHasNumericAttributes()
	{
		return hasNumericAttributes;
	}
	
	public boolean getHasNominalAttributes()
	{
		return hasNominalAttributes;
	}
}
