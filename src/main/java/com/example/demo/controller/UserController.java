package com.example.demo.controller;

import com.example.demo.entity.SimpleUserVO;
import com.example.demo.entity.UserVO;
import com.example.demo.response.Result;
import com.example.demo.validation.ValidGroup;
import com.example.demo.validation.ValidateList;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("user")
@Validated()
public class UserController {

    /**
     * 单个参数验证
     */
    @GetMapping("getUserById")
    public Result<String> getUserById(@NotBlank(message = "用户ID不能为空") String userId,
        @NotBlank(message = "用户名称不能为空") String userName) {

        return Result.success();
    }

    /**
     * 对象参数验证
     */
    @PostMapping(value = "saveSimpleUser")
    public Result<String> saveSimpleUser(@RequestBody @Validated SimpleUserVO user) {
        return Result.success();
    }

    /**
     * 对象参数分组验证
     * <p>
     * 嵌套（级联）参数分组校验
     */
    @PostMapping(value = "saveUser")
    public Result<String> saveUser(@RequestBody @Validated(ValidGroup.Create.class) UserVO user) {
        return Result.success();
    }

    /**
     * 集合参数校验
     */
    @PostMapping(value = "saveUserList")
    public Result<String> saveUserList(
        @RequestBody @Validated(ValidGroup.Create.class) ValidateList<UserVO> userVOList) {
        return Result.success();
    }
}
