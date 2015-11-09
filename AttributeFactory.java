import java.util.Scanner;


public class AttributeFactory {
//A factory for making attributes from a scanner. An attribute has a name, and domain. Types are nominal or numeric.

	public static Attribute make (Scanner scanner) throws Exception
	{
		
		String s = scanner.next();
		s = scanner.next();//skip the "@attribute"
		if(scanner.findInLine("numeric")!=null)
		{
			NumericAttribute na = new NumericAttribute();
			na.setName(s);
			return na;
		}
		else
			{
			NominalAttribute a = new NominalAttribute();
			a.setName(s);
			while(scanner.hasNext())
			{
				a.addValue(scanner.next());
			}
			return a;
			}	
	}
}
