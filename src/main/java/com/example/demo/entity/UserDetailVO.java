package com.example.demo.entity;

import com.example.demo.validation.ValidGroup;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDetailVO {
    @NotEmpty(message = "爱好不能为空", groups = {ValidGroup.Create.class})
    private String hobby;

    @NotEmpty(message = "头像不能为空", groups = {ValidGroup.Create.class})
    private String headPortraitUrl;
}