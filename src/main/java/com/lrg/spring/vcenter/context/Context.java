package com.lrg.spring.vcenter.context;

import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author LRG
 */
@Component
public class Context {
    @Autowired
    public static List<ScanExecutable> scanExecutables;

    private static Properties properties = null;
    private static long lastModified = 0L;

    public static final String getProperties(String key) throws IOException{
        File file = new File(PathUtils.getClasspath()+File.separator+"config"+File.separator+"config.properties");
        synchronized (properties){
            if(isUpdate(file)){
                lastModified = file.lastModified();
                properties = new Properties();
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");
                properties.load(inputStreamReader);
                inputStreamReader.close();
            }
        }
        return properties.getProperty(key);
    }

    private static final boolean isUpdate(File file){
        if(!file.exists()){
            throw new UnknownError("The configuration file["+file.getAbsolutePath()+"] does not exist!");
        }
        return file.lastModified()>lastModified;
    }


}
