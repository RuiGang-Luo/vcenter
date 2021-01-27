package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.context.Constant;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.context.ExecutorServicePool;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 任务调用接口
 */

@RestController
@RequestMapping("/job")
public class JobController implements ScanExecutable {
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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Map> list = DataCache.getDataList(dataClass);
        for(Map map :list){
            List<String> target = (List) map.get("target");
            for(String vmId:target){
                try {
                    ExecutorServicePool.execute(new CallVCenter(Context.getProperties("com.VCenter.ip"),map.get("action").toString(),vmId));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class CallVCenter implements Runnable{
        private String ip;
        private String action;
        private String vmId;
        private final Logger logger = LoggerFactory.getLogger(MaintenanceJob.class);

        public CallVCenter(String ip,String action,String vmId){
            this.action =action;
            this.ip = ip;
            this.vmId = vmId;
        }
        @Override
        public void run() {
            System.out.println(ip+action+vmId);
        }
    }
}
