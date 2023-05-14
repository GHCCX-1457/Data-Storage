package com.ptu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("hazardfactor")
@Data
public class hazardfactor {
    private String code;
    private String name;

}
