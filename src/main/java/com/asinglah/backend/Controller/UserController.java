package com.asinglah.backend.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.CreateSignUpRequest;
import com.asinglah.backend.DTO.ResponseClass.SignUpUserResponseDTO;

import com.asinglah.backend.Service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){


        this.userService=userService;

    }

    //change parameter 
    @PostMapping("/api/users/signup")
    public SignUpUserResponseDTO userSignUp(@Valid @RequestBody CreateSignUpRequest request){
        
        SignUpUserResponseDTO userResponse = userService.createUser(request);
        return userResponse;

        
    }

    @PostMapping("/api/users/login")
    public String userSignIn(@RequestBody String entity) {
        
        
        return entity;
    }
    

}
