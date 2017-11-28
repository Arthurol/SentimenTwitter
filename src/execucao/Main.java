package execucao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import com.twitter.Extractor;

import twitterapi.TwitterAPI;
import view.GUIFrame;

public class Main {

	public static void main(String[] args) throws Throwable {
	
		TwitterAPI twitterAPI = new TwitterAPI();
		Scanner reader = null;
		int opcao = 0;
		String dataInicio;
		String dataFim;
	
        Extractor extractor = new Extractor();
        String strTeste = "@Gessica @Arthur Fora Dilma,fora Lula! #Impeachment #ForaDilma! https://t.co/f6A1ON4DKG https://t.co/M76NAIEz8a";
        
        String tweetsSemUserHash = twitterAPI.removerHashtagEUsername(strTeste);
        String tweetsSemUrl = twitterAPI.removerUrl(tweetsSemUserHash);
        String tweetsSemPontuacao = twitterAPI.removerPontuacao(tweetsSemUrl);
        
		System.out.println("Sem hashtag e usuarios \nAntes: " + strTeste + "\n" + "Depois: " + tweetsSemUserHash);
		System.out.println("\nSem urls \nAntes: " + tweetsSemUserHash + "\n" + "Depois: " + tweetsSemUrl);		
		System.out.println("\nSem pontuacao \nAntes: " + tweetsSemUrl + "\n" + "Depois: " + tweetsSemPontuacao + "\n\n");
	
		
		while (opcao == 0)
		{
			reader = new Scanner(System.in);
			
			System.out.println("Selecione uma opção: \n"
					+ "1 - Montar dataset positivo a partir de query \n"
					+ "2 - Montar dataset negativo a partir de query \n"
					+ "3 - Busca utilizando a API do Twitter \n"
					+ "5 - Sair \n");
			
			opcao = reader.hasNextInt() ? Integer.parseInt(reader.next()) : 0;
			reader.nextLine();
		}
		
		switch(opcao)
		{
			case 1:

				System.out.println("Entre com a query positiva: \n");
				String queryPositiva = reader.nextLine(); 
				//System.out.println("Entre com a data de inicio (formato dd/mm/aaaa): \n");
				//dataInicio = reader.nextLine(); 
				dataInicio = "01/01/2017";
				//System.out.println("Entre com a data de fim (formato dd/mm/aaaa): \n");
				//dataFim = reader.nextLine();
				dataFim = "28/11/2017";
				
				String nomeDatasetPositivo = twitterAPI.montarDatasetPositivo(queryPositiva, 
						twitterAPI.stringToCalendar(dataInicio), twitterAPI.stringToCalendar(dataFim));
				
				System.out.println(nomeDatasetPositivo.isEmpty() ? "Falha na criação do dataset positivo." : "Arquivo de nome " + nomeDatasetPositivo + 
						" criado com sucesso!" );
				break;
						
			case 2:
				
				System.out.println("Entre com a query negativa: \n");
				String queryNegativa = reader.nextLine(); 
				//System.out.println("Entre com a data de inicio (formato dd/mm/aaaa): \n");
				//dataInicio = reader.nextLine(); 
				dataInicio = "17/11/2017";
				dataFim = "29/11/2017";
				//System.out.println("Entre com a data de fim (formato dd/mm/aaaa): \n");
				//dataFim = reader.nextLine();
				
				
				String nomeDatasetNegativo = twitterAPI.montarDatasetNegativo(queryNegativa, 
						twitterAPI.stringToCalendar(dataInicio), twitterAPI.stringToCalendar(dataFim));
				
				System.out.println(nomeDatasetNegativo.isEmpty() ? "Arquivo de nome " + nomeDatasetNegativo + 
						" criado com sucesso!" : "Falha na criação do dataset negativo.");
				break;
		
			case 3:
				GUIFrame gui = new GUIFrame(); // create EventsFrame
				gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				gui.setSize( 1024, 768 ); // set frame size
				gui.setVisible( true ); // display frame
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
				tempWord = twitterAPI.removerPontuacao(tempWord);
				testeTokenizado.add(tempWord); //if not white space than add to ArrayList
			}
		}
		
		//System.out.println(testeTokenizado);
		
	}
	
	
	
	
}
