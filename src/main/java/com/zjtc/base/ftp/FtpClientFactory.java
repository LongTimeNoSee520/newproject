package com.zjtc.base.ftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * FtpClientFactory工厂类
 * @author yuyantian
 * @date 2021/4/7
 * @description
 */
@Component
public class FtpClientFactory implements PooledObjectFactory<FTPClient> {

  /**
   * FTP主机服务器
   */
  @Value("${Ftp.ip}")
  private String ftpHost;
  /**
   * FTP端口
   */
  @Value("${Ftp.port}")
  private int ftpPort;
  /**
   * FTP登录用户名
   */
  @Value("${Ftp.username}")
  private String ftpUserName;
  /**
   * 登录密码
   */
  @Value("${Ftp.password}")
  private String ftpPassword;

  /**
   * 附件上传目录
   */
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;


  //创建连接到池中
  @Override
  public PooledObject<FTPClient> makeObject() {
    FTPClient ftpClient = new FTPClient();//创建客户端实例
    return new DefaultPooledObject<>(ftpClient);
  }
  //销毁连接，当连接池空闲数量达到上限时，调用此方法销毁连接
  @Override
  public void destroyObject(PooledObject<FTPClient> pooledObject) {
    FTPClient ftpClient = pooledObject.getObject();
    try {
      ftpClient.logout();
      if (ftpClient.isConnected()) {
        ftpClient.disconnect();
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not disconnect from server.", e);
    }
  }
  //链接状态检查
  @Override
  public boolean validateObject(PooledObject<FTPClient> pooledObject) {
    FTPClient ftpClient = pooledObject.getObject();
    try {
      return ftpClient.sendNoOp();
    } catch (IOException e) {
      return false;
    }
  }
  //初始化连接
  @Override
  public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    FTPClient ftpClient = pooledObject.getObject();
    ftpClient.connect(ftpHost,ftpPort);
    ftpClient.login(ftpUserName, ftpPassword);
    ftpClient.setControlEncoding("utf-8");
    ftpClient.changeWorkingDirectory("/");
    ftpClient.enterLocalPassiveMode();
    ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置上传文件类型为二进制，否则将无法打开文件
  }
  //钝化连接，使链接变为可用状态
  @Override
  public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    FTPClient ftpClient = pooledObject.getObject();
    try {
      ftpClient.changeWorkingDirectory("/");
      ftpClient.logout();
      if (ftpClient.isConnected()) {
        ftpClient.disconnect();
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not disconnect from server.", e);
    }
  }
}
