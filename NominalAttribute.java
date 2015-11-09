
public class NominalAttribute extends Attribute {
	
	private boolean sorted;

	public void addValue(String value)
	{
		domain.add(value);
	}
	
	public Double getIndex(String value)
	{
		Double a = new Double(domain.indexOf(value));
		return a;
	}
	public String getValue(int index)
	{
		return (String) domain.get(index);
	}
	public boolean validValue(String value)
	{
		return true;
	}
	public String whatAttribute()
	{
		return "Nominal";
	}
	public boolean isNominal()
	{
		return true;
	}
	public boolean isNumeric()
	{
		return false;
	}
	public int getSize()
	{
		return domain.size();
	}
	public java.lang.String toString()
	{
		String output = "@attribute "+this.name +" ";
		for(String tem:domain) output+=tem+" ";
		output+="\n";
		return output;
	}
}
