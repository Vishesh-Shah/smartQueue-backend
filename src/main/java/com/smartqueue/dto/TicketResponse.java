package com.smartqueue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private String ticketCode;
    private String eventName;
    private Integer position;
    private String status;
    private Integer estimatedWaitMinutes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
