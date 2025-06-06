package ee.jackaltech.conferenceplatform.adapter.database.conference;

import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConferenceRoomRepositoryAdapter implements
        SaveConferenceRoom,
        GetConferenceRoom,
        GetConferenceRoomCapacity,
        FindConferenceRooms
{

    private final ConferenceRoomEntityRepository conferenceRoomEntityRepository;

    @Override
    public void execute(SaveConferenceRoom.Request request) {
        conferenceRoomEntityRepository.save(toEntity(request.getConferenceRoom()));
    }

    @Override
    public Optional<ConferenceRoom> execute(GetConferenceRoom.Request request) {
        return conferenceRoomEntityRepository.findById(request.getConferenceRoomId()).map(this::toModel);
    }

    @Override
    public Optional<Long> execute(GetConferenceRoomCapacity.Request request) {
        return conferenceRoomEntityRepository.findCapacityById(request.getConferenceRoomId()).map(ConferenceRoomEntity::getCapacity);
    }

    @Override
    public Map<UUID, ConferenceRoom> execute(FindConferenceRooms.Request request) {
        if (request.getConferenceRoomIds().isEmpty()) {
            return Map.of();
        }
        return conferenceRoomEntityRepository.findAllById(request.getConferenceRoomIds()).stream()
                .collect(Collectors.toMap(ConferenceRoomEntity::getId, this::toModel));
    }

    private ConferenceRoomEntity toEntity(ConferenceRoom conferenceRoom) {
        return ConferenceRoomEntity.builder()
                .id(conferenceRoom.getId())
                .name(conferenceRoom.getName())
                .status(conferenceRoom.getStatus())
                .location(conferenceRoom.getLocation())
                .capacity(conferenceRoom.getCapacity())
                .build();
    }

    private ConferenceRoom toModel(ConferenceRoomEntity conferenceRoomEntity) {
        return ConferenceRoom.builder()
                .id(conferenceRoomEntity.getId())
                .name(conferenceRoomEntity.getName())
                .status(conferenceRoomEntity.getStatus())
                .location(conferenceRoomEntity.getLocation())
                .capacity(conferenceRoomEntity.getCapacity())
                .build();
    }
}
