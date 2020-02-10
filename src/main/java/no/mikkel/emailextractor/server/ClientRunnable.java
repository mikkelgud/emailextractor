package no.mikkel.emailextractor.server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static no.mikkel.emailextractor.common.NetworkConstants.CODE_EMAIL_EXISTS;

public class ClientRunnable implements Runnable {
    private static final String SUCCESS_RESPONSE_INITIAL_MSG = "Code (" + CODE_EMAIL_EXISTS + "): ";
    private Socket client;

    ClientRunnable(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        // Using try-with-resources (java 7) to handle closing of closeables.
        try (
                InputStream input = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream out = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))
        ) {

            String line = reader.readLine();
            System.out.println("Message from client: " + line);

            WebpageScraper webpageScraper = new WebpageScraper();
            try {
                List<String> strings = webpageScraper.readEmailsFromUrl(line);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(SUCCESS_RESPONSE_INITIAL_MSG);
                stringBuilder.append("\n");
                strings.forEach(email -> stringBuilder.append(email).append("\n"));
                System.out.println("Writing response to user: " + stringBuilder.toString());
                writer.write(stringBuilder.toString());
            } catch (ScrapingException e) {
                writer.write(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
