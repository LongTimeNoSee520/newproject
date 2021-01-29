package com.zjtc.service.impl;

import com.zjtc.base.util.CommonUtil;
import com.zjtc.base.util.JxlsUtils;
import com.zjtc.service.CommonService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 公共接口
 * @author lianghao
 * @date 2021/01/04
 */
@Service
public class CommonServiceImpl implements CommonService{

  /**
   * 附件上传盘符
   */
  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  /**
   * 附件上传目录
   */
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Override
  public void export(String fileName, String template, HttpServletRequest request,
      HttpServletResponse response, Map<String, Object> data) {
    try {
      String saveFilePath =
          fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName;
      //模板 流(此方式读取文件流必须将文件放到resource目录下)
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream(template);
      OutputStream outputStream = new FileOutputStream(saveFilePath);
      JxlsUtils.exportExcel(inputStream, outputStream, data);
      outputStream.close();
//      上面步骤是将导出文件写入服务器磁盘，下面操作是将文件写入到客服端，并将服务器上的文件删除
      File getPath = new File(saveFilePath);
      boolean downloadSuccess = CommonUtil.writeBytes(getPath, fileName, request, response);
      //下载完毕删除文件
      boolean canDelFile = downloadSuccess && (getPath.exists() && getPath.isFile());
      if (canDelFile) {
        getPath.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}