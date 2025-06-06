package ee.jackaltech.conferencegateway;

import ee.jackaltech.conferenceplatform.appdomain.conference.exception.CancellingActiveConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceOverlapException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceTimeException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.*;
import ee.jackaltech.conferenceplatform.appdomain.registration.exception.NoSuchRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConferenceTimeException.class)
    public void handleConferenceTimeException(ConferenceTimeException e) {
        log.warn("Given conference time is faulty", e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NoSuchConferenceRoomException.class)
    public void handleNoSuchConferenceRoomException(NoSuchConferenceRoomException e) {
        log.warn("No processable conference room was present", e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NoSuchConferenceException.class)
    public void handleNoSuchConferenceException(NoSuchConferenceException e) {
        log.warn("No processable conference was present", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConferenceRoomUnderConstructionException.class)
    public void handleConferenceRoomUnderConstructionException(ConferenceRoomUnderConstructionException e) {
        log.warn("Conference room is not available", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConferenceOverlapException.class)
    public void handleConferenceOverlapException(ConferenceOverlapException e) {
        log.warn("Conference overlaps with an existing conference", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CancellingActiveConferenceException.class)
    public void handleCancellingActiveConferenceException(CancellingActiveConferenceException e) {
        log.warn("Tried to cancel conference that is already taking place", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConferenceRoomStatusUpdateConflictException.class, ConferenceRoomCapacityUpdateConflictException.class})
    public void handleConferenceRoomUpdateException(Exception e) {
        log.info("Conference room update values in conflict with rules", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConferenceRoomCapacityException.class)
    public void handeConferenceRoomCapacityException(Exception e) {
        log.info("Conference room capacity reached", e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NoSuchRegistrationException.class)
    public void handleNoSuchRegistrationException(Exception e) {
        log.warn("No such registration", e);
    }
}
