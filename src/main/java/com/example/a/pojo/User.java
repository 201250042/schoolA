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
@TableName("demo.dbo.ACC")
@XStreamAlias("账户")
public class User {
    @TableId(value = "账户名")
    @XStreamAlias("账户名")
    private String name;

    @TableField(value = "密码")
    @XStreamAlias("密码")
    private String password;

    @TableField(value = "权限")
    @XStreamAlias("权限")
    private String auth;
}
