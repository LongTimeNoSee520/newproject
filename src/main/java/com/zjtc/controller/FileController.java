package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2020/12/07
 */
@Api(tags = "附件")
@RestController
@RequestMapping("file/")
@Slf4j
public class FileController {


  @Value("${server.servlet-path}")
  private String contextPath;

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

  /**
   * 上下文
   */
  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private FileService fileService;

  @RequestMapping(value = "upload", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation("上传")
  public ApiResponse uploadFile(@Param("file") MultipartFile file,
      @RequestHeader("token") String token) {
    log.info("附件，上传 ==== 参数{" + file + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (!file.isEmpty()) {
      try {
        User user = jwtUtil.getUserByToken(token);
        if (null == user) {
          apiResponse.recordError(500);
        }
        //获取文件保存路径
       // String path = fileUploadRootPath + File.separator + fileUploadPath + File.separator;
        /**调用上传接口*/
        String fileName = fileService.uploadFile(file);
        if (StringUtils.isNotBlank(fileName)) {
          com.zjtc.model.File sysAttrFile = new com.zjtc.model.File();
          sysAttrFile.setId(UUID.randomUUID().toString().replace("-", ""));
          sysAttrFile.setFileName(file.getOriginalFilename());
          sysAttrFile.setFilePath(fileUploadPath + fileName);//非全路径（排除跟目录）
          sysAttrFile.setCreateTime(new Date());
          sysAttrFile.setCreaterId(user.getId());
          sysAttrFile.setDeleted("0");
          /**Todo: node从token中获取*/
          sysAttrFile.setNodeCode(user.getNodeCode());
          /**附件表新增数据*/
          fileService.save(sysAttrFile);
          Map<String, Object> result = new HashMap<>();
          String uploadPath =preViewRealPath + contextPath +fileUploadPath + fileName;
          sysAttrFile.setUrl(uploadPath);
          result.put("file", sysAttrFile);
          result.put("uploadPath", uploadPath);
          apiResponse.setData(result);
          apiResponse.setCode(200);
        }
      } catch (Exception e) {
        log.error("附件上传失败,errMsg==={" + e.getMessage() + "}");
        e.printStackTrace();
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @RequestMapping(value = "uploadFiles", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation("上传")
  public ApiResponse uploadFiles(@Param("files") MultipartFile [] files,
      @RequestHeader("token") String token) {
    log.info("附件，上传 ==== 参数{" + files + "}");
    ApiResponse apiResponse = new ApiResponse();
    List<com.zjtc.model.File> result=new ArrayList<>();
    if (files.length>0) {
      try {
        User user = jwtUtil.getUserByToken(token);
        if (null == user) {
          apiResponse.recordError(500);
        }
        //获取文件保存路径
        String path = fileUploadRootPath + File.separator + fileUploadPath + File.separator;
        /**调用上传接口*/
        for (MultipartFile item : files) {
          if (item.isEmpty()) {
            log.error("");
            continue;
          }
            String fileName = fileService.uploadFile(item);
        if (StringUtils.isNotBlank(fileName)) {
          com.zjtc.model.File sysAttrFile = new com.zjtc.model.File();
          sysAttrFile.setId(UUID.randomUUID().toString().replace("-", ""));
          sysAttrFile.setFileName(item.getOriginalFilename());
          sysAttrFile.setFilePath(fileUploadPath + File.separator + fileName);//非全路径（排除跟目录）
          sysAttrFile.setCreateTime(new Date());
          sysAttrFile.setCreaterId(user.getId());
          sysAttrFile.setDeleted("0");
          /**Todo: node从token中获取*/
          sysAttrFile.setNodeCode(user.getNodeCode());
          /**附件表新增数据*/
          fileService.save(sysAttrFile);
          String uploadPath = preViewRealPath + contextPath + fileUploadPath + fileName;
          sysAttrFile.setUrl(uploadPath);
          result.add(sysAttrFile);
        }
          apiResponse.setData(result);
        }
      } catch (Exception e) {
        log.error("附件上传失败,errMsg==={" + e.getMessage() + "}");

      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }


  @RequestMapping(value = "getUploadPath", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation("附件上传上下文")
  public ApiResponse getUploadPath() {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setCode(200);
    apiResponse.setData(preViewRealPath + contextPath + "/");
    return apiResponse;
  }

  @RequestMapping(value = "preView", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation("文件预览路径")
  public ApiResponse preView(@ApiParam("{\"filePath\":\"文件保存地址\"}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setData( preViewRealPath + contextPath + "/"+jsonObject.getString("filePath"));
    return apiResponse;
  }

  @RequestMapping(value = "download", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation("附件下载")
  public ApiResponse download(@ApiParam("{\"filePath\":\"文件保存地址\"}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token, HttpServletResponse resp) {
    ApiResponse apiResponse = new ApiResponse();
    try{
      fileService.download(jsonObject.getString("filePath"), resp);
    }catch (Exception e){
      log.error("文件下载失败");
      apiResponse.recordError(500);
    }

    return apiResponse;
  }

}
