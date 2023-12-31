package com.example.a.service;

import com.example.a.pojo.User;

import java.util.Map;

public interface LoginService {
    Map<String, String> login(User user);
}
