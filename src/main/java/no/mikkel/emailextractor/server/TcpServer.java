package no.mikkel.emailextractor.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.mikkel.emailextractor.common.NetworkConstants.SERVER_PORT;

/**
 * Simple TCP server, based on code / inspired by:
 * https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
 */
public class TcpServer {

    public static final int MAX_CONNECTIONS = 50;

    public static void main(String[] args) throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();
        // We'll allow 50 simultaneous connections (for demo purposes).
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTIONS, localHost)) {
            // Let's start listening for incoming requests.
            System.out.println("TcpServer ready and accepting connections at " + localHost.getHostAddress() + ":" + SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // To allow multiple clients, let's allocate this connection into a thread.
                executorService.execute(new ClientRunnable(clientSocket));
            }
        }
    }
}
