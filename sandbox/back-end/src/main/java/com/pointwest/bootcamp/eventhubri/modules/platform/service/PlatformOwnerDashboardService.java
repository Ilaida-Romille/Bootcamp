package com.pointwest.bootcamp.eventhubri.modules.platform.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pointwest.bootcamp.eventhubri.modules.account.repository.OrganizationRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.platform.dto.MonthlyEventCountDto;
import com.pointwest.bootcamp.eventhubri.modules.platform.dto.PlatformOwnerDashboardDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatformOwnerDashboardService {

    private static final String[] MONTH_LABELS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final OrganizationRepository organizationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public PlatformOwnerDashboardDto getDashboardMetrics() {
        int currentYear = Year.now().getValue();

        long organizersCount = organizationRepository.count();
        long eventsCount = eventRepository.countEventsCreatedInYear(currentYear);

        Map<Integer, Long> monthlyCounts = new HashMap<>();
        for (Object[] row : eventRepository.countEventsByMonthInYear(currentYear)) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            monthlyCounts.put(month, count);
        }

        List<MonthlyEventCountDto> monthlyEventsData = new ArrayList<>(12);
        for (int month = 1; month <= 12; month++) {
            long value = monthlyCounts.getOrDefault(month, 0L);
            monthlyEventsData.add(new MonthlyEventCountDto(MONTH_LABELS[month - 1], value));
        }

        return new PlatformOwnerDashboardDto(organizersCount, eventsCount, monthlyEventsData);
    }
}