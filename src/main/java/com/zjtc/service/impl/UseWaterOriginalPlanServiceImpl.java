package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.UseWaterOriginalPlanMapper;
import com.zjtc.model.Algorithm;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.model.User;
import com.zjtc.service.AlgorithmService;
import com.zjtc.service.CommonService;
import com.zjtc.service.MessageService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.UseWaterOriginalPlanService;
import com.zjtc.service.UseWaterPlanService;
import com.zjtc.service.UseWaterSelfDefinePlanService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TWUseWaterOriginalPlan的服务接口的实现类
 *
 * @author yuyantian
 */
@Service
public class UseWaterOriginalPlanServiceImpl extends
    ServiceImpl<UseWaterOriginalPlanMapper, UseWaterOriginalPlan> implements
    UseWaterOriginalPlanService {

  @Autowired
  private AlgorithmService algorithmService;
  @Autowired
  private UseWaterPlanService useWaterPlanService;
  @Autowired
  private UseWaterSelfDefinePlanService useWaterSelfDefinePlanService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private CommonService commonService;
  @Autowired
  private SystemLogService systemLogService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse save(User user, JSONObject jsonObject) {
    ApiResponse apiResponse = new ApiResponse();
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    for (UseWaterOriginalPlan item : entity) {
      //已编制的数据不能再编制
      if (item.getPlaned().equals("1")) {
        apiResponse.recordError("当前保存的数据中存在已编制数据");
        apiResponse.setCode(501);
        break;
      }
      /**请选择季度,新户保存需要选择季度*/
      if (0 == item.getAssessQuarter() && "1".equals(item.getAdded())) {
        apiResponse.recordError("请选择考核季度");
        apiResponse.setCode(501);
        break;
      }
      /**如果是新增户，并且本年计划为空*/
      if ("1".equals(item.getAdded()) && null == item.getCurYearPlan()) {
        //本年计划等于下年终计划(基础)
        item.setCurYearPlan(item.getNextYearBaseStartPlan());
      }
      item.setNodeCode(jsonObject.getString("nodeCode"));
    }
    if (200 != apiResponse.getCode()) {
      return apiResponse;
    }
    this.saveOrUpdateBatch(entity);
    systemLogService.logInsert(user, "用水计划编制", "新增", null);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse saveOriginal(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    //用水计划数据
    List<UseWaterPlan> useWaterPlanList = new ArrayList<>();
    //自平表数据
    List<UseWaterSelfDefinePlan> selfDefinePlanList = new ArrayList<>();
    //保存用水单位编号的集合
    List<Map<String, Object>> unitCodeList = new ArrayList<>();
    //判断当前要编制的数据是否存在已编制的数据
    for (UseWaterOriginalPlan item : entity) {
      if (item.getPlaned().equals("1")) {
        apiResponse.recordError("当前要编制的数据中存在已编制数据");
        apiResponse.setCode(501);
        break;
      }
      if ("0".equals(item.getAlgorithmType())) {
        apiResponse.recordError("请选择算法");
        apiResponse.setCode(501);
        break;
      }
      /**请选择季度,新户编制需要选择季度*/
      if (0 == item.getAssessQuarter() && "1".equals(item.getAdded())) {
        apiResponse.recordError("请选择考核季度");
        apiResponse.setCode(501);
        break;
      }
      /**如果是新增户，并且本年计划为空*/
      if ("1".equals(item.getAdded()) && null == item.getCurYearPlan()) {
        //本年计划等于下年终计划(基础)
        item.setCurYearPlan(item.getNextYearBaseStartPlan());
      }
      /**1.更新用水计划原始表状态为已编制*/
      item.setPlaned("1");
      item.setNodeCode(user.getNodeCode());
      //用水表计划需要的数据
      UseWaterPlan useWaterPlan = new UseWaterPlan();
      useWaterPlan.setCreateTime(new Date());
      useWaterPlan.setPlanType("0");
      useWaterPlan.setAdded(item.getAdded());
      useWaterPlan.setNodeCode(user.getNodeCode());
      useWaterPlan.setUseWaterUnitId(item.getUseWaterUnitId());
      useWaterPlan.setUnitCode(item.getUnitCode());
      useWaterPlan.setUnitName(item.getUnitName());
      useWaterPlan.setWaterMeterCode(item.getWaterMeterCode());
      useWaterPlan.setPlanYear(item.getPlanYear());
      useWaterPlan.setBaseWaterAmount(item.getBaseWaterAmount());
      useWaterPlan.setBeforeLastYearWaterAmount(item.getBeforeLastYearWaterAmount());
      useWaterPlan.setLastYearWaterAmount(item.getLastYearWaterAmount());
      useWaterPlan.setThreeYearAvg(item.getThreeYearAvg());
      useWaterPlan.setNowPrice(item.getNowPrice());
      useWaterPlan.setN8(item.getN8());
      useWaterPlan.setMinusPayStatus(item.getMinusPayStatus());
      useWaterPlan.setBalanceTest(item.getBalanceTest());
      useWaterPlan.setCreateType(item.getCreateType());
      useWaterPlan.setCurYearBasePlan(item.getCurYearBasePlan());
      useWaterPlan.setCurYearPlan(item.getCurYearPlan());
      //判断算法
      if (item.getAlgorithmType().equals("1")) {
        useWaterPlan.setAlgorithmType("1");
        useWaterPlan.setNextYearStartPlan(item.getNextYearBaseStartPlan());
        useWaterPlan.setNextYearEndPlan(item.getNextYearBaseEndPlan());
        useWaterPlan.setFirstQuarter(item.getFirstQuarterBase());
        useWaterPlan.setSecondQuarter(item.getSecondQuarterBase());
        useWaterPlan.setThirdQuarter(item.getThirdQuarterBase());
        useWaterPlan.setFourthQuarter(item.getFourthQuarterBase());
      }
      if (item.getAlgorithmType().equals("2")) {
        useWaterPlan.setAlgorithmType("2");
        useWaterPlan.setNextYearStartPlan(item.getNextYearQuotaStartPlan());
        useWaterPlan.setNextYearEndPlan(item.getNextYearQuotaEndPlan());
        useWaterPlan.setFirstQuarter(item.getFirstQuarterQuota());
        useWaterPlan.setSecondQuarter(item.getSecondQuarterQuota());
        useWaterPlan.setThirdQuarter(item.getThirdQuarterQuota());
        useWaterPlan.setFourthQuarter(item.getFourthQuarterQuota());
      }
      //自平表需要的数据
      UseWaterSelfDefinePlan useWaterSelfDefinePlan = new UseWaterSelfDefinePlan();
      useWaterSelfDefinePlan.setCreateTime(new Date());
      useWaterSelfDefinePlan.setAuditStatus("3");
      useWaterSelfDefinePlan.setNodeCode(user.getNodeCode());
      useWaterSelfDefinePlan.setUseWaterUnitId(useWaterPlan.getUseWaterUnitId());
      useWaterSelfDefinePlan.setUnitCode(useWaterPlan.getUnitCode());
      useWaterSelfDefinePlan.setUnitName(useWaterPlan.getUnitName());
      useWaterSelfDefinePlan.setWaterMeterCode(useWaterPlan.getWaterMeterCode());
      useWaterSelfDefinePlan.setPlanYear(useWaterPlan.getPlanYear());
      useWaterSelfDefinePlan.setCurYearPlan(useWaterPlan.getCurYearPlan());
      useWaterSelfDefinePlan.setFirstQuarter(useWaterPlan.getFirstQuarter());
      useWaterSelfDefinePlan.setSecondQuarter(useWaterPlan.getSecondQuarter());
      useWaterSelfDefinePlan.setThirdQuarter(useWaterPlan.getThirdQuarter());
      useWaterSelfDefinePlan.setFourthQuarter(useWaterPlan.getFourthQuarter());
      useWaterSelfDefinePlan.setExecuted("0");
      selfDefinePlanList.add(useWaterSelfDefinePlan);
      useWaterPlanList.add(useWaterPlan);
      //通知需要的单位编号
      Map<String, Object> map = new HashMap<>(4);
      map.put("unitCode", item.getUnitCode());
      map.put("planYear", item.getPlanYear());
      map.put("unitName", item.getUnitName());
      unitCodeList.add(map);
    }
    if (200 != apiResponse.getCode()) {
      return apiResponse;
    }
    /**2.数据保存至用水计划原始表*/
    this.saveOrUpdateBatch(entity);
    /**3.保存至用水计划表*/
    useWaterPlanService.saveBatch(useWaterPlanList);
    /**4.保存至自平表*/
    useWaterSelfDefinePlanService.saveBatch(selfDefinePlanList);
    /**微信公众号：通知用水单位*/
    if (!unitCodeList.isEmpty()) {
      for (Map map : unitCodeList) {
        String content =
            "您单位" + map.get("unitCode").toString() + "(" + map.get("unitName").toString() + ")"
                + map.get("planYear") + "年年计划已经下达，请前往公共服务管理平台与微信公众号年度自平管理模块进行计划自平。";
        messageService.messageToUnit(map.get("unitCode").toString(), content, "业务通知");
      }
    }
    //todo:微信
    systemLogService.logInsert(user, "用水计划编制", "编制", null);
    return apiResponse;
  }


  @Override
  public Map<String, Object> queryPageOld(JSONObject jsonObject) {
    //用户类型
    String userType = jsonObject.getString("userType");
    //编号开头
    String unitCodeStart = jsonObject.getString("unitStart");
    //年份
    Integer year = jsonObject.getInteger("year");
    Integer current = jsonObject.getInteger("current");
    Integer size = jsonObject.getInteger("size");
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);
    if (null == year || year == nowYear) {
      //默认当前年
      year = nowYear;
      if (nowMonth >= 9) {//如果当前的月份大于十月，开始下年计划编制
        year++;
      }
    }
    Map<String, Object> list = null;
    //如果没有编制过则初始化，否则直接查询UseWaterPlan表数据
    if (findPlanRecord(year, jsonObject.getString("nodeCode"))) {
      System.out.print("===编制初始化");
      list = initPlanOldPage(year, userType, unitCodeStart, jsonObject.getString("userId"),
          jsonObject.getString("nodeCode"), current, size);
    } else {
      System.out.print("===编制查询");
      list = nowYearPlanOldPage(year, userType, unitCodeStart, jsonObject.getString("userId"),
          jsonObject.getString("nodeCode"), current, size);
    }
    return list;
  }

  @Override
  public List<Map<String, Object>> goPlanningOld(JSONObject jsonObject) {
    //用户类型
    String userType = jsonObject.getString("userType");
    //编号开头
    String unitCodeStart = jsonObject.getString("unitStart");
    //年份
    Integer year = jsonObject.getInteger("year");
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);
    if (null == year || year == nowYear) {
      //默认当前年
      year = nowYear;
      if (nowMonth >= 9) {//如果当前的月份大于十月，开始下年计划编制
        year++;
      }
    }
    List<Map<String, Object>> list = null;
    //如果没有编制过则初始化，否则直接查询UseWaterPlan表数据
    if (findPlanRecord(year, jsonObject.getString("nodeCode"))) {
      System.out.print("===编制初始化");
      list = initPlanOld(year, userType, unitCodeStart, jsonObject.getString("userId"),
          jsonObject.getString("nodeCode"));
    } else {
      System.out.print("===编制查询");
      list = nowYearPlanOld(year, userType, unitCodeStart, jsonObject.getString("userId"),
          jsonObject.getString("nodeCode"));
    }
    return list;
  }

  @Override
  public List<Map<String, Object>> goPlanningNew(String userId, String nodeCode) {
    List<Map<String, Object>> result = new ArrayList<>();
    //年份
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);

    //判断2021年是否已经编制了,如果2021年未编制，不再进行初始化查询
    //9-12月时，老户未完成编制，新户初始化不显示数据
    boolean flag = false;
    if (9 <= nowMonth && nowMonth < 12) {
      flag = findPlanRecord(nowYear + 1, nodeCode);
    }
    if (!flag) {
      //已完成计划编制
      //初始化数据
      result = initPlanNew(nowYear, nowMonth, userId,
          nodeCode);
    }
    return result;
  }

  private List<Map<String, Object>> initPlanNew(int year, int month, String userId,
      String nodeCode) {
    List<Map<String, Object>> list = new ArrayList<>();
    int ParamOne = year; //计算前三季度用到的参数
    int ParamTwo = year; //计算第四季度季度用到的参数
    //当年水量
    String threeWaterMonth = "0";
    //前一年水量
    String fourWaterMonth = "0";
    //第一季度
    if (0 <= month && month < 3) {
      //只计算去年第3-4季度
      ParamOne--;
      ParamTwo--;
      fourWaterMonth = "july_count + august_count + september_count + october_count + november_count + december_count";
    } else if (3 <= month && month < 6) {
      //计算去年第四季度，今年前三季度(当年第一季度)
      ParamTwo--;
      threeWaterMonth = "january_count + february_count + march_count";
      fourWaterMonth = "october_count + november_count + december_count";
    } else if (6 <= month && month < 9) {
      //计算当年前三季度(当前前两季度)
      threeWaterMonth = "january_count + february_count + march_count + april_count+ may_count+ june_count";
    } else if (9 <= month && month < 12) {
      //一定是初始化下一年的编制
      year++;
      //计算当前年前三季度(当前年中间俩进度，下一年编制是2021，则今年就是2020)
      threeWaterMonth = "april_count + may_count + june_count + july_count + august_count + september_count";
    }
    long start= System.currentTimeMillis();
    //初始化的数据
    list = baseMapper
        .initPlanNew(year, userId, nodeCode, ParamOne, ParamTwo, threeWaterMonth, fourWaterMonth);
    long end= System.currentTimeMillis()-start;
    System.out.println(end);
    /**初始化基础算法*/
    Algorithm algorithmBase = algorithmService.queryAlgorithm(nodeCode, "1");
    /**初始化定额算法*/
    Algorithm algorithmQuota = algorithmService.queryAlgorithm(nodeCode, "2");
    if (!list.isEmpty()) {
      //本年计划,默认等于下年终计划
      double curYearPlan;
      for (Map<String, Object> map : list) {
        //下年初计划(基础)
        double nextYearBaseStartPlan = 0;
        //下年终计划(基础)
        double nextYearBaseEndPlan;
        //第一季度(基础)
        double firstQuarterBase;
        //第二季度(基础)
        double secondQuarterBase;
        //第三季度(基础)
        double thirdQuarterBase;
        //第四季度(基础)
        double fourthQuarterBase;
        //下年初计划(定额)
        double nextYearQuotaStartPlan = (Double) map.get("nextYearQuotaStartPlan");
        //下年终于计划(定额)
        double nextYearQuotaEndPlan = 0;
        //第一季度(定额)
        double firstQuarterQuota;
        //第二季度(定额)
        double secondQuarterQuota;
        //第三季度(定额)
        double thirdQuarterQuota;
        //第四季度(定额)
        double fourthQuarterQuota;
        //三年平均
        double threeYearAvg = (Double) map.get("threeYearAvg");
        //水价
        double nowPrice = (Double) map.get("nowPrice");
        //标识，下年初计划(基础)>下年初计划(定额)
        String sign = "0";
        //单位编号
        String unitCode = (String) map.get("unitCode");
        //截取类型
        String unitType = unitCode.substring(0, 2);
        /**一：如果三年平均、水价不为空*/
        if (threeYearAvg != 0 && nowPrice != 0) {
          /**1.计算下年初计划(基础)*/
          //nowPrice <= priceFloot
          if (nowPrice <= algorithmBase.getPriceBottom()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro1();
            //nowPrice > priceFloot && nowPrice <= priceUp
          } else if (nowPrice > algorithmBase.getPriceBottom() && nowPrice <= algorithmBase
              .getPriceTop()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro2();
          } else if (nowPrice > algorithmBase.getPriceTop()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro3();
          }
          /**2.下年初计划(基础)进10*/
          nextYearBaseStartPlan = ceil(nextYearBaseStartPlan);
//          /**3.如果下年初始计划(基础)大于下年初计划(定额)，基础计划默认等于定额计划*/
//          if (nextYearBaseStartPlan > nextYearQuotaStartPlan) {
//            nextYearBaseStartPlan = nextYearQuotaStartPlan;
//            //标识
//            sign = "1";
//          }
          /**4.下年终计划(基础) 默认等于下年初计划(基础)*/
          nextYearBaseEndPlan = nextYearBaseStartPlan;
          /**5.各季度水量*/
          /**特殊类型*/
          if (unitType.equals("33")) {
            //基础
            firstQuarterBase = 0;
            secondQuarterBase = Math.round(nextYearBaseEndPlan / 2);
            thirdQuarterBase = 0;
            fourthQuarterBase = secondQuarterBase;
          } else {
            /**普通类型*/
            double nextYearPlan2 = Math
                .round(nextYearBaseEndPlan * algorithmBase.getSecondQuarterPro());
            double nextYearPlan1 = Math.round((nextYearBaseEndPlan - (nextYearPlan2 * 2)) / 2);
            firstQuarterBase = nextYearPlan1;
            secondQuarterBase = nextYearPlan2;
            thirdQuarterBase = nextYearPlan2;
            fourthQuarterBase = nextYearPlan1;
          }
          map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
          map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
          map.put("firstQuarterBase", firstQuarterBase);
          map.put("secondQuarterBase", secondQuarterBase);
          map.put("thirdQuarterBase", thirdQuarterBase);
          map.put("fourthQuarterBase", fourthQuarterBase);

          /**二：三年平均为0*/
        } else if (threeYearAvg == 0) {
          map.put("nextYearBaseStartPlan", 0);
          map.put("nextYearBaseEndPlan", 0);
          map.put("firstQuarterBase", 0);
          map.put("secondQuarterBase", 0);
          map.put("thirdQuarterBase", 0);
          map.put("fourthQuarterBase", 0);
        }
        /**下年初计划(定额)进10*/
        nextYearQuotaStartPlan = ceil(nextYearQuotaStartPlan);
        /**下年终计划(定额) 默认等于下年初计划(定额)*/
        nextYearQuotaEndPlan = nextYearQuotaStartPlan;
        /**计算各季度水量(定额)*/
        if (unitType.equals("33")) {
          firstQuarterQuota = 0;
          secondQuarterQuota = Math.round(nextYearQuotaEndPlan / 2);
          thirdQuarterQuota = 0;
          fourthQuarterQuota = secondQuarterQuota;
        } else {
          double nextYearQuotaEndPlan2 = Math
              .round(nextYearQuotaEndPlan * algorithmQuota.getSecondQuarterPro());
          double nextYearQuotaEndPlan1 = Math
              .round((nextYearQuotaEndPlan - (nextYearQuotaEndPlan2 * 2)) / 2);
          //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
          firstQuarterQuota = nextYearQuotaEndPlan1;
          //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
          secondQuarterQuota = nextYearQuotaEndPlan2;
          thirdQuarterQuota = nextYearQuotaEndPlan2;
          fourthQuarterQuota = nextYearQuotaEndPlan1;
        }
        map.put("n8", 0);
        map.put("nextYearQuotaStartPlan", nextYearQuotaStartPlan);
        map.put("nextYearQuotaEndPlan", nextYearQuotaEndPlan);
        map.put("firstQuarterQuota", firstQuarterQuota);
        map.put("secondQuarterQuota", secondQuarterQuota);
        map.put("thirdQuarterQuota", thirdQuarterQuota);
        map.put("fourthQuarterQuota", fourthQuarterQuota);
        map.put("sign", sign);
      }
    }
    return list;
  }

  private List<Map<String, Object>> nowYearPlanNew(int year, String userType, String unitCodeStart,
      String userId, String nodeCode) {
    return baseMapper.nowYearPlanNew(year, userId, nodeCode);
  }

  @Override
  public Map<String, Object> getOldByNextYearBase(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    //下年终计划
    Double nextYearBaseEndPlan = jsonObject.getDouble("nextYearBaseEndPlan");
    if (null == nextYearBaseEndPlan) {
      nextYearBaseEndPlan = 0.0d;
    }
    /**初始化算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    String unitCode = jsonObject.getString("unitCode");
    String unitType = unitCode.substring(2, 4);
    double firstQuarterBase;
    double secondQuarterBase;
    double thirdQuarterBase;
    double fourthQuarterBase;
    /**重新计算【各季度计划】:基础算法*/
    double nextYearBaseEndPlan1;
    double nextYearBaseEndPlan2;
    /**6.1.特殊用户33批次*/
    if (unitType.equals("33")) {
      firstQuarterBase = 0;
      secondQuarterBase = Math.round(nextYearBaseEndPlan / 2);
      thirdQuarterBase = 0;
      fourthQuarterBase = secondQuarterBase;
    } else {
      /**6.2.正常用户*/
      nextYearBaseEndPlan2 = Math
          .round(nextYearBaseEndPlan * algorithm.getSecondQuarterPro());
      nextYearBaseEndPlan1 = Math
          .round((nextYearBaseEndPlan - (nextYearBaseEndPlan2 * 2)) / 2);
      //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
      firstQuarterBase = nextYearBaseEndPlan1;
      //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
      secondQuarterBase = nextYearBaseEndPlan2;
      thirdQuarterBase = nextYearBaseEndPlan2;
      fourthQuarterBase = nextYearBaseEndPlan1;
    }
    map.put("firstQuarterBase", firstQuarterBase);
    map.put("secondQuarterBase", secondQuarterBase);
    map.put("thirdQuarterBase", thirdQuarterBase);
    map.put("fourthQuarterBase", fourthQuarterBase);
    return map;
  }

  @Override
  public Map<String, Object> getNewByNextYearBase(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    //三年平均
    double threeYearAvg = jsonObject.getDouble("threeYearAvg");
    //单位编号
    String unitCode = jsonObject.getString("unitCode");
    //下年终计划
    Double nextYearBaseEndPlan = jsonObject.getDouble("nextYearBaseEndPlan");
    if (null == nextYearBaseEndPlan) {
      nextYearBaseEndPlan = 0.0d;
    }
    //单位类型
    String unitType = unitCode.substring(2, 4);
    double firstQuarterBase;
    double secondQuarterBase;
    double thirdQuarterBase;
    double fourthQuarterBase;
    double nextYearPlan1;
    double nextYearPlan2;
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    /**三年平均计划为0不处理*/
    if (threeYearAvg != 0) {
      //特殊类型
      if (unitType.equals("33")) {
        firstQuarterBase = 0;
        secondQuarterBase = Math.round(nextYearBaseEndPlan / 2);
        thirdQuarterBase = 0;
        fourthQuarterBase = secondQuarterBase;
      } else {
        /**普通类型*/
        nextYearPlan2 = Math.round(nextYearBaseEndPlan * algorithm.getSecondQuarterPro());
        //nextYearPlan3 = Math.round(nextYearPlan * threeQuarterApp);
        nextYearPlan1 = Math.round((nextYearBaseEndPlan - (nextYearPlan2 * 2)) / 2);
        firstQuarterBase = nextYearPlan1;
        secondQuarterBase = nextYearPlan2;
        thirdQuarterBase = nextYearPlan2;
        fourthQuarterBase = nextYearPlan1;
      }
      map.put("firstQuarterBase", firstQuarterBase);
      map.put("secondQuarterBase", secondQuarterBase);
      map.put("thirdQuarterBase", thirdQuarterBase);
      map.put("fourthQuarterBase", fourthQuarterBase);
    }
    return map;
  }

  @Override
  public Map<String, Object> getOldResultByThreeYearAvg(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    Double threeYearAvg = jsonObject.getDouble("threeYearAvg");
    if (null == threeYearAvg) {
      threeYearAvg = 0.0d;
    }
    /**初始化算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    double n8 = jsonObject.getDouble("n8");
    double curYearPlan = jsonObject.getDouble("curYearPlan");
    double nowPrice = jsonObject.getDouble("nowPrice");
    double nextYearBaseStartPlan = 0;
    double nextYearBaseEndPlan;
    /**2.计算下年初始计划(基础)*/
    /**2.1.如：N8<n8Floot */
    if (n8 < algorithm.getN8Floot()) {
      nextYearBaseStartPlan = threeYearAvg * algorithm.getBasePro();
    } else if (algorithm.getN8Up() >= n8 && n8 >= algorithm
        .getN8Floot()) {
      /**2.2：n8Up >= N8 && N8 >= n8Floot*/
      nextYearBaseStartPlan = curYearPlan;
    } else if (n8 > algorithm.getN8Up()) {
      if (nowPrice <= algorithm.getPriceBottom()) {
        /**2.3：N8>n8Up && nowPrice <= priceFloot*/
        nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
      } else if (nowPrice > algorithm.getPriceBottom() && nowPrice <= algorithm
          .getPriceTop()) {
        /**2.4：N8>n8Up &&（nowPrice > priceFloot && nowPrice <= priceUp*/
        nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
      } else if (nowPrice > algorithm
          .getPriceTop()) {
        /**2.5: N8>n8Up && nowPrice > priceUp*/
        nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro3();
      }
    }
    /**3.下年初始计划(基础),如结果不能被10整除，则需进10*/
    if (Math.ceil(nextYearBaseStartPlan) % 10 != 0) {
      nextYearBaseStartPlan =
          Math.ceil(nextYearBaseStartPlan) + (10 - Math.ceil(nextYearBaseStartPlan) % 10);
    } else {
      nextYearBaseStartPlan = Math.ceil(nextYearBaseStartPlan);
    }
    //下年终计划等于下年初始计划
    nextYearBaseEndPlan = nextYearBaseStartPlan;
    map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
    map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    /**重新计算各季度计划水量*/
    jsonObject.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    Map<String, Object> nextMap = getOldByNextYearBase(jsonObject, user);
    map.putAll(nextMap);
    return map;
  }

  @Override
  public Map<String, Object> getResultBycheck(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    /**初始化算法基础*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    //扣加价
    String minusPayStatus = jsonObject.getString("minusPayStatus");
    //水平衡
    String balanceTest = jsonObject.getString("balanceTest");
    //创建
    String createType = jsonObject.getString("createType");
    //当年计划
    double curYearPlan = jsonObject.getDouble("curYearPlan");
    //下年初始计划
    double nextYearBaseStartPlan = jsonObject.getDouble("nextYearBaseStartPlan");
    //下年终计划
    double nextYearBaseEndPlan = nextYearBaseStartPlan;
    //重新计算下年终计划：=下年初始计划+(奖励情况-扣减情况)
    //特殊情况：选择奖励创建，且当年计划>下年终计划，则下年终计划=当年计划
    double param = nextYearBaseEndPlan;
    /**扣加价*/
    if ("1".equals(minusPayStatus)) {
      nextYearBaseEndPlan =
          param + (0 - (param * algorithm.getReducePro()));
    }
    /**水平衡，1奖，2扣*/
    if ("1".equals(balanceTest)) {
      nextYearBaseEndPlan =
          nextYearBaseEndPlan + (param * algorithm.getRewardPro());
    } else if ("2".equals(balanceTest)) {
      nextYearBaseEndPlan =
          nextYearBaseEndPlan + (0 - (param * algorithm.getReducePro()));
    }
    /**创建，1奖，2扣*/
    if ("1".equals(createType)) {
      nextYearBaseEndPlan =
          nextYearBaseEndPlan + (param * algorithm.getRewardPro());
      //特殊情况：
      if (curYearPlan > nextYearBaseStartPlan) {
        nextYearBaseEndPlan = curYearPlan;
      }
    } else if ("2".equals(createType)) {
      nextYearBaseEndPlan =
          nextYearBaseEndPlan + (0 - (param * algorithm.getReducePro()));
    }
    /**下年终计划进10*/
    if (Math.ceil(nextYearBaseEndPlan) % 10 != 0) {
      nextYearBaseEndPlan =
          Math.ceil(nextYearBaseEndPlan) + (10 - Math.ceil(nextYearBaseEndPlan) % 10);
    } else {
      nextYearBaseEndPlan = Math.ceil(nextYearBaseEndPlan);
    }
    map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    /**重新计算各季度计划水量*/
    jsonObject.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    Map<String, Object> nextMap = getOldByNextYearBase(jsonObject, user);
    map.putAll(nextMap);
    return map;
  }

  @Override
  public Map<String, Object> getNewByBaseWaterAmount(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    //下年初计划
    //价格
    double baseWaterAmount = jsonObject.getDouble("baseWaterAmount");
    //新户三年平均=第三年编制基础*2
    double threeYearAvg = baseWaterAmount * 2;
    map.put("threeYearAvg", threeYearAvg);
    //重新计算下年初计划
    jsonObject.put("threeYearAvg", threeYearAvg);
    Map<String, Object> nextMap = getNewResultByThreeYearAvg(jsonObject, user);
    map.putAll(nextMap);
    return map;
  }

  @Override
  public Map<String, Object> getNewByNowPrice(JSONObject jsonObject, User user) {
    //重新计算下年初计划
    Map<String, Object> map = getNewResultByThreeYearAvg(jsonObject, user);
    //调整三年下年初计划
    return map;
  }

  @Override
  public Map<String, Object> getNewResultByThreeYearAvg(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    //水价
    Double nowPrice = jsonObject.getDouble("nowPrice");
    if (null == nowPrice) {
      nowPrice = 0.0d;
    }
    //三年平均水量
    Double threeYearAvg = jsonObject.getDouble("threeYearAvg");
    if (null == threeYearAvg) {
      threeYearAvg = 0.0d;
    }
    /**初始化基础算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    //下年初计划初始值
    double nextYearBaseStartPlan = jsonObject.getDouble("nextYearBaseStartPlan");
    /**'下年初始计划"需按照"如“三年平均水量”大于0且本年计划等于0"的公式重新重新计算*/
    /**2.计算下年初始计划(基础)*/
    /**2.1. nowPrice <= priceFloot*/
    if (nowPrice <= algorithm.getPriceBottom()) {
      nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
    } else if (nowPrice > algorithm.getPriceBottom() && nowPrice <= algorithm.getPriceTop()) {
      /**2.2. nowPrice > priceFloot && nowPrice <= priceUp*/
      nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
    } else if (nowPrice > algorithm.getPriceTop()) {
      /**2.3.  nowPrice >priceUp*/
      nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro3();
    }
    /**3.下年初始计划(基础),如结果不能被10整除，则需进10*/
    if (Math.ceil(nextYearBaseStartPlan) % 10 != 0) {
      nextYearBaseStartPlan =
          Math.ceil(nextYearBaseStartPlan) + (10 - Math.ceil(nextYearBaseStartPlan) % 10);
    } else {
      nextYearBaseStartPlan = Math.ceil(nextYearBaseStartPlan);
    }
    /**4.下年终计划(基础)：默认等于下年初始计划(基础)*/
    double nextYearBaseEndPlan = nextYearBaseStartPlan;
    map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
    map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    /**5.重新计算各季度水量*/
    jsonObject.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
    Map<String, Object> resultMap = getNewByNextYearBase(jsonObject, user);
    map.putAll(resultMap);
    return map;
  }

  @Override
  public boolean deleteAllNotplaned(String nodeCode) {
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("planed", "0");
    wrapper.eq("node_code", nodeCode);
    return this.remove(wrapper);
  }

  @Override
  public void exportOldData(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<UseWaterOriginalPlan> list = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = nowYear + "年度计划编制汇总.xls";
    String templateName = "template/useWaterOriginalPlanOld.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水计划编制", "导出老户", null);
  }

  @Override
  public void exportNewData(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<UseWaterOriginalPlan> list = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "新增户计划编制汇总.xls";
    String templateName = "template/useWaterOriginalPlanOld.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水计划编制", "导出新户", null);
  }

  private List<Map<String, Object>> initPlanOld(int year, String userType, String unitCodeStart,
      String userId, String nodeCode) {
    /**初始化sql返回的数据*/
    List<Map<String, Object>> list = baseMapper
        .initPlanOld(year, userType, unitCodeStart, userId, nodeCode);
    /**初始化算法(基础)*/
    Algorithm algorithmBase = algorithmService.queryAlgorithm(nodeCode, "1");
    /**初始化算法(定额)*/
    Algorithm algorithmQuota = algorithmService.queryAlgorithm(nodeCode, "1");
    if (!list.isEmpty()) {
      double fourthQuarterQuota;
      for (Map<String, Object> map : list) {
        //当年年计划
        double curYearPlan;
        //当年基建计划
        double curYearBasePlan;
        //第三年编制基础
        double baseWaterAmount;
        //三年平均
        double threeYearAvg;
        //水价
        double nowPrice;
        //n8
        double n8;
        //标识
        String sign = "0";
        //下年初始计划(基础)
        double nextYearBaseStartPlan = 0;
        //下年终计划(基础)
        double nextYearBaseEndPlan;
        //下年初始计划(定额)
        double nextYearQuotaStartPlan;
        //下年终计划(定额)
        double nextYearQuotaEndPlan;
        //第一季度(基础)
        double firstQuarterBase;
        //第二季度(基础)
        double secondQuarterBase;
        //第三季度(基础)
        double thirdQuarterBase;
        //第四季度(基础)
        double fourthQuarterBase;
        //第一季度(定额)
        double firstQuarterQuota;
        //第二季度(定额)
        double secondQuarterQuota;
        //第三季度(定额)
        double thirdQuarterQuota;
        //第四季度(定额)
        curYearPlan = (Double) map.get("curYearPlan");
        curYearBasePlan = (Double) map.get("curYearBasePlan");
        baseWaterAmount = (Double) map.get("baseWaterAmount");
        threeYearAvg = (Double) map.get("threeYearAvg");
        nowPrice = (Double) map.get("nowPrice");
        nextYearQuotaStartPlan = (Double) map.get("nextYearQuotaStartPlan");
        //33批次
        String unitCode = (String) map.get("unitCode");
        //类型
        String unitType = unitCode.substring(2, 4);
        /**一：如三年平均水量大于0、本年年计划大于0*/
        if (Double.valueOf(threeYearAvg) != 0 && Double.valueOf(curYearPlan) != 0) {
          /**1.计算n8:(第三年编制基础-当年年计划-当年基建计划)/当年年计划,结果4舍5入*/
          n8 = (baseWaterAmount - curYearPlan - curYearBasePlan) / curYearPlan;
          n8 = Math.round(n8 * 100);
          /**2.计算下年初始计划(基础)*/
          /**2.1.如：N8<n8Floot */
          if (n8 < algorithmBase.getN8Floot()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getBasePro();
          } else if (algorithmBase.getN8Up() >= n8 && n8 >= algorithmBase
              .getN8Floot()) {
            /**2.2：n8Up >= N8 && N8 >= n8Floot*/
            nextYearBaseStartPlan = curYearPlan;
          } else if (n8 > algorithmBase.getN8Up()) {
            if (nowPrice <= algorithmBase.getPriceBottom()) {
              /**2.3：N8>n8Up && nowPrice <= priceFloot*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro1();
            } else if (nowPrice > algorithmBase.getPriceBottom() && nowPrice <= algorithmBase
                .getPriceTop()) {
              /**2.4：N8>n8Up &&（nowPrice > priceFloot && nowPrice <= priceUp*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro2();
            } else if (nowPrice > algorithmBase
                .getPriceTop()) {
              /**2.5: N8>n8Up && nowPrice > priceUp*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro3();
            }
          }
          /**3.赋值*/
          map.put("n8", n8);
        }
        /**二：计算当三年平均水量大于0，但本年计划为0的时候得这种情况,该算法不计算N8*/
        else if (Double.valueOf(threeYearAvg) > 0 && curYearPlan == 0) {
          /**1. n8:不计算*/
          /**2.计算下年初始计划(基础)*/
          /**2.1. nowPrice <= priceFloot*/
          if (nowPrice <= algorithmBase.getPriceBottom()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro1();
          } else if (nowPrice > algorithmBase.getPriceBottom() && nowPrice <= algorithmBase
              .getPriceTop()) {
            /**2.2. nowPrice > priceFloot && nowPrice <= priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro2();
          } else if (nowPrice > algorithmBase.getPriceTop()) {
            /**2.3. nowPrice > priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro3();
          }
          /**3.赋值*/
          map.put("n8", null);
        }
        /**4.下年初始计划(基础),如结果不能被10整除，则需进10*/
        nextYearBaseStartPlan = ceil(nextYearBaseStartPlan);
        /**4.下年初计划(定额)，如结果不能被10整除，则需进10*/
        nextYearQuotaStartPlan = ceil(nextYearQuotaStartPlan);
        /**如果下年初始计划(基础)大于下年初计划(定额)，基础计划默认等于定额计划*/
        if (nextYearBaseStartPlan > nextYearQuotaStartPlan) {
          nextYearBaseStartPlan = nextYearQuotaStartPlan;
          //标识
          sign = "1";
        }
        /**5.下年终计划(基础)：默认等于下年初始计划(基础)*/
        nextYearBaseEndPlan = nextYearBaseStartPlan;
        /**5.下年终计划(定额)：默认等于下年初始计划(定额)*/
        nextYearQuotaEndPlan = nextYearQuotaStartPlan;
        /**6：各季度计划*/
        /**6.1.特殊用户33批次*/
        if (unitType.equals("33")) {
          //基础
          firstQuarterBase = 0;
          secondQuarterBase = Math.round(nextYearBaseEndPlan / 2);
          thirdQuarterBase = 0;
          fourthQuarterBase = secondQuarterBase;
          //定额
          firstQuarterQuota = 0;
          secondQuarterQuota = Math.round(nextYearQuotaEndPlan / 2);
          thirdQuarterQuota = 0;
          fourthQuarterQuota = secondQuarterQuota;

        } else {
          /**6.2.正常用户*/
          //基础
          double nextYearBaseEndPlan2 = Math
              .round(nextYearBaseEndPlan * algorithmBase.getSecondQuarterPro());
          double nextYearBaseEndPlan1 = Math
              .round((nextYearBaseEndPlan - (nextYearBaseEndPlan2 * 2)) / 2);
          //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
          firstQuarterBase = nextYearBaseEndPlan1;
          //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
          secondQuarterBase = nextYearBaseEndPlan2;
          thirdQuarterBase = nextYearBaseEndPlan2;
          fourthQuarterBase = nextYearBaseEndPlan1;
          //定额
          double nextYearQuotaEndPlan2 = Math
              .round(nextYearQuotaEndPlan * algorithmQuota.getSecondQuarterPro());
          double nextYearQuotaEndPlan1 = Math
              .round((nextYearQuotaEndPlan - (nextYearQuotaEndPlan2 * 2)) / 2);
          //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
          firstQuarterQuota = nextYearQuotaEndPlan1;
          //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
          secondQuarterQuota = nextYearQuotaEndPlan2;
          thirdQuarterQuota = nextYearQuotaEndPlan2;
          fourthQuarterQuota = nextYearQuotaEndPlan1;
        }
        //赋值
        map.put("sign", sign);
        map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
        map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
        map.put("firstQuarterBase", firstQuarterBase);
        map.put("secondQuarterBase", secondQuarterBase);
        map.put("thirdQuarterBase", thirdQuarterBase);
        map.put("fourthQuarterBase", fourthQuarterBase);

        map.put("nextYearQuotaStartPlan", nextYearQuotaStartPlan);
        map.put("nextYearQuotaEndPlan", nextYearQuotaEndPlan);
        map.put("firstQuarterQuota", firstQuarterQuota);
        map.put("secondQuarterQuota", secondQuarterQuota);
        map.put("thirdQuarterQuota", thirdQuarterQuota);
        map.put("fourthQuarterQuota", fourthQuarterQuota);
      }
    }
    return list;
  }

  private List<Map<String, Object>> nowYearPlanOld(int year, String userType, String unitCodeStart,
      String userId, String nodeCode) {

    return baseMapper.nowYearPlanOld(year, userType, unitCodeStart, userId, nodeCode);
  }

  /**
   *
   */
  private boolean findPlanRecord(int year, String nodeCode) {
    /**这里不需要查询人员的类型权限，因为保存编制初始化第一次保存一定是保存所有*/
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("node_code", nodeCode);
    wrapper.eq("plan_year", year);
    int count = useWaterPlanService.count(wrapper);
    return count > 0 ? false : true;
  }

  /**
   * 数据不能被10整除，进10的方法
   */
  private Double ceil(Double param) {
    if (Math.ceil(param) % 10 != 0) {
      param =
          Math.ceil(param) + (10 - Math.ceil(param) % 10);
    } else {
      param = Math.ceil(param);
    }
    return param;
  }

  private Map<String, Object> initPlanOldPage(int year, String userType, String unitCodeStart,
      String userId, String nodeCode, Integer current, Integer size) {
    /**初始化sql返回的数据*/
    Map<String, Object> page = new HashMap<>();
    List<Map<String, Object>> list = baseMapper
        .initPlanOldPage(year, userType, unitCodeStart, userId, nodeCode, current, size);
    /**初始化算法(基础)*/
    Algorithm algorithmBase = algorithmService.queryAlgorithm(nodeCode, "1");
    /**初始化算法(定额)*/
    Algorithm algorithmQuota = algorithmService.queryAlgorithm(nodeCode, "1");
    if (!list.isEmpty()) {
      double fourthQuarterQuota;
      for (Map<String, Object> map : list) {
        //当年年计划
        double curYearPlan;
        //当年基建计划
        double curYearBasePlan;
        //第三年编制基础
        double baseWaterAmount;
        //三年平均
        double threeYearAvg;
        //水价
        double nowPrice;
        //n8
        double n8;
        //标识
        String sign = "0";
        //下年初始计划(基础)
        double nextYearBaseStartPlan = 0;
        //下年终计划(基础)
        double nextYearBaseEndPlan;
        //下年初始计划(定额)
        double nextYearQuotaStartPlan;
        //下年终计划(定额)
        double nextYearQuotaEndPlan;
        //第一季度(基础)
        double firstQuarterBase;
        //第二季度(基础)
        double secondQuarterBase;
        //第三季度(基础)
        double thirdQuarterBase;
        //第四季度(基础)
        double fourthQuarterBase;
        //第一季度(定额)
        double firstQuarterQuota;
        //第二季度(定额)
        double secondQuarterQuota;
        //第三季度(定额)
        double thirdQuarterQuota;
        //第四季度(定额)
        curYearPlan = (Double) map.get("curYearPlan");
        curYearBasePlan = (Double) map.get("curYearBasePlan");
        baseWaterAmount = (Double) map.get("baseWaterAmount");
        threeYearAvg = (Double) map.get("threeYearAvg");
        nowPrice = (Double) map.get("nowPrice");
        nextYearQuotaStartPlan = (Double) map.get("nextYearQuotaStartPlan");
        //33批次
        String unitCode = (String) map.get("unitCode");
        //类型
        String unitType = unitCode.substring(2, 4);
        /**一：如三年平均水量大于0、本年年计划大于0*/
        if (Double.valueOf(threeYearAvg) != 0 && Double.valueOf(curYearPlan) != 0) {
          /**1.计算n8:(第三年编制基础-当年年计划-当年基建计划)/当年年计划,结果4舍5入*/
          n8 = (baseWaterAmount - curYearPlan - curYearBasePlan) / curYearPlan;
          n8 = Math.round(n8 * 100);
          /**2.计算下年初始计划(基础)*/
          /**2.1.如：N8<n8Floot */
          if (n8 < algorithmBase.getN8Floot()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getBasePro();
          } else if (algorithmBase.getN8Up() >= n8 && n8 >= algorithmBase
              .getN8Floot()) {
            /**2.2：n8Up >= N8 && N8 >= n8Floot*/
            nextYearBaseStartPlan = curYearPlan;
          } else if (n8 > algorithmBase.getN8Up()) {
            if (nowPrice <= algorithmBase.getPriceBottom()) {
              /**2.3：N8>n8Up && nowPrice <= priceFloot*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro1();
            } else if (nowPrice > algorithmBase.getPriceBottom() && nowPrice <= algorithmBase
                .getPriceTop()) {
              /**2.4：N8>n8Up &&（nowPrice > priceFloot && nowPrice <= priceUp*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro2();
            } else if (nowPrice > algorithmBase
                .getPriceTop()) {
              /**2.5: N8>n8Up && nowPrice > priceUp*/
              nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro3();
            }
          }
          /**3.赋值*/
          map.put("n8", n8);
        }
        /**二：计算当三年平均水量大于0，但本年计划为0的时候得这种情况,该算法不计算N8*/
        else if (Double.valueOf(threeYearAvg) > 0 && curYearPlan == 0) {
          /**1. n8:不计算*/
          /**2.计算下年初始计划(基础)*/
          /**2.1. nowPrice <= priceFloot*/
          if (nowPrice <= algorithmBase.getPriceBottom()) {
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro1();
          } else if (nowPrice > algorithmBase.getPriceBottom() && nowPrice <= algorithmBase
              .getPriceTop()) {
            /**2.2. nowPrice > priceFloot && nowPrice <= priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro2();
          } else if (nowPrice > algorithmBase.getPriceTop()) {
            /**2.3. nowPrice > priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithmBase.getThreeAvgPro3();
          }
          /**3.赋值*/
          map.put("n8", null);
        }
        /**4.下年初始计划(基础),如结果不能被10整除，则需进10*/
        nextYearBaseStartPlan = ceil(nextYearBaseStartPlan);
        /**4.下年初计划(定额)，如结果不能被10整除，则需进10*/
        nextYearQuotaStartPlan = ceil(nextYearQuotaStartPlan);
        /**如果下年初始计划(基础)大于下年初计划(定额)，基础计划默认等于定额计划*/
        if (nextYearBaseStartPlan > nextYearQuotaStartPlan) {
          nextYearBaseStartPlan = nextYearQuotaStartPlan;
          //标识
          sign = "1";
        }
        /**5.下年终计划(基础)：默认等于下年初始计划(基础)*/
        nextYearBaseEndPlan = nextYearBaseStartPlan;
        /**5.下年终计划(定额)：默认等于下年初始计划(定额)*/
        nextYearQuotaEndPlan = nextYearQuotaStartPlan;
        /**6：各季度计划*/
        /**6.1.特殊用户33批次*/
        if (unitType.equals("33")) {
          //基础
          firstQuarterBase = 0;
          secondQuarterBase = Math.round(nextYearBaseEndPlan / 2);
          thirdQuarterBase = 0;
          fourthQuarterBase = secondQuarterBase;
          //定额
          firstQuarterQuota = 0;
          secondQuarterQuota = Math.round(nextYearQuotaEndPlan / 2);
          thirdQuarterQuota = 0;
          fourthQuarterQuota = secondQuarterQuota;

        } else {
          /**6.2.正常用户*/
          //基础
          double nextYearBaseEndPlan2 = Math
              .round(nextYearBaseEndPlan * algorithmBase.getSecondQuarterPro());
          double nextYearBaseEndPlan1 = Math
              .round((nextYearBaseEndPlan - (nextYearBaseEndPlan2 * 2)) / 2);
          //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
          firstQuarterBase = nextYearBaseEndPlan1;
          //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
          secondQuarterBase = nextYearBaseEndPlan2;
          thirdQuarterBase = nextYearBaseEndPlan2;
          fourthQuarterBase = nextYearBaseEndPlan1;
          //定额
          double nextYearQuotaEndPlan2 = Math
              .round(nextYearQuotaEndPlan * algorithmQuota.getSecondQuarterPro());
          double nextYearQuotaEndPlan1 = Math
              .round((nextYearQuotaEndPlan - (nextYearQuotaEndPlan2 * 2)) / 2);
          //第一季度：(下年终计划(基础)-(第二季度计划*2))/2 ;结果4舍5入
          firstQuarterQuota = nextYearQuotaEndPlan1;
          //第二季度：下年终计划(基础)*第二季度(用水)比例,结果4舍5入
          secondQuarterQuota = nextYearQuotaEndPlan2;
          thirdQuarterQuota = nextYearQuotaEndPlan2;
          fourthQuarterQuota = nextYearQuotaEndPlan1;
        }
        //赋值
        map.put("sign", sign);
        map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
        map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
        map.put("firstQuarterBase", firstQuarterBase);
        map.put("secondQuarterBase", secondQuarterBase);
        map.put("thirdQuarterBase", thirdQuarterBase);
        map.put("fourthQuarterBase", fourthQuarterBase);

        map.put("nextYearQuotaStartPlan", nextYearQuotaStartPlan);
        map.put("nextYearQuotaEndPlan", nextYearQuotaEndPlan);
        map.put("firstQuarterQuota", firstQuarterQuota);
        map.put("secondQuarterQuota", secondQuarterQuota);
        map.put("thirdQuarterQuota", thirdQuarterQuota);
        map.put("fourthQuarterQuota", fourthQuarterQuota);
      }
    }
    page.put("records", list);
    page.put("current", current);
    page.put("size", size);
    long count = baseMapper
        .initPlanOldCount(year, userType, unitCodeStart, userId, nodeCode, current, size);
    page.put("total", count);
    page.put("page", count % size == 0 ? count / size : count / size + 1);
    return page;
  }

  private Map<String, Object> nowYearPlanOldPage(int year, String userType,
      String unitCodeStart,
      String userId, String nodeCode, Integer current, Integer size) {
    Map<String, Object> page = new HashMap<>();
    List<Map<String, Object>> list = baseMapper
        .nowYearPlanOldPage(year, userType, unitCodeStart, userId, nodeCode, current, size);
    page.put("records", list);
    page.put("current", current);
    page.put("size", size);
    long count = baseMapper
        .nowYearPlanOldCount(year, userType, unitCodeStart, userId, nodeCode, current, size);
    page.put("total", count);
    page.put("page", count % size == 0 ? count / size : count / size + 1);
    return page;
  }

}