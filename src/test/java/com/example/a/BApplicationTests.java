package com.example.a;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.a.controller.EnrollController;
import com.example.a.mapper.CourseMapper;
import com.example.a.mapper.EnrollMapper;
import com.example.a.pojo.Enroll;
import com.example.a.pojo.User;
import com.example.a.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
class AApplicationTests {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private EnrollMapper enrollMapper;
    @Autowired
    private EnrollController enrollController;
    @Test
    void contextLoads() {
    }

    @Test
    void testDatabase() {
        registerService.register(new User("cc", "ll", "22"));
    }

    @Test
    void testDatabase1() {
        System.out.println(courseMapper.selectById("11"));
    }

    @Test
    void testDatabase3() {
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学生编号", "11");
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        System.out.println(enrollList);
    }
    @Test
    void testLLL() {
        System.out.println(enrollController.getMostPopular());
    }
}
