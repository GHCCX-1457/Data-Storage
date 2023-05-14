package com.ptu.util;

import com.ptu.entity.symptom;
import com.ptu.service.symptomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class UtilTests {

    @Autowired
    private symptomService recordService;

    @Autowired
    private

    @Test
    void mysqlTest() {
        List<symptom> symptomList = recordService.list();
        System.out.println(symptomList);
    }
    @Test
    void hbaseTest() throws IOException {
        HBaseManger.getRowData("order_items","10001");
    }


}