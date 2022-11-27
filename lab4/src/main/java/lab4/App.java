package lab4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class App {
    /* Config. files: */
    private static final String SERVER_CFG_FILE   = "./lab4/src/config/configServer.json";
    private static final String MSG_BODIES_FILE   = "./lab4/src/config/mailBodies.json";
    private static final String MAILING_LIST_FILE = "./lab4/src/config/mailList.json";

    /* Contents of the previous config. files: */
    private static int PORT_SMTP;
    private static String IP;
    private static String[] MAILS;
    private static String[] MSG_SUBJECTS;
    private static String[] MSG_BODIES;

    public static void main(String[] args) {
        /* Read Server's configs and take the one for MockMockServer (@ [0]) */
        try {
            System.out.println("Reading Server's configs...");
            JSONArray serverCfgsRead = JSONManager.readFromFile(SERVER_CFG_FILE);
            JSONObject serverCfg = JSONManager.parseAs((JSONObject) serverCfgsRead.get(0), "config");

            PORT_SMTP = Integer.parseInt(serverCfg.get("portSMTP").toString());
            IP = (String) serverCfg.get("ip");
        } catch (Exception e) {
            System.out.println("Error  : Failed to read/extract server's config from file");
            System.out.println("Details: " + e);
        }

        /* Import mailing list */
        try {
            System.out.println("Reading mailing list...");
            JSONArray mailsRead = JSONManager.readFromFile(MAILING_LIST_FILE);
            System.out.println("Read " + mailsRead.size() + " mails");

            MAILS = new String[mailsRead.size()];
            for (int i = 0; i < mailsRead.size(); i++) {
                JSONObject jsonObj = JSONManager.parseAs((JSONObject) mailsRead.get(i), "mail");
                MAILS[i] = (String) jsonObj.get("address");
            }
        } catch (Exception e) {
            System.out.println("Error  : Failed to read/extract mailing list from file");
            System.out.println("Details: " + e);
        }

        /* Import message bodies */
        try {
            System.out.println("Reading message bodies...");
            JSONArray msgBodiesRead = JSONManager.readFromFile(MSG_BODIES_FILE);
            System.out.println(msgBodiesRead.size() + " message bodies at disposal");

            MSG_BODIES   = new String[msgBodiesRead.size()];
            MSG_SUBJECTS = new String[msgBodiesRead.size()];
            for (int i = 0; i < msgBodiesRead.size(); i++) {
                JSONObject jsonObj = JSONManager.parseAs((JSONObject) msgBodiesRead.get(i), "mailBody");
                MSG_BODIES[i]   = (String) jsonObj.get("body");
                MSG_SUBJECTS[i] = (String) jsonObj.get("subject");
            }
        } catch (Exception e) {
            System.out.println("Error  : Failed to read/extract mails' bodies from file");
            System.out.println("Details: " + e);
        }

        /* Start MockMockServer */
        System.out.println("Creating & Connecting to MockMockServer...");
        try {
            ServerManager serverManager = new ServerManager(IP, PORT_SMTP, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Error  : Failed to create/connect to MockMockServer");
            System.out.println("Details: " + e);
        }


        try {
            Group g = new Group(5);
            String realSender = g.getRealSender();
            String fakeSender = g.getFakeSender();
            String[] victims = g.getVictims();

            System.out.println("Real sender = " + realSender);
            System.out.println("Fake sender = " + fakeSender);
            System.out.println("Victims = " + Arrays.toString(victims));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
