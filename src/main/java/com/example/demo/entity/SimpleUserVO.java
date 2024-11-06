package com.example.demo.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SimpleUserVO {

    @NotEmpty(message = "用户姓名不能为空")
    private String userName;

    @NotNull(message = "性别不能为空")
    private Integer gender;
}
