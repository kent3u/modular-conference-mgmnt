package ee.jackaltech.conferenceplatform.appdomain.conference.exception;

public class NoSuchConferenceException extends RuntimeException {

    public NoSuchConferenceException() {
        super("Conference not found");
    }
}
