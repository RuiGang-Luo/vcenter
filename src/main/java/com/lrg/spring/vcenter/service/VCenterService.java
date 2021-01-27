package com.lrg.spring.vcenter.service;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.context.Constant;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.context.ExecutorServicePool;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import com.lrg.spring.vcenter.utils.HTTPUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class VCenterService implements ScanExecutable {
    private static final String DATA_CLASS="Job.json";
    private static final String SESSION_URL = "/rest/com/vmware/cis/session";
    private static final String SESSION_PARAM = "vmware-api-session-id";
    private static final String LIST_URL = "/rest/vcenter/vm";
    private static final String CLOSE_URL = "/rest/vcenter/vm/$vmId$/power/stop";
    private static final String START_URL = "/rest/vcenter/vm/$vmId$/power/start";

    @Autowired
    private  Context context;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Map> list = DataCache.getDataList(DATA_CLASS);
        for(Map map :list){
            List<Map<String,String>> target = (List) map.get("target");
            for(Map temp:target){
                Object vmId = temp.get("vm");
                if(vmId == null || vmId.equals("")){
                    throw new UnknownError("here is no 'vmId' for this Job["+map.get("primaryKey")+"]");
                }
                ExecutorServicePool.execute(new CallVCenter(map.get("action").toString(),vmId.toString()));
            }

        }
    }

    public boolean start(String vmId) throws IOException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(START_URL,sessionId,true);
        Object value = getValue(response);
        return true;
    }

    public boolean stop(String vmId) throws IOException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(CLOSE_URL,sessionId,true);
        Object value = getValue(response);
        return true;
    }

    public List list() throws IOException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(LIST_URL,sessionId,true);
        Object value = getValue(response);
        if(value instanceof List){
            return (List) value;
        } else {
            throw new UnknownError("the List['value'] is :"+new Gson().toJson(value));
        }
    }

    private String session() throws IOException {
        CloseableHttpResponse response = getResponse(SESSION_URL,null,false);
        Object value = getValue(response);
        if(value != null && value instanceof String && !value.equals("")){
            return  value.toString();
        } else {
            throw new UnknownError("the session['value'] is :"+new Gson().toJson(value));
        }
    }

    private Object getValue(CloseableHttpResponse response ) throws IOException {
        String resultStr = EntityUtils.toString(response.getEntity(), Constant.DEFAULTCHARSET);
        response.close();
        Gson gson = new Gson();
        Map<String,Object> result = gson.fromJson(resultStr,Map.class);
        Object errorType = result.get("type");
        if(errorType != null){
            throw new UnknownError(resultStr);
        } else {
            return result.get("value");
        }
    }

    private CloseableHttpResponse getResponse(String uri,String sessionId,boolean isPost) throws IOException {
        String ip = "";
        ip =context.getProperties("com.VCenter.ip");
        Map map = new HashMap();
        if(sessionId == null){
            String name = "";
            String pass = "";
            name =context.getProperties("com.VCenter.userName");
            pass =context.getProperties("com.VCenter.password");
            String encodeToString = Base64.getEncoder().encodeToString((name+":"+pass).getBytes());
            map.put("Authorization","Basic "+encodeToString);
        }else {
            map.put("vmware-api-session-id",sessionId);
        }
        if (!isPost){
            return HTTPUtils.doGet(map,"http://"+ip,uri);
        } else {
            return HTTPUtils.post(map,"http://"+ip,uri,"");
        }

    }

    class CallVCenter implements Runnable{
        private String action;
        private String vmId;
        private final Logger logger = LoggerFactory.getLogger(MaintenanceJob.class);

        public CallVCenter(String action,String vmId){
            this.action =action;
            this.vmId = vmId;
        }
        @Override
        public void run() {
           switch (action){
               case "1":
                   try {
                       start(vmId);
                   } catch (IOException e) {
                       logger.error(e.getMessage(),e);
                   }
                   break;
               case "2":
                   try {
                       stop(vmId);
                   } catch (IOException e) {
                       logger.error(e.getMessage(),e);
                   }
                   break;
               default:
                   logger.error("The action=["+action+"] is an invalid data value");
           }
        }
    }

    public String getDataClass() {
        return DATA_CLASS;
    }

    public static void main(String[] args) {
        //编码
        String encodeToString = Base64.getEncoder().encodeToString("apple@vsphere.local:VMware1!".getBytes());
        System.out.println(encodeToString);
//解码
        byte[] decode = Base64.getDecoder().decode(encodeToString);
        System.out.println(new String(decode));
    }
}
