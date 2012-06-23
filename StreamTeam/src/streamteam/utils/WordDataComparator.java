package streamteam.utils;

import java.util.Comparator;
import java.util.logging.Logger;





public class WordDataComparator
	implements Comparator
{
	private static Logger logger = Logger.getLogger(WordDataComparator.class.getName());
	//
	public static final int WORDDATA_LABEL = 1;
	public static final int WORDDATA_CARDINALITY = 2;
	public static final int WORDDATA_FREQUENCYRANK = 3;

	//
	private int comparisonAttribute = -1;
	private boolean ascending = true;
	//
	public WordDataComparator(int comparisonAttribute, boolean ascending)
	{
		if (comparisonAttribute < this.WORDDATA_LABEL)
		{
			throw new IllegalArgumentException();
		}
		
		this.comparisonAttribute = comparisonAttribute;
		this.ascending=ascending;
	}

	public int compare(Object o1, Object o2)
	{
		int returnValue = 0;
		if ( (o1 instanceof WordData) && (o2 instanceof WordData))
		{
			WordData wd1 = (WordData) o1;
			WordData wd2 = (WordData) o2;
			//
			if (this.comparisonAttribute == WORDDATA_LABEL)
			{
				returnValue = wd1.getLabel().compareTo(wd2.getLabel());
			}
			else if (this.comparisonAttribute == WORDDATA_CARDINALITY)
			{
				// dies muss noch �berpr�ft werden - vielleicht ist es andersrum
				if (wd1.getCardinaility() > wd2.getCardinaility())
				{
					returnValue = -1;
				}
				else
				{
					returnValue = 1;
				}
			}
			else if (this.comparisonAttribute == WORDDATA_FREQUENCYRANK)
			{
				// dies muss noch �berpr�ft werden - vielleicht ist es andersrum
				if (wd1.getFrequencyRank() > wd2.getFrequencyRank())
				{
					returnValue = -1;
				}
				else
				{
					returnValue = 1;
				}
			}
			
			//
			if (ascending)
			{
			}
			else
			{
				if (returnValue > 0)
				{
					returnValue = -1;
				}
				else if (returnValue < 0)
				{
					returnValue = 1;
				}
			}
			return returnValue;
		}
		logger.info("wrong class types object1 " + o1.getClass().getName() + " object2 " + o2.getClass().getName());
		return 0;
	}
}
