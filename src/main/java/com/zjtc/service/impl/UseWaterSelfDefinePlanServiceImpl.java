package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.TWUseWaterSelfDefinePlanMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.service.UseWaterPlanAddService;
import com.zjtc.service.UseWaterPlanService;
import com.zjtc.service.UseWaterSelfDefinePlanService;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TWUseWaterSelfDefinePlan的服务接口的实现类 用水自平计划表
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/4
 */
@Service
@Slf4j
public class UseWaterSelfDefinePlanServiceImpl extends
    ServiceImpl<TWUseWaterSelfDefinePlanMapper, UseWaterSelfDefinePlan> implements
    UseWaterSelfDefinePlanService {

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;

  @Autowired
  private UseWaterPlanService useWaterPlanService;

  @Override
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);
//    页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");
//    单位名称
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName");
    }
//    用户类型(截取的是3-4位)
    String userType = "";
    if (null != jsonObject.getString("userType")) {
      userType = jsonObject.getString("userType");
    }
//    编号开头
    String areaCode = "";
    if (null != jsonObject.getString("areaCode")) {
      areaCode = jsonObject.getString("areaCode");
    }
//    开始年份
    Integer beginYear = null;
    if (null != jsonObject.getInteger("beginYear")) {
      beginYear = jsonObject.getInteger("beginYear");
    }
//    结束年份
    Integer endYear = null;
    if (null != jsonObject.getInteger("endYear")) {
      endYear = jsonObject.getInteger("endYear");
    }

//    单位编号
    String unitCode = "";
    if (null != jsonObject.getString("unitCode")) {
      unitCode = jsonObject.getString("unitCode");
    }
//    排序
    String rank = "";
    if (null != jsonObject.getString("rank")) {
      rank = jsonObject.getString("rank");
    }
    if (StringUtils.isBlank(nodeCode)) {
      response.recordError("系统异常");
      return response;
    }
    //    是否审核(0:未审核,1:已审核)
    String auditStatus = "";
    if (null != jsonObject.getString("auditStatus")) {
      auditStatus = jsonObject.getString("auditStatus");
    }
    //    是否执行(0:未执行,1:已执行)
    String executed = "";
    if (null != jsonObject.getString("executed")) {
      executed = jsonObject.getString("executed");
    }
//    总条数
    Integer total = this.baseMapper
        .selectCount(unitName, userType, areaCode, beginYear,
            endYear, executed, unitCode, nodeCode, auditStatus);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<UseWaterSelfDefinePlan> waterSelfDefinePlans = this.baseMapper
        .queryList(currPage, pageSize, unitName, userType, areaCode, beginYear,
            endYear, executed, unitCode, nodeCode, auditStatus);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", waterSelfDefinePlans);
    response.setCode(200);
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse audit(String id, String auditPerson, String auditPersonId,
      String auditStatus, String auditResult) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(id) || StringUtils.isBlank(auditPerson) || StringUtils
        .isBlank(auditPersonId) || StringUtils.isBlank(auditStatus)) {
      response.recordError("审核失败");
    }
    UseWaterSelfDefinePlan waterSelfDefinePlan = new UseWaterSelfDefinePlan();
    waterSelfDefinePlan.setId(id);
//    审核状态(1:审核不通过,2:审核通过)
    waterSelfDefinePlan.setAuditStatus(auditStatus);
//    审核时间
    waterSelfDefinePlan.setAuditTime(new Date());
//    审核人
    waterSelfDefinePlan.setAuditPerson(auditPerson);
//    审核人id
    waterSelfDefinePlan.setAuditPersonId(auditPersonId);
