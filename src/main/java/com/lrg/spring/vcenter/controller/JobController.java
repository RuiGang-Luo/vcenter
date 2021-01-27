package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.pojo.ResultEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务调用接口
 */

@RestController
@RequestMapping("/job")
public class JobController {
//    private String dataClass="Job";
    @RequestMapping("/insert")
    public ResultEntity insert(){
        return null;
    }

}
