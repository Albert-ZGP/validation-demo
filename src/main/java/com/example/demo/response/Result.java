package com.example.demo.response;

import lombok.Getter;

@Getter
public class Result<T> {

    private static final String SUCCESS = "success";

    private String msg;

    private T data;

    private Result() {
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.msg = SUCCESS;
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.msg = SUCCESS;
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail(String errorMsg) {
        Result<T> result = new Result<>();
        result.msg = errorMsg;
        return result;
    }
}
