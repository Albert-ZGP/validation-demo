package com.example.demo.config;

import com.alibaba.fastjson2.JSON;
import com.example.demo.response.Result;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 最高优先级，防止有其他的全局异常捕获导致这个无法捕获
public class ExceptionAdvice {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<String> handleValidException(MethodArgumentNotValidException m) {
        // Map<String, String> errorMsg = new LinkedHashMap<>();
        // for (FieldError fieldError : m.getBindingResult().getFieldErrors()) {
        //     errorMsg.put(fieldError.getField(), fieldError.getDefaultMessage());
        // }
        Map<String, String> errorMsg = m.getBindingResult().getFieldErrors().stream().collect(
            Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (k1, k2) -> k1));
        return Result.fail(JSON.toJSONString(errorMsg));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result<String> handleValidException2(ConstraintViolationException m) {
        return Result.fail(m.getMessage());
    }
}
