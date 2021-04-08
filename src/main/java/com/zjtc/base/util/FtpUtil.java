package com.zjtc.base.util;

import com.zjtc.base.ftp.FtpPool;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2021/4/7
 * @description
 */
@Component
@Slf4j
public class FtpUtil {

  private final static Log logger = LogFactory.getLog(FtpUtil.class);
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

  @Autowired
  private ResourceLoader resourceLoader;
  @Autowired
  FtpPool pool;


  /**
   * 获取FTPClient对象
   */
  public String upload(MultipartFile file) throws Exception {
    FTPClient ftpClient = pool.getFTPClient();
    InputStream input = file.getInputStream();
    //开始进行文件上传
    String uploadFileName = file.getOriginalFilename();//上传附件名
    String suffix = uploadFileName
        .substring(uploadFileName.lastIndexOf("."), uploadFileName.length());//文件后缀
    //获取附件上传路径
    String fileName = UUID.randomUUID().toString().replace("-", "") + TimeUtil
        .formatTimeyyyyMMdd() + suffix;//文件名重新命名，避免重复
    try {
      //判断文件目录是否存在
      //切换至存储目录
      boolean flag = ftpClient.changeWorkingDirectory(fileUploadPath);
      if (!flag) {
        ftpClient.makeDirectory(fileUploadPath);
        ftpClient.changeWorkingDirectory(fileUploadPath);
      }
      boolean result = ftpClient.storeFile(fileName, input);//执行文件传输
      // System.out.println( ftpClient.getReplyCode());
      if (!result) {//上传失败
        log.error("附件上传失败");
        throw new RuntimeException("上传失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {//关闭资源
      input.close();
      pool.returnFTPClient(ftpClient);//归还资源
    }
    return fileName;
  }

  @Async("asyncExecutor")
  public void uploadFile(File file) throws Exception {
    FTPClient ftpClient = pool.getFTPClient();
    InputStream input = new FileInputStream(file);
    //开始进行文件上传
    String uploadFileName = file.getName();//上传附件名
    try {
      //判断文件目录是否存在
      //切换至存储目录
      boolean flag = ftpClient.changeWorkingDirectory(fileUploadPath);
      if (!flag) {
        ftpClient.makeDirectory(fileUploadPath);
        ftpClient.changeWorkingDirectory(fileUploadPath);
      }
      boolean result = ftpClient.storeFile(uploadFileName, input);//执行文件传输
      // System.out.println( ftpClient.getReplyCode());
      if (!result) {//上传失败
        log.error("附件上传失败："+uploadFileName);
        throw new RuntimeException("上传失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {//关闭资源
      input.close();
      pool.returnFTPClient(ftpClient);//归还资源
    }
  }
  /**
   * Description: 从FTP服务器下载文件
   *
   * @param fileName FTP服务器中的文件名
   * @param resp 响应客户的响应体
   * @Version1.0
   */
  public void download(String fileName, HttpServletResponse resp) throws IOException {
    FTPClient ftpClient = pool.getFTPClient();
    resp.setContentType("application/octet-stream");
    resp.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
    //将文件直接读取到响应体中
    OutputStream out = resp.getOutputStream();
    ftpClient.retrieveFile( fileName, out);
    out.flush();
    out.close();
    pool.returnFTPClient(ftpClient);
  }

}