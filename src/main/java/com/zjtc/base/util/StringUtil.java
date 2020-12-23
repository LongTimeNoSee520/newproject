package com.zjtc.base.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 字符串操作工具类
 *
 * @author yuchen
 * @date 2020/8/5
 */
public class StringUtil {

  /**
   * 生成指定长度的随机字符串
   */
  public static String randomString(int length) {
    /* 随机串库 */
    String keyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuffer sb = new StringBuffer();
    int len = keyString.length();
    for (int i = 0; i < length; i++) {
      sb.append(keyString.charAt((int) Math.round(Math.random() * (len - 1))));
    }
    return sb.toString();
  }

  /**
   * 读取流
   * @param inStream
   * @return
   * @throws Exception
   */
  public static byte[] readStream(InputStream inStream) throws Exception {
    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = -1;
    while ((len = inStream.read(buffer)) != -1) {
      outSteam.write(buffer, 0, len);
    }
    outSteam.close();
    inStream.close();
    return outSteam.toByteArray();
  }
}
