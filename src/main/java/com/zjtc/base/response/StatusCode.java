package com.zjtc.base.response;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;

/**
 * @author yuchen
 * @date 2019/11/16
 */
public class StatusCode {


  private static Map<Integer, StatusCode> statusCodes = new HashMap<>();

  static {
    statusCodes.put(200, new StatusCode(200, "操作成功"));
    statusCodes.put(500, new StatusCode(500, "操作失败"));
    InputStreamReader in = null;
    try {
      ClassPathResource resource = new ClassPathResource("/errorCode.properties");
      in = new InputStreamReader(resource.getInputStream(), "GBK");
      Properties properties = new Properties();
      properties.load(in);
      Set<String> propertyNames = properties.stringPropertyNames();
      Iterator<String> iterator = propertyNames.iterator();
      while (iterator.hasNext()) {
        String code = iterator.next();
        Integer intCode = Integer.valueOf(code);
        statusCodes.put(intCode, new StatusCode(intCode, properties.getProperty(code)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private int code;

  private String message;

  public static StatusCode getStatusCode(int code) {
    return statusCodes.get(code);
  }

  private StatusCode() {

  }

  public StatusCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
