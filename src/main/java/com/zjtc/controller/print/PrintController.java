package com.zjtc.controller.print;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuyantian
 * @date 2021/5/24
 * @description
 */
@RestController
@RequestMapping("/print")
@Api(tags = "打印请求接口")
@Slf4j
public class PrintController {

  @RequestMapping(value = "buildLicense", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  @ApiOperation(value = "分页查询")
  public String buildLicense(@RequestParam("salt") String salt){

    String digest = "unknown";

    if(salt != null && salt.length() >= 16)
    {
      try {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(("fb333b6ceb4ba0c94ed16f40d6625658"+salt+today).getBytes());
        StringBuffer hexString = new StringBuffer();
        byte[] hash = md.digest();
        for (int i = 0; i < hash.length; i++) {
          if ((0xff & hash[i]) < 0x10) {
            hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
          } else {
            hexString.append(Integer.toHexString(0xFF & hash[i]));
          }
        }
        digest = hexString.toString();
      } catch (Exception e) {
      }
    }
    return String.format("{\"digeset\":\"%s\"}",digest);
  }
}
