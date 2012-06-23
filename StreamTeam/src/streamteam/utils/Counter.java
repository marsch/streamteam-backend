package streamteam.utils;

public interface Counter
{
	public void add(Object key);

	public void add(Object key, long cardinality);

	public long getCount(Object key);

	// public void add(Counter downloadCounter);
	public int size();

	public long count();
}
