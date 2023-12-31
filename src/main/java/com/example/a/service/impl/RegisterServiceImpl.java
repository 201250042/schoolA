package com.example.a.service.impl;

import com.example.a.mapper.UserMapper;
import com.example.a.pojo.User;
import com.example.a.service.RegisterService;
import com.example.a.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> register(User user) {
        String name = user.getName();
        String password = user.getPassword();
        String auth = user.getAuth();
        // check if the name is used
        User tmp = userMapper.selectById(user.getName());
        Map<String, String> res = new HashMap<>();
        if (tmp != null) {
            res.put("msg", "用户名已被占用");
            return res;
        }
        // check the password length
        if (password.length() > 6) {
            res.put("msg", "密码长度不能大于6");
            return res;
        }
        name = name.trim();
        // password = encoder.encode(password);
        userMapper.insert(new User(name, password, auth));
        res.put("msg", "注册成功");
        //String token = TokenUtil.getToken(name);
        //res.put("token", token);
        res.put("account", name);
        return res;
    }
}
