package com.example.a.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("demo.dbo.ENROLL")
@XStreamAlias("选课")
public class Enroll {
    @TableField(value = "学生编号")
    @XStreamAlias("学号")
    private String sno;
    @TableField(value = "课程编号")
    @XStreamAlias("课程编号")
    private String cno;
    @TableField(value = "成绩")
    @XStreamAlias("成绩")
    private String grade;
}
