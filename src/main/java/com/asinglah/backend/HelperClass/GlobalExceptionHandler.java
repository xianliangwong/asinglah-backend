// package com.asinglah.backend.HelperClass;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;


// import jakarta.servlet.http.HttpServletRequest;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(RuntimeException.class)
//     public ResponseEntity<APIResponse<Object>> handelRunTimeException(RuntimeException ex, HttpServletRequest request)
//     {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.failure(ex.getMessage()));


//     }

//     @ExceptionHandler(SecurityException.class) 
//     public ResponseEntity<APIResponse<Object>> handleUnauthorized(SecurityException ex, HttpServletRequest request) 
//     { 
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.unauthorized("Not authorized: " + ex.getMessage())); 

//     } 

// }
