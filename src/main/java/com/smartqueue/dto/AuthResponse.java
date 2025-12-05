package com.smartqueue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String identifier; // email or username

    public AuthResponse(String token, Long id, String identifier) {
        this.token = token;
        this.id = id;
        this.identifier = identifier;
    }
}
