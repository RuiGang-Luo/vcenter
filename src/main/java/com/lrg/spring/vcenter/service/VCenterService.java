package com.lrg.spring.vcenter.service;

import com.google.gson.Gson;
import com.lrg.spring.vcenter.context.Constant;
import com.lrg.spring.vcenter.context.Context;
import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.context.ExecutorServicePool;
import com.lrg.spring.vcenter.inter.Initializable;
import com.lrg.spring.vcenter.inter.ScanExecutable;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import com.lrg.spring.vcenter.task.MaintenanceJob;
import com.lrg.spring.vcenter.utils.CronUtils;
import com.lrg.spring.vcenter.utils.FileUtils;
import com.lrg.spring.vcenter.utils.HTTPUtils;
import com.lrg.spring.vcenter.utils.PathUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VCenterService implements ScanExecutable, Initializable {
    private static final String DATA_CLASS = "Job.json";
    private static final String CACHE_CLASS = "VM.json";
    private static final String SESSION_URL = "/rest/com/vmware/cis/session";
    private static String SESSION_ID = "vmware-api-session-id";
    private static Date expiration = null;
    private static final String LIST_URL = "/rest/vcenter/vm";
    private static final String CLOSE_URL = "/rest/vcenter/vm/$vmId$/power/stop";
    private static final String START_URL = "/rest/vcenter/vm/$vmId$/power/start";
    private final Logger logger = LoggerFactory.getLogger(VCenterService.class);
    @Autowired
    private Context context;
    @Autowired
    private PowerShellService powerShellService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Map> list = DataCache.getDataList(DATA_CLASS);
        for (Map map : list) {
            List<Map<String, String>> target = (List) map.get("target");
            Object cron = map.get("cron");
            if (cron == null || cron.equals("")) {
                throw new UnknownError("here is no 'cron' for this Job[" + map.get("primaryKey") + "]");
            }
            try {
                Date currentDate = new Date();
                if (CronUtils.filterWithCronTime(cron.toString(), currentDate)) {
                    Object startDate = map.get("startDate");
                    Object endDate = map.get("endDate");
                    Object endJobTime = map.get("endJobTime");
                    Object jobTime = map.get("jobTime");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd h:m");
                    if (startDate != null) {
                        String start = startDate.toString() + " " + jobTime.toString();
                        if (simpleDateFormat.parse(start).after(currentDate)) {
                            continue;
                        }
                    }
                    if (endDate != null) {
                        String end = endDate.toString();
                        if (endJobTime != null && !endJobTime.equals("")) {
                            end += " " + endJobTime.toString();
                        } else {
                            end += " " + "0:0";
                        }
                        if (simpleDateFormat.parse(end).before(currentDate)) {
                            continue;
                        }
                    }
                    logger.error(cron.toString());
                    initModules();
                    Object poolId = map.get("poolId");
                    if(poolId !=null && poolId.toString().trim() !=""){
                        Object action = map.get("action");
                        String command = FileUtils.readJsonFile(PathUtils.getClasspath() + File.separator + "config" + File.separator + "command.ps1");
                        String status = action.equals("1") ? "$true" : "$false";
                        command = command.replace("$poolId$",poolId.toString())
                                .replace("$serverIp$",context.getProperties("com.Horizon.ip"))
                                .replace("$userName$",context.getProperties("com.Horizon.userName"))
                                .replace("$password$",context.getProperties("com.Horizon.password"))
                                .replace("$status$",status);
                        logger.error(command);
                        ResultEntity resultEntity = powerShellService.run(command);
                        if(resultEntity.getMessage().indexOf("Exception")>0){
                            logger.error(resultEntity.getMessage());
                            continue;
                        } else {
                            logger.info(resultEntity.getMessage());
                        }
                    }

                    for (Map temp : target) {
                        Object vmId = temp.get("vm");
                        logger.info(vmId.toString());
                        if (vmId == null || vmId.equals("")) {
                            throw new UnknownError("here is no 'vmId' for this Job[" + map.get("primaryKey") + "]");
                        }
                        ExecutorServicePool.execute(new CallVCenter(map.get("action").toString(), vmId.toString()));
                    }
                }
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } catch (Throwable throwable){
                logger.error(throwable.getMessage(), throwable);
            }
        }
    }

    public void initModules() throws IOException {
        context = new Context();
        File file = new File(context.getProperties("com.Horizon.ps.path") + File.separator + "Horizon.ps1");
        if (!file.exists()) {
            new File(context.getProperties("com.Horizon.ps.path")).mkdirs();
            File psRoot = new File(PathUtils.getClasspath() + File.separator + "config" + File.separator + "Modules");
            copyFile(psRoot, context.getProperties("com.Horizon.ps.path"));
        }
    }

    private void copyFile(File file, String target) throws IOException {
        if (file.isFile()) {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(target + File.separator + file.getName()));
            Files.copy(file.toPath(), fileOutputStream);
            fileOutputStream.close();
        } else {
            File[] root = file.listFiles();
            for (File temp : root) {
                new File(target + File.separator + file.getName()).mkdirs();
                copyFile(temp, target + File.separator + file.getName());
            }
        }
    }

    public boolean start(String vmId) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(START_URL.replace("$vmId$", vmId), sessionId, true);
        Object value = getValue(response);
        if (value != null && !value.equals("")) {
            throw new UnknownError(value.toString());
        }
        return true;
    }

    public boolean stop(String vmId) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(CLOSE_URL.replace("$vmId$", vmId), sessionId, true);
        Object value = getValue(response);
        if (value != null && !value.equals("")) {
            throw new UnknownError(value.toString());
        }
        return true;
    }

    public List list() throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String sessionId = session();
        CloseableHttpResponse response = getResponse(LIST_URL, sessionId, false);
        Object value = getValue(response);
        if (value instanceof List) {
            return (List) value;
        } else {
            throw new UnknownError("the List['value'] is :" + new Gson().toJson(value));
        }
    }

    private String session() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (expiration == null || expiration.before(new Date(System.currentTimeMillis() - (1000 * 60 * 10)))) {
            CloseableHttpResponse response = getResponse(SESSION_URL, null, true);
            Object value = getValue(response);
            if (value != null && value instanceof String && !value.equals("")) {
                SESSION_ID = value.toString();
            } else {
                throw new UnknownError("the session['value'] is :" + new Gson().toJson(value));
            }
        }
        return SESSION_ID;
    }

    private Object getValue(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String resultStr = EntityUtils.toString(entity, Constant.DEFAULTCHARSET);
        if (response.getStatusLine().getStatusCode() == 200) {
            response.close();
            if (resultStr != null && !resultStr.equals("")) {
                Gson gson = new Gson();
                Map<String, Object> result = gson.fromJson(resultStr, Map.class);
                Object errorType = result.get("type");
                if (errorType != null) {
                    throw new UnknownError(resultStr);
                } else {
                    return result.get("value");
                }
            } else {
                return "";
            }
        } else {
            throw new UnknownError(resultStr);
        }
    }

    private CloseableHttpResponse getResponse(String uri, String sessionId, boolean isPost) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String ip = "";
        String port = null;
        String protocol = null;
        ip = context.getProperties("com.VCenter.ip");
        port = context.getProperties("com.VCenter.port");
        protocol = context.getProperties("com.VCenter.protocol");
        Map map = new HashMap();
        if (sessionId == null) {
            String name = "";
            String pass = "";
            name = context.getProperties("com.VCenter.userName");
            pass = context.getProperties("com.VCenter.password");
            String encodeToString = Base64.getEncoder().encodeToString((name + ":" + pass).getBytes());
            map.put("Authorization", "Basic " + encodeToString);
        } else {
            map.put("vmware-api-session-id", sessionId);
        }
        if (ip == null || ip.equals("")) {
            throw new UnknownError("the IP is null");
        }
        if (protocol == null || protocol.equals("")) {
            protocol = "http";
            logger.warn("the protocol is null,using default value 'http'");
        }
        if (port == null || port.equals("")) {
            if (protocol.equals("http")) {
                port = "80";
                logger.warn("the port is null,using default value '80'");
            } else if (protocol.equals("https")) {
                port = "443";
                logger.warn("the port is null,using default value '443'");
            }
        }

        System.out.println(protocol + "://" + ip + ":" + port + uri);
        if (!isPost) {
            return HTTPUtils.doGet(map, protocol + "://" + ip + ":" + port, uri);
        } else {
            return HTTPUtils.post(map, protocol + "://" + ip + ":" + port, uri, "");
        }

    }

    @Override
    public boolean init() throws Throwable {
        DataCache.deleteClass(this.getCacheClass());
        List<Map> list = DataCache.getDataList(this.getDataClass());
        for (Map<String, Object> map : list) {
            Gson gson = new Gson();
            Map temp = gson.fromJson(gson.toJson(map), Map.class);
            temp.remove("target");
            List<Map<String, String>> vmList = (List<Map<String, String>>) map.get("target");
            for (Map<String, String> vmInfo : vmList) {
                String vmId = vmInfo.get("vm");
                Map map1 = DataCache.getData(this.getCacheClass(), vmId);
                if (map1 == null) {
                    map1 = new HashMap();
                }

                map1.put(map.get("primaryKey").toString(), temp);
                DataCache.insertData(this.getCacheClass(), vmId, map1);
            }
        }
        return true;
    }

    class CallVCenter implements Runnable {
        private String action;
        private String vmId;
        private final Logger logger = LoggerFactory.getLogger(MaintenanceJob.class);

        public CallVCenter(String action, String vmId) {
            this.action = action;
            this.vmId = vmId;
        }

        @Override
        public void run() {
            switch (action) {
                case "1":
                    try {
                        start(vmId);
                    } catch (IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                        logger.error(e.getMessage(), e);
                    }
                    break;
                case "2":
                    try {
                        stop(vmId);
                    } catch (IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                        logger.error(e.getMessage(), e);
                    }
                    break;
                default:
                    logger.error("The action=[" + action + "] is an invalid data value");
            }
        }
    }

    public String getDataClass() {
        return DATA_CLASS;
    }

    public String getCacheClass() {
        return CACHE_CLASS;
    }

    public static void main(String[] args) throws ParseException {
//        //编码
//        String encodeToString = Base64.getEncoder().encodeToString("apple@vsphere.local:VMware1!".getBytes());
//        System.out.println(encodeToString);
////解码
//        byte[] decode = Base64.getDecoder().decode(encodeToString);
//        System.out.println(new String(decode));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd h:m");
//        String endDate = "2021-03-25";
//        String endJobTime = "18:5";
//        if (endDate != null) {
//            String end = endDate.toString();
//            if (endJobTime != null && !endJobTime.equals("")) {
//                end += " " + endJobTime.toString();
//            } else {
//                end += " " + "0:0";
//            }
//            if (simpleDateFormat.parse(end).before(new Date(System.currentTimeMillis()))) {
//                return;
//            }
//        }

        VCenterService vCenterService = new VCenterService();
        try {
            vCenterService.initModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
