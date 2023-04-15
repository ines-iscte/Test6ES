package json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.FileWriter;

import org.json.CDL;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CSVtoJSON {
	
		public static void convert(String csv) {
			try {
				File file = new File("/horario_json.json");
				//String json = CDL.toJSONArray(csv).toString(2);
	            //Files.write( Paths.get("/horario_json.json"), json.getBytes());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				//File file = new File("/horario_json.json");
				String json = gson.toJson(csv);
				json = gson.toJson(JsonParser.parseString(json));
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				//System.out.println(json);
				System.out.println(gson.toJson(JsonParser.parseString(json)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	    public static void main(String[] args) {
	        // Read csv data file and store it in a string
	        InputStream is = CSVtoJSON.class.getResourceAsStream("/exemplo.csv");
	        
	        String csv = new BufferedReader(
	                new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))
	                .lines()
	                .collect(Collectors.joining("/n"));
	        System.out.println(csv);
	        
	        convert(csv);
	      
	        /*try {
	            // Convert csv text to JSON string, and save it 
	            // to a data.json file.
	            String json;
				try {
					json = Gson.toJson(csv);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println(json);
	            //Files.write(Path.of("data.json"), json.getBytes());
	        
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    }
	}

