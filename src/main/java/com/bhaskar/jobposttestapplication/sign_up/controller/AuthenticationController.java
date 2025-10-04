package com.bhaskar.jobposttestapplication.sign_up.controller;

import com.bhaskar.jobposttestapplication.sign_up.model.BaseModel;
import com.bhaskar.jobposttestapplication.sign_up.model.UserData;
import com.bhaskar.jobposttestapplication.sign_up.model.LoginUserDto;
import com.bhaskar.jobposttestapplication.sign_up.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    private ResponseEntity<BaseModel> userSignIn(@RequestBody LoginUserDto loginUserDto) {
        return authenticationService.fetchUserAuthenticationData(loginUserDto);
    }

    @PostMapping("signup")
    private ResponseEntity<BaseModel> userSignUp(@RequestBody UserData userData) {
        return authenticationService.saveUserAuthentication(userData);
    }

    @PostMapping("me")
    public ResponseEntity<BaseModel> authenticatedUser(@RequestBody UserData userData) {
        return authenticationService.aboutYou(userData);
    }
}
