package lab4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;


public class App {
    /* Config. files + Global(s): */
    private static final String SERVER_CFG_FILE = "config/configServer.json";
    private static final String MSG_BODIES_FILE = "config/mailBodies.json";
    private static final String MAILING_LIST_FILE = "config/mailList.json";

    /* Contents of the previous config. files: */
    private static int SMTP_PORT;
    private static String IP, ENCODING;
    private static String[] MAILS;
    private static String[] MSG_SUBJECTS;
    private static String[] MSG_BODIES;

    /* Socket to MockMockServer (using ServerWrapper created class) */
    private static ServerWrapper SERVER;

    public static void main(String[] args) {
        /* Random index, used for: Group size, Subject, Mail Bodies */
        Random randomIndex = new Random();

        /* Read config. files: */
        System.out.println("CFG $> Reading Server's configs...");
        readServerConfig();
        System.out.println("CFG $> " + IP + ":" + SMTP_PORT + " with " + ENCODING + " encoding");

        System.out.println("CFG $> Reading mailing list...");
        readMailingList();
        System.out.println("CFG $> " + MAILS.length + " mails addresses extracted");

        System.out.println("CFG $> Reading message bodies...");
        readMailBodies();
        System.out.println("CFG $> " + MSG_BODIES.length + " message bodies & subjects at disposal\n");

        /* Generate random group */
        System.out.print("Create group of ");
        int groupSize;
        do {
            groupSize = randomIndex.nextInt(MAILS.length);
        } while (groupSize < Group.GROUP_SIZE_MIN);
        System.out.println(groupSize + " victims\n");

        Group group = new Group(groupSize, MAILS);


        System.out.println("Creating & Starting connection to mock server...");
        createConnectServer();

        /* Send msg corresponding to SMTP format */
        sendMail(group);

        /* Closing connection to server properly */
        try {
            SERVER.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Import server config. and stock resulting param's in global
     * SMTP_PORT, IP and ENCODING
     */
    private static void readServerConfig() {
        try {
            JSONArray serverCfgsRead = JSONExtractor.readFromFile(SERVER_CFG_FILE);

            /* Take first index for MockMockServer */
            JSONObject serverCfg = JSONExtractor.parseAs((JSONObject) serverCfgsRead.get(0), "config");

            SMTP_PORT = Integer.parseInt(serverCfg.get("portSMTP").toString());
            IP = (String) serverCfg.get("ip");
            ENCODING = (String) serverCfg.get("encoding");
        } catch (Exception e) {
            throw new RuntimeException("Error  : Failed to read/extract server's config from file\nDetails: " + e.getMessage());
        }
    }

    /**
     * Import mailing list and stock resulting mail addresses in MAILS
     */
    private static void readMailingList() {
        try {
            JSONArray mailsRead = JSONExtractor.readFromFile(MAILING_LIST_FILE);

            MAILS = new String[mailsRead.size()];
            for (int i = 0; i < mailsRead.size(); i++) {
                JSONObject jsonObj = JSONExtractor.parseAs((JSONObject) mailsRead.get(i), "mail");
                MAILS[i] = (String) jsonObj.get("address");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error  : Failed to read/extract mailing list from file\nDetails: " + e.getMessage());
        }
    }

    /*
     * Import message bodies and stock resulting mail's content in global
     * MSG_SUBJECTS and MSG_BODIES
     */
    private static void readMailBodies() {
        try {
            JSONArray msgBodiesRead = JSONExtractor.readFromFile(MSG_BODIES_FILE);

            MSG_BODIES = new String[msgBodiesRead.size()];
            MSG_SUBJECTS = new String[msgBodiesRead.size()];
            for (int i = 0; i < msgBodiesRead.size(); i++) {
                JSONObject jsonObj = JSONExtractor.parseAs((JSONObject) msgBodiesRead.get(i), "mailBody");
                MSG_BODIES[i] = (String) jsonObj.get("body");
                MSG_SUBJECTS[i] = (String) jsonObj.get("subject");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error  : Failed to read/extract mails' bodies from file\nDetails: " + e.getMessage());
        }
    }

    /**
     * Create & connect to server (MockMock for this application)
     * and stock resulting socket in global SERVER
     */
    private static void createConnectServer() {
        try {
            SERVER = new ServerWrapper(IP, SMTP_PORT, Charset.forName(ENCODING));
            System.out.println("S: " + SERVER.receive());

            //SERVER.send("HELP");
            //System.out.print("S: " + SERVER.receiveHelp("214 End of HELP info"));
            System.out.println("-------------------------------------");
        } catch (Exception e) {
            throw new RuntimeException("Error  : Failed to create/connect to MockMockServer\nDetails: " + e.getMessage());
        }
    }

    /**
     * Send randomly chosen mail to group of victims, respecting SMTP format
     */
    private static void sendMail(Group group) {
        String[] msgFormat = {
                "HELO Dump",
                "MAIL FROM:<%s>",
                "RCPT TO:<%s>",
                "DATA",
                "QUIT"
        };
        Random randomIndex = new Random();

        String realSender = group.getRealSender();
        String fakeSender = group.getFakeSender();
        String[] recipients =  group.getVictims();

        try {
            for (int i = 0; i < msgFormat.length; i++) {
                switch (i) {
                    case 1: /* MAIL FROM */
                        SERVER.send(String.format(msgFormat[i], realSender));
                        break;
                    case 2: /* RCPT TO */
                        for (String recipient : recipients)
                            SERVER.send(String.format(msgFormat[i], recipient));
                        break;
                    case 3: /* DATA */
                        SERVER.send(msgFormat[i]);
                        System.out.println("S: " + SERVER.receive());

                        /* Mail header */
                        /* From: */
                        SERVER.send("From: " + fakeSender);

                        /* To: (for each victims) */
                        StringBuilder listVictims = new StringBuilder();
                        listVictims.append("To: ");
                        for (int j = 0; j < recipients.length; j++) {
                            listVictims.append(recipients[j]);

                            if (j + 1 != recipients.length)
                                listVictims.append(", ");
                        }
                        SERVER.send(listVictims.toString());

                        /* Add encoding to Subject AND Bodies */
                        SERVER.send("Content-Type: text/plain; charset=" + ENCODING);

                        /* Subject: */
                        int rand = randomIndex.nextInt(MSG_SUBJECTS.length);
                        SERVER.send("Subject: " + encodeToHtml(MSG_SUBJECTS[rand], ENCODING, "B"));
                        // Empty line needed to be clean between header and body
                        SERVER.send();

                        /* Mail Body (actual message) */
                        /* Encoding the MSG_BODIES[] lead to display the Base64
                         *      ended version in the mailbox
                         *          instead of the ASCII readable char. */
                        //SERVER.send(encodeToHtml(MSG_BODIES[rand], ENCODING, "B"));
                        SERVER.send(MSG_BODIES[rand]);
                        SERVER.send(".");
                        break;
                    default:
                        SERVER.send(msgFormat[i]);
                        break;
                }
                System.out.println("C: " + msgFormat[i]);
                System.out.println("S: " + SERVER.receive());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error  : Failed to send mail\nDetails: " + e.getMessage());
        }
    }

    /**
     * Add HTML-utf8 + base64 encoding
     * @return String encoded
     */
    private static String encodeToHtml(String str, String encoding, String charset) {
        return String.format("=?%s?%s?%s?=", encoding, charset, Base64.getEncoder().encodeToString(str.getBytes()));
    }
}

