package com.ptu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("symptom")
@Data
public class symptom {
    private String code;
    private String name;
}
