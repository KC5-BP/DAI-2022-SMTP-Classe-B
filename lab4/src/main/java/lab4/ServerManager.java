package lab4;

import java.net.Socket;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Objects;

public class ServerManager {
    private final String IP;
    private final int  PORT;
    private final Socket socket;
    private final BufferedReader  in;
    private final BufferedWriter out;

    public ServerManager(String ip, int port) throws Exception {
        if (ip == null || ip.isEmpty())
            throw new Exception("IP cannot be null or empty");

        /* Initialise attributes */
        this.IP = ip;
        this.PORT = port;

        /* Create socket */
        this.socket = new Socket(this.IP, this.PORT);

        /* Create in + out */
        this.in  = new BufferedReader(new InputStreamReader( this.socket.getInputStream() ));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        if (in  == null)
            throw new Exception("Input stream is null");

        if (out == null)
            throw new Exception("Output stream is null");
    }

    public void send() throws Exception {
        this.out.write("\r\n");
        this.out.flush();
    }

    public void send(String message) throws Exception {
        if (message == null || message.isEmpty())
            throw new Exception("Message cannot be null or empty");

        this.out.write(message + "\r\n");
        this.out.flush();
    }

    public String receive() throws Exception {

        String message = this.in.readLine();
        while (message == null || message.isEmpty()) {
            message = this.in.readLine();
        }

        return message;
    }

    public void close() throws Exception {
        this.in.close();
        this.out.close();
        this.socket.close();
    }
}
