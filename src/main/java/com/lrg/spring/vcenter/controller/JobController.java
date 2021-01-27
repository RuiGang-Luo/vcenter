package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 任务调用接口
 */

@RestController
@RequestMapping("/job")
public class JobController {
    private String dataClass="Job.json";

    @PostMapping("/insert")
    public ResultEntity insert(@RequestBody Map data){
        if(DataCache.insertData(dataClass,data.get("primaryKey").toString(),data)){
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @PostMapping("/update")
    public ResultEntity update(@RequestBody Map data){
        if(DataCache.updateData(dataClass,data.get("primaryKey").toString(),data)){
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @DeleteMapping("/delete/{primaryKey}")
    public ResultEntity delete(@PathVariable("primaryKey") String primaryKey){
        DataCache.deleteData(dataClass,primaryKey);
        return new ResultEntity(200);
    }

    @GetMapping("/select/{primaryKey}")
    public ResultEntity select(@PathVariable("primaryKey") String primaryKey){
        return new ResultEntity(200,null, DataCache.getData(dataClass,primaryKey));
    }

    @GetMapping("/list")
    public ResultEntity list(){
        return new ResultEntity(200,null,DataCache.getDataList(dataClass));
    }
}
