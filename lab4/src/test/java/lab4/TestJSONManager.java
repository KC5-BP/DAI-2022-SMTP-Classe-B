package lab4;

import java.sql.SQLOutput;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TestJSONManager {
    public static void main(String[] args) {
        JSONManager jsonManager = new JSONManager();
        Random random = new Random();

        JSONArray mailingList = jsonManager.readFromFile("./lab4/src/config/mailList.json");
        System.out.println("Mailing list size: " + mailingList.size());

        /* Print all mail address */
        for (Object mail : mailingList) {
            JSONObject jsonObj = JSONManager.parseAs((JSONObject) mail, "mail");
            System.out.println("Mail address: " + jsonObj.get("address"));
        }
        System.out.println();

        /* Get one random mail address */
        int index = random.nextInt(mailingList.size());
        System.out.println("Random mail address: " + JSONManager.parseAs((JSONObject) mailingList.get(index), "mail").get("address"));

        /* Server's config*/
        JSONArray serverCfgList = jsonManager.readFromFile("./lab4/src/config/configServer.json");
        System.out.println("Server's config size: " + serverCfgList.size());
        System.out.println("Server's config [0]: " + serverCfgList.get(0));
        int port  = Integer.parseInt(JSONManager.parseAs((JSONObject) serverCfgList.get(0), "config").get("port").toString());
        String ip = (String) JSONManager.parseAs((JSONObject) serverCfgList.get(0), "config").get("ip");
        System.out.println("Server's config [0]: " + ip + ":" + port);
    }
}
