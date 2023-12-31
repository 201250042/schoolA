package com.example.a.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("demo.dbo.STUDENT")
@XStreamAlias("学生")
public class Student {
    @TableId(value = "学号")
    @XStreamAlias("学号")
    private String sno;
    @TableField(value = "姓名")
    @XStreamAlias("姓名")
    private String name;
    @TableField(value = "性别")
    @XStreamAlias("性别")
    private String sex;
    @TableField(value = "院系")
    @XStreamAlias("院系")
    private String dept;
    @TableField(value = "关联账户")
    @XStreamAlias("关联账户")
    private String account; // 学生对应的账户名，外键


}
