import org.springframework.stereotype.Component;

import java.util.List;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public RegistrationResponseDto registerAttendee(RegisterAttendeeRequestDto requestDto) {
        return registrationService.registerAttendee(requestDto);
    }

    public RegistrationResponseDto updateRegistration(String registrationId, UpdateRegistrationRequestDto requestDto) {
        return registrationService.updateRegistration(registrationId, requestDto);
    }

    public void cancelRegistration(String registrationId) {
        registrationService.cancelRegistration(registrationId);
    }

    public RegistrationResponseDto getRegistrationById(String registrationId) {
        return registrationService.getRegistrationById(registrationId);
    }

    public List<RegistrationResponseDto> getRegistrationsByEventId(String eventId) {
        return registrationService.getRegistrationsByEventId(eventId);
    }

    public List<RegistrationResponseDto> getRegistrationsByAttendeeId(String attendeeId) {
        return registrationService.getRegistrationsByAttendeeId(attendeeId);
    }
}