package ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception;

public class NoSuchConferenceRoomException extends RuntimeException {

    public NoSuchConferenceRoomException() {
        super("Conference room not found");
    }
}
