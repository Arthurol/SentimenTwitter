package twitterapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twitter.Extractor;

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
		Query query = new Query(busca);
		
		query.setCount(400);		// m�ximo de tweets recuperados (por p�gina)
		
		return query;
	}
	
	public ArrayList<String> getTweetsAndAuthors(String topic) 
	{
		ArrayList<String> tweetList = new ArrayList<String>();
		
		try 
		{
			Query query = new Query(topic);
			
			query.setCount(200);  // m�ximo de tweets recuperados
			query.setLang("en");
			query.setSinceId(1);
			query.maxId(500);
			
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
			query.setUntil("2017-11-11");
			query.setSince("2017-11-01");
			QueryResult result;
			int numeroTweet = 1;
			long maxId = 0;
			
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) 
				{	
					
					//O tweet � adicionado � lista somente se sua data de cria��o estiver no intervalo estipulado
					Calendar dataTweet = dateToCalendar(tweet.getCreatedAt()); 
					//if (dataTweet.compareTo(dataInicio) >= 0 && dataTweet.compareTo(dataFim) <= 0)
					//{
						String lingua = tweet.getLang();
						if (lingua.equalsIgnoreCase("en"))
						{
							String data = dataTweet.getTime().toString();
							String texto = "";
							
							if (tweet.getRetweetedStatus() != null)
							{
								int tamanhoRetweetedStatus = tweet.getRetweetedStatus().getText().length();
								int tamanhoTweet = tweet.getText().length();
								texto = tamanhoRetweetedStatus >=  tamanhoTweet ? 
										tweet.getRetweetedStatus().getText() : tweet.getText();
							}
							
							else
								texto = tweet.getText();
							
							tweetList.add(preprocessarTweet(texto));
							numeroTweet ++;
		
						}
					//}
					
				}
				maxId = result.getMaxId();
				query.setSinceId(maxId);
				
			} while ((query = result.nextQuery()) != null);

	        file = new File("treinamento\\datasetPositivo.txt");

	        // if file doesnt exists, then create it
	        if (!file.exists()) 
	        {
	            file.createNewFile();
	        }

	        //FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        //BufferedWriter bw = new BufferedWriter(fw);
	        
	        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
	        
	        for (String tweetText : tweetList)
	        {
	        	out.println(tweetText);
	        	//bw.write(tweetText);
	        	//bw.newLine();
	        }
	        
	        out.close();
	       // bw.close();

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
			query.setUntil("2017-11-30");
			query.setSince("2017-11-17");
			int numeroTweet = 1;
				
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) 
				{	
					//O tweet � adicionado � lista somente se sua data de cria��o estiver no intervalo estipulado
					Calendar dataTweet = dateToCalendar(tweet.getCreatedAt()); 
					//if (dataTweet.compareTo(dataInicio) >= 0 && dataTweet.compareTo(dataFim) <= 0)
					//{
						String lingua = tweet.getLang();
						if (lingua.equalsIgnoreCase("en"))
						{
							String data = dataTweet.getTime().toString();
							String texto = "";
							
							if (tweet.getRetweetedStatus() != null)
							{
								int tamanhoRetweetedStatus = tweet.getRetweetedStatus().getText().length();
								int tamanhoTweet = tweet.getText().length();
								texto = tamanhoRetweetedStatus >=  tamanhoTweet ? tweet.getRetweetedStatus().getText() 
									: tweet.getText();
							}
							
							else
								texto = tweet.getText();
							
							tweetList.add(preprocessarTweet(texto));
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
	private String preprocessarTweet(String entrada)
	{
		String tweet = removerHashtagEUsername(entrada);
		tweet = removerUrl(tweet);
		tweet = removerPontuacao(tweet);
		return tweet;
	}
	
	public String removerUrl(String entrada)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(entrada);
        int i = 0;
        while (m.find()) {
        	entrada = entrada.replaceAll(urlPattern,"").trim();
            i++;
        }
        return entrada;
    }
	
	public String removerHashtagEUsername(String entrada)
    {
        String patternHashtag = "(?!\\s)[#|@]([A-Za-z]|\\d[A-Za-z]|\\d*[A-Za-z])\\w*\\b";
        Pattern p = Pattern.compile(patternHashtag,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(entrada);
        int i = 0;
        while (m.find()) {
        	entrada = entrada.replaceAll(patternHashtag,"").trim();
            i++;
        }
        return entrada;
    }
	
	public String removerPontuacao(String entrada){
		entrada = entrada.replaceAll("[!?:.,;()+]", " "); 
		entrada = entrada.replaceAll("[\\s*]", " ");
		entrada = entrada.replaceAll("RT", "");
		entrada = entrada.toLowerCase();
		entrada = entrada.trim();
		return entrada;
	}
}