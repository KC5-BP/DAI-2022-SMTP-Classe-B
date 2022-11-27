package lab4;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONManager {

    public static JSONArray readFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;

        try
        {   /* Open file through FileReader */
            FileReader fr = new FileReader(filename);

            /* Read JSON file as Object */
            Object obj = jsonParser.parse(fr);

            /* Object parsed as jsonArray */
            jsonArray = (JSONArray) obj;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return jsonArray;
    }

    public static JSONObject parseAs(JSONObject jsonObj, String type) {
         return (JSONObject) jsonObj.get(type);
    }
}
