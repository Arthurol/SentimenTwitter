package twitterapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.*;

public class TwitterAPI {

	Twitter twitter;
	
	public TwitterAPI()
	{
		this.twitter = new TwitterFactory().getInstance();
	}
	
	public Query prepararQuery(String busca) 
	{
		ArrayList<String> tweetList = new ArrayList<String>();
		QueryResult result = null;
		Query query = new Query(busca);
		query.setCount(500);		// máximo de tweets recuperados (por página)
		return query;
	}
	
	public ArrayList<String> getTweetsAndAuthors(String topic) 
	{
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
				{
					tweetList.add("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
				}
				
			} while ((query = result.nextQuery()) != null);
			
		} catch (TwitterException te) 
		{
			te.printStackTrace();
			System.out.println("Falha na busca pelos tweets: " + te.getMessage());
		}
		return tweetList;
	}

	public String montarDatasetPositivo(String busca, Calendar dataInicio, Calendar dataFim) 
	{
		File file = null;
		String nomeDatasetPositivo = "";
		ArrayList<String> tweetList = new ArrayList<String>();
		
		try 
		{
			Query query = prepararQuery(busca);
			QueryResult result;
			int numeroTweet = 1;
				
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) 
				{	
					//O tweet é adicionado à lista somente se sua data de criação estiver no intervalo estipulado
					Calendar dataTweet = dateToCalendar(tweet.getCreatedAt()); 
					//if (dataTweet.compareTo(dataInicio) >= 0 && dataTweet.compareTo(dataFim) <= 0)
					//{
						String lingua = tweet.getLang();
						if (lingua.equalsIgnoreCase("en"))
						{
							tweetList.add(String.valueOf(numeroTweet) + ") "+ tweet.getText());
							numeroTweet ++;
						}
					//}
					
				}
			} while ((query = result.nextQuery()) != null);

	        file = new File("treinamento\\datasetPositivo.txt");

	        // if file doesnt exists, then create it
	        if (!file.exists()) 
	        {
	            file.createNewFile();
	        }

	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        
	        for (String tweetText : tweetList)
	        {
	        	bw.write(tweetText);
	        	bw.newLine();
	        }
	        
	        bw.close();

	        System.out.println("Tweets adicionados ao arquivo txt com sucesso.");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		catch (TwitterException te) 
		{
			te.printStackTrace();
			System.out.println("Falha na busca pelos tweets: " + te.getMessage());
		}
		return file.getName();
		
	}

	public String montarDatasetNegativo(String busca, Calendar dataInicio, Calendar dataFim) 
	{
		File file = null;
		String nomeDatasetNegativo = "";
		ArrayList<String> tweetList = new ArrayList<String>();
		
		try 
		{
			Query query = prepararQuery(busca);
			QueryResult result;
			int numeroTweet = 1;
				
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) 
				{	
					//O tweet é adicionado à lista somente se sua data de criação estiver no intervalo estipulado
					//Calendar dataTweet = dateToCalendar(tweet.getCreatedAt()); 
					//if (dataTweet.compareTo(dataInicio) >= 0 && dataTweet.compareTo(dataFim) <= 0)
					//{
					String lingua = tweet.getLang();
					if (lingua.equalsIgnoreCase("en"))
					{
						tweetList.add(String.valueOf(numeroTweet) + ") "+ tweet.getText());
						numeroTweet ++;
					}
					//}
					
				}
			} while ((query = result.nextQuery()) != null);

	        file = new File("treinamento\\datasetNegativo.txt");

	        // if file doesnt exists, then create it
	        if (!file.exists()) 
	        {
	            file.createNewFile();
	        }

	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        
	        for (String tweetText : tweetList)
	        {
	        	bw.write(tweetText);
	        	bw.newLine();
	        }
	        
	        bw.close();

	        System.out.println("Tweets adicionados ao arquivo txt com sucesso.");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		catch (TwitterException te) 
		{
			te.printStackTrace();
			System.out.println("Falha na busca pelos tweets: " + te.getMessage());
		}
		return file.getName();
		
	}
	
	public Calendar stringToCalendar(String dataString)
	{
		Calendar cal = null;
		try 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			cal = Calendar.getInstance();
			cal.setTime(sdf.parse(dataString));
			
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		return cal;
	}
	
	public Calendar dateToCalendar(Date date)
	{
		Calendar cal = null;
		try 
		{	  
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			cal = Calendar.getInstance();
			String dataFormatada = sdf.format(date);
			cal.setTime(sdf.parse(dataFormatada));
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return cal;
	}
	
}