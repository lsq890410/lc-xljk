package com.lc.xljk.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lc.xljk.exception.BusinessException;


/**
 * 利用HttpClient的工具类
 * 
 * @author lisq
 *
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static String charSet = "UTF-8";
    private static CloseableHttpClient httpClient = null;
    private static CloseableHttpResponse response = null;
    
    /**
     * https的post请求
     * @param url
     * @param jsonstr
     * @param charset
     * @return
     */
    public static String doHttpsPost(String url, String jsonStr) {
        try {
            httpClient = SSLClient.createSSLClientDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            
            StringEntity se = new StringEntity(jsonStr);
            se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(se);
            
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    return EntityUtils.toString(resEntity, charSet);
                }
            }
        } catch (Exception ex) {
           logger.error("调用https请求失败："+ex.getMessage(),ex);
        }finally {
             if(httpClient != null){
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(response != null){
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        return null;
    }
    
    /**
     * http的post请求(用于key-value格式的参数) 
     * @param url
     * @param param
     * @return
     * @throws BusinessException 
     */
    public static String doHttpPost(String url,Map<String,String> param) throws BusinessException{
        try {
            //请求发起客户端
            httpClient = HttpClients.createDefault();
            //参数集合
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            //遍历参数并添加到集合
            for(Map.Entry<String, String> entry:param.entrySet()){
                postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            
            //通过post方式访问
            HttpPost post = new HttpPost(url);
            HttpEntity paramEntity = new UrlEncodedFormEntity(postParams,charSet);
            post.setEntity(paramEntity);
            response = httpClient.execute(post);
            StatusLine status = response.getStatusLine();  
            int state = status.getStatusCode();  
            if (state == HttpStatus.SC_OK) {  
                HttpEntity valueEntity = response.getEntity();
                String content = EntityUtils.toString(valueEntity);
                //jsonObject = JSONObject.fromObject(content);
                return content;
            }
        } catch(Exception e) {
            throw new BusinessException(e.getMessage(),e);
        }finally{
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
     /** 
     * http的post请求（用于请求json格式的参数） 
     * @param url 
     * @param params 
     * @return 
     * @throws BusinessException 
     */  
    public static String doHttpPost(String url, String jsonStr) throws BusinessException {  
        try {
            httpClient = HttpClients.createDefault();
          
            // 创建httpPost
            HttpPost httpPost = new HttpPost(url);     
            httpPost.setHeader("Accept", "application/json");   
              
            StringEntity entity = new StringEntity(jsonStr, charSet);  
            entity.setContentType("text/json");
            entity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(entity);          
            //发送post请求
            response = httpClient.execute(httpPost);  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                HttpEntity responseEntity = response.getEntity();  
                String jsonString = EntityUtils.toString(responseEntity);  
                return jsonString;  
            }
        }catch(Exception e) {
            throw new BusinessException(e.getMessage(),e);
        }finally {
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }  
        return null;  
    }  
    
    /**
     * http的Get请求
     * @param url
     * @param param
     * @return
     * @throws BusinessException 
     */
    public static String doHttpGet(String url,Map<String,String> param) throws BusinessException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            if(param != null && !param.isEmpty()) {
                //参数集合
                List<NameValuePair> getParams = new ArrayList<NameValuePair>();
                for(Map.Entry<String, String> entry:param.entrySet()){
                    getParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                url +="?"+EntityUtils.toString(new UrlEncodedFormEntity(getParams), "UTF-8");
            }
            //发送gey请求
            HttpGet httpGet = new HttpGet(url);  
            response = httpclient.execute(httpGet);  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                return EntityUtils.toString(response.getEntity());  
            } 
        }catch(Exception e) {
            throw new BusinessException(e.getMessage(),e);
        }finally{
            if(httpclient != null){
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws BusinessException {
    	HttpClientUtil.doHttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code", null);
    }
    
}