package streamteam;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Field;

import streamteam.utils.SimpleCounter;
import streamteam.utils.WordList;

public class TweetProcessor {

	private static Logger logger = Logger.getLogger(TweetProcessor.class.getName());
	public static final String TEXTFIELD = "text";
	private static Directory directory = null;
	private static IndexWriter writer = null;
	private static IndexReader reader = null;
	private static IndexSearcher searcher = null;

	static {
		try {
			String indexDirectoryFileName = "temp/streamteam-tweetindex";
			directory = FSDirectory.open(new File(indexDirectoryFileName));
			Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					analyzer);

			// config.setRAMBufferSizeMB(500);
			writer = new IndexWriter(directory, config);
			reader = IndexReader.open(directory);
			logger.info("IndexReader has " + reader.numDocs() + " docs in it");
			searcher = new IndexSearcher(reader);

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void add(String tweetText) {
		try {
			tweetText = tweetText.toLowerCase();
			tweetText = getAlphabeticStringParts(tweetText);
			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new Field(TEXTFIELD, tweetText, Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getAlphabeticStringParts(String token) {
		char[] ch = token.toCharArray();
		for (int i = 0; i < token.length(); i++) {
			if (org.apache.commons.lang.CharUtils.isAsciiAlpha(ch[i])) {
			} else if (ch[i] == '#' || ch[i] == 'Ä' || ch[i] == 'Ü'
					|| ch[i] == 'Ö' || ch[i] == 'ä' || ch[i] == 'ü'
					|| ch[i] == 'ö' || ch[i] == 'ß' || ch[i] == 'é'
					|| ch[i] == '@') {
			} else {
				ch[i] = ' ';
			}
		}
		return new String(ch);
	}

	public static List<String> retrieve(String queryString) {
		logger.info("Retrieve has been called on "+ queryString);
		SimpleCounter hashTagCounter = new SimpleCounter();
		SimpleCounter mentionCounter = new SimpleCounter();
		SimpleCounter wordCounter = new SimpleCounter();
		TermQuery query = new TermQuery(new Term(TEXTFIELD, queryString));
		TopDocs topDocs;
		try {
			reader = IndexReader.open(TweetProcessor.writer, false);
			logger.info("IndexReader has " + reader.numDocs() + " docs in it");
			searcher = new IndexSearcher(reader);
			topDocs = searcher.search(query, 1);
			logger.info("found " + topDocs.totalHits + " topDocs");
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int d = 0; d < scoreDocs.length; ++d) {
				org.apache.lucene.document.Document doc = searcher
						.doc(scoreDocs[d].doc);
				String text = doc.get(TEXTFIELD);
				String[] textParts = text.split(" ");
				for (int i = 0; i < textParts.length; i++) {
					logger.info("Retrieve itterating over part " + textParts[i] );
					if (textParts[i].startsWith("#")) {
						hashTagCounter.add(textParts[i]);
					} else if (textParts[i].startsWith("@")) {
						mentionCounter.add(textParts[i]);
					} else {
						wordCounter.add(textParts[i]);
					}
				}
			}
			searcher.close();
			reader.close();
			List<String> results = hashTagCounter.getOrderedResults();
			List<String> results2 = mentionCounter.getOrderedResults();
			List<String> results3 = wordCounter.getOrderedResults();
			return results;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<String>();

	}
}
