package com.zjtc.base.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description:FTP连接池 1.可以获取池中空闲链接 2.可以将链接归还到池中 3.当池中空闲链接不足时，可以创建链接
 *
 * @author yuyantian
 * @date 2021/4/7
 * @description
 */
@Component
public class FtpPool {

  FtpClientFactory factory;

  private final GenericObjectPool<FTPClient> internalPool;

  //初始化连接池
  public FtpPool(@Autowired FtpClientFactory factory) {
    this.factory = factory;
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(100);
    poolConfig.setMinIdle(5);
    poolConfig.setMaxIdle(10);
    poolConfig.setMaxWaitMillis(500);
    this.internalPool = new GenericObjectPool<FTPClient>(factory, poolConfig);
  }

  /**
   * 从连接池中取连接
   * @return
   */
  public FTPClient getFTPClient() {
    try {
      return internalPool.borrowObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将链接归还到连接池
   * @param ftpClient
   */
  public void returnFTPClient(FTPClient ftpClient) {
    try {
      internalPool.returnObject(ftpClient);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 销毁池子
   */
  public void destroy() {
    try {
      internalPool.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
