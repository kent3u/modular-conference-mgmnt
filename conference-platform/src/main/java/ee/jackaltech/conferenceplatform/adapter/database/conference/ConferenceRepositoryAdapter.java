package ee.jackaltech.conferenceplatform.adapter.database.conference;

import ee.jackaltech.conferenceplatform.appdomain.conference.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConferenceRepositoryAdapter implements
        ExistsConferenceOverlapInRoom,
        SaveConference,
        GetConference,
        DeleteConference,
        ExistsConferenceInRoomInTheFuture,
        ExistsFutureConferenceWithMinRegistrationsByRoomId,
        FindConferencesForTimePeriod
{

    private final ConferenceEntityRepository conferenceEntityRepository;
    private final ConferenceSpecifications conferenceSpecifications;
    private final EntityManager entityManager;

    @Override
    public boolean execute(ExistsConferenceOverlapInRoom.Request request) {
        Specification<ConferenceEntity> specification = conferenceSpecifications.conferenceRoomIdEquals(request.getConferenceRoomId())
                .and(conferenceSpecifications.lastingInFuture())
                .and(conferenceSpecifications.overlapsWithTime(request.getConferenceStartTime(), request.getConferenceEndTime()));
        return conferenceEntityRepository.exists(specification);
    }

    @Override
    public void execute(SaveConference.Request request) {
        conferenceEntityRepository.save(toEntity(request.getConference()));
    }

    @Override
    public Optional<Conference> execute(GetConference.Request request) {
        return conferenceEntityRepository.findById(request.getConferenceId()).map(this::toModel);
    }

    @Override
    public void execute(DeleteConference.Request request) {
        ConferenceEntity toBeDeleted = toEntity(request.getConference());
        toBeDeleted.setDeletedAt(LocalDateTime.now());
        conferenceEntityRepository.saveAndFlush(toBeDeleted);
        entityManager.detach(toBeDeleted);
    }

    @Override
    public boolean execute(ExistsConferenceInRoomInTheFuture.Request request) {
        Specification<ConferenceEntity> specification = conferenceSpecifications.conferenceRoomIdEquals(request.getConferenceRoomId())
                .and(conferenceSpecifications.lastingInFuture());
        return conferenceEntityRepository.exists(specification);
    }

    @Override
    public boolean execute(ExistsFutureConferenceWithMinRegistrationsByRoomId.Request request) {
        Specification<ConferenceEntity> specification = conferenceSpecifications.conferenceRoomIdEquals(request.getConferenceRoomId())
                .and(conferenceSpecifications.lastingInFuture())
                .and(conferenceSpecifications.minNumberOfRegistrations(request.getNumberOfMinRegistrations()));
        return conferenceEntityRepository.exists(specification);
    }

    @Override
    public Set<Conference> execute(FindConferencesForTimePeriod.Request request) {
        Specification<ConferenceEntity> specification = conferenceSpecifications.startTimeBetween(request.getFrom(), request.getUntil());
        return conferenceEntityRepository.findAll(specification).stream()
                .map(this::toModel)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Conference toModel(ConferenceEntity conferenceEntity) {
        return Conference.builder()
                .id(conferenceEntity.getId())
                .conferenceRoomId(conferenceEntity.getConferenceRoomId())
                .startTime(conferenceEntity.getStartTime())
                .endTime(conferenceEntity.getEndTime())
                .build();
    }

    private ConferenceEntity toEntity(Conference conference) {
        return ConferenceEntity.builder()
                .id(conference.getId())
                .conferenceRoomId(conference.getConferenceRoomId())
                .startTime(conference.getStartTime())
                .endTime(conference.getEndTime())
                .build();
    }
}
