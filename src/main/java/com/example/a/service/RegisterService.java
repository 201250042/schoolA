package com.example.a.service;

import com.example.a.pojo.User;

import java.util.Map;

public interface RegisterService {
    Map<String, String> register(User user);
}
