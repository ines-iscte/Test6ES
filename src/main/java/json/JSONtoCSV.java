package json;

import java.io.File;
import java.util.Scanner;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONtoCSV {

	public static void main(String args[])
	{
		// Class data members
		String jsonString;
		JSONObject jsonObject;

		// Try block to check for exceptions
		try {

			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showOpenDialog(null);
			String filePath = null;
			if (returnValue == JFileChooser.APPROVE_OPTION)
				filePath = fileChooser.getSelectedFile().getPath();


//	        	String currentDirectory = System.getProperty("user.dir");
//	    		Path filePath = Paths.get(currentDirectory + "/arquivo.json");

			// Step 1: Reading the contents of the JSON file
			// using readAllBytes() method and
			// storing the result in a string
			jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
//	            System.out.println(jsonString);

			// Step 2: Construct a JSONObject using above
			// string
			jsonObject = new JSONObject(jsonString);

			// Step 3: Fetching the JSON Array test
			// from the JSON Object
			JSONArray docs
					= jsonObject.getJSONArray("aulas");

			// Step 4: Create a new CSV file using
			//  the package java.io.File
//	            File file = new File( currentDirectory + "/testCSV.csv");
			Scanner scanner = new Scanner(System.in);
			System.out.print("Qual é o Path do ficheiro json onde pretende guardar?");
			String path = scanner.nextLine();
			File file = new File(path);


			// Step 5: Produce a comma delimited text from
			// the JSONArray of JSONObjects
			// and write the string to the newly created CSV
			// file

			String csvString = CDL.toString(docs);
			FileUtils.writeStringToFile(file, csvString);
		}

		// Catch block to handle exceptions
		catch (Exception e) {

			// Display exceptions on console with line
			// number using printStackTrace() method
			e.printStackTrace();
		}
	}

}
