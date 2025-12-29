package com.asinglah.backend.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.asinglah.backend.DTO.CreateSignUpRequest;
import com.asinglah.backend.DTO.UserRequestDTO.ResetPasswordDTO;
import com.asinglah.backend.DTO.UserRequestDTO.SignInRequestDTO;
import com.asinglah.backend.DTO.UserResponseDTO.LogInResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.ResetPasswordResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.SignUpUserResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.UserIdResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.UsersEmailResponseDTO;
import com.asinglah.backend.Entity.User;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.HelperClass.JwtUtil;
import com.asinglah.backend.Repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil){

        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;

    }

    @Transactional
    public APIResponse<SignUpUserResponseDTO> createUser(CreateSignUpRequest newUser){

        

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

                return APIResponse.successCreate(userResponse);
        }
        else{

            return APIResponse.failure("Failed to create user" );
            // SignUpUserResponseDTO userResponse =  new SignUpUserResponseDTO(
            //             LocalDateTime.now(),
            //             "Failed to create"
            //     );
       

      
        }
    }


    

    @Transactional
    public APIResponse<LogInResponseDTO> signInVerification(SignInRequestDTO signInInfo){

        

        User user = userRepository.findByEmailNative(signInInfo.emailAddress());
        
        if(user==null){
              throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
          "Invalid Credentials"
            );
        }

        if (!passwordEncoder.matches(signInInfo.password(), user.getPassword())) 
        {
                throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
          "Invalid Credentials"
            );
        }
        else{
            String accessToken = jwtUtil.generateAccessToken(user.getEmailAddress());
            LogInResponseDTO response = new LogInResponseDTO(user.getEmailAddress(), accessToken, LocalDateTime.now());

            return APIResponse.success(response);

        }

        //provide back jwt token 

        
        
    }

    
    public String genRefreshToken(SignInRequestDTO signInInfo){

        return jwtUtil.generateRefreshToken(signInInfo.emailAddress());
    }


    @Transactional
    public APIResponse<ResetPasswordResponseDTO> resetPassword(ResetPasswordDTO request){

        try{
        User existingUser = userRepository.findByEmailNative(request.emailAddress());

        if(existingUser!=null){
            String hashedPassword = passwordEncoder.encode(request.newPassword());

            existingUser.setPassword(hashedPassword);

            userRepository.save(existingUser);

            ResetPasswordResponseDTO responseDTO = new ResetPasswordResponseDTO(existingUser.getEmailAddress(), LocalDateTime.now());

            return APIResponse.success(responseDTO);
        }
        else{

            return APIResponse.failure("Failed to reset password" );

        }
        }
        catch(Exception e){
            return APIResponse.failure("Failed to reset password"+ e.getMessage());
        }
        
       
    }

    @Transactional
    public APIResponse<LogInResponseDTO> refreshToken(HttpServletRequest req)
    {
        // Get all cookies from the request
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                String refreshToken = cookie.getValue();

                // Validate and issue new access token
                if (jwtUtil.isTokenValid(refreshToken)) {
                    String emailAddress = jwtUtil.extractUsername(refreshToken);
                    String newAccessToken = jwtUtil.generateAccessToken(emailAddress);

                    LogInResponseDTO responseDTO = new LogInResponseDTO(emailAddress, newAccessToken, LocalDateTime.now());

                    return APIResponse.success(responseDTO);

                   
                } else {

                    return APIResponse.failure("failed to refresh token");
                    
                }
            }
        }
    }

    return APIResponse.failure("failed to refresh token");

       
    }

    @Transactional
    public APIResponse<UserIdResponseDTO> fetchUserId(String email)
    {
        try{


        User existingUser = userRepository.findByEmailNative(email);

        if(existingUser==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid email address");
        }
        

        UserIdResponseDTO responseDTO = new UserIdResponseDTO(existingUser.getId());

       

        return APIResponse.success(responseDTO);
        }
        catch(Exception e){
            return APIResponse.failure(
                "failed to get user id");
        }

    }


    @Transactional
    public APIResponse<List<UsersEmailResponseDTO>> searchUsersEmail(String email){

        try{

            List<User> existingUsers = userRepository.searchUserByEmail(email+"%")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"users not found"));

            List<UsersEmailResponseDTO> responseDTO = existingUsers.stream().map(user -> new UsersEmailResponseDTO(user.getEmailAddress(),user.getId())) .toList();


            return APIResponse.success(responseDTO);
        }catch(Exception e){
            return APIResponse.failure(
                "failed to search users");
        }
    }
   
}
