import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   EventRepository eventRepository,
                                   UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RegistrationResponseDto registerAttendee(RegisterAttendeeRequestDto requestDto) {
        // 1. Validate Event existence
        Event event = eventRepository.findById(requestDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + requestDto.getEventId()));

        // 2. Validate User/Attendee existence
        User user = userRepository.findById(requestDto.getAttendeeId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + requestDto.getAttendeeId()));

        if (!(user instanceof Attendee)) {
            throw new IllegalArgumentException("User with ID " + requestDto.getAttendeeId() + " is not an Attendee");
        }
        Attendee attendee = (Attendee) user;

        // 3. Business Rule: Prevent Duplicate Registrations
        if (registrationRepository.existsByAttendeeIdAndEventId(attendee.getAttendeeId(), event.getEventId())) {
            throw new IllegalStateException("Attendee is already registered for this event.");
        }

        // 4. Business Rule: Check Event Capacity
        if (event.getCapacity() != null) {
            long confirmedCount = registrationRepository.countByEventIdAndStatus(event.getEventId(), RegistrationStatus.CONFIRMED);
            if (confirmedCount >= event.getCapacity()) {
                throw new IllegalStateException("Event capacity reached. Cannot complete registration.");
            }
        }

        // 5. Construct Registration Domain Object
        String registrationId = UUID.randomUUID().toString();
        Registration registration = new Registration(
                registrationId,
                attendee,
                event,
                event.isFoodProvided() ? requestDto.getDietaryRestrictions() : null
        );

        // 6. Map Selected Sessions (if any)
        if (requestDto.getSelectedSessionIds() != null && event.getAgenda() != null) {
            List<Session> eventSessions = event.getAgenda().getSessions();
            for (String sessionId : requestDto.getSelectedSessionIds()) {
                for (Session session : eventSessions) {
                    if (session.getSessionId().equals(sessionId)) {
                        SessionSelection selection = new SessionSelection(UUID.randomUUID().toString(), session);
                        registration.addSessionSelection(selection);
                        break;
                    }
                }
            }
        }

        Registration savedRegistration = registrationRepository.save(registration);
        return mapToRegistrationResponseDto(savedRegistration);
    }

    @Override
    public RegistrationResponseDto updateRegistration(String registrationId, UpdateRegistrationRequestDto requestDto) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + registrationId));

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled registration.");
        }

        // Update dietary restrictions if event provides food
        if (registration.getEvent().isFoodProvided() && requestDto.getDietaryRestrictions() != null) {
            registration.setDietaryRestrictions(requestDto.getDietaryRestrictions());
        }

        // Update selected sessions
        if (requestDto.getSelectedSessionIds() != null && registration.getEvent().getAgenda() != null) {
            List<SessionSelection> newSelections = new ArrayList<>();
            List<Session> eventSessions = registration.getEvent().getAgenda().getSessions();

            for (String sessionId : requestDto.getSelectedSessionIds()) {
                for (Session session : eventSessions) {
                    if (session.getSessionId().equals(sessionId)) {
                        newSelections.add(new SessionSelection(UUID.randomUUID().toString(), session));
                        break;
                    }
                }
            }
            registration.setSessionSelections(newSelections);
        }

        Registration updatedRegistration = registrationRepository.save(registration);
        return mapToRegistrationResponseDto(updatedRegistration);
    }

    @Override
    public void cancelRegistration(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + registrationId));

        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    @Override
    public RegistrationResponseDto getRegistrationById(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + registrationId));

        return mapToRegistrationResponseDto(registration);
    }

    @Override
    public List<RegistrationResponseDto> getRegistrationsByEventId(String eventId) {
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        List<RegistrationResponseDto> dtos = new ArrayList<>();
        for (Registration registration : registrations) {
            dtos.add(mapToRegistrationResponseDto(registration));
        }
        return dtos;
    }

    @Override
    public List<RegistrationResponseDto> getRegistrationsByAttendeeId(String attendeeId) {
        List<Registration> registrations = registrationRepository.findByAttendeeId(attendeeId);
        List<RegistrationResponseDto> dtos = new ArrayList<>();
        for (Registration registration : registrations) {
            dtos.add(mapToRegistrationResponseDto(registration));
        }
        return dtos;
    }

    // Mapper Helper Method
    private RegistrationResponseDto mapToRegistrationResponseDto(Registration registration) {
        RegistrationResponseDto dto = new RegistrationResponseDto();
        dto.setRegistrationId(registration.getRegistrationId());
        
        if (registration.getAttendee() != null) {
            dto.setAttendeeId(registration.getAttendee().getAttendeeId());
            dto.setAttendeeName(registration.getAttendee().getName());
            dto.setAttendeeEmail(registration.getAttendee().getEmail());
        }

        if (registration.getEvent() != null) {
            dto.setEventId(registration.getEvent().getEventId());
            dto.setEventTitle(registration.getEvent().getTitle());
        }

        dto.setRegisteredAt(registration.getRegisteredAt());
        dto.setStatus(registration.getStatus());
        dto.setDietaryRestrictions(registration.getDietaryRestrictions());

        List<SessionResponseDto> selectedSessionDtos = new ArrayList<>();
        if (registration.getSessionSelections() != null) {
            for (SessionSelection selection : registration.getSessionSelections()) {
                if (selection.getSession() != null) {
                    selectedSessionDtos.add(mapToSessionResponseDto(selection.getSession()));
                }
            }
        }
        dto.setSelectedSessions(selectedSessionDtos);

        return dto;
    }

    private SessionResponseDto mapToSessionResponseDto(Session session) {
        SessionResponseDto dto = new SessionResponseDto();
        dto.setSessionId(session.getSessionId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setStartDateTime(session.getStartDateTime());
        dto.setEndDateTime(session.getEndDateTime());
        dto.setLocation(session.getLocation());

        if (session instanceof PresentationSession) {
            dto.setSessionType("PRESENTATION");
            dto.setSpeaker(((PresentationSession) session).getSpeaker());
        } else if (session instanceof BreakSession) {
            dto.setSessionType("BREAK");
            dto.setBreakType(((BreakSession) session).getBreakType());
        }

        return dto;
    }
}