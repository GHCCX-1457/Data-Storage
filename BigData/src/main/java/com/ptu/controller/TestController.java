package com.ptu.controller;

import com.ptu.service.symptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @Autowired
    private symptomService symptomService;


}
