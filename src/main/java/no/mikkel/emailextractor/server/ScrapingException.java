package no.mikkel.emailextractor.server;

class ScrapingException extends Throwable {
    ScrapingException(int code, String reason) {
        super(String.format("code (%s): %s", code, reason));
    }
}
