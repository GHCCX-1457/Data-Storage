package com.ptu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ptu.entity.region;
import com.ptu.mapper.regionMapper;
import com.ptu.service.regionService;
import org.springframework.stereotype.Service;


@Service
public class regionServiceImpl extends ServiceImpl<regionMapper, region>
        implements regionService {

}
