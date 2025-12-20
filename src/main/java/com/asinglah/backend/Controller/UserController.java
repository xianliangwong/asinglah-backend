package com.asinglah.backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.CreateSignUpRequest;
import com.asinglah.backend.DTO.UserRequestDTO.ResetPasswordDTO;
import com.asinglah.backend.DTO.UserRequestDTO.SignInRequestDTO;
import com.asinglah.backend.DTO.UserResponseDTO.LogInResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.ResetPasswordResponseDTO;
import com.asinglah.backend.DTO.UserResponseDTO.SignUpUserResponseDTO;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){


        this.userService=userService;

    }

    //change parameter 
    @PostMapping("/api/users/signup")
    public ResponseEntity<APIResponse<SignUpUserResponseDTO>> userSignUp(@Valid @RequestBody CreateSignUpRequest request){
        
        APIResponse<SignUpUserResponseDTO> userResponse = userService.createUser(request);
       
        return ResponseEntity.status(userResponse.getStatus()).body(userResponse);
     

        
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<APIResponse<LogInResponseDTO>> userSignIn(@Valid @RequestBody SignInRequestDTO request, HttpServletResponse httpResponse) {
        
        APIResponse<LogInResponseDTO>  responseDTO= userService.signInVerification(request);

        if(responseDTO.getStatus()==200)
        {
            String refreshToken = userService.genRefreshToken(request);

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // use HTTPS
            cookie.setAttribute("SameSite", "Strict");
            cookie.setPath("/");    // available for all endpoints
            cookie.setMaxAge(1 * 24 * 60 * 60); // 1 days, or don set the maxage so that it becomes session cookies
            //else in log out endpoint, will need to clear the cookie
            httpResponse.addCookie(cookie);

        }

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<APIResponse<String>> userSignOut(HttpServletResponse httpResponse) {
        
        APIResponse<String> responseDTO;
       
        try{
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // still enforce HTTPS
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");    // must match the original path
        cookie.setMaxAge(0);    // delete immediately

        httpResponse.addCookie(cookie);

        responseDTO = APIResponse.success("log out success");
        }
        catch(Exception e){

            responseDTO = APIResponse.failure("log out fail: "+e.getMessage());
        }

       

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/api/users/resetPassword")
    public ResponseEntity<APIResponse<ResetPasswordResponseDTO>> userResetPassword(@Valid @RequestBody ResetPasswordDTO requestDTO) {
       
        APIResponse<ResetPasswordResponseDTO> responseDTO = userService.resetPassword(requestDTO);

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/api/users/refreshAccessToken")
    public ResponseEntity<APIResponse<LogInResponseDTO>> userRefreshAccessToken(HttpServletRequest request) {
        
        APIResponse<LogInResponseDTO> responseDTO =userService.refreshToken(request);
        
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    
    
    

}
