package lab4;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONExtractor {

    /**
     * Read JSON file and return content as JSONArray
     * @return JSONArray
     */
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

            fr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return jsonArray;
    }

    /**
     * Parse from JSONObject json attribute defined by type
     * @return JSONObject
     */
    public static JSONObject parseAs(JSONObject jsonObj, String type) {
         return (JSONObject) jsonObj.get(type);
    }
}
