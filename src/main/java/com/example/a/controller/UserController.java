package com.example.a.controller;

import com.example.a.mapper.UserMapper;
import com.example.a.pojo.User;
import com.example.a.service.LoginService;
import com.example.a.service.RegisterService;
import com.example.a.utils.TokenUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
public class UserController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginService loginService;
    @Autowired
    RegisterService registerService;

    @GetMapping("/register")
    public String register(@RequestParam String userAccountXml) {
        xStream.processAnnotations(User.class);
        User user = (User) xStream.fromXML(userAccountXml);
        Map<String, String> res = registerService.register(user);
        return xStream.toXML(res);
    }
    @GetMapping("/login")
    public String login(@RequestParam String userAccountXml) {
        // 解析XML到User实例
        xStream.processAnnotations(User.class);
        User user = (User) xStream.fromXML(userAccountXml);
        // 接收结果
        Map<String, String> res = loginService.login(user);
        // 将结果转换为XML返回
        return xStream.toXML(res);
    }
    @GetMapping("/checkToken")
    public String checkToken(@RequestParam Map<String, String> data) {
        String name = data.get("name");
        String token = data.get("token");
        if (name.equals(TokenUtil.parseToken(token))) {
            return "1"; // valid
        } else {
            return "0";
        }
    }


}
