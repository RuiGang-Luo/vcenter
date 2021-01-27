package com.lrg.spring.vcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URL;

@SpringBootApplication
public class VCenterApplication {

    public static void main(String[] args) {
        String path  = File.separator+"config"+File.separator+"logback.xml";
        File file = new File(System.getProperty("user.dir")+path);
        if(file.exists())
            System.setProperty("logging.config",System.getProperty("user.dir")+path);
        SpringApplication.run(VCenterApplication.class, args);
    }

}
