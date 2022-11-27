package lab4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.charset.Charset;
import java.util.Arrays;    // TODO Might not be needed


public class App {
    /* Config. files: */
    private static final String SERVER_CFG_FILE   = "./lab4/src/config/configServer.json";
    private static final String MSG_BODIES_FILE   = "./lab4/src/config/mailBodies.json";
    private static final String MAILING_LIST_FILE = "./lab4/src/config/mailList.json";

    /* Contents of the previous config. files: */
    private static int SMTP_PORT;
    private static String IP, ENCODING;
    private static String[] MAILS;
    private static String[] MSG_SUBJECTS;
    private static String[] MSG_BODIES;

    /* Socket to MockMockServer (using ServerManager created class) */
    private static ServerManager SERVER;

    public static void main(String[] args) {
        String[] msgFormat = {
                "HELO Dump",
                "MAIL FROM:<%s>",
                "RCPT TO:<%s>",
                "DATA",
                "QUIT"
        };

        /* Read config. files: */
        readConfigFiles();

        /* Start MockMockServer */
        createConnectServer();


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

        /* Closing connection to MockMockServer properly */
        try {
            SERVER.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private static void readConfigFiles() {
        /* --- Import server config. --- */
        try {
            System.out.println("Reading Server's configs...");
            JSONArray serverCfgsRead = JSONManager.readFromFile(SERVER_CFG_FILE);

            /* Take first index for MockMockServer */
            JSONObject serverCfg = JSONManager.parseAs((JSONObject) serverCfgsRead.get(0), "config");

            SMTP_PORT = Integer.parseInt(serverCfg.get("portSMTP").toString());
            IP = (String) serverCfg.get("ip");
            ENCODING = (String) serverCfg.get("encoding");
        } catch (Exception e) {
            System.out.println("Error  : Failed to read/extract server's config from file");
            System.out.println("Details: " + e);
        }

        /* --- Import mailing list --- */
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

        /* --- Import message bodies --- */
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
    }

    /**
     *
     */
    private static void createConnectServer() {
        System.out.println("Creating & Connecting to MockMockServer...");
        try {
            SERVER = new ServerManager(IP, SMTP_PORT, Charset.forName(ENCODING));
            System.out.println("S: " + SERVER.receive());

            SERVER.send("HELP");
            System.out.println("S: " + SERVER.receiveHelp("214 End of HELP info"));
            System.out.println("--------------------");

        } catch (Exception e) {
            System.out.println("Error  : Failed to create/connect to MockMockServer");
            System.out.println("Details: " + e);
        }
    }
}
