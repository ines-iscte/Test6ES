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

    private String erroCSV="Não foi possível criar o ficheiro CSV";
    private String erroJSON="Não foi possível criar o ficheiro JSON";

    private static final Logger logger= Logger.getLogger(Functionalities.class.getName());

    public void csvToJson() throws IOException {
        try {
            Scanner scanner1 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH do ficheiro que pretende converter para JSON: ");
            String filePath = scanner1.nextLine();

            BufferedReader reader = getReader(filePath);
            if (reader==null)
                return;
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
            logger.log(Level.INFO, erroJSON);
        }
    }

    public void jsonToCsv() {

        String jsonString;
        JSONObject jsonObject;
        try {
            Scanner scanner1 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH do ficheiro que pretende converter para CSV: ");
            String filePath = scanner1.nextLine();

            jsonString = getJsonString(filePath);
            if (jsonString==null)
                return;
            jsonObject = new JSONObject(jsonString);
            JSONArray docs = jsonObject.getJSONArray("aulas");

            Scanner scanner2 = new Scanner(System.in);
            logger.log(Level.INFO, "Por favor indique o PATH onde pretende guardar o ficheiro CSV: ");
            String path = scanner2.nextLine();
            File file = new File(path);

            String csvString = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csvString);

            logger.log(Level.INFO, "Ficheiro CSV criado com sucesso");
        } catch (IOException e) {
            logger.log(Level.INFO, erroCSV);
        }
    }

    public String copyURLToFile(String urls, String type) throws IOException {
        Scanner scanner = new Scanner(System.in);
        logger.log(Level.INFO, "Por favor indique o Path do ficheiro " + type + " onde pretende guardar: ");
        String path = scanner.nextLine();
        FileWriter writer;
        try {
            URL url = new URL(urls);
            InputStream input = url.openStream();
            if (type.equals("CSV")) {
                writer = new FileWriter(path);
            } else if (type.equals("JSON")){
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

    public BufferedReader getReader(String filePath) throws FileNotFoundException {
        BufferedReader reader;
        try {
            URL url = new URL(filePath);
            String newPath = copyURLToFile(filePath, "CSV");
            if (newPath == null) {
                logger.log(Level.INFO, erroCSV);
                return null;
            }
            reader = new BufferedReader(new FileReader(newPath));
            logger.log(Level.INFO, "Escolhido ficheiro remoto");
        } catch (IOException e) {
            reader = new BufferedReader(new FileReader(filePath));
            logger.log(Level.INFO, "Escolhido ficheiro local");
        }
        return reader;
    }

    public String getJsonString (String filePath) {
        String jsonString;
        try {
            URL url = new URL(filePath);
            String newPath = copyURLToFile(filePath, "JSON");

            if (newPath == null) {
                logger.log(Level.INFO, erroJSON);
                return null;
            }
            try {
                jsonString = new String(Files.readAllBytes(Paths.get(newPath)));
                logger.log(Level.INFO, "Escolhido ficheiro remoto");
            } catch (IOException e1) {
                logger.log(Level.INFO, erroCSV);
                return null;
            }
        } catch (MalformedURLException e) {
            try {
                jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
                logger.log(Level.INFO, "Escolhido ficheiro local");
            } catch (IOException e2) {
                logger.log(Level.INFO, erroCSV);
                return null;
            }
        } catch (IOException e) {
            logger.log(Level.INFO, erroCSV);
            return null;
        }
        return jsonString;
    }
}
