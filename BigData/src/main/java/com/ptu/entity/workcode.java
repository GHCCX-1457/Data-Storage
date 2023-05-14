package com.ptu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("workcode")
@Data
public class workcode {
    private String code;
    private String name;
}
