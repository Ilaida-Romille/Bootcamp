import java.util.List;

public interface RegistrationService {
    RegistrationResponseDto registerAttendee(RegisterAttendeeRequestDto requestDto);
    RegistrationResponseDto updateRegistration(String registrationId, UpdateRegistrationRequestDto requestDto);
    void cancelRegistration(String registrationId);
    RegistrationResponseDto getRegistrationById(String registrationId);
    List<RegistrationResponseDto> getRegistrationsByEventId(String eventId);
    List<RegistrationResponseDto> getRegistrationsByAttendeeId(String attendeeId);
}