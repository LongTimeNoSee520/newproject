package com.zjtc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JOSEException;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterQuotaService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuyantian
 * @date 2020/12/17
 * @description
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResoourcesTests {
  @Autowired
  private UseWaterQuotaService useWaterQuotaService;
  @Test
  public void bb() throws JOSEException {
    String json="{\n"
        + " \"quotaFile\":[\n"
        + "        {\n"
        + "            \"subIndustryOption\":[\n"
        + "                {\n"
        + "                    \"dictItemName\":\"通用仪器仪表制造\",\n"
        + "                    \"dictItemCode\":\"b3156a33a9c14585bd52393b1661c100\",\n"
        + "                    \"id\":\"b3156a33a9c14585bd52393b1661c100\",\n"
        + "                    \"nodeCode\":\"510100\",\n"
        + "                    \"parentId\":\"b3156a33a9c14585bd52393b16337\",\n"
        + "                    \"parentName\":\"仪器仪表制造业\",\n"
        + "                    \"industryName\":\"通用仪器仪表制造\",\n"
        + "                    \"industryCode\":\"C401\",\n"
        + "                    \"quotaUnit\":null,\n"
        + "                    \"quotaValue\":null,\n"
        + "                    \"createTime\":\"2021-01-26\",\n"
        + "                    \"deleted\":\"0\",\n"
        + "                    \"deleteTime\":null,\n"
        + "                    \"remark\":null,\n"
        + "                    \"advanceValue\":null,\n"
        + "                    \"commonValue\":null,\n"
        + "                    \"quotaRate\":null,\n"
        + "                    \"children\":[\n"
        + "                        {\n"
        + "                            \"dictItemName\":\"量具\",\n"
        + "                            \"dictItemCode\":\"n3156a33a9c14585bd52393b16378\",\n"
        + "                            \"id\":\"n3156a33a9c14585bd52393b16378\",\n"
        + "                            \"nodeCode\":\"510100\",\n"
        + "                            \"parentId\":\"b3156a33a9c14585bd52393b1661c100\",\n"
        + "                            \"parentName\":\"通用仪器仪表制造\",\n"
        + "                            \"industryName\":\"量具\",\n"
        + "                            \"industryCode\":\"C4013\",\n"
        + "                            \"quotaUnit\":\"m3/万件\",\n"
        + "                            \"quotaValue\":null,\n"
        + "                            \"createTime\":\"2022-02-07\",\n"
        + "                            \"deleted\":\"0\",\n"
        + "                            \"deleteTime\":null,\n"
        + "                            \"remark\":null,\n"
        + "                            \"advanceValue\":200,\n"
        + "                            \"commonValue\":300,\n"
        + "                            \"quotaRate\":1\n"
        + "                        }\n"
        + "                    ]\n"
        + "                },\n"
        + "                {\n"
        + "                    \"dictItemName\":\"钟表与计时仪器制造\",\n"
        + "                    \"dictItemCode\":\"b3156a33a9c14585bd52393b1661c101\",\n"
        + "                    \"id\":\"b3156a33a9c14585bd52393b1661c101\",\n"
        + "                    \"nodeCode\":\"510100\",\n"
        + "                    \"parentId\":\"b3156a33a9c14585bd52393b16337\",\n"
        + "                    \"parentName\":\"仪器仪表制造业\",\n"
        + "                    \"industryName\":\"钟表与计时仪器制造\",\n"
        + "                    \"industryCode\":\"C403\",\n"
        + "                    \"quotaUnit\":null,\n"
        + "                    \"quotaValue\":null,\n"
        + "                    \"createTime\":\"2021-01-26\",\n"
        + "                    \"deleted\":\"0\",\n"
        + "                    \"deleteTime\":null,\n"
        + "                    \"remark\":null,\n"
        + "                    \"advanceValue\":null,\n"
        + "                    \"commonValue\":null,\n"
        + "                    \"quotaRate\":null,\n"
        + "                    \"children\":[\n"
        + "                        {\n"
        + "                            \"dictItemName\":\"手表\",\n"
        + "                            \"dictItemCode\":\"n3156a33a9c14585bd52393b16379\",\n"
        + "                            \"id\":\"n3156a33a9c14585bd52393b16379\",\n"
        + "                            \"nodeCode\":\"510100\",\n"
        + "                            \"parentId\":\"b3156a33a9c14585bd52393b1661c101\",\n"
        + "                            \"parentName\":\"钟表与计时仪器制造\",\n"
        + "                            \"industryName\":\"手表\",\n"
        + "                            \"industryCode\":\"C4030\",\n"
        + "                            \"quotaUnit\":\"m3/万只\",\n"
        + "                            \"quotaValue\":null,\n"
        + "                            \"createTime\":\"2022-02-08\",\n"
        + "                            \"deleted\":\"0\",\n"
        + "                            \"deleteTime\":null,\n"
        + "                            \"remark\":null,\n"
        + "                            \"advanceValue\":500,\n"
        + "                            \"commonValue\":600,\n"
        + "                            \"quotaRate\":1\n"
        + "                        }\n"
        + "                    ]\n"
        + "                }\n"
        + "            ],\n"
        + "            \"productOption\":[\n"
        + "                {\n"
        + "                    \"dictItemName\":\"量具\",\n"
        + "                    \"dictItemCode\":\"n3156a33a9c14585bd52393b16378\",\n"
        + "                    \"id\":\"n3156a33a9c14585bd52393b16378\",\n"
        + "                    \"nodeCode\":\"510100\",\n"
        + "                    \"parentId\":\"b3156a33a9c14585bd52393b1661c100\",\n"
        + "                    \"parentName\":\"通用仪器仪表制造\",\n"
        + "                    \"industryName\":\"量具\",\n"
        + "                    \"industryCode\":\"C4013\",\n"
        + "                    \"quotaUnit\":\"m3/万件\",\n"
        + "                    \"quotaValue\":null,\n"
        + "                    \"createTime\":\"2022-02-07\",\n"
        + "                    \"deleted\":\"0\",\n"
        + "                    \"deleteTime\":null,\n"
        + "                    \"remark\":null,\n"
        + "                    \"advanceValue\":200,\n"
        + "                    \"commonValue\":300,\n"
        + "                    \"quotaRate\":1\n"
        + "                }\n"
        + "            ],\n"
        + "            \"subIndustry\":\"b3156a33a9c14585bd52393b1661c100\",\n"
        + "            \"product\":\"n3156a33a9c14585bd52393b16378\",\n"
        + "            \"quotaUnit\":\"m3/万件\",\n"
        + "            \"advanceValue\":200,\n"
        + "            \"commonValue\":300,\n"
        + "            \"quotaRate\":1,\n"
        + "            \"amount\":\"500\"\n"
        + "        }\n"
        + "    ]\n"
        + "}";
    JSONObject jsonObject= JSON.parseObject(json);
     List<UseWaterQuota> list=jsonObject.getJSONArray("quotaFile").toJavaList(UseWaterQuota.class);
    useWaterQuotaService.add(list,"0f7aeb4ed54b417fa3811e8d0ebcfe46","510100");
  }

  @Test
  public void aa() throws JOSEException {
    //用户生成token
//    User user = new User();
//    user.setId("329096824ef2ad51014f442278960336");
//    user.setNodeCode("510100000000");
//    user.setPassword("123456");
//    user.setUsername("管理员");
//    String token = jwtUtil.creatToken(user, jwtUtil.getPublicKey());
//    System.out.println(token);
//
//    //测试人员1，生成token
//    User user2 = new User();
//    user2.setNodeCode("510100000000");
//    user2.setId("4028839f4d506396014d5084d9bd0000");
//    user2.setPassword("1");
//    user2.setUsername("测试人员1");
//    String token2 = jwtUtil.creatToken(user2, jwtUtil.getPublicKey());
//    System.out.println(token2);
//
//    //测试人员2，生成token
//    User user3 = new User();
//    user3.setId("4028839f4d506396014d5084d9bd0001");
//    user3.setNodeCode("510100000000");
//    user3.setPassword("1");
//    user3.setUsername("测试人员2");
//    String token3 = jwtUtil.creatToken(user3, jwtUtil.getPublicKey());
//    System.out.println(token3);
//
//    //用户：吴蔚生成token
//    User user4 = new User();
//    user4.setNodeCode("510100000000");
//    user4.setId("4028839f4d506396014d5084d9bd004d");
//    user4.setPassword("1");
//    user4.setUsername("吴蔚");
//    String token4 = jwtUtil.creatToken(user4, jwtUtil.getPublicKey());
//    System.out.println(token4);

//    User user = new User();
//    user.setId("329096824ef2ad51014f442278960336");
//    user.setNodeCode("510100000000");
//    user.setPassword("123456");
//    user.setUsername("test1");
//    String token = jwtUtil.creatToken(user, jwtUtil.getPublicKey());
//    System.out.println(token);
//
    //测试人员1，生成token
    User user2 = new User();
    user2.setNodeCode("510100");
    user2.setId("4028839f4d506396014d5084d9bd0000");
    user2.setPassword("1");
    user2.setUsername("test2");
//    String token2 = jwtUtil.creatToken(user2, jwtUtil.getPublicKey());
//    System.out.println(token2);
//    StringBuilder sql = new StringBuilder();
//    sql.append(" MERGE INTO WaterMonthUseData w USING (SELECT max(useYear) as useYear, max(caliber) as caliber, max(useWaterUnitId) as useWaterUnitId, max(waterUseKinds) as waterUseKinds,");
//    sql.append(" max(sector) as sector, max(waterMeterCode) as waterMeterCode, id, max(price) as price, isnull(sum([1]), 0) AS januaryCount, isnull(sum([2]), 0) ");
//    sql.append(" AS februaryCount, isnull(sum([3]), 0) AS marchCount, isnull(sum([4]), 0) AS aprilCount, isnull(sum([5]), 0) AS mayCount, isnull(sum([6]),");
//    sql.append(" 0) AS juneCount, isnull(sum([7]), 0) AS julyCount, isnull(sum([8]), 0) AS augustCount, isnull(sum([9]), 0) AS septemberCount, isnull(sum([10]), ");
//    sql.append(" 0) AS octoberCount, isnull(sum([11]), 0) AS novemberCount, isnull(sum([12]), 0) AS decemberCount,MAX(unitName) AS unitNames,MAx(unitAddress) as unitAddresss FROM(SELECT id = (CAST (useYear AS VARCHAR(5))");
//    sql.append(" + waterMeterCode), waterUseKinds, useYear, waterNumber, useMonth, caliber, price, useWaterUnitId, sector, waterMeterCode,unitName,unitAddress FROM WaterUseData");
//    sql.append(" WHERE useYear = '2019'  AND useMonth IN ('1')) t1 PIVOT (max (waterNumber) FOR useMonth IN ([1],[2],[3],[4],[5],[6],[7],[8],[9],[10],[11],[12])");
//    sql.append(" ) pvt GROUP BY id) t2 on w.id = t2.id");
//    sql.append(" WHEN matched THEN UPDATE SET w.nowPrice = t2.price,");
//    sql.append(" w.isWarning=0,");
//    sql.append(1);
//    sql.append(" WHEN NOT matched THEN INSERT (isWarning, id, useYear, caliber, useWaterUnitId, sector, waterMeterCode,");
//    sql.append(" januaryCount, februaryCount, marchCount, aprilCount, mayCount, juneCount, julyCount, augustCount, septemberCount, octoberCount,");
//    sql.append(" novemberCount, decemberCount, nowPrice, waterUseKinds,unitNames,unitAddresss) VALUES(0,t2.id,t2.useYear, t2.caliber, t2.useWaterUnitId, t2.sector, t2.waterMeterCode,t2.januaryCount,");
//    sql.append(" t2.februaryCount, t2.marchCount, t2.aprilCount, t2.mayCount, t2.juneCount, t2.julyCount, t2.augustCount, t2.septemberCount,");
//    sql.append(" t2.octoberCount, t2.novemberCount, t2.decemberCount, t2.price, t2.waterUseKinds,t2.unitNames,t2.unitAddresss);");
//
//    System.out.println(sql);
    String a="123456";
  System.out.println(a.substring(1,2));
    System.out.println(a.substring(1,3));
  }


}
