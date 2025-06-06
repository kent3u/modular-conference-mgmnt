package ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception;

public class ConferenceRoomStatusUpdateConflictException extends RuntimeException {

    public ConferenceRoomStatusUpdateConflictException() {
        super("Conference room has future conferences and under construction status cannot be used");
    }
}
