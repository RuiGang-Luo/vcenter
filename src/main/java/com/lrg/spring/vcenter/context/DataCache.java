package com.lrg.spring.vcenter.context;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author LRG
 * 数据缓存
 */
public class DataCache {
    private static Map<String,Map> cache = new LinkedHashMap();
    static {
        //加载Job.json
        //加载的时候需要获取config目录，然后迭代文件加载，最后以文件名作为key将数据缓存在map中，
        File directory = new File(System.getProperty("user.dir")+File.separator+"config");
        if(!directory.exists()){
            directory.mkdirs();
        } else {
            File[] files = directory.listFiles();
            for(int index=0;index<files.length;index++){
                String jsonStr = null;
                try {
                    jsonStr = FileUtils.readJsonFile(files[index].getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                if(jsonStr == null || "".equals(jsonStr)){
                    continue;
                }
                Map data = gson.fromJson(jsonStr,Map.class);
                cache.put(files[index].getName(),data);
            }

        }


    }
    //以下方法注意线程安全性
    //数据查询
    public static Map getData(String dataClass,String primaryKey){
        Map temp = cache.get(dataClass);
        if(temp == null){
            return new HashMap();
        }
        Object data = null;
        synchronized (temp){
            data = temp.get(primaryKey);
            if( data == null ){
                return new HashMap();
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
    //数据更新
    public static boolean updateData(String dataClass,String primaryKey,Map data){
        Map temp = null;
        synchronized (cache){
            temp = cache.get(dataClass);
            if(temp == null){
                temp = new HashMap();
            }
        }
        synchronized (temp){
            temp.put(primaryKey,data);
        }
        return true;
    }
}
