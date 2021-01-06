package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterOriginalPlanMapper;
import com.zjtc.model.Algorithm;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.service.AlgorithmService;
import com.zjtc.service.UseWaterOriginalPlanService;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    boolean result = this.insertBatch(entity);
    return result;
  }

  @Override
  public boolean saveOriginal(JSONObject jsonObject) {
    List<UseWaterOriginalPlan> entity = jsonObject.getJSONArray("data")
        .toJavaList(UseWaterOriginalPlan.class);
    /**1.更新用水计划原始表状态为已编制*/
    /**2.数据保存至用水计划原始表*/
    /**3.保存至用水计划表*/
    /**4.保存至自平表*/
    /**待办：Todo*/
    /**公众号：Todo*/
    return false;
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
  public List<Map<String, Object>> goPlanning(JSONObject jsonObject) {
    //用户类型
    String userType = jsonObject.getString("userType");
    //编号开头
    String unitCodeStart = jsonObject.getString("unitStart");
    //年份
    int year = jsonObject.getInteger("year");
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);
    if (0 == Integer.valueOf(year) || year == nowYear) {
      //默认当前年
      year = nowYear;
      if (nowMonth >= 9) {//如果当前的月份大于十月，开始下年计划编制
        year++;
      }
    }
    List<Map<String, Object>> list = null;
    //如果没有编制过则初始化，否则直接查询UseWaterPlan表数据
    if (findPlanRecord(year)) {
      System.out.print("===编制初始化");
      list = initPlan(year, userType, unitCodeStart, jsonObject.getString("userId"),
          jsonObject.getString("nodeCode"));
    } else {
      System.out.print("===编制查询");
      list = nowYearPlan(year, userType, unitCodeStart);
    }
    return list;
  }

  private List<Map<String, Object>> initPlan(int year, String userType, String unitCodeStart,
      String userId, String nodeCode) {
    /**初始化编制算法返回的数据*/
    List<Map<String, Object>> list = baseMapper
        .initPlan(year, userType, unitCodeStart, userId, nodeCode);
    /**初始化算法*/
    Wrapper algorithmWrapper = new EntityWrapper();
    algorithmWrapper.eq("node_code", nodeCode);
    algorithmWrapper.eq("algorithm_type", "1");
    Algorithm algorithm = algorithmService.selectOne(algorithmWrapper);
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
        double nextYearBaseEndPlan ;
        //下年初始计划(定额)
        double nextYearQuotaStartPlan ;
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
            if (nowPrice <= algorithm.getPriceTop()) {
              /**2.3：N8>n8Up && nowPrice <= priceFloot*/
              nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
            } else if (nowPrice > algorithm.getPriceTop() && nowPrice <= algorithm
                .getPriceBottom()) {
              /**2.4：N8>n8Up &&（nowPrice > priceFloot && nowPrice <= priceUp*/
              nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
            } else if (nowPrice > algorithm
                .getPriceBottom()) {
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
          if (nowPrice <= algorithm.getPriceTop()) {
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro1();
          } else if (nowPrice > algorithm.getPriceTop() && nowPrice <= algorithm.getPriceBottom()) {
            /**2.2. nowPrice > priceFloot && nowPrice <= priceUp*/
            nextYearBaseStartPlan = threeYearAvg * algorithm.getThreeAvgPro2();
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

  private List<Map<String, Object>> nowYearPlan(int year, String userType, String unitCodeStart) {

    return baseMapper.nowYearPlan(year, userType, unitCodeStart);
  }

  private boolean findPlanRecord(int year) {
    return false;
  }
}