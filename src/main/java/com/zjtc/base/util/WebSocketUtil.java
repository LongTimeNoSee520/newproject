package com.zjtc.base.util;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * webSocket工具类
 *
 * @author yuchen
 * @date 2021/3/11
 */
@Component
public class WebSocketUtil {

  /**
   * 节水平台待办webSocket地址
   */
  @Value("${webSocket.waterTodoUrl}")
  private String waterTodoUrl;
  /**
   * 节水平台消息webSocket地址
   */
  @Value("${webSocket.waterNewsUrl}")
  private String waterNewsUrl;

  /**
   * 公共服务平台消息webSocket地址
   */
  @Value("${webSocket.publicNewsUrl}")
  private String publicNewsUrl;

  /**
   * 推送待办消息
   *
   * @param nodeCode 节点编码
   * @param loginId 登录账号
   */
  public void pushWaterTodo(String nodeCode, String loginId) {
    String url = waterTodoUrl + "?nodeCode=" + nodeCode + "&loginId=" + loginId;
    doGet(url);
  }

  /**
   * 推送节水平台消息
   *
   * @param nodeCode 节点编码
   * @param loginId 登录账号
   */
  public void pushWaterNews(String nodeCode, String loginId) {
    String url = waterTodoUrl + "?nodeCode=" + nodeCode + "&loginId=" + loginId;
    doGet(url);
  }

  /**
   * 推送公众服务平台平台消息
   *
   * @param nodeCode 节点编码
   * @param unitCode 用水单位编号
   */
  public void pushPublicNews(String nodeCode, String unitCode) {
    String url = waterTodoUrl + "?nodeCode=" + nodeCode + "&loginId=" + unitCode;
    doGet(url);
  }


  /**
   * 发送get请求
   *
   * @param url 完整的url（带参数）
   */
  public void doGet(String url) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).get().build();
    final Call call = client.newCall(request);
    try {
      Response response = call.execute();
      System.out.println("OkHttpClient返回结果:" + response.body().toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
