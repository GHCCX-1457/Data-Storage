package com.ptu.service;

import com.ptu.entity.HbaseInfo;

import java.io.IOException;
import java.util.List;

public interface HBaseService {
    List<HbaseInfo> getPageHbaseInfoList(int pageIndex,int pageSize,String tableName) throws Exception;

    List<HbaseInfo> getAllHbaseData(String tableName) throws IOException;
}