//    审核结果
    waterSelfDefinePlan.setAuditResult(auditResult);
    int integer = this.baseMapper.updateById(waterSelfDefinePlan);
    if (integer > 0) {
      response.setCode(200);
      response.setMessage("审核成功");
      return response;
    }
    response.recordError("审核失败");
    return response;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public ApiResponse execute(List<String> ids, String executor, String executorId,
      String codeNode) {
    ApiResponse response = new ApiResponse();
    if (ids.isEmpty() || StringUtils.isBlank(executor) || StringUtils.isBlank(executorId)
        || StringUtils.isBlank(codeNode)) {
      response.recordError("系统异常");
    }
//    自平表响应结果
    int zp = 0;
//    计划用水调整响应结果
    boolean planAdd = false;
//    用水计划表数据响应结果
    int water = 0;
//>>>>>>>>> 第一步:更新自平表数据<<<<<<<<<
    for (String id : ids) {
//      如果是未审核的数据不能被执行
      UseWaterSelfDefinePlan auditStatus = this.baseMapper.selectAuditStatus(id);
      UseWaterSelfDefinePlan executed = this.baseMapper.selectExecuted(id);
      if (null != auditStatus) {
        response.recordError("单位名称为『" + auditStatus.getUnitName() + "』的数据未审核或审核不通过,不能执行");
        return response;
//      如果是已经执行过的数据不能再被执行
      } else if (null != executed) {
        response.recordError("单位名称为『" + executed.getUnitName() + "』的数据已经被执行,不能再被执行");
        return response;
      }
    }
    for (String id : ids) {
      // ====  需要修改自平数据表的字段======
//        是否执行(0:否,1:是)
//        执行时间
//        执行人
//        执行人Id
//        执行结果(0:否,1:是)
      zp = this.baseMapper.updateExecuteData(id, executor, executorId, new Date());
    }
//>>>>>>>>>> 第二步:新增用水计划调整表<<<<<<<<<<
    EntityWrapper<UseWaterSelfDefinePlan> wrapper = new EntityWrapper<>();
    List<UseWaterSelfDefinePlan> useWaterSelfDefinePlans = null;
//    根据执行id查询出对应的数据
    wrapper.in("id", ids);
    useWaterSelfDefinePlans = this.baseMapper.selectList(wrapper);
    UseWaterPlanAdd waterPlanAdd = new UseWaterPlanAdd();
//    附件id
    String fileId;
    for (UseWaterSelfDefinePlan useWaterSelfDefinePlan : useWaterSelfDefinePlans) {
//      主键
      waterPlanAdd.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//      节点编码
      waterPlanAdd.setNodeCode(codeNode);
//      单位id
      waterPlanAdd.setUseWaterUnitId(useWaterSelfDefinePlan.getUseWaterUnitId());
//      单位名称
      waterPlanAdd.setUnitName(useWaterSelfDefinePlan.getUnitName());
//      单位编号
      waterPlanAdd.setUnitCode(useWaterSelfDefinePlan.getUnitCode());
//      水表档案号
      waterPlanAdd.setWaterMeterCode(useWaterSelfDefinePlan.getWaterMeterCode());
//      编制年度
      waterPlanAdd.setPlanYear(useWaterSelfDefinePlan.getPlanYear());
//      本年计划（当前年计划）
      waterPlanAdd.setCurYearPlan(useWaterSelfDefinePlan.getCurYearPlan());
//      第一季度计划
      waterPlanAdd.setFirstQuarter(useWaterSelfDefinePlan.getFirstQuarter());
//      第二季度计划
      waterPlanAdd.setSecondQuarter(useWaterSelfDefinePlan.getSecondQuarter());
//      第三季度计划
      waterPlanAdd.setThirdQuarter(useWaterSelfDefinePlan.getThirdQuarter());
//      第四季度计划
      waterPlanAdd.setFourthQuarter(useWaterSelfDefinePlan.getFourthQuarter());
//      调整类型(数据字典值(增加计划))
      waterPlanAdd.setChangeType(useWaterSelfDefinePlan.getChangeType());
//      调整季度
      waterPlanAdd.setChangeQuarter("1、2、3、4季度");
//      创建时间
      waterPlanAdd.setCreateTime(new Date());
//      创建者
      waterPlanAdd.setCreater(executor);
//      创建者id
      waterPlanAdd.setCreaterId(executorId);
//      查询出当前id对应的附件id
      fileId = this.baseMapper.selectFileId(useWaterSelfDefinePlan.getId());
//      审批申请附件id
      waterPlanAdd.setAuditFileId(fileId);
//      状态(1:草稿、2:审核、3:累加)
      waterPlanAdd.setStatus("2");
      planAdd = useWaterPlanAddService.insert(waterPlanAdd);
//>>>>>>>>第三步:更新用水计划表数据<<<<<<<<<<
//    匹配用水计划表里的那条数据
      List<UseWaterPlan> waterPlan = this.baseMapper.selectWaterPlan(
          useWaterSelfDefinePlan.getNodeCode(),
          useWaterSelfDefinePlan.getUseWaterUnitId(),
          useWaterSelfDefinePlan.getUnitName(),
          useWaterSelfDefinePlan.getUnitCode(),
          useWaterSelfDefinePlan.getPlanYear());
      System.out.println("匹配到的用水计划表数据:" + waterPlan);
//      修改用水计划表季度水量
      for (UseWaterPlan useWaterPlan : waterPlan) {
        water = this.baseMapper
            .updateUseWaterPlanWater(useWaterPlan.getId(), useWaterSelfDefinePlan.getFirstQuarter(),
                useWaterSelfDefinePlan.getSecondQuarter(), useWaterSelfDefinePlan.getThirdQuarter(),
                useWaterSelfDefinePlan.getFourthQuarter(), executorId, new Date());
      }
    }
    if (zp > 0 && planAdd && water > 0) {
      response.setCode(200);
      return response;
    } else {
      response.recordError("操作失败");
      return response;
    }
  }
}