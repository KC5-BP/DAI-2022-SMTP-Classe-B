package lab4;

import java.sql.SQLOutput;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TestJSONManager {
    public static void main(String[] args) {
        Random random = new Random();

        JSONArray mailingList = JSONExtractor.readFromFile("config/mailList.json");
        System.out.println("Mailing list size: " + mailingList.size());

        /* Print all mail address */
        for (Object mail : mailingList) {
            JSONObject jsonObj = JSONExtractor.parseAs((JSONObject) mail, "mail");
            System.out.println("Mail address: " + jsonObj.get("address"));
        }
        System.out.println();

        /* Get one random mail address */
        int index = random.nextInt(mailingList.size());
        System.out.println("Random mail address: " + JSONExtractor.parseAs((JSONObject) mailingList.get(index), "mail").get("address"));

        /* Server's config*/
        JSONArray serverCfgList = JSONExtractor.readFromFile("config/configServer.json");
        System.out.println("Server's config size: " + serverCfgList.size());
        System.out.println("Server's config [0]: " + serverCfgList.get(0));

        int portSMTP  = Integer.parseInt(JSONExtractor.parseAs((JSONObject) serverCfgList.get(0), "config").get("portSMTP").toString());
        int portHTTP  = Integer.parseInt(JSONExtractor.parseAs((JSONObject) serverCfgList.get(0), "config").get("portHTTP").toString());
        String ip = (String) JSONExtractor.parseAs((JSONObject) serverCfgList.get(0), "config").get("ip");
        String encoding = (String) JSONExtractor.parseAs((JSONObject) serverCfgList.get(0), "config").get("ip");
        System.out.println("Server's config [0]: ");
        System.out.println("\tip: " + ip);
        System.out.println("\tencoding: " + encoding);
        System.out.println("\tportSMTP: " + portSMTP);
        System.out.println("\tportHTTP: " + portHTTP);
    }
}
