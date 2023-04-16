package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

import com.google.gson.JsonElement;

public class CSVtoJSON {

	public static void main(String[] args) throws IOException {
		String currentDirectory = System.getProperty("user.dir");
		String filePath = currentDirectory + "/horario_exemplo.csv";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		// Ler a primeira linha e definir os nomes das colunas
		String primeiraLinha = reader.readLine();
		String[] nomesColunas = primeiraLinha.split(";");
		
		List<String[]> aulas = new ArrayList<String[]>();
		String linha;
		while ((linha = reader.readLine()) != null) {
			String[] campos = linha.split(";");
			aulas.add(campos);
		}
		reader.close();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = gson.toJsonTree(aulas);
		
		// Criar um objeto JsonObject para adicionar os nomes das colunas ao JSON
		JSONObject jsonObject = new JSONObject();
		JsonArray jsonArray = new JsonArray();
		for (String[] campos : aulas) {
			JsonObject linhaObjeto = new JsonObject();
		    for (int i = 0; i < campos.length; i++) {
		        linhaObjeto.addProperty(nomesColunas[i], campos[i]);
		    }
		    jsonArray.add(linhaObjeto);
		    //System.out.println(jsonArray);
		}
		jsonObject.put("aulas", jsonArray);
			
		FileWriter writer = new FileWriter(currentDirectory + "/arquivo.json");
		gson.toJson(jsonObject.toMap(), writer);
		writer.close();

		String jsonFormatado = new JsonParser().parse(jsonElement.toString()).toString();
		System.out.println(jsonFormatado);
	}
}

