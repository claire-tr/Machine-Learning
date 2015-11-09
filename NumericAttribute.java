
public class NumericAttribute extends Attribute{

	public Double getIndex(String value)
	{
		Double a = Double.valueOf(value);
		//System.out.println(value);
		return a;
	}
	public String whatAttribute()
	{
		return "Numeric";
	}
	public boolean isNominal()
	{
		return false;
	}
	public boolean isNumeric()
	{
		return true;
	}
	public boolean validValue(java.lang.Double value)
	{
		return true;
	}
	public java.lang.String toString()
	{
		String output="@attribute "+this.name+" "+"numeric"+"\n";
		return output;
	}
}
