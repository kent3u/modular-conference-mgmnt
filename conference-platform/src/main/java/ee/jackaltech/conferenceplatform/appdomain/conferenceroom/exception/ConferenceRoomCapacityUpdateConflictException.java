package ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception;

public class ConferenceRoomCapacityUpdateConflictException extends RuntimeException {

    public ConferenceRoomCapacityUpdateConflictException() {
        super("Conference room has future conferences that exceed new participant limit");
    }
}
