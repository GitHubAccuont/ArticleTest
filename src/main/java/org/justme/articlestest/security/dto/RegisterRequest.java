package org.justme.articlestest.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
