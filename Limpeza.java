import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Limpeza {
	
	//https://www.ranks.nl/stopwords/brazilian
	
	private static final String COMMA_DELIMITER = ",";
	private String enderecoPadrao = "C:\\Users\\P_999439\\Downloads\\projeto\\";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new Limpeza().processa();
	}

	protected  void processa() throws IOException, FileNotFoundException {
		List<List<String>> records = new ArrayList<>();
		String[] msg = new String[]{"palavras_removidas.txt", "stopword_es.txt","stopword_pt_br.txt"} ;
		FileWriter csvWriterClassificado = new FileWriter("limpoClassificado_java.csv");
		FileWriter csvWriterNaoClassificado = new FileWriter("limpoNaoClassificado_java.csv");
		int fim=0;
		
		try (BufferedReader br = new BufferedReader(new FileReader(enderecoPadrao+"train.csv"))) {
		    String line;
		    String[] values  = null;
		    criarTituloArquivoCSV(csvWriterClassificado, csvWriterNaoClassificado, br);        	
		    while ((line = br.readLine()) != null) {		    	
		        if(line.contains("\"")){		        
		        	String[] conteudoAspas  = 	line.split("\"");
		        	String[] conteudoVirgula = conteudoAspas[2].split(COMMA_DELIMITER);
		        	values = new String[]{conteudoAspas[1], conteudoVirgula[1],conteudoVirgula[2],conteudoVirgula[3]};
		        }else{
		        	values = line.split(COMMA_DELIMITER);	
		        }
		    	 
		    	
		        String titulo = values[0];
		        String categoria = values[3];
		       
				for (int i = 0; i < msg.length; i++) {
		        	try (BufferedReader stopWords = new BufferedReader(new FileReader(enderecoPadrao+msg[i]))){
		        		  String stop=null;
		        		  	while ((stop = stopWords.readLine()) != null) {
		        			  titulo =  titulo.toUpperCase().replaceAll(" "+stop.toUpperCase(), " ");
		        		   }
		        	}
			        //records.add(Arrays.asList(values));
				}
		        //remove simbolos
		        try (BufferedReader stopWords = new BufferedReader(new FileReader(enderecoPadrao+ "stopword_simbolos.txt"))){
		        	  String stop=null;
		        	  while ((stop = stopWords.readLine()) != null) {
	        			  titulo =  titulo.toUpperCase().replace(stop.toUpperCase(), " ");
	        		   }
		        }
		       
		        values[0]= titulo;
		        values[3] = "__label__"+categoria;
		        if(titulo.trim().equals("")){
		        	System.out.println("Vazio");
		        }else{
		        	String arrayFinal[] = new String[]{values[0],values[3]};	
			          if(!values[1].equals("unreliable")){
			        	csvWriterClassificado.append(String.join(" ", arrayFinal));
			        	csvWriterClassificado.append("\n");
			        }else{		        
			        	csvWriterNaoClassificado.append(String.join(" ", arrayFinal));
			        	csvWriterNaoClassificado.append("\n");
			        }
			        fim++;
	//		        if(fim==1000){
	//		        	break;
	//		        }
		        }		    
		    }
		}
		
		csvWriterClassificado.flush();
		csvWriterClassificado.close();
		
		csvWriterNaoClassificado.flush();
		csvWriterNaoClassificado.close();
		System.out.println(records.size());
	}

	protected void criarTituloArquivoCSV(FileWriter csvWriterClassificado, FileWriter csvWriterNaoClassificado,
			BufferedReader br) throws IOException {
		String tituloCSV =  br.readLine();
		csvWriterClassificado.append(String.join(" ", tituloCSV));
		csvWriterClassificado.append("\n");
		csvWriterNaoClassificado.append(String.join(" ", tituloCSV));
		csvWriterNaoClassificado.append("\n");
	}
	

}
