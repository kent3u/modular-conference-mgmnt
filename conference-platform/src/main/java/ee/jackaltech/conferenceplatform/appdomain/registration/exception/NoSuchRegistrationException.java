package ee.jackaltech.conferenceplatform.appdomain.registration.exception;

public class NoSuchRegistrationException extends RuntimeException {

    public NoSuchRegistrationException() {
        super("Registration not found");
    }
}
