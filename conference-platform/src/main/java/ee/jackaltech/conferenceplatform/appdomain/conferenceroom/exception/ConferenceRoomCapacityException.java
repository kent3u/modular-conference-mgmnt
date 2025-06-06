package ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception;

public class ConferenceRoomCapacityException extends RuntimeException {

    public ConferenceRoomCapacityException() {
        super("Conference room capacity exceeded");
    }
}
