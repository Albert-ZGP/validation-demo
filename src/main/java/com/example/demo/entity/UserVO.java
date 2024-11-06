package com.example.demo.entity;

import com.example.demo.validation.PrefixByAbc;
import com.example.demo.validation.ValidGroup;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserVO {
    @NotEmpty(message = "用户ID不能为空", groups = {ValidGroup.Update.class})
    private String userId;

    @NotEmpty(message = "用户姓名不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private String userName;

    @NotNull(message = "性别不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private Integer gender;

    @NotEmpty(message = "身份证号不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "身份证号格式错误")
    private String idCard;

    @NotEmpty(message = "电话号码不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
        message = "电话号码格式错误")
    private String mobilePhone;

    @AssertTrue(message = "当前用户是无效用户", groups = {ValidGroup.Update.class})
    private Boolean valid;

    @AssertFalse(message = "当前用户已被删除", groups = {ValidGroup.Update.class})
    private Boolean deleted;

    @Min(message = "年龄必须大于0", value = 0, groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    @Max(message = "年龄不能超过150", value = 150, groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    private Integer age;

    @NotNull(message = "用户详情不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    @Valid
    private UserDetailVO userDetail;

    @NotNull(message = "账号信息不能为空", groups = {ValidGroup.Update.class, ValidGroup.Create.class})
    @Size(max = 2, message = "每个用户最多2个账号")
    @Valid
    private List<AccountVO> accounts;

    @PrefixByAbc
    private String prefix;
}
