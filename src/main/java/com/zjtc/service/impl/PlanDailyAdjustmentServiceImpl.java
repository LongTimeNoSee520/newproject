package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.PlanDailyAdjustmentMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.User;
import com.zjtc.model.vo.PlanDailyAdjustmentVO;
import com.zjtc.model.vo.PrintVO;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.UseWaterPlanAddService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianghao
 * @date 2021/01/04
 */

@Service
@Slf4j
public class PlanDailyAdjustmentServiceImpl extends
    ServiceImpl<PlanDailyAdjustmentMapper, UseWaterPlan> implements
    PlanDailyAdjustmentService {

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;
  @Autowired
  private EndPaperService endPaperService;

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();//节点编码
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String unitName = jsonObject.getString("unitName");//单位名称
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Date planYearStart = jsonObject.getDate("planYearStart");//计划年度起始
    Date planYearEnd = jsonObject.getDate("planYearEnd");//计划年度截止
    Integer yearStart=0;
    Integer yearEnd=0;
    if (null != planYearStart){
      yearStart = Integer.parseInt(TimeUtil.formatTimeStr(planYearStart).substring(0, 4));
    }
    if (null != planYearEnd){
      yearEnd = Integer.parseInt(TimeUtil.formatTimeStr(planYearEnd).substring(0, 4));
    }

    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
    map.put("nodeCode", nodeCode);
    map.put("userId",userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(waterMeterCode)) {
      map.put("waterMeterCode", waterMeterCode);
    }
    if (null != yearStart && 0 != yearStart) {
      map.put("yearStart", yearStart);
    }
    if (null != yearEnd && 0 != yearEnd) {
      map.put("yearEnd", yearEnd);
    }

    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    /**查出满足条件的数据*/
    List<PlanDailyAdjustmentVO> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  public ApiResponse editRemarks(String id,String editType, String remarks) {
     ApiResponse response =new ApiResponse();
     if ("0".equals(editType)){
       /**修改计划表备注*/
        this.baseMapper.updateRemarks(id,remarks);
     }else if ("1".equals(editType)){
       /**修改计划调整表备注*/
       useWaterPlanAddService.updateRemarks(id,remarks);
     }else {
       response.recordError("参数错误，修改备注失败");
     }
    return response;
  }

  @Override
  public ApiResponse numberAfterCalculation(JSONObject jsonObject) {

     ApiResponse response = new ApiResponse();
     Double calculatedNumber =-1d;
     Double firstQuarter =jsonObject.getDouble("firstQuarter");
     Double secondQuarter = jsonObject.getDouble("secondQuarter");
     Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
     Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
     Double firstWater = jsonObject.getDouble("firstWater");
     Double secondWater = jsonObject.getDouble("secondWater");
     String addWay = jsonObject.getString("addWay");//加计划的方式：1-平均，2-最高
     Integer quarter = jsonObject.getInteger("quarter");
     Boolean year = jsonObject.getBoolean("year"); //是否在年计划上增加

    double tempNum =0;
     if("1".equals(addWay)){//平均
        tempNum =Math.floor((firstWater+secondWater)/2);//平均后向下取整
      }else if ("2".equals(addWay)){//最高
       tempNum =(firstWater>secondWater)?firstWater:secondWater;
     }
       if(year){//选择在年计划上增加
         switch (TimeUtil.toQuarter(new Date())) {
           case 1:
             calculatedNumber= tempNum*12-(firstQuarter+secondQuarter+thirdQuarter+fourthQuarter);
             break;
           case 2:
             calculatedNumber= tempNum*9-(secondQuarter+thirdQuarter+fourthQuarter);
             break;
           case 3:
             calculatedNumber= tempNum*6-(thirdQuarter+fourthQuarter);
             break;
           case 4:
             calculatedNumber= tempNum*3-fourthQuarter;
             break;
         }
       }else{
         switch (quarter) {
           case 1:
             calculatedNumber= tempNum*3-firstQuarter;
             break;
           case 2:
             calculatedNumber= tempNum*3-secondQuarter;
             break;
           case 3:
             calculatedNumber= tempNum*3-thirdQuarter;
             break;
           case 4:
             calculatedNumber= tempNum*3-fourthQuarter;
             break;
       }
     }
    if (calculatedNumber <= 0){
      response.setCode(500);
      response.recordError("没做任何改变，不能增加计划");
    }
    else {
      Map<String,Object> result = new HashMap<>();
      result.put("calculatedNumber",UpRoundTen(calculatedNumber));
      response.setData(result);
    }
    return response;
  }

  @Override
  public ApiResponse adjustPlan(User user,JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String id = jsonObject.getString("id");
    Double firstQuarter =jsonObject.getDouble("firstQuarter");
    Double secondQuarter = jsonObject.getDouble("secondQuarter");
    Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
    Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
    Double firstWater = jsonObject.getDouble("firstWater");
    Double secondWater = jsonObject.getDouble("secondWater");
    Double addNumber = jsonObject.getDouble("addNumber");
    Integer quarter = jsonObject.getInteger("quarter");
    Boolean year = jsonObject.getBoolean("year"); //是否在年计划上增加
    String remarks = jsonObject.getString("remarks");//备注

    /**根据id查询数据库原数据*/
    UseWaterPlan  useWaterPlan =this.baseMapper.selectById(id);
    /**如果有未完成流程的办结单，则不允许操作*/
    if("1".equals(useWaterPlan.getExistSettlementForm())){
      response.recordError("该计划存在未完成的办结单");
      return response;
    }
    /**计算每个季度与原数据的差值*/
    Double firstQuarterDiff = firstQuarter - useWaterPlan.getFirstQuarter();//填写的与原数据作差
    Double secondQuarterDiff = secondQuarter - useWaterPlan.getSecondQuarter();
    Double thirdQuarterDiff = thirdQuarter - useWaterPlan.getThirdQuarter();
    Double fourthQuarterDiff = fourthQuarter - useWaterPlan.getFourthQuarter();
    /**判断季度数据是否有修改*/
    if(firstQuarterDiff != 0 || secondQuarterDiff != 0 || thirdQuarterDiff != 0 || fourthQuarterDiff != 0){
      /**判断四个季度的总和是否与年计划相等*/
      if ((firstQuarter+secondQuarter+thirdQuarter+fourthQuarter) != useWaterPlan.getCurYearPlan()){
        response.recordError("四个季度和不等于年计划，不能修改");
        return response;
      }
      /**相等，且有修改，则在计划调整表新增一条“日常调整”信息*/
      UseWaterPlanAdd useWaterPlanAdd =new UseWaterPlanAdd();
      useWaterPlanAdd.setPlanType("0");//日常调整
      useWaterPlanAdd.setCreateTime(new Date());
      useWaterPlanAdd.setCreaterId(user.getId());
      useWaterPlanAdd.setCreater(user.getUsername());
      useWaterPlanAdd.setCurYearPlan(0d);//double
      useWaterPlanAdd.setFirstQuarter(firstQuarterDiff);
      useWaterPlanAdd.setSecondQuarter(secondQuarterDiff);
      useWaterPlanAdd.setThirdQuarter(thirdQuarterDiff);
      useWaterPlanAdd.setFourthQuarter(fourthQuarterDiff);
      useWaterPlanAdd.setPlanYear(useWaterPlan.getPlanYear());
      useWaterPlanAdd.setNodeCode(useWaterPlan.getNodeCode());
      useWaterPlanAdd.setUnitCode(useWaterPlan.getUnitCode());
      useWaterPlanAdd.setUnitName(useWaterPlan.getUnitName());
      useWaterPlanAdd.setUseWaterUnitId(useWaterPlan.getUseWaterUnitId());
      useWaterPlanAdd.setWaterMeterCode(useWaterPlan.getWaterMeterCode());
      useWaterPlanAdd.setRemarks(remarks);
      useWaterPlanAdd.setPrinted("0");
      useWaterPlanAdd.setStatus("2");//已审核，可累加
      useWaterPlanAddService.insert(useWaterPlanAdd);
    } else if (firstQuarterDiff == 0 && secondQuarterDiff == 0 && thirdQuarterDiff == 0
        && fourthQuarterDiff == 0) {
      //季度数据没有调整(业务只允许季度数据修改 和 增加计划 只能存在一种，且季度数据修改后，默认不能进行“增加计划”)
      /**判断有没有增加水量*/
      if(addNumber != 0 && null != addNumber){
        /**有，新增一条“增加计划”信息*/
        UseWaterPlanAdd useWaterPlanAdd =new UseWaterPlanAdd();
        useWaterPlanAdd.setPlanType("1");//增加计划
        useWaterPlanAdd.setCreateTime(new Date());
        useWaterPlanAdd.setCreaterId(user.getId());
        useWaterPlanAdd.setCreater(user.getUsername());
        /**先4个季度数量都设置为0*/
        useWaterPlanAdd.setFirstQuarter(0d);//double
        useWaterPlanAdd.setSecondQuarter(0d);
        useWaterPlanAdd.setThirdQuarter(0d);
        useWaterPlanAdd.setFourthQuarter(0d);
        if(year){//在年计划上增加
          /**只设置年计划,4个季度都是0*/
          useWaterPlanAdd.setCurYearPlan(addNumber);
        }else {//选择了在某个季度且没有勾选年计划，则默认将增加的水量加到这个季度
          switch (quarter) {
            case 1:
              useWaterPlanAdd.setFirstQuarter(addNumber);
              useWaterPlanAdd.setCurYearPlan(addNumber);
              useWaterPlanAdd.setChangeQuarter("1");
              break;
            case 2:
              useWaterPlanAdd.setSecondQuarter(addNumber);
              useWaterPlanAdd.setCurYearPlan(addNumber);
              useWaterPlanAdd.setChangeQuarter("2");
              break;
            case 3:
              useWaterPlanAdd.setThirdQuarter(addNumber);
              useWaterPlanAdd.setCurYearPlan(addNumber);
              useWaterPlanAdd.setChangeQuarter("3");
              break;
            case 4:
              useWaterPlanAdd.setFourthQuarter(addNumber);
              useWaterPlanAdd.setCurYearPlan(addNumber);
              useWaterPlanAdd.setChangeQuarter("4");
              break;
          }
        }
        useWaterPlanAdd.setPlanYear(useWaterPlan.getPlanYear());
        useWaterPlanAdd.setNodeCode(useWaterPlan.getNodeCode());
        useWaterPlanAdd.setUnitCode(useWaterPlan.getUnitCode());
        useWaterPlanAdd.setUnitName(useWaterPlan.getUnitName());
        useWaterPlanAdd.setUseWaterUnitId(useWaterPlan.getUseWaterUnitId());
        useWaterPlanAdd.setWaterMeterCode(useWaterPlan.getWaterMeterCode());
        useWaterPlanAdd.setRemarks(remarks);
        useWaterPlanAdd.setPrinted("0");
        useWaterPlanAdd.setStatus("2");//已审核，可累加
        useWaterPlanAdd.setFirstWater(firstWater);
        useWaterPlanAdd.setSecondWater(secondWater);
        useWaterPlanAddService.insert(useWaterPlanAdd);
      }else {
        response.recordError("没有作任何修改");
        return response;
      }
    }
    return response;
  }

  @Override
  public ApiResponse editPlanAdd(User user, List<UseWaterPlanAdd> useWaterPlanAdds) {
    ApiResponse response = new ApiResponse();

    for (UseWaterPlanAdd useWaterPlanAdd :useWaterPlanAdds){
        /**判断修改后的数据是否满足4个季度的和为年计划的数量*/
        double sum =
            useWaterPlanAdd.getFirstQuarter() + useWaterPlanAdd.getSecondQuarter() + useWaterPlanAdd
                .getThirdQuarter() + useWaterPlanAdd.getFourthQuarter();
        if (sum != useWaterPlanAdd.getCurYearPlan()){
          response.recordError("数据修改后4个季度的和应该和年计划的数量相等，"+"水表号为:"+useWaterPlanAdd.getWaterMeterCode());
          return response;
        }
      }
      useWaterPlanAddService.updatePlanAdd(useWaterPlanAdds);
    return response;
  }

  @Override
  public ApiResponse accumulate(User user,UseWaterPlanAdd useWaterPlanAdd) {
    ApiResponse response = new ApiResponse();
   /**通过节点编码,单位编号,年份查询计划表数据*/
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", useWaterPlanAdd.getNodeCode());
    wrapper.eq("unit_code", useWaterPlanAdd.getUnitCode());
    wrapper.eq("plan_year", useWaterPlanAdd.getPlanYear());
    UseWaterPlan useWaterPlan = this.selectOne(wrapper);
    /**如果有未完成流程的办结单，则不允许操作*/
    if("1".equals(useWaterPlan.getExistSettlementForm())){
      response.recordError("计划存在未完成的办结单");
      return response;
    }
    useWaterPlan.setCurYearPlan(useWaterPlan.getCurYearPlan() + useWaterPlanAdd.getCurYearPlan());
    useWaterPlan.setFirstQuarter(useWaterPlan.getFirstQuarter() + useWaterPlanAdd.getFirstQuarter());
    useWaterPlan.setSecondQuarter(useWaterPlan.getSecondQuarter() + useWaterPlanAdd.getSecondQuarter());
    useWaterPlan.setThirdQuarter(useWaterPlan.getThirdQuarter() + useWaterPlanAdd.getThirdQuarter());
    useWaterPlan.setFourthQuarter(useWaterPlan.getFourthQuarter() + useWaterPlanAdd.getFourthQuarter());
    if (useWaterPlanAdd.getCurYearPlan() != (useWaterPlanAdd.getFirstQuarter() + useWaterPlanAdd
        .getSecondQuarter() + useWaterPlanAdd.getThirdQuarter() + useWaterPlanAdd
        .getFourthQuarter())) {
      /**如果四个季度和不等于年计划(日常调整的和不为0，
       * 增加计划只在年计划上有增加水量没有在4个季度上分配或者分配后和不等于年计划)
       * 则不让累加*/
      response.recordError("各季度计划和不等于年计划");
      return response;
    }
    /**更新调整表累加状态*/
    useWaterPlanAdd.setStatus("3");//已累加
    boolean result = useWaterPlanAddService.updateStatusOrPrinted(useWaterPlanAdd.getId(),useWaterPlanAdd.getStatus(),null);
    if (result) {
      /**更新计划表数据*/
      useWaterPlan.setUpdateTime(new Date());
      useWaterPlan.setUpdateUserId(user.getId());
      this.baseMapper.updateById(useWaterPlan);
    }else {
      log.error("更新调整表累加状态失败");
      response.recordError("更新调整表累加状态失败");
    }
   return response;
  }

  @Override
  public boolean signPrinted(List<PrintVO> printList) {
    for (PrintVO printVO : printList) {
      if ("0".equals(printVO.getPrintType())) {
        this.baseMapper.updatePrintStatus(printVO.getId());
      } else if ("1".equals(printVO.getPrintType())) {
        /**修改调整表打印状态*/
        useWaterPlanAddService.updateStatusOrPrinted(printVO.getId(), null, "1");
      }
    }
    return true;
  }

  @Override
  public List<Map<String, Object>> queryMessage(User user, String unitCode) {
    List<Map<String, Object>> result = new ArrayList<>();
    Map<String,Object> map = new HashMap<>();
    map.put("userId",user.getId());
    map.put("nodeCode",user.getNodeCode());
    if (StringUtils.isNotBlank(unitCode)){
      map.put("unitCode",unitCode);
    }

    /**上月月份 用于查询第二水量*/
    Integer monthBefore = Integer.parseInt(TimeUtil.formatTimeStr(TimeUtil.getMonthFirstDay(new Date(),-1)).substring(5,7));
    /**上月所处的年份*/
    Integer monthBeforeYear = Integer.parseInt(TimeUtil.formatTimeStr(TimeUtil.getMonthFirstDay(new Date(),-1)).substring(0,4));
    /**本月月份 用于查询第一水量*/
    Integer thisMonth = Integer.parseInt(TimeUtil.formatTimeStr(new Date()).substring(5,7));
    /**本月所处的年份*/
    Integer thisMonthYear = Integer.parseInt(TimeUtil.formatTimeStr(new Date()).substring(0,4));

    map.put("monthBefore",monthBefore);
    map.put("monthBeforeYear",monthBeforeYear);
    map.put("thisMonth",thisMonth);
    map.put("thisMonthYear",thisMonthYear);
    result = this.baseMapper.queryMessage(map);
    return result;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse initiateSettlement(User user,JSONObject jsonObject) {

    ApiResponse response = new ApiResponse();
    String unitCode = jsonObject.getString("unitCode");
    String unitName = jsonObject.getString("unitName");
    String waterMeterCode = jsonObject.getString("waterMeterCode");
    Integer planYear = jsonObject.getInteger("planYear");
    String paperType = jsonObject.getString("paperType");//调整类型(申报类型)
    Double firstQuarter =jsonObject.getDouble("firstQuarter");
    Double secondQuarter = jsonObject.getDouble("secondQuarter");
    Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
    Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
    Double firstWater = jsonObject.getDouble("firstWater");
    Double secondWater = jsonObject.getDouble("secondWater");
    String quarter = jsonObject.getString("quarter");//季度
    String opinions = jsonObject.getString("opinions");//意见
    List<String> auditFileIds = jsonObject.getJSONArray("auditFileIds").toJavaList(String.class);
    List<String> waterProofFileIds = jsonObject.getJSONArray("waterProofFileIds").toJavaList(String.class);
    List<String> otherFileIds = jsonObject.getJSONArray("otherFileIds").toJavaList(String.class);


    /**查询该用水单位是否存在没有走完办结流程的办结单*/
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.eq("unit_code", unitCode);
    wrapper.eq("plan_year", planYear);
    UseWaterPlan useWaterPlan = this.selectOne(wrapper);
     /**有则不让在发起*/
    if(null !=useWaterPlan && "1".equals(useWaterPlan.getExistSettlementForm())){
      response.recordError("该计划已存在未完成的办结单");
      return response;
    }
    EndPaper endPaper = new EndPaper();
    endPaper.setNodeCode(user.getNodeCode());
    endPaper.setUseWaterUnitId(useWaterPlan.getUseWaterUnitId());
    endPaper.setUnitName(unitName);
    endPaper.setUnitCode(unitCode);
    endPaper.setWaterMeterCode(waterMeterCode);
    endPaper.setPaperType(paperType);
    endPaper.setDataSources("2");//现场申报
    endPaper.setConfirmed("1");//现场申报的都是已确认的
    endPaper.setConfirmTime(new Date());
    endPaper.setCreateTime(new Date());
    endPaper.setCreaterId(user.getId());
    endPaper.setCreaterName(user.getUsername());
    endPaper.setPlanYear(planYear);
    endPaper.setChangeQuarter(quarter);
    if (null != firstQuarter && null != secondQuarter && null != thirdQuarter
        && null != fourthQuarter) {
      endPaper.setFirstQuarter(firstQuarter);
      endPaper.setSecondQuarter(secondQuarter);
      endPaper.setThirdQuarter(thirdQuarter);
      endPaper.setFourthQuarter(fourthQuarter);
      endPaper.setCurYearPlan(firstQuarter + secondQuarter + thirdQuarter + fourthQuarter);
    }
    if (null != firstWater && null != secondWater) {
      endPaper.setFirstWater(firstWater);
      endPaper.setSecondWater(secondWater);
    }
    endPaper.setMinusPayStatus(useWaterPlan.getMinusPayStatus());
    endPaper.setBalanceTest(useWaterPlan.getBalanceTest());
    endPaper.setCreateType(useWaterPlan.getCreateType());
    if (!auditFileIds.isEmpty()){
      String auditFileId = StringUtils.strip(auditFileIds.toString(),"[]").replace(" ", "");
      endPaper.setAuditFileId(auditFileId);
    }
    if (!waterProofFileIds.isEmpty()){
      String waterProofFileId = StringUtils.strip(waterProofFileIds.toString(),"[]").replace(" ", "");
      endPaper.setWaterProofFileId(waterProofFileId);
    }
    if (!otherFileIds.isEmpty()){
      String otherFileId = StringUtils.strip(otherFileIds.toString(),"[]").replace(" ", "");
      endPaper.setOtherFileId(otherFileId);
    }
    endPaperService.insert(endPaper);
    /**TODO 审核流程信息新增*/

    /**发起办结单后，将计划表中的“是否存在没有走完办结流程的办结单”的状态改为是(办结流程走完后或者撤销办结单后，改为否)。*/
    this.updateExistSettlement("1",unitCode,user.getNodeCode(),planYear);
    return response;
  }

  @Override
  public void updateExistSettlement(String existSettlement,String unitCode, String nodeCode, Integer planYear) {
    this.baseMapper.updateExistSettlement(existSettlement,unitCode,nodeCode,planYear);
  }

  @Override
  public List<PlanDailyAdjustmentVO> queryList(User user, JSONObject jsonObject) {

    String nodeCode = user.getNodeCode();//节点编码
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String unitName = jsonObject.getString("unitName");//单位名称
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Integer yearStart = jsonObject.getInteger("yearStart");//计划年度起始
    Integer yearEnd = jsonObject.getInteger("yearEnd");//计划年度截止

    Map<String, Object> map = new HashMap();
    map.put("nodeCode", nodeCode);
    map.put("userId",userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(waterMeterCode)) {
      map.put("waterMeterCode", waterMeterCode);
    }
    if (null != yearStart && 0 != yearStart) {
      map.put("yearStart", yearStart);
    }
    if (null != yearEnd && 0 != yearEnd) {
      map.put("yearEnd", yearEnd);
    }

    /**查出满足条件的数据*/
    List<PlanDailyAdjustmentVO> records = this.baseMapper.queryList(map);
    return records;
  }

  /**
   * 向上十位取整
   * 34512 返回34520。
   * */
  private int UpRoundTen(double num){
    int upNumber;
    upNumber= (int) Math.ceil(num / 10);
    return upNumber*10;
  }

}