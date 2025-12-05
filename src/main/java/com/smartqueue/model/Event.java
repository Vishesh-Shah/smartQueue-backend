package com.smartqueue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private Integer maxTokens;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer currentTokenCount;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10")
    private Integer averageServiceMinutes;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(nullable = true)
    private Long adminId;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private LocalDate eventDate;

    @Column(nullable = true)
    private LocalTime eventTime;

    @Column(nullable = true)
    private Integer durationHours;
}
