package com.lrg.spring.vcenter.service;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.context.Constant;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.context.ExecutorServicePool;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import com.lrg.spring.vcenter.utils.HTTPUtils;
import com.lrg.spring.vcenter.utils.SpringContextUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VCenterService implements ScanExecutable {
    private static final String DATA_CLASS="Job.json";
    private static final String SESSION_URL = "/rest/com/vmware/cis/session";
    private static final String SESSION_PARAM = "vmware-api-session-id";
    private static final String LIST_URL = "/rest/vcenter/vm";
    private static final String CLOSE_URL = "/rest/vcenter/vm/$targetId$/power/stop";
    private static final String START_URL = "/rest/vcenter/vm/$targetId$/power/start?";

    @Autowired
    private  Context context;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        List<Map> list = DataCache.getDataList(DATA_CLASS);
        for(Map map :list){
            List<String> target = (List) map.get("target");
            for(String vmId:target){
                ExecutorServicePool.execute(new CallVCenter(map.get("action").toString(),vmId));
            }

        }
    }

    public boolean start(String vmId) throws IOException {
        String sessionId = session();
        return false;
    }

    public boolean stop(String vmId) throws IOException {
        String sessionId = session();
        return false;
    }

    public List list() throws IOException {
        String sessionId = session();
        return new ArrayList();
    }

    private CloseableHttpResponse getResponse(String uri) throws IOException {
        String ip =context.getProperties("com.VCenter.ip");
        Map map = new HashMap();
        map.put("Authorization","Basic "+"YXBwbGVAdnNwaGVyZS5sb2NhbDpWTXdhcmUxIQ==");
        return HTTPUtils.doGet(map,"http://"+ip,uri);
    }


    private String session() throws IOException {
        String ip = "";
        try {
            ip =context.getProperties("com.VCenter.ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("Authorization","Basic "+"YXBwbGVAdnNwaGVyZS5sb2NhbDpWTXdhcmUxIQ==");
        CloseableHttpResponse response = HTTPUtils.doGet(map,"http://"+ip,SESSION_URL);
        String resultStr = EntityUtils.toString(response.getEntity(), Constant.DEFAULTCHARSET);
        Gson gson = new Gson();
        Map<String,String> result = gson.fromJson(resultStr,Map.class);
        String errorType = result.get("type");
        if(errorType != null){
           throw new UnknownError(resultStr);
        } else {
           return result.get("value");
        }
    }

    class CallVCenter implements Runnable{
        private String ip;
        private String action;
        private String vmId;
        private final Logger logger = LoggerFactory.getLogger(MaintenanceJob.class);

        public CallVCenter(String action,String vmId){
            this.action =action;
            this.ip = ip;
            this.vmId = vmId;
        }
        @Override
        public void run() {
            System.out.println(ip+action+vmId);
        }
    }

    public String getDataClass() {
        return DATA_CLASS;
    }
}
