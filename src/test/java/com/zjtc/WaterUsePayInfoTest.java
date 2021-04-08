package com.zjtc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * WaterUsePayInfo的测试
 * 
 * @author 
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterUsePayInfoTest {

	/**
	 * 单元测试:查询所有WaterUsePayInfo数据的方法
	 * @throws Exception
	 */
	@Test
	public void findTest() throws Exception {
		//ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/WaterUsePayInfo")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
		String loginUserId="userId";
    String userType="userType";
    String sql = "select t3.id as invoiceId,t3.invoiceNumber,t3.isPrint,t3.invoiceStart,t3.invoiceEnd,t3.person,t3.drawer,t3.createTime,t2.unitName,t2.unitCode,t2.userType,t2.invoiceUnitName,t2.zipAddress,t2.department,t2.zipCode,t2.unitAddress,t2.zipUnitName,t2.mobileNumber,t2.phoneNumber,t2.waterMeterCode,t2.responsibilityCode,t5.isSigning,t5.isOtherBank,t5.agreementNumber,t5.bankOfDeposit,t5.bankAccount,t5.entrustUnitName,t2.contactMan,t5.focusUserRemark, " +
        "t1.*,t1.isPrint as payIsPrint,case payStatus when 1 then '已缴费' when 4 then '已缴费' else  '未缴费' end as payStr,t4.planYear,t4.curYearPlan,t4.oneQuarter,t4.twoQuarter,t4.threeQuarter,t4.fourQuarter,t6.auditPerson,t6.auditDate from  WaterUsePayInfo t1 left join UseWaterUnitInvoice" +
        " t3 on t1.id=t3.payInfoId and isEnable=0 join (SELECT uu.*,[waterMeterCode] = STUFF((SELECT ',' + [waterMeterCode] FROM UseWaterUnitMeter t WHERE useWaterUnitId = uu.id FOR XML PATH ('')),1, 1, '') FROM UseWaterUnit uu,UseWaterUnitRole ur " +
        " WHERE ur.personId='" + loginUserId + "' and uu.userType=ur.userTypeId "+(!userType.equals("")?"and uu.userType='"+userType+"'":"")+") t2 on  t1.useWaterUnitId = t2.id left join UserBank t5 on t2.id=t5.useWaterUnitId and t5.isMain='1' left join UseWaterBasePlan t4 on t4.useWaterUnitId=t2.id  and t4.planYear=t1.countYear left join (select t.payInfoId,max(t.auditDate) as auditDate,max(t.auditPerson) as auditPerson from WaterUserPayInfoAudit t  group by t.payInfoId) t6 on t6.payInfoId=t1.id "+
        "where 1=1 order by SUBSTRING(unitCode, 3, 2),unitCode,userType,countQuarter desc ";
    System.out.println(sql);
	}
	
	/**
	 * 单元测试:通过id查询WaterUsePayInfo数据的方法
	 * @throws Exception
	 */
	@Test
	public void findOneTest() throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT a.*, (@i := @i + 1) AS ORD_NUM ");
		sqlBuilder.append(" FROM( ");
		sqlBuilder.append("  SELECT BYNAME AS USER_ID,USER_NAME AS USER_NAME,DEPT_NAME AS DEPT_NAME FROM user_view WHERE BYNAME NOT IN ('admin') ");
		sqlBuilder.append(" ) a,(SELECT @i := 0) b ");
		System.out.println(sqlBuilder);
	}
	
	/**
	 * 单元测试:插入WaterUsePayInfo属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void saveTest() throws Exception {
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	
	/**
	 * 单元测试:更新WaterUsePayInfo属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
			// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}

	/**
	 * 单元测试:通过id删除WaterUsePayInfo数据方法
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	@Test
	public void init(){
		StringBuilder sql = new StringBuilder();
		String delOrSelParm="param";
		sql.append(" select id,actualAmount=Convert(decimal(18,2),t7.exceedWater * t7.price * t7.multiple),amountReceivable=Convert(decimal(18,2),t7.exceedWater * t7.price * t7.multiple),");
		sql.append(" countQuarter,countYear,exceedWater,multiple,payRatio,payStatus,payType,price,quarterPlan,");
		sql.append(" useWaterUnitId,waterNumber1,waterNumber2,waterNumber3,waterNumberCount,yearPlan,isWarning=0 from ");
		sql.append(" (select id=STUFF(STUFF(useWaterUnitId, 1, 0, countYear),1,0,countQuarter),t5.countQuarter,t5.countYear,t5.exceedWater,");
		sql.append(" (case when payRatio < 10 then 1 when  ( payRatio>=10 and payRatio<30) then 2 when payRatio >= 30 then 3 END) AS multiple,");
		sql.append(" t5.payRatio,payStatus =0,payType=0,t5.price,t5.quarterPlan,t5.useWaterUnitId,t5.waterNumber1,");
		sql.append(" t5.waterNumber2,t5.waterNumber3,t5.waterNumberCount,t5.yearPlan from ");
		sql.append(" (select t4.*,exceedWater=(waterNumberCount-quarterPlan), ");
		sql.append(" case quarterPlan when 0 then 0 else FLOOR(((waterNumberCount-quarterPlan)/quarterPlan)*100) end as payRatio from (SELECT t1.useWaterUnitId,t3.price,");
		sql.append(" CASE countQuarter WHEN 'oneQuarter' THEN 1 ");
		sql.append(" WHEN 'twoQuarter' THEN 2 WHEN 'threeQuarter' THEN 3 ");
		sql.append(" WHEN 'fourQuarter' THEN 4 END AS countQuarter,");
		sql.append(" CASE countQuarter WHEN 'oneQuarter' THEN (januaryCount+februaryCount+marchCount) ");
		sql.append(" WHEN 'twoQuarter' THEN (aprilCount+mayCount+juneCount) WHEN 'threeQuarter' THEN (julyCount+augustCount+septemberCount) ");
		sql.append(" WHEN 'fourQuarter' THEN (octoberCount+novemberCount+decemberCount) END AS waterNumberCount,");
		sql.append(" CASE countQuarter WHEN 'oneQuarter' THEN januaryCount ");
		sql.append(" WHEN 'twoQuarter' THEN aprilCount WHEN 'threeQuarter' THEN julyCount ");
		sql.append(" WHEN 'fourQuarter' THEN octoberCount END AS waterNumber1,");
		sql.append(" CASE countQuarter WHEN 'oneQuarter' THEN februaryCount ");
		sql.append(" WHEN 'twoQuarter' THEN mayCount WHEN 'threeQuarter' THEN augustCount ");
		sql.append(" WHEN 'fourQuarter' THEN novemberCount END AS waterNumber2,");
		sql.append(" CASE countQuarter WHEN 'oneQuarter' THEN marchCount ");
		sql.append(" WHEN 'twoQuarter' THEN juneCount WHEN 'threeQuarter' THEN septemberCount ");
		sql.append(" WHEN 'fourQuarter' THEN decemberCount END AS waterNumber3,quarterPlan,");
		sql.append(" planYear AS countYear,curYearPlan AS yearPlan FROM ");
		sql.append("  (select p.useWaterUnitId,p.planYear,curYearPlan=(p.curYearPlan+ISNULL(bp.curYearPlan,0)),");
		sql.append(" oneQuarter=(p.oneQuarter+ISNULL(bp.oneQuarter,0)),fourQuarter=(p.fourQuarter+ISNULL(bp.fourQuarter,0)),");
		sql.append(" twoQuarter=(p.twoQuarter+ISNULL(bp.twoQuarter,0)),threeQuarter=(p.threeQuarter+ISNULL(bp.threeQuarter,0))");
		sql.append(" from (select * from UseWaterPlan where planType=0) p left JOIN UseWaterBasePlan bp on  p.useWaterUnitId=bp.useWaterUnitId and p.planYear=bp.planYear)");
		sql.append(" as UseWaterPlan UNPIVOT ([quarterPlan] FOR [countQuarter] IN (");
		sql.append(" [oneQuarter],[twoQuarter],[threeQuarter],[fourQuarter])) AS t1");
		sql.append(" inner JOIN (SELECT SUM (januaryCount) AS januaryCount,SUM (februaryCount) AS februaryCount,");
		sql.append(" SUM (marchCount) AS marchCount,SUM (aprilCount) AS aprilCount,");
		sql.append(" SUM (mayCount) AS mayCount,SUM (juneCount) AS juneCount,");
		sql.append(" SUM (julyCount) AS julyCount,SUM (augustCount) AS augustCount,");
		sql.append(" SUM (septemberCount) AS septemberCount,SUM (octoberCount) AS octoberCount,");
		sql.append(" SUM (novemberCount) AS novemberCount,SUM (decemberCount) AS decemberCount,");
		sql.append(" min(nowPrice) as price,useYear,useWaterUnitId FROM");
		sql.append(" (SELECT nowPrice,case when unitCodeStart='33' then 0 else januaryCount end as januaryCount,");
		sql.append(" case when unitCodeStart='33' then 0 else februaryCount end as februaryCount,");
		sql.append(" case when unitCodeStart='33' then 0 else marchCount end as marchCount,");
		sql.append(" case when unitCodeStart='33' then januaryCount+aprilCount else aprilCount end as aprilCount,");
		sql.append(" case when unitCodeStart='33' then februaryCount+mayCount else mayCount end as mayCount,");
		sql.append(" case when unitCodeStart='33' then marchCount+juneCount else juneCount end as juneCount,");
		sql.append(" case when unitCodeStart='33' then 0 else julyCount end as julyCount,");
		sql.append(" case when unitCodeStart='33' then 0 else augustCount end as augustCount,");
		sql.append(" case when unitCodeStart='33' then 0 else septemberCount end as septemberCount,");
		sql.append(" case when unitCodeStart='33' then julyCount+octoberCount else octoberCount end as octoberCount,");
		sql.append(" case when unitCodeStart='33' then augustCount+novemberCount else novemberCount end as novemberCount,");
		sql.append(" case when unitCodeStart='33' then septemberCount+decemberCount else decemberCount end as decemberCount,");
		sql.append(" useYear, useWaterUnitId FROM WaterMonthUseDataHis pay,UseWaterUnit u where pay.useWaterUnitId=u.id) as WaterMonthUseData GROUP BY useYear,useWaterUnitId");
		sql.append(" ) t3 ON t1.useWaterUnitId = t3.useWaterUnitId AND t3.useYear = t1.planYear) t4) t5 where exceedWater>0) t7 where t7.id not in (select t.id from WaterUsePayInfo t where (t.payStatus=1 or t.payStatus=5 or t.isUpAr=1)) "+delOrSelParm);
System.out.println(sql);
	}
}