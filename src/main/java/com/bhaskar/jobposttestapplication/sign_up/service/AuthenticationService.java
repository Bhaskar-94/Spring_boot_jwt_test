package com.bhaskar.jobposttestapplication.sign_up.service;

import com.bhaskar.jobposttestapplication.sign_up.model.BaseModel;
import com.bhaskar.jobposttestapplication.sign_up.model.LoginResponse;
import com.bhaskar.jobposttestapplication.sign_up.model.UserData;
import com.bhaskar.jobposttestapplication.sign_up.repository_dao.AuthenticationDao;
import com.bhaskar.jobposttestapplication.sign_up.model.LoginUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationDao authenticationDao;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<BaseModel> saveUserAuthentication(UserData userData) {
        try {
            Optional<UserData> user = authenticationDao.findByEmail(userData.getEmail());
            System.out.println("USER DATA: " + user);
            if (user.isEmpty()) {
                userData.setPassword(passwordEncoder.encode(userData.getPassword()));
                authenticationDao.save(userData);
                String jwtToken = jwtService.generateToken(userData);
                BaseModel baseModel = new BaseModel();
                baseModel.setData(jwtToken);
                baseModel.setMessage("Success");
                return new ResponseEntity<>(baseModel, HttpStatus.CREATED);
            } else {
                BaseModel baseModel = new BaseModel();
                baseModel.setData(null);
                baseModel.setMessage("User has been already registered!");
                return new ResponseEntity<>(baseModel, HttpStatus.OK);
            }
        } catch (Exception e) {
            BaseModel baseModel = new BaseModel();
            baseModel.setData(null);
            baseModel.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(baseModel, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BaseModel> fetchUserAuthenticationData(LoginUserDto loginUserDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserDto.getEmail(),
                            loginUserDto.getPassword()
                    )
            );
            UserData userData = authenticationDao.findByEmail(loginUserDto.getEmail()).orElseThrow();
            if (authentication.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(userData);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(jwtToken);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());

                BaseModel baseModel = new BaseModel();
                baseModel.setData(loginResponse);
                baseModel.setMessage("Login Successfully");
                return new ResponseEntity<>(baseModel, HttpStatus.OK);
            } else {
                BaseModel baseModel = new BaseModel();
                baseModel.setData(null);
                baseModel.setMessage("Failed to Login");
                return new ResponseEntity<>(baseModel, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            BaseModel baseModel = new BaseModel();
            baseModel.setData(null);
            baseModel.setMessage("EXP-Failure");
            return new ResponseEntity<>(baseModel, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BaseModel> aboutYou(UserData userData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String currentUser = authentication.getPrincipal().toString();
        String jwtToken = jwtService.generateToken(userData);
        BaseModel baseModel = new BaseModel();
        baseModel.setData(jwtToken);
        baseModel.setMessage("About you: ");
        return new ResponseEntity<>(baseModel, HttpStatus.OK);
    }
}
