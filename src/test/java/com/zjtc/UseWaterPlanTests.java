package com.zjtc;

import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.UseWaterUnitService;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UseWaterPlanTests {

  @Autowired
  private UseWaterUnitRefService useWaterUnitRefService;

  @Autowired
  private UseWaterUnitService useWaterUnitService;
  @Autowired
  private UseWaterUnitMeterService useWaterUnitMeterService;

  /**
   * 一期计划编制sql
   * @throws NoSuchMethodException
   * @throws NoSuchFieldException
   */
  @Test
  public void test() throws NoSuchMethodException, NoSuchFieldException {
    int year=2020;
    String firstBatch = "unitCodeStart<=" + 61 + " or unitCodeStart>" + 63;
    String secondBatch = "unitCodeStart="+ 62;
    String thirdBatch = "unitCodeStart=" + 63;
    StringBuilder sql = new StringBuilder();
    String oneYearPro=" oneYearPro";
    String twoYearPro=" twoYearPro";
    String threeYearPro=" threeYearPro";
    sql.append(" SELECT [waterMeterCode] = STUFF((SELECT ',' + [waterMeterCode] FROM UseWaterUnitMeter WHERE useWaterUnitId = u.id FOR XML PATH ('')),1, 1, ''),");
    sql.append("u.id AS useWaterUnitId,isFirst = 0,planYear="+year+", balanceTest = 0,addBalanceTest = 0, saveTypeUnit = 0, addSaveTypeUnit = 0, payStatus = 0,u.userType,");
    sql.append(" u.unitCode, u.unitName, u.isSaveTypeUnit, curYearPlan, curYearBasePlan, nowPrice, t3.baseWaterAmount,waterAmount2,waterAmount1, CASE WHEN " + thirdBatch + " THEN");
    sql.append(" CEILING(t3.baseWaterAmount) WHEN " + secondBatch + " THEN CEILING(t3.baseWaterAmount*" + oneYearPro + " + waterAmount2*" + (twoYearPro + threeYearPro) + ")");
    sql.append(" WHEN " + firstBatch + " THEN CEILING(ISNULL(t3.baseWaterAmount,0)*" + oneYearPro + "  + ISNULL(waterAmount1,0)*" + threeYearPro + " + ISNULL(waterAmount2,0)*" + twoYearPro + ")");
    sql.append(" END AS threeYearAvg FROM(SELECT MAX (nowPrice) AS nowPrice, useWaterUnitId, SUM (topThree + fourth) AS baseWaterAmount FROM(SELECT");
    sql.append(" t1.useWaterUnitId, t1.nowPrice, topThree, fourth FROM(SELECT nowPrice, useWaterUnitId, waterMeterCode, topThree = (januaryCount +");
    sql.append(" februaryCount + marchCount + aprilCount + mayCount + juneCount + julyCount + augustCount + septemberCount)FROM WaterMonthUseDataHis w");
    sql.append(" WHERE useYear = ?) t1 LEFT JOIN (SELECT useWaterUnitId, waterMeterCode, fourth = (octoberCount + novemberCount + decemberCount)FROM");
    sql.append(" WaterMonthUseDataHis w WHERE useYear = ?) t2 ON t1.waterMeterCode = t2.waterMeterCode) t11 GROUP BY t11.useWaterUnitId) t3 RIGHT JOIN (");
    sql.append(" SELECT uu.id, uu.unitCode,uu.unitCodeOrder,uu.unitCodeStart, uu.unitName,uu.isSaveTypeUnit,uu.userType FROM UseWaterUnit uu WHERE uu.isDelete = 0");
    sql.append(" ) u ON u.id = t3.useWaterUnitId  LEFT JOIN (SELECT useWaterUnitId FROM UseWaterPlan pp WHERE planYear = ?");
    sql.append(" ) p ON u.id = p.useWaterUnitId LEFT JOIN (select p.usewaterunitid,p.curYearPlan ,ISNULL(bp.curYearPlan,0) as curYearBasePlan from (select ");
    sql.append(" usewaterunitid,curYearPlan from UseWaterPlan where planType=0 and planYear=?) p left JOIN (select curYearPlan,usewaterunitid from UseWaterBasePlan");
    sql.append(" where planyear=?) bp on  p.useWaterUnitId=bp.useWaterUnitId) t4 ON t4.useWaterUnitId = t3.useWaterUnitId LEFT JOIN (SELECT useWaterUnitId, waterAmount2 = (");
    sql.append(" SUM (januaryCount) + SUM (februaryCount) + SUM (marchCount) + SUM (aprilCount) + SUM (mayCount) + SUM (juneCount) + SUM (julyCount) +");
    sql.append(" SUM(augustCount) + SUM(septemberCount) + SUM(octoberCount) + SUM(novemberCount) + SUM(decemberCount))FROM WaterMonthUseDataHis");
    sql.append(" WHERE useYear = ? GROUP BY useWaterUnitId) t00 ON t00.useWaterUnitId = u.id LEFT JOIN (SELECT useWaterUnitId, waterAmount1 = (SUM (januaryCount)");
    sql.append(" + SUM (februaryCount) + SUM (marchCount) + SUM (aprilCount) + SUM (mayCount) + SUM (juneCount) + SUM (julyCount) + SUM (augustCount) +");
    sql.append(" SUM(septemberCount) + SUM(octoberCount) + SUM(novemberCount) + SUM(decemberCount))FROM WaterMonthUseDataHis WHERE useYear = ? GROUP BY");
    sql.append(" useWaterUnitId) t01 ON u.id = t01.useWaterUnitId where ISNULL(p.useWaterUnitId, '1')='1' ");

    System.out.println(sql);
  }

  @Test
  public void test2() throws NoSuchFieldException {
    Map<String ,Object> map=new HashMap<>();
    map.put("test","");
    double aa=(Double)(map.get("test"));
    System.out.println(aa);
  }

  @Test
  public void test3(){
   String  threeWaterMonth = "januaryCount + februaryCount + marchCount";
   String   fourWaterMonth= "octoberCount + novemberCount + decemberCount";
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT threeYearAvg=baseWaterAmount*2,[waterMeterCode] = STUFF((SELECT ',' + [waterMeterCode] FROM UseWaterUnitMeter WHERE useWaterUnitId = u.id FOR XML PATH ('')),");
    sql.append("1,1,''),u.id AS useWaterUnitId,balanceTest = 0,addBalanceTest = 0,saveTypeUnit = 0,addSaveTypeUnit = 0,payStatus = 0,u.unitCode,");
    sql.append("u.unitName,u.isSaveTypeUnit,curYearPlan,curYearBasePlan,nowPrice,t3.baseWaterAmount FROM(SELECT MAX (nowPrice) AS nowPrice,useWaterUnitId,");
    sql.append("SUM (three + four) AS baseWaterAmount FROM(SELECT t12.useWaterUnitId,t12.nowPrice,three,four FROM(SELECT nowPrice,useWaterUnitId,waterMeterCode,");
    sql.append("three = ("+threeWaterMonth+") ");
    sql.append("FROM WaterMonthUseDataHis w WHERE useYear = ?) t12 INNER JOIN (SELECT useWaterUnitId,waterMeterCode,");
    sql.append("four = ("+fourWaterMonth+") ");
    sql.append("FROM WaterMonthUseDataHis w WHERE useYear = ?) t13 ON t12.waterMeterCode = t13.waterMeterCode) t11 GROUP BY t11.useWaterUnitId) t3 ");
    sql.append("RIGHT JOIN (SELECT uu.id,uu.unitCode,uu.unitCodeOrder,uu.unitCodeStart,uu.unitName,uu.isSaveTypeUnit FROM UseWaterUnit uu ");
    sql.append("WHERE uu.isDelete = 0) u ON u.id = t3.useWaterUnitId LEFT JOIN (SELECT useWaterUnitId FROM OriginalUseWaterPlan pp WHERE planYear = ?) p ");
    sql.append("ON u.id = p.useWaterUnitId LEFT JOIN (SELECT p.usewaterunitid,p.curYearPlan,ISNULL(bp.curYearPlan, 0) AS curYearBasePlan FROM(");
    sql.append("SELECT usewaterunitid,curYearPlan FROM OriginalUseWaterPlan WHERE planType = 0 AND planYear = ?) p LEFT JOIN (SELECT curYearPlan,");
    sql.append("usewaterunitid FROM UseWaterBasePlan WHERE planyear = ?) bp ON p.useWaterUnitId = bp.useWaterUnitId) t4 ON ");
    sql.append("t4.useWaterUnitId = t3.useWaterUnitId WHERE ISNULL(p.useWaterUnitId, '1') = '1' ORDER BY unitCodeOrder,unitCode");
//    parameter.add(waterYear);
//    parameter.add(waterYear-1);
//    parameter.add(planYear);
//    parameter.add(planYear-1);
//    parameter.add(planYear-1);
    System.out.println(sql);
  }

  @Test
  public void test4(){
    String threeWaterMonth = "januaryCount + februaryCount + marchCount";
    String fourWaterMonth = "octoberCount + novemberCount + decemberCount";
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT threeYearAvg=isnull(baseWaterAmount*2,0),[waterMeterCode] = STUFF((SELECT ',' + [waterMeterCode] FROM UseWaterUnitMeter WHERE useWaterUnitId = u.id FOR XML PATH ('')),");
    sql.append("1,1,''),u.id AS useWaterUnitId,balanceTest = 0,addBalanceTest = 0,saveTypeUnit = 0,addSaveTypeUnit = 0,payStatus = 0,u.unitCode,");
    sql.append("u.unitName,u.isSaveTypeUnit,curYearPlan,curYearBasePlan,nowPrice,t3.baseWaterAmount FROM(SELECT MAX (nowPrice) AS nowPrice,useWaterUnitId,");
    sql.append("SUM (three + four) AS baseWaterAmount FROM(SELECT t12.useWaterUnitId,t12.nowPrice,three,(isnull((select four = ("+fourWaterMonth+") FROM WaterMonthUseDataHis w1 WHERE useYear = ? and w1.waterMeterCode=t12.waterMeterCode),0)) as four  FROM(SELECT nowPrice,useWaterUnitId,waterMeterCode,");
    sql.append("three = ("+threeWaterMonth+")");
    sql.append("FROM WaterMonthUseDataHis w WHERE useYear = ?) t12 ");
    sql.append(") t11 GROUP BY t11.useWaterUnitId) t3 ");
    sql.append("RIGHT JOIN (SELECT uu.id,uu.unitCode,uu.unitCodeOrder,uu.unitCodeStart,uu.unitName,uu.isSaveTypeUnit FROM UseWaterUnit uu ");
    sql.append("WHERE uu.isDelete = 0) u ON u.id = t3.useWaterUnitId LEFT JOIN (SELECT useWaterUnitId FROM UseWaterPlan pp WHERE planYear = ?) p ");
    sql.append("ON u.id = p.useWaterUnitId LEFT JOIN (SELECT p.usewaterunitid,p.curYearPlan,ISNULL(bp.curYearPlan, 0) AS curYearBasePlan FROM(");
    sql.append("SELECT usewaterunitid,curYearPlan FROM UseWaterPlan WHERE planType = 0 AND planYear = ?) p LEFT JOIN (SELECT curYearPlan,");
    sql.append("usewaterunitid FROM UseWaterBasePlan WHERE planyear = ?) bp ON p.useWaterUnitId = bp.useWaterUnitId) t4 ON ");
    sql.append("t4.useWaterUnitId = t3.useWaterUnitId WHERE ISNULL(p.useWaterUnitId, '1') = '1'  ORDER BY unitCodeOrder,unitCode");
    System.out.println(sql);
  }

  @Test
  public void test5(){
  }
}
