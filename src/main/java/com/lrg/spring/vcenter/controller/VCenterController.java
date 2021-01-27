package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.context.Constant;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.context.ExecutorServicePool;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import com.lrg.spring.vcenter.service.VCenterService;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import com.lrg.spring.vcenter.utils.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 任务调用接口
 */

@RestController
@RequestMapping("/vcenter")
public class VCenterController {

    @Autowired
    private VCenterService vCenterService;

    @PostMapping("/job/insert")
    public ResultEntity insert(@RequestBody Map data){
        if(DataCache.insertData(vCenterService.getDataClass(),data.get("primaryKey").toString(),data)){
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @PostMapping("/job/update")
    public ResultEntity update(@RequestBody Map data){
        if(DataCache.updateData(vCenterService.getDataClass(),data.get("primaryKey").toString(),data)){
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @DeleteMapping("/job/delete/{primaryKey}")
    public ResultEntity delete(@PathVariable("primaryKey") String primaryKey){
        DataCache.deleteData(vCenterService.getDataClass(),primaryKey);
        return new ResultEntity(200);
    }

    @GetMapping("/job/select/{primaryKey}")
    public ResultEntity select(@PathVariable("primaryKey") String primaryKey){
        return new ResultEntity(200,null, DataCache.getData(vCenterService.getDataClass(),primaryKey));
    }

    @GetMapping("/job/list")
    public ResultEntity jobList(){
        return new ResultEntity(200,null,DataCache.getDataList(vCenterService.getDataClass()));
    }

    @PostMapping("/power/start")
    public ResultEntity start(@RequestBody Map data){
        return new ResultEntity(500);
    }
    @PostMapping("/power/stop")
    public ResultEntity stop(@RequestBody Map data){
        return new ResultEntity(500);
    }

    @GetMapping("/list")
    public ResultEntity VMlist(){
        return new ResultEntity(500);
    }
}
