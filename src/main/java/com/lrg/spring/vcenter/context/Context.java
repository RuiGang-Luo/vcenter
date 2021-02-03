package com.lrg.spring.vcenter.context;

import com.lrg.spring.vcenter.inter.Initializable;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author LRG
 */
@Component
public class Context {
    @Autowired
    private List<ScanExecutable> scanExecutables;
    @Autowired
    private List<Initializable> initializables;

    private Properties properties = null;
    private long lastModified = 0L;

    public final String getProperties(String key) throws IOException{
        File file = new File(PathUtils.getClasspath()+File.separator+"config"+File.separator+"config.properties");
        if(isUpdate(file)){
            lastModified = file.lastModified();
            properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),Constant.DEFAULTCHARSET);
            properties.load(inputStreamReader);
            inputStreamReader.close();
        }
        return properties.getProperty(key);
    }

    private final boolean isUpdate(File file){
        if(!file.exists()){
            throw new UnknownError("The configuration file["+file.getAbsolutePath()+"] does not exist!");
        }
        return file.lastModified()>lastModified;
    }

    @PostConstruct
    public void init(){
        boolean isRun = true;
        for(Initializable initializable : initializables){
            try {
                isRun = initializable.init();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if(!isRun){
               throw new UnknownError("Initialization exception, interrupt loading, system exit");
            }
        }
    }

    public List<ScanExecutable> getScanExecutables() {
        return scanExecutables;
    }

}
