package com.ptu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ptu.entity.hazardfactor;
import com.ptu.mapper.hazardfactorMapper;
import com.ptu.service.hazardfactorService;
import org.springframework.stereotype.Service;

@Service
public class hazardfactorServiceImpl extends
        ServiceImpl<hazardfactorMapper, hazardfactor>
        implements hazardfactorService {
}
