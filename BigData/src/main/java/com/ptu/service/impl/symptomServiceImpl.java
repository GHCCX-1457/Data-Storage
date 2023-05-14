package com.ptu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ptu.mapper.symptomMapper;
import com.ptu.entity.symptom;
import com.ptu.service.symptomService;
import org.springframework.stereotype.Service;


@Service
public class symptomServiceImpl extends ServiceImpl<symptomMapper, symptom>
        implements symptomService {

}
