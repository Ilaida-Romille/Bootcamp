package com.pointwest.bootcamp.eventhubri.modules.platform.dto;

import java.util.List;

public record PlatformOwnerDashboardDto(
        long organizersCount,
        long eventsCount,
        List<MonthlyEventCountDto> monthlyEventsData) {
}