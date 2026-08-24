package com.pointwest.bootcamp.eventhubri.dto;

public interface SessionPopularityStatsProjectionDto {
    Long getCount();
    Long getSum();
    Double getAverage();
    Long getMin();
    Long getMax();
}