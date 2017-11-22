package analisesentimentos;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import twitterapi.TweetManager;
import view.GUIFrame;

public class Main {

	public static void main(String[] args) throws Throwable {
	
		Scanner reader = null;
		int opcao = 0;
		
		while (opcao == 0)
		{
			reader = new Scanner(System.in);
			
			System.out.println("1 - Busca utilizando a API do Twitter \n"
					+ "2 - Análise de sentimento de um tweet usando OpenNLP \n"
					+ "3 - Sair \n");
			
			opcao = reader.hasNextInt() ? Integer.parseInt(reader.next()) : 0;
		}
		
		switch(opcao)
		{
			case 1:
				GUIFrame gui = new GUIFrame(); // create EventsFrame
				gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				gui.setSize( 1280, 1024 ); // set frame size
				gui.setVisible( true ); // display frame
				break;
				
			case 2:
				System.out.println("Simule um tweet:");
				Scanner leitorTweet = new Scanner(System.in);
				String tweet = leitorTweet.next();
				
				OpenNLPCategorizer twitterCategorizer = new OpenNLPCategorizer();
				twitterCategorizer.trainModel();
				twitterCategorizer.classifyNewTweet(tweet);
				leitorTweet.close();
				
				break;
				
			case 3:
				reader.close();
				break;
		}		
	
	
		String teste = "A internet deu voz à muitas aberrações. O anonimato é a ilusão de impunidade suficiente para que muitos mostrem seus demônios em público.";
		ArrayList <String>testeTokenizado = new ArrayList<String>();
		StringTokenizer textToken = new StringTokenizer(teste);
		
		
		while(textToken.hasMoreTokens()){
			String tempWord = textToken.nextToken();
			if(!tempWord.equals(null)){
				tempWord = stripWord(tempWord);
				testeTokenizado.add(tempWord); //if not white space than add to ArrayList
			}
		}
		
		System.out.println(testeTokenizado);
		
	}
	
	public static String stripWord(String word){
		word = word.toLowerCase();
		word = word.replaceAll("([a-z]+)[?:!.,;]*", "$1"); //Remove punctuation
		return word;
	}
	
}
