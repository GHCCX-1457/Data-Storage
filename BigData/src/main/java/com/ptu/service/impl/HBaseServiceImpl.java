package com.ptu.service.impl;

import com.ptu.entity.HbaseInfo;
import com.ptu.service.HBaseService;
import com.ptu.util.HBaseManger;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class HBaseServiceImpl implements HBaseService {
    @Override
    public List<HbaseInfo> getPageHbaseInfoList(int pageIndex, int pageSize, String tableName) throws Exception {
        ResultScanner pageData = HBaseManger.getPageData(pageIndex, pageSize, tableName);
        List<HbaseInfo> hbaseInfoList=new ArrayList<>();
        for (Result result: pageData) {
            for (Cell cell : result.listCells()) {
                String rowKey = Bytes.toString(cell.getRow());
                String columnFamily=Bytes.toString(cell.getFamily());
                String column=Bytes.toString(cell.getQualifier());
                String value=Bytes.toString(cell.getValue());
                String time= String.valueOf(cell.getTimestamp());
                hbaseInfoList.add(new HbaseInfo(rowKey,columnFamily,column,value,time));
            }

        }
        return hbaseInfoList;
    }

    @Override
    public List<HbaseInfo> getAllHbaseData(String tableName) throws IOException {
        return HBaseManger.getAllData(tableName);
    }
}
