package com.lrg.spring.vcenter.utils;

import com.google.gson.Gson;

import java.io.*;

public class FileUtils {
    //读取json文件
    public static String readJsonFile(String fileName) throws IOException {
        String jsonStr = "";
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
    }
    //写入json文件
    public static void JsonWrite(String fileName,Object object) throws IOException{
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            Gson gson = new Gson();//创建JSONObject对象
            String data = gson.toJson(object);
            System.out.println(data);
            osw.write(data);
            osw.flush();//清空缓冲区，强制输出数据
        }finally {
            if(osw != null)
            osw.close();//关闭输出流
        }
    }
}
