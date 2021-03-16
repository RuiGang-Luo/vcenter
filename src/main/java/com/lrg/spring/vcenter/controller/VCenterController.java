package com.lrg.spring.vcenter.controller;

import com.google.gson.Gson;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 任务调用接口
 */

@RestController
@RequestMapping("/vcenter")
public class VCenterController {
    private static final String DATA_CLASS="VM.json";

    @Autowired
    private VCenterService vCenterService;

    @PostMapping("/job/insert")
    public ResultEntity insert(@RequestBody Map<String,Object> data) throws Throwable {
        if(DataCache.insertData(vCenterService.getDataClass(),data.get("primaryKey").toString(),data)){
            vCenterService.init();
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @PostMapping("/job/update")
    public ResultEntity update(@RequestBody Map data) throws Throwable {
        if(DataCache.updateData(vCenterService.getDataClass(),data.get("primaryKey").toString(),data)){
            vCenterService.init();
            return new ResultEntity(200);
        } else {
            return new ResultEntity(500);
        }
    }

    @DeleteMapping("/job/delete/{primaryKey}")
    public ResultEntity delete(@PathVariable("primaryKey") String primaryKey) throws Throwable {
        Map<String,Object> temp = DataCache.deleteData(vCenterService.getDataClass(),primaryKey);
//        DataCache.deleteData(vCenterService.getCacheClass(),temp.get("t"));
        vCenterService.init();
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
    public ResultEntity start(@RequestBody List<Map> data) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, InterruptedException {
        return startOrStop(data,true);
    }

    @PostMapping("/power/stop")
    public ResultEntity stop(@RequestBody List<Map> data) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, InterruptedException {
        return startOrStop(data,false);
    }

    private ResultEntity startOrStop(List target,boolean isStart) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, InterruptedException {
        for (Object obj : (List)target){
            if(obj!=null ){
                //完整数据格式
                if(obj instanceof Map){
                    Object vmId = ((Map) obj).get("vm");

                    if(vmId != null){
                        if(vmId instanceof String){
                            if(isStart){
                                if(((Map) obj).get("power_state").equals("POWERED_OFF")){
                                    vCenterService.start((String) vmId);
//                                    Thread.sleep(1000);
                                    continue;
                                }
                            }else {
                                if(((Map) obj).get("power_state").equals("POWERED_ON")){
                                    vCenterService.stop((String) vmId);
//                                    Thread.sleep(1000);
                                    continue;
                                }
                            }

                        }
                    }
                    //target只传字符串数组
                } else if(obj instanceof String){
                    // vCenterService.start((String) obj);
                    return new ResultEntity(200);
                }
            }
        }
        return new ResultEntity(200);
//        throw new UnknownError("The target is error :"+new Gson().toJson(target));
    }

    @GetMapping("/list")
    public ResultEntity VMlist() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
//        List<Map<String,Object>> temp = new Gson().fromJson(Constant.VM_LIST_STR,List.class);
        List<Map<String,Object>> temp = vCenterService.list();
        for(Map<String,Object> map : temp){
            String vmId = map.get("vm").toString();
            Map obj = (Map) DataCache.getData(vCenterService.getCacheClass(),vmId);
            if(obj !=null && obj.size()>0){
                map.put("jobInfo",obj);
            }
        }
        return new ResultEntity(200,null,temp);
    }


}
