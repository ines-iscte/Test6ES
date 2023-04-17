package json;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Functionalities func = new Functionalities();
        func.csvToJson();
        func.jsonToCsv();
    }
}
