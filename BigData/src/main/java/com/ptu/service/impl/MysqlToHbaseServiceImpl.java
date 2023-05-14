package com.ptu.service.impl;

import com.ptu.entity.symptom;
import com.ptu.service.MysqlToHbaseService;
import com.ptu.service.symptomService;
import com.ptu.util.HBaseManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MysqlToHbaseServiceImpl implements MysqlToHbaseService {

    @Autowired
    private symptomService recordService;

    @Override
    public boolean importAll(String HbaseTableName){
        try{
            List<symptom> symptomList = recordService.list();
            for (int i = 0; i < symptomList.size(); i++) {
                String code = symptomList.get(i).getCode();
                String name = symptomList.get(i).getName();
                if(!HBaseManger.isTableExist(HbaseTableName)){
                    HBaseManger.createTable(HbaseTableName,"detail");
                }
                HBaseManger.addData(HbaseTableName,(i+1)+"",
                        "detail","code",code);
                HBaseManger.addData(HbaseTableName,(i+1)+"",
                        "detail","name",name);
        }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("操作失败");
            return false;
        }
        System.out.println("操作成功");
        return true;
    }
}
