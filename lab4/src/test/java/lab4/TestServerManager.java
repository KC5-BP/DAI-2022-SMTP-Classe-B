package lab4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import java.nio.charset.StandardCharsets;

public class TestServerManager {

    private final static String IP = "127.0.0.1";
    private final static int SMTP_PORT = 25000;
    private final static String CRLF = "\r\n";

    public static void main(String[] args) {
        String[] msgFormat = {
                "HELO Dump",
                "MAIL FROM:<%s>",
                "RCPT TO:<%s>",
                "DATA",
                "QUIT"
        };
        Group group = new Group(5);
        String realSender = group.getRealSender();
        String fakeSender = group.getFakeSender();
        String[] recipients = group.getVictims();

        try {
            ServerManager server = new ServerManager(IP, SMTP_PORT, StandardCharsets.UTF_8);
            System.out.println("S: " + server.receive());

            server.send("HELP");
            System.out.println("S: " + server.receiveHelp("214 End of HELP info"));
            System.out.println("--------------------");

            for (int i = 0; i < msgFormat.length; i++) {
                switch (i) { // MAIL FROM
                    case 1:
                        server.send(String.format(msgFormat[i], realSender));
                        break;
                    case 2: // RCPT TO
                        for (String recipient : recipients) {
                            server.send(String.format(msgFormat[i], recipient));
//                            System.out.println(String.format(msgFormat[i], recipient));
                        }
                        break;
                    case 3: // DATA
                        server.send(msgFormat[i]);
                        System.out.println("S: " + server.receive());

                        // From and To
                        server.send("From: " + fakeSender);
                        StringBuilder listVictims = new StringBuilder();
                        listVictims.append("To: ");
                        for (String recipient : recipients) {
                            listVictims.append(recipient).append(", ");
                        }
                        // Deleting the last ", "
                        listVictims.delete(listVictims.length() - 2, listVictims.length());
                        server.send(listVictims.toString());

                        // Subject and Body
                        String[] subjectAndBody = getMailSubjectAndBody();
                        server.send("Subject: " + subjectAndBody[0]);
                        server.send();
                        server.send(subjectAndBody[1]);
                        server.send(".");
                        break;
                    default: // ELSE
                        server.send(msgFormat[i]);
                        break;
                }
                System.out.println("S: " + server.receive());
            }

            server.close();
        } catch (Exception e) { // TODO gestion des exeptions?
            e.printStackTrace();
//            System.out.println(e.getMessage());
        }
    }

    private static String[] getMailSubjectAndBody() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String[] result = new String[2];

        try (FileReader reader = new FileReader("./lab4/src/config/mailBodies.json")) {
            //Read JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray mailBodies = (JSONArray) jsonObject.get("mailBody");

            Random random = new Random();
            int rndIndex = random.nextInt(mailBodies.size());
            result[0] = ((JSONObject) mailBodies.get(rndIndex)).get("subject").toString();
            result[1] = ((JSONObject) mailBodies.get(rndIndex)).get("body").toString();

        } catch (IOException | ParseException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("config/mailBodies.json files not found or invalid");
        }
        return result;
    }
}
