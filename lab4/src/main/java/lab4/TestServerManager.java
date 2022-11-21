package lab4;

public class TestServerManager {

    private final static String IP = "127.0.0.1";
    private final static int SMTP_PORT = 25000;

    public static void main(String[] args) {
        try {
            ServerManager server = new ServerManager(IP, SMTP_PORT);
            System.out.println("S: " + server.receive());

            server.send("HELO Dump");
            System.out.println("S: " + server.receive());

            /* Sender */
            server.send("MAIL FROM: <" + "boblebricoleur@gmail.com" + ">");
            System.out.println("S: " + server.receive());

            /* Receptionist */
            server.send("RCPT TO: <" + "gne@gmail.com" + ">");
            System.out.println("S: " + server.receive());

            /* DATA */
            server.send("DATA");
            System.out.println("S: " + server.receive());

            /* Mail's Body */
            server.send("From: boblebricoleur@gmail.com");
            server.send("To: gne@gmail.com");
            server.send("Subject: Test");
            server.send();
            server.send("Hello world!");
            server.send(".");
            System.out.println("S: " + server.receive());

            /* QUITTING */
            server.send("QUIT");
            System.out.println("S: " + server.receive());

            server.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
