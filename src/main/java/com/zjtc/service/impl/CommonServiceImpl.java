package com.zjtc.service.impl;

import com.zjtc.base.constant.SystemConstants;
import com.zjtc.base.util.CommonUtil;
import com.zjtc.base.util.JxlsUtils;
import com.zjtc.mapper.waterBiz.CommonMapper;
import com.zjtc.service.CommonService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * 公共接口
 * @author lianghao
 * @date 2021/01/04
 */
@Service
@Slf4j
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

  @Autowired
  private CommonMapper commonMapper;

  @Override
  public boolean export(String fileName, String template, HttpServletRequest request,
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
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**解析excel数据到bean*/
  @Override
  public  Map<String, List> importExcel(Map<String, List> beans, String xmlConfig,
      String fileRealPath,boolean isThrowException) throws Exception {
    File file = new File(fileRealPath);
    String fileName = file.getName();
    InputStream inputXLS = null;
    InputStream inputXML = null;
    try {
      // 文件流
      inputXLS = new FileInputStream(file);
      // xml配置文件流
      Resource resource = new ClassPathResource(xmlConfig);
      inputXML = getClass().getClassLoader()
          .getResourceAsStream(xmlConfig);//非静态方法可以用此方法获取xml配置文件流
      // 执行解析
      XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
      //按照xml中的配置将数据从文件中读入beens中对应key的value中
      XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
      if (readStatus.isStatusOK()) {
        log.debug("读取excel文件成功: 【{}】", fileName);
      }
    } catch (Exception e) {
      handleException(e, isThrowException);
    } finally {
      try {
        if (inputXLS != null) {
          inputXLS.close();
        }
        if (inputXML != null) {
          inputXML.close();
        }
      } catch (IOException e) {
        log.error("parse excel error : 【{}】", e.getMessage());
      }
    }
    return beans;
  }

  @Override
  public Map<String, List> importExcel(Map<String, List> beans, MultipartFile file,
      String xmlConfig, boolean isThrowException) throws Exception {
    String fileName = file.getName();
    InputStream inputXLS = file.getInputStream();
    InputStream inputXML = null;
    try {
      // xml配置文件流
      Resource resource = new ClassPathResource(xmlConfig);
      inputXML = getClass().getClassLoader()
          .getResourceAsStream(xmlConfig);//非静态方法可以用此方法获取xml配置文件流
      // 执行解析
      //注册转换器
      ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes( true );
      XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
      //按照xml中的配置将数据从文件中读入beens中对应key的value中
      XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
      if (readStatus.isStatusOK()) {
        log.debug("读取excel文件成功: 【{}】", fileName);
      }
    } catch (Exception e) {
      handleException(e, isThrowException);
    } finally {
      try {
        if (inputXLS != null) {
          inputXLS.close();
        }
        if (inputXML != null) {
          inputXML.close();
        }
      } catch (IOException e) {
        log.error("parse excel error : 【{}】", e.getMessage());
      }
    }
    return beans;
  }

  @Override
  public boolean updatePrintStatus(List<String> ids, String module) {
    boolean result = false;
    if (SystemConstants.DAILY_ADJUST_PRINT.equals(module)) {
      result = commonMapper.updatePrintStatus(ids, SystemConstants.DAILY_ADJUST_PRINT_TABLE);
    } else if (SystemConstants.PAY_PRINT.equals(module)) {
      result = commonMapper.updatePrintStatus(ids, SystemConstants.PAY_PRINT_TABLE);
    } else if (SystemConstants.ADJUST_AUDIT_PRINT.equals(module)) {
      result = commonMapper.updatePrintStatus(ids, SystemConstants.ADJUST_AUDIT_PRINT_TABLE);
    } else {
      result = false;
    }
    return result;
  }

  /**
   * 处理异常
   *
   * @param e: 异常
   * @param isThrowException: 是否抛出异常
   */
  private  void handleException(Exception e, boolean isThrowException)
      throws Exception {
    // ① 记录错误位置
    String errorCell = e.getMessage().split(" ")[3];
    // ② 记录错误原因
    String errorMsg = e.getCause().toString();
    String[] causeMsgArray = errorMsg.split(":");
    errorMsg = errorMsg.substring(causeMsgArray[0].length() + 2).split(":")[0];
    switch (errorMsg) {
      case "For input string":
        errorMsg = "格式不正确(时间)";
        break;
      case "Error converting from 'String' to 'Integer' For input string":
        errorMsg = "请填写数字类型";
        break;
      default:
        break;
    }
    errorMsg = "读取文件异常: " + errorCell + errorMsg;
    if (isThrowException) {
      throw new Exception(errorMsg);
    } else {
      log.error(errorMsg);
    }
  }
}