package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.entity.UserVO;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 验证参数不开启快速失败
 * <p>
 * {@link com.example.demo.config.ValidatorConfiguration#validator()} 关闭快速失败
 */

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    UserVO user;

    List<UserVO> users;

    @BeforeEach
    public void setUp() throws IOException {
        String root = System.getProperty("user.dir");
        String fileName = "user-data.json";
        List<String> dirs = List.of(root, "src", "test", "resources", fileName);
        String filePath = StringUtils.join(dirs, File.separator);
        String data = Files.readString(Paths.get(filePath));
        users = JSONArray.parseArray(data, UserVO.class);
        user = users.get(0);
    }

    /**
     * 正常请求
     */
    @Test
    void testGetUserById() throws Exception {
        String responseResult = mockMvc.perform(get("/user/getUserById")
                .param("userId", "userId")
                .param("userName", "userName")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertEquals("success", msg);
    }

    /**
     * 单个参数验证
     */
    @Test
    void testGetUserById_except() throws Exception {
        String responseResult = mockMvc.perform(
                get("/user/getUserById")
                    .param("userId", "")
                    .param("userName", "")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertTrue(msg.contains("用户ID不能为空"));
        Assertions.assertTrue(msg.contains("用户名称不能为空"));
    }

    /**
     * 对象参数验证
     */
    @Test
    void testSaveSimpleUser_except() throws Exception {
        String responseResult = mockMvc.perform(
                post("/user/saveSimpleUser")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertTrue(msg.contains("用户姓名不能为空"));
        Assertions.assertTrue(msg.contains("性别不能为空"));
    }

    /**
     * 对象参数分组验证
     */
    @Test
    void testSaveUser_except() throws Exception {
        String responseResult = mockMvc.perform(
                post("/user/saveUser")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertFalse(msg.contains("用户ID不能为空"));
        Assertions.assertTrue(msg.contains("用户姓名不能为空"));
        Assertions.assertTrue(msg.contains("性别不能为空"));
        Assertions.assertTrue(msg.contains("身份证号不能为空"));
    }

    /**
     * 嵌套（级联）参数分组校验
     */
    @Test
    void testSaveUser2_except() throws Exception {
        String responseResult = mockMvc.perform(
                post("/user/saveUser")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSONString(user)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertFalse(msg.contains("用户ID不能为空"));
        Assertions.assertTrue(msg.contains("账号不能为空"));
        Assertions.assertTrue(msg.contains("爱好不能为空"));
        Assertions.assertTrue(msg.contains("必须以Abc开头"));
    }

    /**
     * 集合参数校验
     */
    @Test
    void testSaveUserList_except() throws Exception {
        String responseResult = mockMvc.perform(
                post("/user/saveUserList")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.toJSONString(users)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Map<String, String> map = JSONObject.parseObject(msg, Map.class);
        Assertions.assertFalse(msg.contains("用户ID不能为空"));
        Assertions.assertEquals("账号不能为空", map.get("list[0].accounts[0].account"));
        Assertions.assertEquals("爱好不能为空", map.get("list[0].userDetail.hobby"));
        Assertions.assertEquals("账号不能为空", map.get("list[1].accounts[0].account"));
        Assertions.assertEquals("爱好不能为空", map.get("list[1].userDetail.hobby"));
    }

    /**
     * 自定义参数校验
     */
    @Test
    void testSaveUser3_except() throws Exception {
        String responseResult = mockMvc.perform(
                post("/user/saveUser")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"prefix\": \"\" }"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        String msg = String.valueOf(JSONObject.parseObject(responseResult).get("msg"));
        Assertions.assertFalse(msg.contains("用户ID不能为空"));
        Assertions.assertTrue(msg.contains("必须以Abc开头"));
    }
}
