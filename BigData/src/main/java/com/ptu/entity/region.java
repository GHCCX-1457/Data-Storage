package com.ptu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("region")
@Data
public class region {
    private String code;
    private String name;
}
