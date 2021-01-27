package com.lrg.spring.vcenter.utils;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HTTPUtils {
//    private static Map<String, CloseableHttpClient> clientMap = new HashMap<>();

    // 1.使用get方式发送报文
    public static CloseableHttpResponse doGet(Map<String,String> headerMap,String domain, String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        if(headerMap != null){
            Set<String> keys = headerMap.keySet();
            for (String string : keys) {
                get.addHeader(string, headerMap.get(string).toString());
            }
        }
        try {
            CloseableHttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            // LogUtil.debug(statusCode);
            if (statusCode == 200) {
                return response;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    // 使用POST方法发送FORM表单数据
    public static CloseableHttpResponse post(Map<String, String> headerMap, String domain, String url,
                                               String requsestData) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(domain + url);
        Set<String> keys = headerMap.keySet();
        for (String string : keys) {
            post.addHeader(string, headerMap.get(string));
        }
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) //连接超时时间
                .setConnectionRequestTimeout(1000) //从连接池中取的连接的最长时间
                .setSocketTimeout(3 *1000) //数据传输的超时时间
                .build();
        post.setConfig(config);
        //将报文信息设置到请求体中
        post.setEntity(new StringEntity(requsestData,"UTF-8"));
        // 一个域一个连接对象
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        response = closeableHttpClient.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 302) {
            Header header = response.getFirstHeader("location"); // 跳转的目标地址是在HTTP-HEAD中的
            String newuri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
//				LogUtil.info("重定向地址为：" + newuri);
            return post(headerMap, domain, newuri, requsestData);
        }
        return response;
    }
}
