package com.lrg.spring.vcenter.context;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LRG
 * 数据缓存
 */
public class DataCache {
    private static Map<String,Map> cache = new LinkedHashMap();
    static {
        //加载Job.json
        //加载的时候需要获取config目录，然后迭代文件加载，最后以文件名作为key将数据缓存在map中，
        File file = new File("");
        cache.put(file.getName(),new LinkedHashMap<>());
    }
    //以下方法注意线程安全性
    //数据查询
    public static Map getData(String dataClass,String primaryKey){
        //待实现
        return (Map) cache.get(dataClass).get(primaryKey);
    }
    //数据新增
    public static boolean insertData(String dataClass,String primaryKey,Map data){
        //待实现
        return true;
    }
    //数据删除
    public static Map deleteData(String dataClass,String primaryKey){
        //待实现
        return (Map) cache.get(dataClass).get(primaryKey);
    }
    //数据更新
    public static Map updateData(String dataClass,String primaryKey,Map data){
        //待实现
        return (Map) cache.get(dataClass).get(primaryKey);
    }
}
