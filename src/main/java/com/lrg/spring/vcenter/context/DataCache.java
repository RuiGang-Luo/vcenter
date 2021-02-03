package com.lrg.spring.vcenter.context;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import com.lrg.spring.vcenter.utils.FileUtils;
import com.lrg.spring.vcenter.utils.PathUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author LRG
 * 数据缓存
 */
public class DataCache {
    private static String root = PathUtils.getClasspath()+File.separator+"config"+File.separator+"json";
    private static Map<String,Map> cache = new LinkedHashMap();
    private static Map<String,Map> vmCache = new LinkedHashMap();
    private static final Logger logger = LoggerFactory.getLogger(DataCache.class);
    static {
        //加载Job.json
        //加载的时候需要获取config目录，然后迭代文件加载，最后以文件名作为key将数据缓存在map中，
        File directory = new File(root);
        if(!directory.exists()){
            directory.mkdirs();
        } else {
            File[] files = directory.listFiles();
            for(int index=0;index<files.length;index++){
                String jsonStr = null;
                try {
                    jsonStr = FileUtils.readJsonFile(files[index].getAbsolutePath());
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
                Gson gson = new Gson();
                if(jsonStr == null || "".equals(jsonStr)){
                    continue;
                }
                Map data = gson.fromJson(jsonStr,Map.class);

                cache.put(files[index].getName(),data);
            }

        }
        try    {
            SchedulerFactory schedFact  =   new  org.quartz.impl.StdSchedulerFactory();
            Scheduler sched  =  schedFact.getScheduler();
            JobDetail jobDetail  =   JobBuilder.newJob(MaintenanceJob.class).withIdentity("dataFlush").build();
            jobDetail.getJobDataMap().put("rootPath",root);
            jobDetail.getJobDataMap().put("data",cache);
            CronScheduleBuilder trigger  =  CronScheduleBuilder.cronSchedule("0 * * * * ?");
            sched.scheduleJob(jobDetail, TriggerBuilder.newTrigger().withIdentity("minute").startNow().withSchedule(trigger).build());
            sched.start();
        }   catch  (Exception e)   {
            logger.error(e.getMessage(),e);
        }

    }
    //以下方法注意线程安全性
    //数据查询
    public static Map getData(String dataClass,String primaryKey){
        Map temp = cache.get(dataClass);
        if(temp == null){
            return null;
        }
        Object data = null;
        synchronized (temp){
            data = temp.get(primaryKey);
            if( data == null ){
                Map map = new HashMap();
                temp.put(primaryKey,map);
                return map;
            }
        }
        return (Map)data;
    }
    //获取数据列表
    public static List getDataList(String dataClass){
        Map temp = cache.get(dataClass);
        if(temp == null){
            return new ArrayList();
        }
        List list = null;
        synchronized (temp){
            list  = new ArrayList(temp.values());
        }
        return list;
    }
    //数据新增
    public static boolean insertData(String dataClass,String primaryKey,Map data){
        Map temp = null;
        synchronized (cache){
            temp = cache.get(dataClass);
            if(temp == null){
                temp = new HashMap();
                cache.put(dataClass,temp);
            }
        }
        synchronized (temp){
            temp.put(primaryKey,data);
        }
        return true;
    }
    //数据删除
    public static Map deleteData(String dataClass,String primaryKey){
        Map temp = cache.get(dataClass);
        if(temp == null){
            return null;
        }
        Map result = null;
        synchronized (temp){
            result =(Map) temp.remove(primaryKey);
        }
        return result;
    }
    //数据删除
    public static void deleteClass(String dataClass){
        Map temp = cache.get(dataClass);
        if(temp == null){
            return;
        }
        cache.remove(dataClass);
    }
    //数据更新
    public static boolean updateData(String dataClass,String primaryKey,Map data){
        Map temp = null;
        synchronized (cache){
            temp = cache.get(dataClass);
            if(temp == null){
                temp = new HashMap();
                cache.put(dataClass,temp);
            }
        }
        synchronized (temp){
            temp.put(primaryKey,data);
        }
        return true;
    }


}
