package lab4;

public class TestServerManager {

    private final static String IP = "127.0.0.1";
    private final static int SMTP_PORT = 25000;
    private final static String CRLF = "\r\n";

    public static void main(String[] args) {
        String[] msgFormat = {
            "HELO Dump",
            "MAIL FROM: <%s>",
            "RCPT TO: <%s>",
            "DATA",
            "QUIT"
        };
        String sender = "boblebricoleur@gmail.com";
        String[] recipients = {
                "gne@gmail.com"
        };
        String[] msgBody = { "Hey!", "Listen!" };
        try {
            ServerManager server = new ServerManager(IP, SMTP_PORT);
            System.out.println("S: " + server.receive());

            server.send("HELP");
            System.out.println("S: " + server.receiveHelp("214 End of HELP info"));
            System.out.println("--------------------");

            for (int i = 0; i < msgFormat.length; i++) {
                switch (i) {
                    case 1:
                        server.send(String.format(msgFormat[i], sender));
                        break;
                    case 2:
                        for (String recipient : recipients) {
                            server.send(String.format(msgFormat[i], recipient));
                        }
                        break;
                    case 3:
                        server.send(msgFormat[i]);
                        System.out.println("S: " + server.receive());

                        server.send("Subject: Test");
                        server.send();
                        server.send(msgBody);
                        server.send(".");
                        break;
                    default:
                        server.send(msgFormat[i]);
                        break;
                }
                System.out.println("S: " + server.receive());
            }

            server.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
