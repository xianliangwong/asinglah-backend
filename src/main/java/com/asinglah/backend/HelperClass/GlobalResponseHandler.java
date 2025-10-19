// package com.asinglah.backend.HelperClass;

// import org.springframework.core.MethodParameter;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;



// @ControllerAdvice
// public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

//     @Override
//     public boolean supports(MethodParameter returnType,
//                             Class<? extends HttpMessageConverter<?>> converterType) {
//         return true; // apply to all responses
//     }

    

//     @Override
//     public Object beforeBodyWrite(Object body, MethodParameter returnType, org.springframework.http.MediaType selectedContentType,
//             Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        
//         String path = request.getURI().getPath();

//         //  Exclude Springdoc/Swagger endpoints
//         if (path.startsWith("/v3/api-docs") ||
//             path.startsWith("/swagger-ui") ||
//             path.startsWith("/swagger-resources")) {
//             return body;
//         }

//         if (body instanceof APIResponse) {
//             return body; // already wrapped
//         }

//         // Default success wrapper
//         return APIResponse.success(body);
//     }

   
   
// }
