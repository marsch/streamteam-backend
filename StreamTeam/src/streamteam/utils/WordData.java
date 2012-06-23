package streamteam.utils;

import java.text.DecimalFormat;
import java.util.logging.Logger;







public class WordData
{
	private static Logger logger = Logger.getLogger(WordData.class.getName());
	//
	private String label;
	private long cardinality;
	private int frequencyRank;

	//
	//
	public WordData(String label, long cardinality)
	{
		this.label = label;
		this.cardinality = cardinality;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public long getCardinaility()
	{
		return cardinality;
	}

	public void setCardinaility(long cardinaility)
	{
		this.cardinality = cardinaility;
	}

	public int getFrequencyRank()
	{
		return frequencyRank;
	}

	public void setFrequencyRank(int frequencyRank)
	{
		this.frequencyRank = frequencyRank;
	}

	
	
	

	

	
}
