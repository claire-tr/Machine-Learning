import java.util.*;
public class Attribute extends java.lang.Object {

	protected String name; //this attribute's name
	java.util.ArrayList<java.lang.String> domain  = new ArrayList<String>();
	
	
	public Attribute()
		{
		
		}
	
	public Attribute(String a)  throws java.lang.Exception
	{
		this.name = a;
	}
	public String getName()  throws java.lang.Exception
		{
		 return name;
		}
	public int getSize()  throws java.lang.Exception
		{
		 return domain.size(); 
		}
	public void setName(String a)  throws java.lang.Exception
		{
		name = a;
		}
	public String toString()
		{
		String s = "";
		for(int i = 0;i < domain.size();i++){ 
			s+=domain.get(i)+" ";
		}
		return s;
		}
	public static void main(java.lang.String[] args)
	{
		
	}

	public Double getIndex(String next) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	public String whatAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValue(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isNominal()
	{
		return false;
	}
	public boolean isNumeric()
	{
		return false;
	}

}
