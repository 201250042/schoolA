package com.example.a.controller;

import com.example.a.mapper.StudentMapper;
import com.example.a.pojo.Student;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/students")
    public String getAllStudents() {
        xStream.processAnnotations(Student.class);
        List<Student> students = studentMapper.selectList(null);
        System.out.println(students.size());
        return xStream.toXML(students);
    }

    @GetMapping("/students/get_students_count")
    public String getStudentCnt() {
        xStream.processAnnotations(Student.class);
        return xStream.toXML(studentMapper.selectList(null).size());
    }

    @GetMapping("/students/add")
    public void addStudent(@RequestParam String studentXml) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXml);
        if (studentMapper.selectById(student.getSno()) != null) {
            return;
        }
        studentMapper.insert(student);
    }

    @GetMapping("/students/delete")
    public void deleteStudent(@RequestParam String studentXml) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXml);
        studentMapper.deleteById(student.getName());
    }

    @GetMapping("/students/update")
    public void updateStudent(@RequestParam String studentXml) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXml);
        studentMapper.updateById(student);
    }

}
