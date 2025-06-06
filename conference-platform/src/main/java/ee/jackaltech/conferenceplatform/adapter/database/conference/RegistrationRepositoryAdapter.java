package ee.jackaltech.conferenceplatform.adapter.database.conference;

import ee.jackaltech.conferenceplatform.appdomain.registration.*;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationRepositoryAdapter implements
        DeleteRegistrationsByConferenceId,
        FindConferenceRegistrationAmount,
        FindRegistrationsAmountsByConferenceIds,
        SaveRegistration,
        GetRegistrationByParticipantCode,
        DeleteRegistration
{

    private final RegistrationEntityRepository registrationEntityRepository;
    private final EntityManager entityManager;

    @Override
    public void execute(DeleteRegistrationsByConferenceId.Request request) {
        LocalDateTime deletionTime = LocalDateTime.now();

        Set<RegistrationEntity> registrationEntities = registrationEntityRepository.findByConferenceId(request.getConferenceId()).stream()
                .map(registrationEntity -> {
                    registrationEntity.setDeletedAt(deletionTime);
                    return registrationEntity;
                })
                .collect(Collectors.toUnmodifiableSet());
        registrationEntityRepository.saveAllAndFlush(registrationEntities);
    }

    @Override
    public Long execute(FindConferenceRegistrationAmount.Request request) {
        return registrationEntityRepository.countByConferenceId(request.getConferenceId());
    }

    @Override
    public Map<UUID, Long> execute(FindRegistrationsAmountsByConferenceIds.Request request) {
        if (request.getConferenceIds().isEmpty()) {
            return Map.of();
        }
        return registrationEntityRepository.findByConferenceIdIn(request.getConferenceIds()).stream()
                .collect(Collectors.groupingBy(RegistrationEntity::getConferenceId, Collectors.counting()));
    }

    @Override
    public void execute(SaveRegistration.Request request) {
        registrationEntityRepository.save(toEntity(request.getRegistration()));
    }

    @Override
    public Optional<Registration> execute(GetRegistrationByParticipantCode.Request request) {
        return registrationEntityRepository.findByConferenceParticipantCode(request.getConferenceParticipantCode().getCode())
                .map(this::toModel);
    }

    @Override
    public void execute(DeleteRegistration.Request request) {
        registrationEntityRepository.findByConferenceParticipantCode(request.getConferenceParticipantCode().getCode())
                .ifPresentOrElse(registration -> {
                    registration.setDeletedAt(LocalDateTime.now());
                    registrationEntityRepository.saveAndFlush(registration);
                    entityManager.detach(registration);
                }, () -> log.warn("Registration with participant code {} not present for deletion", request.getConferenceParticipantCode()));
    }

    private Registration toModel(RegistrationEntity registrationEntity) {
        return Registration.builder()
                .conferenceId(registrationEntity.getConferenceId())
                .conferenceParticipantCode(ConferenceParticipantCode.of(registrationEntity.getConferenceParticipantCode()))
                .participantId(registrationEntity.getParticipantId())
                .build();
    }

    private RegistrationEntity toEntity(Registration registration) {
        return RegistrationEntity.builder()
                .conferenceParticipantCode(registration.getConferenceParticipantCode().getCode())
                .conferenceId(registration.getConferenceId())
                .participantId(registration.getParticipantId())
                .build();
    }
}
