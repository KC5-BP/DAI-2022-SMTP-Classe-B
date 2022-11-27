package lab4;

import java.net.Socket;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.*;

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
        this.in  = new BufferedReader(new InputStreamReader( this.socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        if (in  == null)
            throw new Exception("Input stream is null");

        if (out == null)
            throw new Exception("Output stream is null");
    }

    public ServerManager(String ip, int port, Charset cs) throws Exception {
        if (ip == null || ip.isEmpty())
            throw new Exception("IP cannot be null or empty");

        /* Initialise attributes */
        this.IP = ip;
        this.PORT = port;

        /* Create socket */
        this.socket = new Socket(this.IP, this.PORT);

        /* Create in + out */
        this.in  = new BufferedReader(new InputStreamReader( this.socket.getInputStream() , cs));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), cs));

        if (in  == null)
            throw new Exception("Input stream is null");

        if (out == null)
            throw new Exception("Output stream is null");
    }

    /**
     * Send only CRLF to the server
     */
    public void send() throws Exception {
        this.out.write("\r\n");
        this.out.flush();
    }

    /**
     * Send message (String) to the server
     * @throws Exception message can not be null
     */
    public void send(String message) throws Exception {
        if (message == null)
            throw new Exception("Message cannot be null");

        if (message.isEmpty()) {
            this.send();
            return;
        }

        this.out.write(message + "\r\n");
        this.out.flush();
    }

    /**
     * Send message (String Array) to the server
     * @throws Exception message can not be null
     */
    public void send(String[] message) throws Exception {
        for (String line : message)
            this.send(line);
    }

    /**
     * Receive a message from the server
     * @return Server's response as String
     */
    public String receive() throws Exception {
        String message = this.in.readLine();
        while (message == null || message.isEmpty())
            message = this.in.readLine();

        return message;
    }

    /**
     * Receive <n> message from the server
     * @return Server's response as String
     */
    public String receive(int n) throws Exception {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < n; i++)
            message.append(this.receive()).append("\r\n");

        return message.toString();
    }

    /**
     * Receive HELP result from the server
     * @return HELP's response as String
     */
    public String receiveHelp(String endOfHelp) throws Exception {
        StringBuilder message = new StringBuilder();
        String line = this.in.readLine();
        while ( !line.equals(endOfHelp) ) {
            message.append(line).append("\r\n");
            line = this.in.readLine();
        }
        return message.toString();
    }

    /**
     * Close the socket, its input and output streams
     */
    public void close() throws Exception {
        this.in.close();
        this.out.close();
        this.socket.close();
    }
}
