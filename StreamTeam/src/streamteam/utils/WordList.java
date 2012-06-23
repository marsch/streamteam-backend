package streamteam.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;






public class WordList
	
{
	private static Logger logger = Logger.getLogger(WordList.class.getName());
	//
	private List<WordData> wordList = new ArrayList<WordData> ();
	private Map<String, WordData> labelIndex = new HashMap<String, WordData> ();
	private boolean changed = true;
	//
	private long corpusCardinality = -1;
	private long referenceWordsCardinality = -1;
	//
	public WordList()
	{
	}

	public WordList(SimpleCounter counter)
	{
		// this(downloadCounter.getMap());
		Map map = counter.getMap();
		Set keys = map.entrySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String currentKey = (String) entry.getKey();
			Long currenValue = (Long) entry.getValue();
			//
			WordData wordData = new WordData(currentKey, currenValue);
			this.add(wordData);
		}
	}

	public WordList(Map map)
	{
		Set keys = map.entrySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String currentKey = (String) entry.getKey();
			String currenValue = (String) entry.getValue();
			//
			WordData wordData = new WordData(currentKey, Integer.parseInt(currenValue));
			this.add(wordData);
		}
	}

	public void add(final WordData newWordData)
	{
		String label = newWordData.getLabel();
		// if (label.startsWith("have")) logger.info("label.startsWith(\"have\")");
		// if (label.equals("have")) logger.info("label.equals(\"have\")");
		WordData existingWordData = this.labelIndex.get(label);
		if (existingWordData == null)
		{
			this.wordList.add(newWordData);
			this.labelIndex.put(label, newWordData);
		}
		else
		{
			long existingCardinality = existingWordData.getCardinaility();
			long newWordCardinality = newWordData.getCardinaility();
			long resultingCardinality = existingCardinality + newWordCardinality;
			existingWordData.setCardinaility(resultingCardinality);
		}
		this.changed = true;
	}





	

	public WordData getWordData(String label)
	{
		return this.labelIndex.get(label);
	}

	public long getCardinality(String label)
	{
		// log ("getCardinality(String label "+label);
		WordData wordData = this.labelIndex.get(label);
		if (wordData == null)
		{
			return -1;
		}
		else
		{
			return wordData.getCardinaility();
		}
	}

	public List<WordData> getFullList()
	{
		return this.wordList;
	}

	/**
	 *
	 * @param minCardinality int
	 * @param maxListsize int
	 * @return List
	 */
	public List<WordData> getSubList(int minCardinality, int maxListsize)
	{
		Comparator comparator = new WordDataComparator(WordDataComparator.WORDDATA_CARDINALITY, true);
		Collections.sort(wordList, comparator);
		List<WordData> subList = new ArrayList<WordData> ();
		int size = Math.min(maxListsize, wordList.size());
		// wenn i gr��er als maxListsize wird aush�ren
		for (int i = 0; i < size; i++)
		{
			WordData wordData = wordList.get(i);
			if (wordData.getCardinaility() >= minCardinality)
			{
				subList.add(wordData);
			}
			else
			{
				break;
				/*
				 no further Entries expected to fullfill the minCardinality criteria
				 - if list was sortet properly
				 */
			}
		}
		return subList;
	}

	public Map<String, String> getMap()
	{
		Map<String, String> map = new HashMap<String, String> ();
		for (int i = 0; i < wordList.size(); i++)
		{
			WordData wordData = wordList.get(i);
			map.put(wordData.getLabel(), String.valueOf(wordData.getCardinaility()));
		}
		return map;
	}




	

	public void assignFrequencyRank()
	{
		Comparator comparator = new WordDataComparator(WordDataComparator.WORDDATA_CARDINALITY, true);
		Collections.sort(this.wordList, comparator);
		for (int i = 0; i < this.wordList.size(); i++)
		{
			wordList.get(i).setFrequencyRank(i+1);
		}
	}

	

	public void sort(Comparator comparator)
	{
		Collections.sort(this.wordList, comparator);
	}

	

	public String toString()
	{
		return toString(500);
	}

	public String toString(int size)
	{
		int length = Math.min(this.wordList.size(), size);
		StringBuffer content = new StringBuffer(length * 100);
		for (int i = 0; i < length; i++)
		{
			WordData wordData = (WordData) wordList.get(i);
			content.append(i);
			content.append("\t");
			content.append(wordData.toString());
			content.append("\n");
		}
		return content.toString();
	}

	public List<String> getWordList(int size)
	{
		int length = Math.min(this.wordList.size(), size);
		ArrayList<String> words = new ArrayList<String> ();
		for (int i = 0; i < length; i++)
		{
			WordData wordData = (WordData) wordList.get(i);
			words.add(wordData.getLabel());
		}
		return words;
	}

	

	
}
