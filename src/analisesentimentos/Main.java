package analisesentimentos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import twitterapi.TwitterAPI;
import view.GUIFrame;

public class Main {

	public static void main(String[] args) throws Throwable {
	
		TwitterAPI twitterAPI = new TwitterAPI();
		Scanner reader = null;
		int opcao = 0;
		String dataInicio;
		String dataFim;
		
		while (opcao == 0)
		{
			reader = new Scanner(System.in);
			
			System.out.println("Selecione uma opção: \n"
					+ "1 - Montar dataset positivo a partir de query \n"
					+ "2 - Montar dataset negativo a partir de query \n"
					+ "3 - Busca utilizando a API do Twitter \n"
					+ "4 - Análise de sentimento de um tweet usando OpenNLP \n"
					+ "5 - Sair \n");
			
			opcao = reader.hasNextInt() ? Integer.parseInt(reader.next()) : 0;
			reader.nextLine();
		}
		
		switch(opcao)
		{
			case 1:

				System.out.println("Entre com a query positiva: \n");
				String queryPositiva = reader.nextLine(); 
				System.out.println("Entre com a data de inicio (formato dd/mm/aaaa): \n");
				dataInicio = reader.nextLine(); 
				System.out.println("Entre com a data de fim (formato dd/mm/aaaa): \n");
				dataFim = reader.nextLine();
				
				String nomeDatasetPositivo = twitterAPI.montarDatasetPositivo(queryPositiva, 
						twitterAPI.stringToCalendar(dataInicio), twitterAPI.stringToCalendar(dataFim));
				
				System.out.println(nomeDatasetPositivo.isEmpty() ? "Falha na criação do dataset positivo." : "Arquivo de nome " + nomeDatasetPositivo + 
						" criado com sucesso!" );
				
				main(new String [1]);
						
			case 2:
				
				System.out.println("Entre com a query negativa: \n");
				String queryNegativa = reader.nextLine(); 
				System.out.println("Entre com a data de inicio (formato dd/mm/aaaa): \n");
				dataInicio = reader.nextLine(); 
				System.out.println("Entre com a data de fim (formato dd/mm/aaaa): \n");
				dataFim = reader.nextLine();
				
				String nomeDatasetNegativo = twitterAPI.montarDatasetNegativo(queryNegativa, 
						twitterAPI.stringToCalendar(dataInicio), twitterAPI.stringToCalendar(dataFim));
				
				System.out.println(nomeDatasetNegativo.isEmpty() ? "Arquivo de nome " + nomeDatasetNegativo + 
						" criado com sucesso!" : "Falha na criação do dataset negativo.");
				
				main(new String [1]);
		
			case 3:
				GUIFrame gui = new GUIFrame(); // create EventsFrame
				gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				gui.setSize( 1024, 768 ); // set frame size
				gui.setVisible( true ); // display frame
				break;
				
			case 4:
				System.out.println("Simule um tweet:");
				Scanner leitorTweet = new Scanner(System.in);
				String tweet = leitorTweet.nextLine();
				
				OpenNLPCategorizer twitterCategorizer = new OpenNLPCategorizer();
				twitterCategorizer.trainModel();
				twitterCategorizer.classifyNewTweet(tweet);
				leitorTweet.close();
				
				break;
				
			case 5:
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
