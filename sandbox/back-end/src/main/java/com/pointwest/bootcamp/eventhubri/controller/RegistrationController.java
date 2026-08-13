package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionPopularitySummaryDto;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.service.RegistrationService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public RegistrationDto registerAttendee(String attendeeId, Long eventId, String dietaryInfo) {
        System.out.println("Registering Attendee: " + attendeeId + " for Event: " + eventId);
        Registration registration = registrationService.registerAttendee(attendeeId, eventId, dietaryInfo);
        return new RegistrationDto(registration);
    }

    public void cancelRegistration(String registrationId) {
        System.out.println("Cancelling Registration: " + registrationId);
        registrationService.cancelRegistration(registrationId);
    }

    public void selectSessions(String registrationId, List<String> sessionIds) {
        System.out.println("Selecting Sessions for Registration: " + registrationId);
        registrationService.selectSessions(registrationId, sessionIds);
    }

    public SessionPopularitySummaryDto getSessionPopularitySummary(Long eventId){
        System.out.println("Fetching Session Popularity Summary for event " + eventId);
        return registrationService.getSessionPopularitySummary(eventId);
    }
}
