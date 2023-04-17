package json;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import com.google.gson.Gson;

import java.io.FileWriter;

public class withOpenCSV {
	public static void main(String[] args) {
	 try {
		 String currentDirectory = System.getProperty("user.dir");
			String filePath = currentDirectory + "/exemplo.csv";
	        // Create an object of file reader class with CSV file as a parameter.
	        	  
	        List<Horario> horario = new CsvToBeanBuilder<Horario>(new FileReader(filePath))
	                .withType(Horario.class)
	                .build()
	                .parse();

	        // Write JSON file
	        FileWriter fileWriter = new FileWriter(currentDirectory + "/outputJSON.json");
	        new Gson().toJson(horario, fileWriter);
	        fileWriter.close();
	    }
	  
	    catch (Exception e) {
	        e.printStackTrace();
	    }
     }
 }

class Horario {
	@CsvBindByPosition(position = 0)
    private String curso;
	@CsvBindByPosition(position = 1)
    private String unidadeCurricular;
	@CsvBindByPosition(position = 2)
    private String turno;
	
	Horario(String curso, String unidadeCurricular, String turno){
		this.curso = curso;
		this.unidadeCurricular = unidadeCurricular;
		this.turno = turno;
	}

	//teste
}
	 

