package no.mikkel.emailextractor.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static no.mikkel.emailextractor.common.NetworkConstants.SERVER_PORT;


/**
 * TcpClient for sending URLs to the server (requires a running server instance).
 * Based on / inspired by:
 * https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
 */
public class TcpClient {

    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getLocalHost();

        String inputFromUser;

        try (Socket socket = new Socket(host, SERVER_PORT);
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {
            System.out.println("Connected to server!");
            System.out.println("Type: \"exit\" to leave.");
            do {

                try (
                        BufferedReader userConsole = new BufferedReader(new InputStreamReader(System.in))
                ) {

                    try (
                            InputStream serverInput = socket.getInputStream();
                            BufferedReader serverMessages = new BufferedReader(new InputStreamReader(serverInput))
                    ) {
                        System.out.println("Type in a webpage:");
                        inputFromUser = userConsole.readLine();
                        writer.println(inputFromUser);
                        serverMessages.lines().forEach(System.out::println);
                    }
                }
            } while (!socket.isClosed());

        }

    }
}
