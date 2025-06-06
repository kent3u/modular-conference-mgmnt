package ee.jackaltech.conferenceplatform.appdomain.conference.exception;

public class ConferenceOverlapException extends RuntimeException {

    public ConferenceOverlapException() {
        super("Conference overlap fault");
    }
}
