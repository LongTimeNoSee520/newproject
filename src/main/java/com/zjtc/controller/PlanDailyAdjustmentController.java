package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.User;
import com.zjtc.model.vo.PrintVO;
import com.zjtc.service.PlanDailyAdjustmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lianghao
 * @date 2021/01/04
 */

@RestController
@RequestMapping("/dailyAdjustment")
@Api(tags = "用水计划日常调整")
@Slf4j
public class PlanDailyAdjustmentController {

  @Autowired
  private PlanDailyAdjustmentService planDailyAdjustmentService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页,数字类型\",\n"
          + " \"size\":\"每页条数,数字类型\" ,\n"
          + " \"unitCode\":\"单位编号\",\n"
          + " \"unitName\":\"单位名称\",\n"
          + " \"waterMeterCode\":\"水表档案号\",\n"
          + " \"planYearStart\":\"计划年度起始\",\n"
          + " \"planYearEnd\":\"计划年度截止\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        Map<String, Object> result = planDailyAdjustmentService.queryPage(user, jsonObject);
        response.setData(result);
      } catch (Exception e) {
        log.error("分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "queryList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "列表查询")
  public ApiResponse queryList(@RequestHeader("token") String token,
      @ApiParam("{\"unitCode\":\"单位编号\",\n"
          + " \"unitName\":\"单位名称\",\n"
          + " \"waterMeterCode\":\"水表档案号\",\n"
          + " \"yearStart\":\"计划年度起始 数字\",\n"
          + " \"yearEnd\":\"计划年度截止 数字\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("列表查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        response.setData( planDailyAdjustmentService.queryList(user, jsonObject));
      } catch (Exception e) {
        log.error("列表查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "editRemarks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "修改备注")
  public ApiResponse editRemarks(@RequestHeader("token") String token,
      @ApiParam("{\"id\":\"id\",\n"
          + "\"editType\":\"修改备注的类型,0代表修改主数据备注，1代表修改展开列表的调整数据的备注\",\n"
          + " \"remarks\":\"备注\" \n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("修改备注 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        //User user = jwtUtil.getUserByToken(token);
        String  id= jsonObject.getString("id");
        String editType = jsonObject.getString("editType");
        String remarks = jsonObject.getString("remarks");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(editType)){
          response.recordError("id和修改备注的类型不能为空");
        }else {
          response = planDailyAdjustmentService.editRemarks(id, editType, remarks);
          response.setCode(200);
        }
      } catch (Exception e) {
        log.error("修改备注失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("修改备注参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "numberAfterCalculation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "获取计算后的增加水量")
  public ApiResponse numberAfterCalculation(@RequestHeader("token") String token,
      @ApiParam("{\"firstQuarter\": 第一季度水量 数字,\n"
          + "\"secondQuarter\": 第二季度水量 数字,\n"
          + "\"thirdQuarter\": 第三季度水量 数字,\n"
          + "\"fourthQuarter\": 第四季度水量 数字,\n"
          + "\"firstWater\": 第一水量 数字,\n"
          + "\"secondWater\":第二水量 数字,\n"
          + "\"addWay\": \"加计划的方式：1-平均，2-最高\",\n"
          + "\"quarter\": 选择的季度 数字,\n"
          + "\"year\": \"是否勾选年计划 0否，1是\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("获取计算后的增加水量 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        //User user = jwtUtil.getUserByToken(token);
          response = planDailyAdjustmentService.numberAfterCalculation(jsonObject);
      } catch (Exception e) {
        log.error("获取计算后的增加水量失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("获取计算后的增加水量参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "adjustPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "计划调整")
  public ApiResponse adjustPlan(@RequestHeader("token") String token,
      @ApiParam("{\"id\": \"id\",\n"
          + "\"firstQuarter\":  第一季度水量 数字,\n"
          + "\"secondQuarter\": 第二季度水量 数字,\n"
          + "\"thirdQuarter\": 第三季度水量 数字,\n"
          + "\"fourthQuarter\": 第四季度水量 数字,\n"
          + "\"firstWater\": 第一水量 数字,\n"
          + "\"secondWater\":第二水量 数字,\n"
          + "\"addNumber\": 增加的水量，数字,\n"
          + "\"quarter\": 选择的季度 数字,\n"
          + "\"year\": \"是否在年计划上增加 0否，1是\",\n"
          + "\"remarks\": \"备注\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("计划调整 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        response = planDailyAdjustmentService.adjustPlan(user,jsonObject);
      } catch (Exception e) {
        log.error("计划调整失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("计划调整参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "editPlanAdd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "行内编辑修改调整计划的4个季度水量")
  public ApiResponse editPlanAdd(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"useWaterPlanAdds\": [{\n"
          + "\"id\": \"id\",\n"
          + "\"curYearPlan\": \"年计划水量 数字\",\n"
          + "\"firstQuarter\": \"第一季度水量 数字\",\n"
          + "\"secondQuarter\": \"第二季度水量 数字\",\n"
          + "\"thirdQuarter\": \"第三季度水量 数字\",\n"
          + "\"fourthQuarter\": \"第四季度水量 数字\",\n"
          + "\"waterMeterCode\": \"水表档案号\"\n"
          + "}]\n"
          + "}") @RequestBody JSONObject jsonObject ) {
    log.info("计划调整行内编辑 ==== 参数{" +jsonObject.toJSONString()+ "}");
    ApiResponse response = new ApiResponse();
      try {
        List<UseWaterPlanAdd> useWaterPlanAdds = jsonObject.getJSONArray("useWaterPlanAdds").toJavaList(UseWaterPlanAdd.class);
        if (null == useWaterPlanAdds || useWaterPlanAdds.size()==0 ) {
          response.recordError("参数转换失败或者参数为空");
        }else {
          User user = jwtUtil.getUserByToken(token);
          response = planDailyAdjustmentService.editPlanAdd(user,useWaterPlanAdds);
        }
      } catch (Exception e) {
        log.error("修改调整计划失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    return response;
  }
  @RequestMapping(value = "accumulate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "累加")
  public ApiResponse accumulate(@RequestHeader("token") String token,
      @ApiParam("{\"id\": \"id\",\n"
          + " \"unitCode\":\"单位编号\",\n"
          + " \"nodeCode\":\"节点编码\",\n"
          + " \"planYear\":年份 数字,\n"
          + "\"curYearPlan\": 年计划 数字,\n"
          + "\"firstQuarter\": 第一季度水量 数字,\n"
          + "\"secondQuarter\": 第二季度水量 数字,\n"
          + "\"thirdQuarter\": 第三季度水量 数字,\n"
          + "\"fourthQuarter\": 第四季度水量 数字\n"
          + "}") @RequestBody UseWaterPlanAdd useWaterPlanAdd) {
    log.info("累加 ==== 参数{" + useWaterPlanAdd.toString()+ "}");
    ApiResponse response = new ApiResponse();
    if (null != useWaterPlanAdd) {
      try {
         User user = jwtUtil.getUserByToken(token);
         response = planDailyAdjustmentService.accumulate(user,useWaterPlanAdd);
      } catch (Exception e) {
        log.error("累加失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("累加参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "signPrinted", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "打印后标记修改打印状态")
  public ApiResponse signPrinted(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"printList\": 打印列表: [{\n"
          + "\"id\": \"主数据id或者展开数据id\",\n"
          + "\"printType\": \"打印的类型0为主数据，1为展开列表的调整数据\"\n"
          + "}]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("修改打印状态==== 参数{" + jsonObject.toJSONString()+ "}");
    ApiResponse response = new ApiResponse();
    List<PrintVO> printList = jsonObject.getJSONArray("printList").toJavaList(PrintVO.class);
    if (null != printList) {
      try {
       // User user = jwtUtil.getUserByToken(token);
        boolean result = planDailyAdjustmentService.signPrinted(printList);
        if (!result){
          response.recordError("修改打印状态失败");
        }
      } catch (Exception e) {
        log.error("修改打印状态失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("累加参数不能为空");
    }
    return response;
  }



  @RequestMapping(value = "queryMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "发起办结单时输入单位编码后回填数据")
  public ApiResponse queryMessage(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"unitCode\": \"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询单位信息和一、二水量==== 参数{" + jsonObject.toJSONString()+ "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        String unitCode = jsonObject.getString("unitCode");
        List<Map<String,Object>> result = planDailyAdjustmentService.queryMessage(user,unitCode);
        response.setData(result);
      } catch (Exception e) {
        log.error("查询单位信息和一、二水量失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("查询单位信息和一、二水量参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "initiateSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "发起办结单")
  public ApiResponse initiateSettlement(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"unitCode\": \"单位编号\",\n"
          + "\"unitName\": \"单位名称\",\n"
          + "\"waterMeterCode\": \"水表档案号\",\n"
          + "\"planYear\": \"年份 数字\",\n"
          + "\"paperType\": \"申报类型(数据字典code为changeType)\",\n"
          + "\"firstQuarter\": \"一季度水量 数字(调整计划时才填)\",\n"
          + "\"secondQuarter\": \"二季度水量 数字(调整计划时才填)\",\n"
          + "\"thirdQuarter\": \"三季度水量 数字(调整计划时才填)\",\n"
          + "\"fourthQuarter\": \"四季度水量 数字(调整计划时才填)\",\n"
          + "\"firstWater\": \"第一水量(增加计划时才填)\",\n"
          + "\"secondWater\": \"第二水量(增加计划时才填)\",\n"
          + "\"quarter\": \"季度(增加计划时才填)\",\n"
          + "\"opinions\": \"具体意见\",\n"
          + "\"auditFiles\"(审批申请附件列表,没有时传[]): [\n"
          + "     { \"id\":\"附件上传时返回的id\",\n"
          + "    \"deleted\":\"上传文件后是否进行了删除操作0否1是\"\n"
          + "    }] ,\n"
          + "\"waterProofFiles\"(近2月水量凭证附件列表,没有时传[]): [\n"
          + "     { \"id\":\"附件上传时返回的id\",\n"
          + "    \"deleted\":\"上传文件后是否进行了删除操作0否1是\"\n"
          + "    }] ,\n"
          + "\"otherFiles\"(其他证明材料列表,没有时传[]): [\n"
          + "     { \"id\":\"附件上传时返回的id\",\n"
          + "    \"deleted\":\"上传文件后是否进行了删除操作0否1是\"\n"
          + "    }] ,\n"
          + "\"auditorName\": \"下一环节审核人员名称\",\n"
          + "\"auditorId\": \"下一环节审核人员id\",\n"
          + "\"businessJson\": \"关联业务json数据(待办相关)\",\n"
          + "\"detailConfig\": \"详情配置文件(待办相关)\",\n"
          + "\"nextNodeId\": \"下一审核环节id\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("发起办结单==== 参数{" + jsonObject.toJSONString()+ "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        jsonObject.put("dataSources", "2");
        response = planDailyAdjustmentService.initiateSettlement(user, jsonObject);
      } catch (Exception e) {
        log.error("发起办结单失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("发起办结单参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "导出最新计划")
  public ApiResponse export(@RequestHeader("token") String token,
      @ApiParam("{\"planYear\":\"年份,数字类型\"\n"
          + "}") @RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    log.debug("导出最新计划导出，参数param==={" + jsonObject.toString() + "}");
    try {
      Integer planYear = jsonObject.getInteger("planYear");
      User user =  jwtUtil.getUserByToken(token);
      apiResponse = planDailyAdjustmentService.export(user,planYear,request,response);
    } catch (Exception e) {
      log.error("导出最新计划失败,errMsg==={}" + e.getMessage());
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "查询办结单发起时审核流程的角色下人员信息")
  @RequestMapping(value = "secondAuditRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse secondAuditRole(@RequestHeader("token") String token,
      @ApiParam("{\"changeType\":\"调整类型：四个季度间的调整为0,增加计划为1 \"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    String  changeType = jsonObject.getString("changeType");
    if (StringUtils.isNotBlank(changeType)) {
      try {
        List<Map<String, Object>> result = planDailyAdjustmentService.secondAuditRole(user,changeType);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError("查询失败");
      }
    } else {
      apiResponse.recordError("调整类型不能为空");
    }
    return apiResponse;
  }
}
