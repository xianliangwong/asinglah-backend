package com.asinglah.backend.HelperClass;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Standard API response wrapper")
public class APIResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int status; 
    @Schema(description = "Response message", example = "Success")
    private String message; 
    @Schema(description = "Payload data")
    private T data;


    public APIResponse(int status, String message, T data) 
    { 
        this.status = status; 
        this.message = message; 
        this.data = data; 
    }

    public static<T> APIResponse<T> success(T data)
    {
        return new APIResponse<>(200,"Success",data);

    }

    public static<T> APIResponse<T> successCreate(T data)
    {
        return new APIResponse<>(201,"Success Created",data);

    }

    public static<T> APIResponse<T> failure(String message)
    {
        return new APIResponse<>(400,message,null);

    }

    public static<T> APIResponse<T> unauthorized(String message)
    {
        return new APIResponse<>(401,message,null);

    }
}
