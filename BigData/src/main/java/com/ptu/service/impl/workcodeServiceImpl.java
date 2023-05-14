package com.ptu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ptu.entity.workcode;
import com.ptu.mapper.workcodeMapper;
import com.ptu.service.workcodeService;
import org.springframework.stereotype.Service;


@Service
public class workcodeServiceImpl extends ServiceImpl<workcodeMapper, workcode>
        implements workcodeService {

}
