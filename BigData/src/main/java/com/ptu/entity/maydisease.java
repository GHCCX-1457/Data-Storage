package com.ptu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("maydisease")
@Data
public class maydisease {
    private String code;
    private String name;
}
