package com.zjtc.base.util;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http工具类
 *
 * @author yuchen
 * @date 2020/05/14
 */
public class HttpUtil {


  /**
   * Get请求
   */
  public static String doGet(String url) {
    String result = null;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpClientContext context = HttpClientContext.create();
    try {
      HttpGet httpget = new HttpGet(url);
      System.out.println("executing request " + httpget.getURI());
      // 执行get请求.
      CloseableHttpResponse response = httpclient.execute(httpget, context);
      // 获取响应实体
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        result = EntityUtils.toString(entity, "UTF-8");
      }

    } catch (IOException e) {
      System.out.println("http请求失败，url：" + url);
      e.printStackTrace();
    } finally {
      // 关闭连接,释放资源
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * Post请求
   */
  public static String doPost(String url, String params) throws Exception {

    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);// 创建httpPost
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-Type", "application/json");
    String charSet = "UTF-8";
    StringEntity entity = new StringEntity(params, charSet);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = null;
    HttpClientContext context = HttpClientContext.create();
    try {

      response = httpclient.execute(httpPost, context);
      StatusLine status = response.getStatusLine();
      int state = status.getStatusCode();
      if (state == HttpStatus.SC_OK) {
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity);
        return jsonString;
      } else {
        //do log
      }
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * post请求
   *
   * @return json格式对象
   */
  public static JSONObject sendPostRequest(String url) {
    JSONObject resultJson = null;
    StringBuffer stringBuffer = new StringBuffer("");
    try {
      URL postUrl = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) postUrl
          .openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestMethod("POST");
      connection.setUseCaches(false);
      connection.setInstanceFollowRedirects(true);
      connection.setRequestProperty("Content-Type", "application/json");
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuffer.append(line);
      }
      reader.close();
      resultJson = JSONObject.parseObject(stringBuffer.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultJson;
  }


  /**
   * Post请求 带token
   */
  public static String doPost(String token,String url, String params) throws Exception {

    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);// 创建httpPost
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("token", token);
    String charSet = "UTF-8";
    StringEntity entity = new StringEntity(params, charSet);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = null;
    HttpClientContext context = HttpClientContext.create();
    try {

      response = httpclient.execute(httpPost, context);
      StatusLine status = response.getStatusLine();
      int state = status.getStatusCode();
      if (state == HttpStatus.SC_OK) {
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity);
        return jsonString;
      } else {
        //do log
      }
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static String doPostPseron(String url, String params) throws Exception {

    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);// 创建httpPost
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-Type", "application/json");
    String charSet = "UTF-8";
    StringEntity entity = new StringEntity(params, charSet);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = null;
    HttpClientContext context = HttpClientContext.create();
    try {

      response = httpclient.execute(httpPost, context);
      StatusLine status = response.getStatusLine();
      int state = status.getStatusCode();
      if (state == HttpStatus.SC_OK) {
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity);
        return jsonString;
      } else {
        //do log
      }
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}