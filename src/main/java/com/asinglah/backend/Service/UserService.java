package com.asinglah.backend.Service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.asinglah.backend.DTO.CreateSignUpRequest;
import com.asinglah.backend.DTO.SignInDTO;
import com.asinglah.backend.DTO.ResponseClass.SignUpUserResponseDTO;
import com.asinglah.backend.Entity.User;

import com.asinglah.backend.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){

        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;

    }

    @Transactional
    public SignUpUserResponseDTO createUser(CreateSignUpRequest newUser){

        

        //find user by email address
        User existingUser = userRepository.findByEmailNative(newUser.getEmailAddress());

        if(existingUser!=null){
            throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
            "The email address " + newUser.getEmailAddress() + " has been taken, failed to sign up"
            );
           
        }
        

        //business logic
        //get the hashpassword function from helper class
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());

        User user = new User();
        
        user.setEmailAddress(newUser.getEmailAddress());
        user.setFullName(newUser.getFullName());
        user.setPassword(hashedPassword);
        user.setUpdatedAt(LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0));
    

        User success =userRepository.save(user);

        if(success!=null){
        SignUpUserResponseDTO userResponse =  new SignUpUserResponseDTO(
                        LocalDateTime.now(),
                        "User created successfully!"
                );

                return userResponse;
        }
        else{
            SignUpUserResponseDTO userResponse =  new SignUpUserResponseDTO(
                        LocalDateTime.now(),
                        "Failed to create"
                );
       

        return  userResponse;
        }
    }


    

    @Transactional
    public User signInVerification(SignInDTO signInInfo){

        

        User user = userRepository.findByEmailNative(signInInfo.getEmailAddress());
        
        if(user==null){
              throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
          "Invalid Credentials"
            );
        }

        if (!passwordEncoder.matches(signInInfo.getPassword(), user.getPassword())) 
        {
                throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
          "Invalid Credentials"
            );
        }

        //provide back jwt token 

        return user;
        
    }

}
