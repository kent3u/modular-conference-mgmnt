package ee.jackaltech.conferenceplatform.appdomain.conference.exception;

public class ConferenceTimeException extends RuntimeException {

    public ConferenceTimeException() {
        super("Conference time before end or in the past");
    }
}
