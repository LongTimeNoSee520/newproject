package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.vo.OrgTreeVO;
import com.zjtc.service.ContactsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/4/28
 */
@Api(tags = "用水单位联系人")
@RestController
@RequestMapping("contacts/")
@Slf4j
public class ContactsController {

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private ContactsService contactsService;


  @ResponseBody
  @ApiOperation(value = "部门人员树-查询人员")
  @RequestMapping(value = "selectContacts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse selectContacts(@ApiParam("{\"nodeCode\":\"节点编码\"}")
  @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String nodeCode = jsonObject.getString("nodeCode");
    try {
      List<OrgTreeVO> list = contactsService.selectContacts(nodeCode);
      response.setData(list);
      return response;
    } catch (Exception e) {
      log.error("部门人员树-查询人员异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
