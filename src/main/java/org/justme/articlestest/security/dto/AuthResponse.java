package org.justme.articlestest.security.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
