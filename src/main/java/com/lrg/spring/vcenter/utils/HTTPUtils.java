package com.lrg.spring.vcenter.utils;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

public class HTTPUtils {
//    private static Map<String, CloseableHttpClient> clientMap = new HashMap<>();

    // 1.使用get方式发送报文
    public static CloseableHttpResponse doGet(Map<String,String> headerMap,String domain, String url) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        CloseableHttpClient client = null;
        if(domain.startsWith("http:")){
            client = HttpClients.createDefault();
        } else if(domain.startsWith("https:")){
            //采用绕过验证的方式处理https请求
            SSLConnectionSocketFactory sslcontext = createIgnoreVerifySSL();
            //创建自定义的httpclient对象
            client = HttpClients.custom().setSSLSocketFactory(sslcontext).build();
        }
        System.out.println(domain+url);
        HttpGet get = new HttpGet(domain+url);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) //连接超时时间
                .setConnectionRequestTimeout(1000) //从连接池中取的连接的最长时间
                .setSocketTimeout(300 *1000) //数据传输的超时时间
                .build();
        get.setConfig(config);
        if(headerMap != null){
            Set<String> keys = headerMap.keySet();
            for (String string : keys) {
                get.addHeader(string, headerMap.get(string).toString());
            }
        }
        CloseableHttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        // LogUtil.debug(statusCode);
        return response;

    }
    // 使用POST方法发送FORM表单数据
    public static CloseableHttpResponse post(Map<String, String> headerMap, String domain, String url,
                                               String requsestData) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {


        HttpPost post = new HttpPost(domain + url);
        Set<String> keys = headerMap.keySet();
        for (String string : keys) {
            post.addHeader(string, headerMap.get(string));
        }
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) //连接超时时间
                .setConnectionRequestTimeout(1000) //从连接池中取的连接的最长时间
                .setSocketTimeout(300 *1000) //数据传输的超时时间
                .build();
        post.setConfig(config);
        //将报文信息设置到请求体中
        post.setEntity(new StringEntity(requsestData,"UTF-8"));
        CloseableHttpClient client = null;
        if(domain.startsWith("http:")){
            client = HttpClients.createDefault();
        } else if(domain.startsWith("https:")){
            //采用绕过验证的方式处理https请求
            SSLConnectionSocketFactory sslcontext = createIgnoreVerifySSL();
            //创建自定义的httpclient对象
            client = HttpClients.custom().setSSLSocketFactory(sslcontext).build();
        }
        CloseableHttpResponse response = null;
        response = client.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 302) {
            Header header = response.getFirstHeader("location"); // 跳转的目标地址是在HTTP-HEAD中的
            String newuri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
//				LogUtil.info("重定向地址为：" + newuri);
            return post(headerMap, domain, newuri, requsestData);
        }
        return response;
    }


    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLConnectionSocketFactory createIgnoreVerifySSL() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //使用 loadTrustMaterial() 方法实现一个信任策略，信任全部证书
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任全部
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        //NoopHostnameVerifier类:  做为主机名验证工具，实质上关闭了主机名验证，它接受任何
        //有效的SSL会话并匹配到目标主机。
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        return sslsf;
    }
}
