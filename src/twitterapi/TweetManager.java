package twitterapi;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TweetManager {

	public ArrayList<String> getTweets(String topic) 
	{
		Twitter twitter = new TwitterFactory().getInstance();
		ArrayList<String> tweetList = new ArrayList<String>();
		
		try 
		{
			Query query = new Query(topic);
			
			query.setCount(500);				// máximo de tweets recuperados
			query.setLang("en");
			
			QueryResult result;
			
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) 
					tweetList.add("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
				
			} while ((query = result.nextQuery()) != null);
			
		} catch (TwitterException te) 
		{
			te.printStackTrace();
			System.out.println("Falha na busca pelos tweets: " + te.getMessage());
		}
		return tweetList;
	}
}