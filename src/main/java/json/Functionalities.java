package json;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Functionalities {

    private static final Logger logger= Logger.getLogger(Functionalities.class.getName());

    public void csvToJson() throws IOException {
        try {
            Scanner scanner1 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH do ficheiro que pretende converter para JSON: ");
            String filePath = scanner1.nextLine();

            BufferedReader reader;
            try {
                URL url = new URL(filePath);
                String newPath = copyURLToFile(filePath, "CSV");
                if (newPath==null)
                    logger.log(Level.INFO, "Não foi possível guardar ficheiro CSV");
                reader = new BufferedReader(new FileReader(newPath));
                logger.log(Level.INFO, "Escolhido ficheiro remoto");
            } catch (MalformedURLException e){
                reader = new BufferedReader(new FileReader(filePath));
                logger.log(Level.INFO, "Escolhido ficheiro local");
            }
            // Ler a primeira linha e definir os nomes das colunas
            String primeiraLinha = reader.readLine();
            String[] nomesColunas = primeiraLinha.split(";");

            List<String[]> aulas = new ArrayList<String[]>();
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                aulas.add(campos);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Criar um objeto JsonObject para adicionar os nomes das colunas ao JSON
            JSONObject jsonObject = new JSONObject();
            JsonArray jsonArray = new JsonArray();
            for (String[] campos : aulas) {
                JsonObject linhaObjeto = new JsonObject();
                for (int i = 0; i < campos.length; i++) {
                    linhaObjeto.addProperty(nomesColunas[i], campos[i]);
                }
                jsonArray.add(linhaObjeto);
            }
            Scanner scanner2 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH onde pretende guardar o ficheiro JSON: ");
            String path = scanner2.nextLine();

            FileWriter writer = new FileWriter(path);
            jsonObject.put("aulas", jsonArray);
            gson.toJson(jsonObject.toMap(), writer);

            reader.close();
            writer.close();

            logger.log(Level.INFO, "Ficheiro JSON criado com sucesso");

        } catch (IOException e){
            logger.log(Level.INFO, "Não foi possível criar ficheiro JSON");
        }
    }

    public void jsonToCsv() {
        // Class data members
        String jsonString;
        JSONObject jsonObject;

        // Try block to check for exceptions
        try {
            Scanner scanner1 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH do ficheiro que pretende converter para CSV: ");
            String filePath = scanner1.nextLine();

            // Step 1: Reading the contents of the JSON file using readAllBytes() method and storing the result in a string
            try {
                URL url = new URL(filePath);
                String newPath = copyURLToFile(filePath, "JSON");
                if (newPath==null)
                    logger.log(Level.INFO, "Não foi possível guardar ficheiro JSON");
                try {
                    jsonString = new String(Files.readAllBytes(Paths.get(newPath)));
                    logger.log(Level.INFO, "Escolhido ficheiro remoto");
                }catch (IOException e1){
                    logger.log(Level.INFO, "Não foi possível criar ficheiro CSV");
                    return;
                }
            } catch (MalformedURLException e){
                try {
                    jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
                    logger.log(Level.INFO, "Escolhido ficheiro local");
                }catch (IOException e2){
                    logger.log(Level.INFO, "Não foi possível criar ficheiro CSV");
                    return;
                }
            }

            // Step 2: Construct a JSONObject using above string
            jsonObject = new JSONObject(jsonString);

            // Step 3: Fetching the JSON Array test from the JSON Object
            JSONArray docs = jsonObject.getJSONArray("aulas");

            // Step 4: Create a new CSV file using the package java.io.File
            Scanner scanner2 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH onde pretende guardar o ficheiro CSV: ");
            String path = scanner2.nextLine();
            File file = new File(path);

            // Step 5: Produce a comma delimited text from the JSONArray of JSONObjects and write the string to the newly created CSV file
            String csvString = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csvString);

            logger.log(Level.INFO, "Ficheiro CSV criado com sucesso");
        } catch (IOException e) {
            logger.log(Level.INFO, "Não foi possível criar ficheiro CSV");
        }
    }

    public String copyURLToFile(String urls, String type) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Qual é o Path do ficheiro " + type + " onde pretende guardar?");
        String path = scanner.nextLine();
        FileWriter writer;
        try {
            URL url = new URL(urls);
            InputStream input = url.openStream();
            if (type=="CSV") {
                writer = new FileWriter(path);
            } else if (type=="JSON"){
                writer = new FileWriter(path + ".json");
            } else {
                return null;
            }
                char[] buffer = new char[4096];
                int n = 0;
                while (-1 != (n = new InputStreamReader(input).read(buffer))) {
                    writer.write(buffer, 0, n);
                }
            input.close();
            writer.close();
            return path + ".json";
        } catch (IOException ioEx) {
            return null;
        }
    }
}
