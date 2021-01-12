package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterOriginalPlanMapper;
import com.zjtc.model.Algorithm;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.model.User;
import com.zjtc.service.AlgorithmService;
import com.zjtc.service.UseWaterOriginalPlanService;
import com.zjtc.service.UseWaterPlanService;
import com.zjtc.service.UseWaterSelfDefinePlanMapperService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TWUseWaterOriginalPlan的服务接口的实现类
 *
 * @author
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
  private UseWaterSelfDefinePlanMapperService useWaterSelfDefinePlanMapperService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse save(JSONObject jsonObject) {
    ApiResponse apiResponse = new ApiResponse();
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    for (UseWaterOriginalPlan item : entity) {
      //已编制的数据不能再编制
      if (item.getPlaned().equals("1")) {
        apiResponse.recordError("当前保存的数据中存在已编制数据");
        break;
      }
      item.setNodeCode(jsonObject.getString("nodeCode"));
    }
    if (200 != apiResponse.getCode()) {
      return apiResponse;
    }
    this.insertOrUpdateBatch(entity);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse saveOriginal(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    List<UseWaterPlan> useWaterPlanList = new ArrayList<>();
    List<UseWaterSelfDefinePlan> selfDefinePlanList = new ArrayList<>();
    //判断当前要编制的数据是否存在已编制的数据
    for (UseWaterOriginalPlan item : entity) {
      if (item.getPlaned().equals("1")) {
        apiResponse.recordError("当前要编制的数据中存在已编制数据");
        return apiResponse;
      }
      if ("0".equals(item.getAlgorithmType())) {
        apiResponse.recordError("请选择算法");
        break;
      }
      /**请选择季度,新户需要选择季度？*/
      if (0 == item.getAssessQuarter() && "1".equals(item.getAdded())) {
        apiResponse.recordError("请选择考核季度");
        break;
      }
      /**1.更新用水计划原始表状态为已编制*/
      item.setPlaned("1");
      //用水表计划需要的数据
      UseWaterPlan useWaterPlan = new UseWaterPlan();
      useWaterPlan.setCreateTime(new Date());
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
      useWaterPlan.setCreateTime(new Date());
      useWaterSelfDefinePlan.setAuditStatus("0");
      useWaterSelfDefinePlan.setAuditStatus("3");
      useWaterSelfDefinePlan.setNodeCode(useWaterPlan.getNodeCode());
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
      /**todo:定额算法*/
    }
    if (200 != apiResponse.getCode()) {
      return apiResponse;
    }
    /**2.数据保存至用水计划原始表*/
    this.insertOrUpdateBatch(entity);
    /**3.保存至用水计划表*/
    useWaterPlanService.insertBatch(useWaterPlanList);
    /**4.保存至自平表*/
    useWaterSelfDefinePlanMapperService.insertBatch(selfDefinePlanList);
    /**待办：Todo*/
    /**公众号：Todo*/
    return apiResponse;
  }


  @Override
  public boolean updateModel(JSONObject jsonObject) {
    UseWaterOriginalPlan entity = jsonObject.toJavaObject(UseWaterOriginalPlan.class);
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    UseWaterOriginalPlan entity = jsonObject.toJavaObject(UseWaterOriginalPlan.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public Page<UseWaterOriginalPlan> queryPage(JSONObject jsonObject) {
    return null;
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
    //初始化的数据
    list = baseMapper
        .initPlanNew(year, userId, nodeCode, ParamOne, ParamTwo, threeWaterMonth, fourWaterMonth);
    /**初始化基础算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(nodeCode, "1");
    if (!list.isEmpty()) {
      double threeYearAvg;
      double nowPrice;
      double nextYearBaseStartPlan = 0;
      double nextYearBaseEndPlan;
      double firstQuarterBase;
      double secondQuarterBase;
      double thirdQuarterBase;
      double fourthQuarterBase;
      //本年计划,默认等于下年终计划
      double curYearPlan;
      for (Map<String, Object> map : list) {
        threeYearAvg = (Double) map.get("threeYearAvg");
        nowPrice = (Double) map.get("nowPrice");
        String unitCode = (String) map.get("unitCode");
        //截取类型
        String unitType = unitCode.substring(0, 2);
        /**如果三年平均、水价不为空*/
        if (threeYearAvg != 0 && nowPrice != 0) {
          /**计算下年初计划(基础)*/
          //nowPrice <= priceFloot
          if (nowPrice <= algorithm.getPriceBottom()) {
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
            //nowPrice > priceFloot && nowPrice <= priceUp
          } else if (nowPrice > algorithm.getPriceBottom() && nowPrice <= algorithm.getPriceTop()) {
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
          } else if (nowPrice > algorithm.getPriceTop()) {
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro3();
          }
          /**进10*/
          if (Math.ceil(nextYearBaseStartPlan) % 10 != 0) {
            nextYearBaseStartPlan =
                Math.ceil(nextYearBaseStartPlan) + (10 - Math.ceil(nextYearBaseStartPlan) % 10);
          } else {
            nextYearBaseStartPlan = Math.ceil(nextYearBaseStartPlan);
          }
          /**下年终计划(基础) 默认等于下年初计划(基础)*/
          nextYearBaseEndPlan = nextYearBaseStartPlan;
          //本年计划，默认等于下年终计划
          curYearPlan = nextYearBaseEndPlan;
          /**特殊类型*/
          double nextYearPlan1;
          double nextYearPlan2;
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
            map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
            map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
            map.put("curYearPlan", curYearPlan);
            map.put("firstQuarterBase", firstQuarterBase);
            map.put("secondQuarterBase", secondQuarterBase);
            map.put("thirdQuarterBase", thirdQuarterBase);
            map.put("fourthQuarterBase", fourthQuarterBase);
          }
          /**三年平均为0*/
        } else if (threeYearAvg == 0) {
          map.put("nextYearBaseStartPlan", 0);
          map.put("nextYearBaseEndPlan", 0);
          map.put("firstQuarterBase", 0);
          map.put("secondQuarterBase", 0);
          map.put("thirdQuarterBase", 0);
          map.put("fourthQuarterBase", 0);
        }
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
    /**初始化算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    //下年终计划
    double nextYearBaseEndPlan = jsonObject.getDouble("nextYearBaseEndPlan");
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
    double nextYearBaseEndPlan = jsonObject.getDouble("nextYearBaseEndPlan");
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
        map.put("firstQuarterBase", firstQuarterBase);
        map.put("secondQuarterBase", secondQuarterBase);
        map.put("thirdQuarterBase", thirdQuarterBase);
        map.put("fourthQuarterBase", fourthQuarterBase);
      }
    }
    return map;
  }

  @Override
  public Map<String, Object> getOldResultByThreeYearAvg(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    /**初始化算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    double threeYearAvg = jsonObject.getDouble("threeYearAvg");
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
  public Map<String, Object> getNewResultByThreeYearAvg(JSONObject jsonObject, User user) {
    Map<String, Object> map = new HashMap<>();
    //水价
    double nowPrice = jsonObject.getDouble("nowPrice");
    //三年平均水量
    double threeYearAvg = jsonObject.getDouble("threeYearAvg");
    /**初始化基础算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(user.getNodeCode(), "1");
    //下年初计划初始值
    double nextYearBaseStartPlan = jsonObject.getDouble("nextYearBaseStartPlan");
    double nextYearBaseEndPlan = nextYearBaseStartPlan;
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
    nextYearBaseEndPlan = nextYearBaseStartPlan;
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
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("planed", "0");
    wrapper.eq("node_code", nodeCode);
    return this.delete(wrapper);
  }

  private List<Map<String, Object>> initPlanOld(int year, String userType, String unitCodeStart,
      String userId, String nodeCode) {
    /**初始化编制算法返回的数据*/
    List<Map<String, Object>> list = baseMapper
        .initPlanOld(year, userType, unitCodeStart, userId, nodeCode);
    /**初始化算法*/
    Algorithm algorithm = algorithmService.queryAlgorithm(nodeCode, "1");
    if (!list.isEmpty()) {
      for (Map<String, Object> map : list) {
        //当年年计划
        double curYearPlan = (Double) map.get("curYearPlan");
        //当年基建计划
        double curYearBasePlan = (Double) map.get("curYearBasePlan");
        //第三年编制基础
        double baseWaterAmount = (Double) map.get("baseWaterAmount");
        //第二年
        double beforeLastYearWaterAmount = (Double) map.get("beforeLastYearWaterAmount");
        //第一年
        double lastYearWaterAmount = (Double) map.get("lastYearWaterAmount");
        //三年平均
        double threeYearAvg = (Double) map.get("threeYearAvg");
        //水价
        double nowPrice = (Double) map.get("nowPrice");
        //n8
        double n8;
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
        double fourthQuarterQuota;
        //33批次
        String unitCode = (String) map.get("unitCode");
        String unitType = unitCode.substring(2, 4);
        /**一：如三年平均水量大于0、本年年计划大于0*/
        if (Double.valueOf(threeYearAvg) != 0 && Double.valueOf(curYearPlan) != 0) {
          /**1.计算n8:(第三年编制基础-当年年计划-当年基建计划)/当年年计划,结果4舍5入*/
          n8 = (baseWaterAmount - curYearPlan - curYearBasePlan) / curYearPlan;
          n8 = Math.round(n8 * 100);
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
          /**3.赋值*/
          map.put("n8", n8);
        }
        /**二：计算当三年平均水量大于0，但本年计划为0的时候得这种情况,该算法不计算N8*/
        else if (Double.valueOf(threeYearAvg) > 0 && curYearPlan == 0) {
          /**1. n8:不计算*/
          /**2.计算下年初始计划(基础)*/
          /**2.1. nowPrice <= priceFloot*/
          if (nowPrice <= algorithm.getPriceBottom()) {
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
          } else if (nowPrice > algorithm.getPriceBottom() && nowPrice <= algorithm.getPriceTop()) {
            /**2.2. nowPrice > priceFloot && nowPrice <= priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
          } else if (nowPrice > algorithm.getPriceTop()) {
            /**2.3. nowPrice > priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro3();
          }
          /**3.赋值*/
          map.put("n8", null);
        }
        /**4.下年初始计划(基础),如结果不能被10整除，则需进10*/
        if (Math.ceil(nextYearBaseStartPlan) % 10 != 0) {
          nextYearBaseStartPlan =
              Math.ceil(nextYearBaseStartPlan) + (10 - Math.ceil(nextYearBaseStartPlan) % 10);
        } else {
          nextYearBaseStartPlan = Math.ceil(nextYearBaseStartPlan);
        }
        /**5.下年终计划(基础)：默认等于下年初始计划(基础)*/
        nextYearBaseEndPlan = nextYearBaseStartPlan;
        /**6：各季度计划*/
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
        /**定额算法：Todo*/
        //赋值
        map.put("nextYearBaseStartPlan", nextYearBaseStartPlan);
        map.put("nextYearBaseEndPlan", nextYearBaseEndPlan);
        map.put("firstQuarterBase", firstQuarterBase);
        map.put("secondQuarterBase", secondQuarterBase);
        map.put("thirdQuarterBase", thirdQuarterBase);
        map.put("fourthQuarterBase", fourthQuarterBase);
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
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", nodeCode);
    wrapper.eq("plan_year", year);
    int count = useWaterPlanService.selectCount(wrapper);
    return count > 0 ? false : true;
  }
}