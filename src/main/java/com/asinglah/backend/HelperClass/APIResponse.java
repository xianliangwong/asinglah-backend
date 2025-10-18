package com.asinglah.backend.HelperClass;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class APIResponse<T> {

    private int status; 
    private String message; 
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

    public static<T> APIResponse<T> failure(String message)
    {
        return new APIResponse<>(400,message,null);

    }

    public static<T> APIResponse<T> unauthorized(String message)
    {
        return new APIResponse<>(401,message,null);

    }
}
