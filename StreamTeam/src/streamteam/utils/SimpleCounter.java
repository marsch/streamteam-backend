package streamteam.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;



public final class SimpleCounter implements Counter
{
	private static Logger logger = Logger.getLogger(SimpleCounter.class.getName());
	//
	private HashMap map = null;

	//
	public SimpleCounter()
	{
		this(new HashMap());
	}

	public SimpleCounter(HashMap map)
	{
		this.map = map;
	}

	@Override
	public final void add(Object key)
	{
		Object value = map.get(key);
		if (value == null)
		{
			this.map.put(key, new Long(1));
		} else
		{
			Long integer = (Long) value;
			long intValue = integer.intValue();
			intValue++;
			this.map.put(key, intValue);
		}
	}

	public final void remove(Object key)
	{
		this.map.remove(key);
	}

	@Override
	public final void add(Object key, long cardinality)
	{
		Object value = this.map.get(key);
		if (value == null)
		{
			map.put(key, new Long(cardinality));
		} else
		{
			Long integer = (Long) value;
			long intValue = integer.intValue();
			intValue = intValue + cardinality;
			this.map.put(key, intValue);
		}
	}

	@Override
	public final long getCount(Object key)
	{
		Object value = map.get(key);
		if (value == null)
		{
			return 0;
		} else
		{
			Long integer = (Long) value;
			return integer.intValue();
		}
	}

	public void addCounter(SimpleCounter countingHashSet)
	{
		Set keys = countingHashSet.getMap().entrySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String currentKey = (String) entry.getKey();
			Integer currenValue = (Integer) entry.getValue();
			{
				Object value = map.get(currentKey);
				if (value == null)
				{
					map.put(currentKey, currenValue);
				} else
				{
					Integer integer = (Integer) value;
					int intValue = integer.intValue();
					intValue = intValue + currenValue.intValue();
					map.put(currentKey, new Integer(intValue));
				}
			}
		}
	}

	/**
	 * 
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuffer content = new StringBuffer(map.size() * 20);
		Set keys = map.entrySet();
		Iterator iter = keys.iterator();
		long entryCount = 0;
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String currentKey = (String) entry.getKey();
			Long currenValue = (Long) entry.getValue();
			entryCount = entryCount + currenValue.longValue();
		}
		content.append("size: ");
		content.append(map.size());
		content.append("\nentryCount: ");
		content.append(entryCount);
		content.append("\n");
		return content.toString();
	}

	

	/**
	 * sorted output
	 * 
	 * @return String
	 */
	public List<String> getOrderedResults()
	{
		List keyList = new ArrayList(map.keySet());
		List<String> result = new ArrayList<String>();
		// NumericStringComparator _NumericStringComparator = new NumericStringComparator();
		// Collections.sort(keyList, _NumericStringComparator);
		Collections.sort(keyList);
		Iterator iter = keyList.iterator();
		while (iter.hasNext())
		{
			// String currentKey = (String) iter.next();
			Object currentKey = iter.next();
			Long currenValue = (Long) map.get(currentKey);
			result.add(currentKey.toString());
		}
		return result;		
	}

	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public long count()
	{
		Set keys = map.entrySet();
		Iterator iter = keys.iterator();
		long entryCount = 0;
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String currentKey = (String) entry.getKey();
			Integer currenValue = (Integer) entry.getValue();
			entryCount = entryCount + currenValue.intValue();
		}
		return entryCount;
	}

	public Map getMap()
	{
		return map;
	}


	

}
