// package com.pointwest.bootcamp.eventhubri.repository;

// import com.pointwest.bootcamp.eventhubri.model.Registration;
// import org.springframework.stereotype.Repository;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Repository
// public class RegistrationRepositoryImpl implements RegistrationRepository {
//     private final List<Registration> registrations = new ArrayList<>();

//     @Override
//     public List<Registration> findByEventId(Long eventId) {
//         if (eventId == null)
//             return new ArrayList<>();
//         return registrations.stream()
//                 .filter(r -> eventId.equals(r.getEventId()))
//                 .toList();
//     }

//     @Override
//     public List<Registration> findByAttendeeId(String attendeeId) {
//         if (attendeeId == null)
//             return new ArrayList<>();
//         return registrations.stream()
//                 .filter(r -> attendeeId.equals(r.getAttendeeId()))
//                 .toList();
//     }

//     @Override
//     public Optional<Registration> findById(String registrationId) {
//         if (registrationId == null)
//             return Optional.empty();
//         return registrations.stream()
//                 .filter(r -> registrationId.equals(r.getRegistrationId()))
//                 .findFirst();
//     }

//     @Override
//     public Registration save(Registration registration) {
//         registrations.removeIf(r -> r.getRegistrationId().equals(registration.getRegistrationId()));
//         registrations.add(registration);
//         return registration;
//     }
// }
