package com.example.a.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装一个Response对象，返回给前端
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("Resp")
public class Resp {
    @XStreamAlias("msg")
    private String msg;
}
