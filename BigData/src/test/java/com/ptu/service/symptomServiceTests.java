package com.ptu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ptu.entity.HbaseInfo;
import com.ptu.entity.symptom;
import com.ptu.mapper.symptomMapper;
import com.ptu.util.HBaseManger;
import com.ptu.util.HBasePrintUtil;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class symptomServiceTests {

    @Autowired
    private symptomService symptomService;

    @Autowired
    private MysqlToHbaseService mysqlToHbaseService;

    @Autowired
    private HBaseService HBaseService;


    @Test
    void test() throws Exception {
//        mysqlToHbase.importAll("symptom");

//        HBaseManger.addData("symptom","777","detail","name","测试");
//        HBaseManger.getRowData("symptom","27");
//        HBaseManger.listTables();
//        HBaseManger.listTables();
//        List<HbaseInfo> symptom = HBaseService.getPageHbaseInfoList(1, 10, "symptom");
//        System.out.println(symptom);
//        for (symptom symptom : symptomService.list()) {
//            System.out.println(symptom);
//        }

//        LambdaQueryWrapper<symptom> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(symptom::getCode, "神经系统");
//        if(symptomService.list(wrapper).size()==0){
//            wrapper.clear();
//            wrapper.eq(symptom::getName,"神经系统");
//        }
//        System.out.println(symptomService.list(wrapper));
//        System.out.println(HBaseService.getAllHbaseData("symptom"));
        ResultScanner symptom = HBaseManger.getPageData(
                1, 5, "symptom");
        HBasePrintUtil.printResultScanner(symptom);


    }

}