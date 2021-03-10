package com.lrg.spring.vcenter.task;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.utils.FileUtils;
import com.lrg.spring.vcenter.utils.SpringContextUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MaintenanceJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(MaintenanceJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dataStore(jobExecutionContext);
        vcenterJob(jobExecutionContext);
    }

    private void dataStore(JobExecutionContext jobExecutionContext){
        String root = jobExecutionContext.getMergedJobDataMap().get("rootPath").toString();
        File directory = new File(root);
        if(!directory.exists()){
            directory.mkdirs();
        } else {
            Map<String,Map> temp = (Map<String, Map>) jobExecutionContext.getMergedJobDataMap().get("data");
            Gson gson = new Gson();
            Map<String,Map> cache = gson.fromJson(gson.toJson(temp),Map.class);
            for(Map.Entry entry : cache.entrySet()){
                String fileName = entry.getKey().toString();

                Object object = entry.getValue();
                synchronized (object){
                    if(object == null){
                        continue;
                    }
                    try {
                        FileUtils.JsonWrite(root+File.separator+fileName,object);
                    } catch (IOException e) {
                        logger.error(e.getMessage(),e);
                    }
                }
                logger.debug("the file["+fileName+"] save complete");
            }

        }
    }
    private void vcenterJob(JobExecutionContext jobExecutionContext){
        Context context= (Context) SpringContextUtils.getBean(Context.class);
        List<ScanExecutable> list = context.getScanExecutables();
        for(ScanExecutable scanExecutable : list){
            try{
                scanExecutable.execute(jobExecutionContext);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

}