package com.zjtc.controller;

import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${rootConfig.rootPort}")
  private String port;

  @Value("${rootConfig.rootIp}")
  private String ip;

  @Value("${rootConfig.imgBrowsePath}")
  private String imgBrowsePath;

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
        String path = fileUploadRootPath + File.separator + fileUploadPath + File.separator;
        /**调用上传接口*/
        String fileName = fileService.uploadFile(file, path);
        if (StringUtils.isNotBlank(fileName)) {
          com.zjtc.model.File sysAttrFile = new com.zjtc.model.File();
          sysAttrFile.setId(UUID.randomUUID().toString().replace("-", ""));
          sysAttrFile.setFileName(file.getOriginalFilename());
          sysAttrFile.setFilePath(fileUploadPath + File.separator + fileName);//非全路径（排除跟目录）
          sysAttrFile.setCreateTime(new Date());
          sysAttrFile.setCreaterId(user.getId());
          sysAttrFile.setDeleted("0");
          /**Todo: node从token中获取*/
          //sysAttrFile.setNodeCode();
          /**附件表新增数据*/
          fileService.insert(sysAttrFile);
          Map<String, Object> result = new HashMap<>();
          String uploadPath = "http://" + ip + ":" + port + contextPath + imgBrowsePath + fileName;
          sysAttrFile.setUrl(uploadPath);
          result.put("file", sysAttrFile);
          result.put("uploadPath", uploadPath);
          apiResponse.setData(result);
          apiResponse.setCode(200);
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
    apiResponse.setData("http://" + ip + ":" + port + contextPath + "/");
    return apiResponse;
  }

}
