package com.example.a.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.a.mapper.CourseMapper;
import com.example.a.mapper.EnrollMapper;
import com.example.a.mapper.StudentMapper;
import com.example.a.pojo.Course;
import com.example.a.pojo.Enroll;
import com.example.a.pojo.Resp;
import com.example.a.pojo.Student;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EnrollController {
    XStream xStream = new XStream(new StaxDriver());
//    private String ip = "192.168.43.195"; // ip of central server
    private String ip = "localhost"; // ip of central server
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EnrollMapper enrollMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/courses_selection")
    public String getAllCoursesSelectionTable(){
        xStream.processAnnotations(Enroll.class);
        return xStream.toXML(enrollMapper.selectList(null)); // 返回全部课程
    }

    @GetMapping("/courses_selection/add")
    public void addCoursesSelectionTable(@RequestParam String courses_selectionXml){
        System.out.println("frontend return: " + courses_selectionXml);
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        System.out.println(enroll);
        // 判断课程号是否以1开头，如果以1开头，表示是本院系的课，直接添加一条选课记录
        String cno = enroll.getCno();
        if (cno.charAt(0) == '1') {
            // 判断下是否已经有选课记录
            QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("课程编号", cno);
            queryWrapper.eq("学生编号", enroll.getSno());
            Enroll tmp = enrollMapper.selectOne(queryWrapper);
            if (tmp != null) {
                return;
            }
            enrollMapper.insert(enroll);
        } else {
            // 不是以1开头，向集成服务器发送选课请求
            String url = "http://" + ip + ":8081/integration/httpTest/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
            Student student = studentMapper.selectById(enroll.getSno());
            xStream.processAnnotations(Student.class);
            String studentXML = xStream.toXML(student);
            String from = "a";
            String to;
            if (cno.charAt(0) == '2') {
                // 请求共享B的课
                to = "b";
            } else {
                to = "c";
            }
            String resp = restTemplate.getForObject(url, String.class, studentXML, courses_selectionXml, from, to);
        }
    }
    @GetMapping("/courses_selection/handle_share_request")
    public String handleShareRequest(@RequestParam String course_selectionXml, @RequestParam String studentXml) {
        // 检查目标课程是否支持共享
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(course_selectionXml);
        Course course = courseMapper.selectById(enroll.getCno());
        if (course.getShared().equals("0")) {
            // 不支持共享
            Resp resp = new Resp("课程不支持共享");
            xStream.processAnnotations(Resp.class);
            return xStream.toXML(resp);
        } else {
            // 可以共享，将学生和对应选课记录插入本地数据库
            xStream.processAnnotations(Student.class);
            Student student = (Student) xStream.fromXML(studentXml);
            studentMapper.insert(student);
            enrollMapper.insert(enroll);
            xStream.processAnnotations(Resp.class);
            Resp resp = new Resp("选课成功");
            return xStream.toXML(resp);
        }
    }

    @GetMapping("/courses_selection/delete")
    public void deleteCoursesSelectionTable(@RequestParam String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        // 校验课程号
        String cno = enroll.getCno();
        if (cno.charAt(0) == '1') {
            // 本院系的课，直接删除
            QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("课程编号", enroll.getCno());
            queryWrapper.eq("学生编号", enroll.getSno());
            enrollMapper.delete(queryWrapper);
            // 校验一下学号，如果不是本学院的学生，且该学生在本学院没有选课了，删除该学生
            String sno = enroll.getSno();
            if (sno.charAt(0) != '1') {
                // 不是本学院的学生
                // 查一下该学生在本学院是否还有选课记录
                QueryWrapper<Enroll> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("学生编号", sno);
                List<Enroll> enrollList = enrollMapper.selectList(queryWrapper1);
                if (enrollList.size() == 0) {
                    studentMapper.deleteById(sno);
                }
            }
        } else {
            String url = "http://" + ip + ":8081/integration/httpTestDelete/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
            String from = "a";
            String to;
            if (cno.charAt(0) == '2') {
                to = "b";
            } else {
                to = "c";
            }
            Student student = studentMapper.selectById(enroll.getSno());
            xStream.processAnnotations(Student.class);
            String studentXML = xStream.toXML(student);
            String resp = restTemplate.getForObject(url, String.class, studentXML, courses_selectionXml, from, to);
        }
    }

    @GetMapping("/courses_selection/delete_share_course")
    public void deleteSharedCoursesSelection(@RequestParam String courses_selectionXml) {
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        // 获取学号
        String sno = enroll.getCno();
        // 先删除掉当前的选课信息
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学生编号", sno);
        queryWrapper.eq("课程编号", enroll.getCno());
        enrollMapper.delete(queryWrapper);
        // 检查当前跨选生在本院系是否还有选课记录，如没有，删除该学生
        QueryWrapper<Enroll> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("学生编号", sno);
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper0);
        if (enrollList.size() == 0) {
            studentMapper.deleteById(sno);
        }
    }

    @GetMapping("/courses_selection/update")
    public void updateCoursesSelectionTable(@RequestParam String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        enrollMapper.updateById(enroll);
    }

    @GetMapping("/courses_selection/searchBySno")
    public String findEnrollBySno(@RequestParam String courses_selectionXml) {
        xStream.processAnnotations(Enroll.class);
        Enroll enroll0 = (Enroll) xStream.fromXML(courses_selectionXml);
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学生编号", enroll0.getSno());
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        System.out.println(enrollList);
        System.out.println(xStream.toXML(enrollList));
        return xStream.toXML(enrollList);
    }

    @GetMapping("/courses_selection/getStudentDistribution")
    public List<Integer> getStudentDistribution(@RequestParam String cno) {
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.eq("课程编号", cno);
        List<Enroll> enrollList = enrollMapper.selectList(enrollQueryWrapper);
        int[] cnts = new int[3];
        for (Enroll enroll: enrollList) {
            String sno = enroll.getSno();
            int index = sno.charAt(0) - '1';
            cnts[index]++;
        }
        List<Integer> ans = new ArrayList<>();
        for (int cnt : cnts) {
            ans.add(cnt);
        }
        return ans;
    }

    @GetMapping("/courses_selection/getMostPopularCourse")
    public List<String> getMostPopular() {
        List<String> ans = new ArrayList<>();
        List<Enroll> enrollList = enrollMapper.selectList(null); // select all
        HashMap<String, String> no2Name = new HashMap<>(); // cno -> cname
        HashMap<String, Integer> no2Cnt = new HashMap<>(); // cno -> cnt
        List<Course> courseList = courseMapper.selectList(null);
        for (Course course: courseList) {
            String cno = course.getCno();
            String cname = course.getCName();
            if (!no2Name.containsKey(cno)) {
                no2Name.put(cno, cname);
            }
        }
        for (Enroll enroll: enrollList) {
            String cno = enroll.getCno();
            no2Cnt.put(cno, no2Cnt.getOrDefault(cno, 0) + 1);
        }
        for (String cno: no2Cnt.keySet()) {
            String cname = no2Name.get(cno);
            int cnt = no2Cnt.get(cno);
            ans.add(cname + ":" + cnt);
        }
        return ans;
    }

    @GetMapping("/courses_selection/getGradeDistribution")
    public List<Integer> grades(@RequestParam String cno) {
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("课程编号", cno);
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        int[] cnts = new int[3];
        for (Enroll enroll: enrollList) {
            int grade = Integer.parseInt(enroll.getGrade());
            if (grade < 60) {
                cnts[0]++;
            } else if (grade < 90) {
                cnts[1]++;
            } else {
                cnts[2]++;
            }
        }
        List<Integer> ans = new ArrayList<>();
        for (int cnt: cnts) {
            ans.add(cnt);
        }
        return ans;
    }
}
