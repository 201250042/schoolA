package com.example.a.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("demo.dbo.COURSE")
@XStreamAlias("课程")
public class Course {
    @TableId(value = "课程编号")
    @XStreamAlias("课程编号")
    private String cno;
    @TableField(value = "课程名称")
    @XStreamAlias("课程名称")
    private String cName;
    @TableField(value = "学分")
    @XStreamAlias("学分")
    private String points;

    @TableField(value = "授课老师")
    @XStreamAlias("授课老师")
    private String teacher;
    @TableField(value = "授课地点")
    @XStreamAlias("授课地点")
    private String place;
    @TableField(value = "共享")
    @XStreamAlias("共享")
    private String shared;
}
