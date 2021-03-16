package com.lrg.spring.vcenter.service;

import com.lrg.spring.vcenter.pojo.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PowerShellService {
    private final Logger logger = LoggerFactory.getLogger(PowerShellService.class);

    public ResultEntity run(String command) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReaderErr = null;
        Process powerShellProcess = null;
        try{
            if(powerShellProcess == null || powerShellProcess.isAlive()){
                String temp = "powershell.exe ";
                powerShellProcess = Runtime.getRuntime().exec(temp);
            }
//            powerShellProcess.getOutputStream().close();
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    powerShellProcess.getOutputStream()));
            bufferedWriter.append(command);
            bufferedWriter.flush();
            bufferedWriter.close();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line+"\n");
            }
            bufferedReader.close();
            bufferedReaderErr = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getErrorStream()));
            while ((line = bufferedReaderErr.readLine()) != null) {
                stringBuffer.append(line+"\n");
            }
            bufferedReaderErr.close();
//            logger.info(stringBuffer.toString());
            return new ResultEntity(200,stringBuffer.toString());
        }catch (IOException e){
            e.printStackTrace();
            return new ResultEntity(500,e.toString());
        }finally {
//            if(bufferedReader != null){
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(bufferedWriter != null){
//                try {
//                    bufferedWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(bufferedReaderErr != null){
//                try {
//                    bufferedReaderErr.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if(powerShellProcess != null){
                powerShellProcess.destroy();
            }
        }
    }

    public static void main(String[] args) {
        PowerShellService powerShellService = new PowerShellService();
        ResultEntity a = powerShellService.run("cd c:\\ \n " +
                "cd C:\\Users");
        System.out.println(a.getMessage());
    }
}
