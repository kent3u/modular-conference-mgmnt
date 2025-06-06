package ee.jackaltech.conferenceplatform.appdomain.conference.exception;

public class CancellingActiveConferenceException extends RuntimeException {

    public CancellingActiveConferenceException() {
        super("Cancelling conference already taking place is not allowed");
    }
}
