package com.bhaskar.jobposttestapplication.sign_up.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
}
