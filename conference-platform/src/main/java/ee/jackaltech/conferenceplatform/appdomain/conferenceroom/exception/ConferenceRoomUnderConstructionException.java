package ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception;

public class ConferenceRoomUnderConstructionException extends RuntimeException {

    public ConferenceRoomUnderConstructionException() {
        super("Conference room in under construction status");
    }
}
