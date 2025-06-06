package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import ee.jackaltech.conferenceplatform.appdomain.conference.ExistsConferenceInRoomInTheFuture;
import ee.jackaltech.conferenceplatform.appdomain.conference.ExistsFutureConferenceWithMinRegistrationsByRoomId;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomCapacityUpdateConflictException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomStatusUpdateConflictException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangeConferenceRoom {

    private final GetConferenceRoom getConferenceRoom;
    private final SaveConferenceRoom saveConferenceRoom;
    private final ExistsConferenceInRoomInTheFuture existsConferenceInRoomInTheFuture;
    private final ExistsFutureConferenceWithMinRegistrationsByRoomId existsFutureConferenceWithMinRegistrationsByRoomId;


    public void execute(Request request) {
        ConferenceRoom conferenceRoom = getConferenceRoom.execute(GetConferenceRoom.Request.of(request.getConferenceRoomId()))
                .orElseThrow(NoSuchConferenceRoomException::new);

        validateConferenceRoomChange(request, conferenceRoom.getCapacity());

        ConferenceRoom changedConferenceRoom = conferenceRoom.toBuilder()
                .capacity(request.getConferenceRoomCapacity())
                .status(request.getConferenceRoomStatus())
                .build();
        saveConferenceRoom.execute(SaveConferenceRoom.Request.of(changedConferenceRoom));
    }

    private void validateConferenceRoomChange(Request request, Long oldCapacity) {
        if (Objects.equals(ConferenceRoomStatus.UNDER_CONSTRUCTION, request.getConferenceRoomStatus())
                && existsConferenceInRoomInTheFuture.execute(ExistsConferenceInRoomInTheFuture.Request.of(request.getConferenceRoomId()))) {
            throw new ConferenceRoomStatusUpdateConflictException();
        }
        if (request.getConferenceRoomCapacity() < oldCapacity
                && existsFutureConferenceWithMinRegistrationsByRoomId.execute(ExistsFutureConferenceWithMinRegistrationsByRoomId.Request.of(request.getConferenceRoomId(), request.getConferenceRoomCapacity()))) {
            throw new ConferenceRoomCapacityUpdateConflictException();
        }
    }

    @Value
    @Builder
    public static class Request {
        UUID conferenceRoomId;
        Long conferenceRoomCapacity;
        ConferenceRoomStatus conferenceRoomStatus;
    }
}
