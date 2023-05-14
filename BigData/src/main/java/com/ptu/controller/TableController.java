package com.ptu.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ptu.entity.*;
import com.ptu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private HBaseService HbaseInfoService;

    @Autowired
    private symptomService symptomService;

    @Autowired
    private hazardfactorService hazardfactorService;

    @Autowired
    private maydiseaseService maydiseaseService;

    @Autowired
    private regionService regionService;

    @Autowired
    private workcodeService workcodeService;

    @GetMapping("/search")
    public String search(String searchContent,String tableName){

        switch (tableName){
            case "symptom":{
                LambdaQueryWrapper<symptom> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(symptom::getCode, searchContent);
                if(symptomService.list(wrapper).size()==0){
                    wrapper.clear();
                    wrapper.eq(symptom::getName,searchContent);
                }
                return JSON.toJSONString(symptomService.list(wrapper));
            }
            case "hazardfactor":{
                LambdaQueryWrapper<hazardfactor> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(hazardfactor::getCode, searchContent);
                if(hazardfactorService.list(wrapper).size()==0){
                    wrapper.clear();
                    wrapper.eq(hazardfactor::getName,searchContent);
                }
                return JSON.toJSONString(hazardfactorService.list(wrapper));
            }
            case "maydisease":{
                LambdaQueryWrapper<maydisease> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(maydisease::getCode, searchContent);
                if(maydiseaseService.list(wrapper).size()==0){
                    wrapper.clear();
                    wrapper.eq(maydisease::getName,searchContent);
                }
                return JSON.toJSONString(maydiseaseService.list(wrapper));
            }
            case "region":{
                LambdaQueryWrapper<region> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(region::getCode, searchContent);
                if(regionService.list(wrapper).size()==0){
                    wrapper.clear();
                    wrapper.eq(region::getName,searchContent);
                }
                return JSON.toJSONString(regionService.list(wrapper));
            }
            case "workcode":{
                LambdaQueryWrapper<workcode> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(workcode::getCode, searchContent);
                if(workcodeService.list(wrapper).size()==0){
                    wrapper.clear();
                    wrapper.eq(workcode::getName,searchContent);
                }
                return JSON.toJSONString(workcodeService.list(wrapper));
            }
            default:{
                return JSON.toJSONString(symptomService.list());
            }
        }

    }


    @GetMapping("/symptom")
    public String symptom() throws IOException {
        List<symptom> symptomList = symptomService.list();

        return JSON.toJSONString(symptomList);
    }

    @GetMapping("/hazardfactor")
    public String hazardfactor() throws IOException {
        List<hazardfactor> hazardfactorList = hazardfactorService.list();

        return JSON.toJSONString(hazardfactorList);
    }

    @GetMapping("/region")
    public String region() throws IOException {
        List<region> regionList = regionService.list();

        return JSON.toJSONString(regionList);
    }

    @GetMapping("/maydisease")
    public String maydisease() throws IOException {
        List<maydisease> maydiseaseList = maydiseaseService.list();

        return JSON.toJSONString(maydiseaseList);
    }

    @GetMapping("/workcode")
    public String workcode() throws IOException {
        List<workcode> workcodeList = workcodeService.list();
        return JSON.toJSONString(workcodeList);
    }


}
