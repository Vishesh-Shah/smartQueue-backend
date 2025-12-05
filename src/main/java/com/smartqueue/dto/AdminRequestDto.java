package com.smartqueue.dto;

import lombok.Data;

@Data
public class AdminRequestDto {
    private String businessName;
    private String ownerName;
    private String email;
    private String phone;
    private String businessAddress;
    private String businessType;
}