package no.mikkel.emailextractor.server;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static no.mikkel.emailextractor.common.NetworkConstants.*;

public class WebpageScraper {

    // Simple (slightly altered) regex taken from: https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$";

    /**
     * (Optimistic) method to retrieve all e-mails from a web page.
     *
     * @param url Any valid URL.
     * @return a List of all e-mail adresses found on the web page.
     * @throws ScrapingException If the web page was not found, or no e-mail adresses found - or any error occurs.
     */
    List<String> readEmailsFromUrl(final String url) throws ScrapingException {
        try (BufferedReader buf = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            List<String> collect = buf.lines().collect(Collectors.toList());
            List<String> linesIncludingAt = collect.stream().filter(htmlLine -> htmlLine.contains("@")).collect(Collectors.toList());
            List<String> words = new ArrayList<>();
            linesIncludingAt.forEach(line -> Collections.addAll(words, line.split("[ :<>]")));
            List<String> emailsFound = words.stream().filter(s -> s.matches(EMAIL_REGEX)).collect(Collectors.toList());

            if (emailsFound.isEmpty()) {
                throw new NoEmailsFoundException();
            }

            return emailsFound;
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            throw new ScrapingException(CODE_WEB_PAGE_NOT_EXISTS, PAGE_NOT_FOUND_MESSAGE);
        } catch (NoEmailsFoundException e) {
            System.out.println("No emails found in url: " + url);
            throw new ScrapingException(CODE_EMAIL_NOT_EXISTS, EMAILS_NOT_FOUND_MESSAGE);
        }
    }
}
