package streamteam.web.resources;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.xml.bind.annotation.XmlRootElement;

import streamteam.TweetProcessor;
@Path("/tweet")
public class Tweet {
	
	@XmlRootElement
	public static class TweetBean {
		public String text;
	}

	@XmlRootElement
	public static class StringList {
		public String[] text;
	}
	
	@GET
	@Produces("application/json")
	public TweetBean someFunction() {
		TweetBean tweet = new TweetBean();
		tweet.text = "Hello";
		return tweet;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public TweetBean otherFunction(TweetBean in) throws Exception {
		if (in == null)
			throw new Exception("Empty tweet!!!!!");
		
		TweetProcessor.add(in.text);
		System.out.println(in.text);
		return in;
	}
	
	@GET
	@Produces("application/json")
	@Path("/most-frequent")
	public StringList MostFreq(@QueryParam("q") String queryString){
		List<String> foo = TweetProcessor.retrieve(queryString);
		StringList s = new StringList();
		s.text = new String[foo.size()];
		foo.toArray(s.text);
		return (s);
	}
}
