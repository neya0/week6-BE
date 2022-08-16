package com.example.studyblog.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(T date) {
        return new ResponseDto<>(true, date , null);
    }

    @Getter
    @AllArgsConstructor
    static class Error{
        private String code;
        private String message;
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false,null, new Error(code, message));
    }
}
