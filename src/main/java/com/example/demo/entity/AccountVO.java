package com.example.demo.entity;

import com.example.demo.validation.ValidGroup;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccountVO {
    @NotEmpty(message = "账号不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private String account;

    @NotEmpty(message = "密码不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private String password;

    @NotEmpty(message = "用户ID不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private String userId;
}