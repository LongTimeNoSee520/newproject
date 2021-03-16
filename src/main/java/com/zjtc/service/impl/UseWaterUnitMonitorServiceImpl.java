package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.ColorCellValue;
import com.zjtc.mapper.waterBiz.UseWaterUnitMonitorMapper;
import com.zjtc.model.UseWaterUnitMonitor;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterMonitorExportVO;
import com.zjtc.service.CommonService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.UseWaterUnitMonitorService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author lianghao
 * @date 2021/01/15
 */
@Service
public class UseWaterUnitMonitorServiceImpl extends
    ServiceImpl<UseWaterUnitMonitorMapper, UseWaterUnitMonitor> implements
    UseWaterUnitMonitorService {

  @Autowired
  private CommonService commonService;
  @Autowired
  private SystemLogService systemLogService;

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    Map<String, Object> result = new HashMap<>();
    Integer size = jsonObject.getInteger("size");
    Integer current = jsonObject.getInteger("current");
   // String nodeCode = user.getNodeCode();
    String userId = user.getId();
    String monitorType = jsonObject.getString("monitorType");
    String unitName = jsonObject.getString("unitName");
    String unitCode = jsonObject.getString("unitCode");
    String unitCodeType = jsonObject.getString("unitCodeType");//用户类型
    Integer year = jsonObject.getInteger("year");
    String industryId = jsonObject.getString("industryId");
    String mobileNumber = jsonObject.getString("mobileNumber");
    String contacts = jsonObject.getString("contacts");

    Map<String, Object> map = new HashMap<>();
    map.put("size", size);
    map.put("current", current);
    map.put("monitorType", monitorType);
    //    map.put("nodeCode", nodeCode);
    if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
      map.put("nodeCode", jsonObject.getString("nodeCode"));
    }else{
      map.put("nodeCode", user.getNodeCode());
    }
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitCodeType)) {
      map.put("unitCodeType", unitCodeType);
    }
    if (null != year) {
      map.put("year", year);
    }
    if (StringUtils.isNotBlank(industryId)) {
      map.put("industryId", industryId);
    }
    if (StringUtils.isNotBlank(mobileNumber)) {
      map.put("mobileNumber", mobileNumber);
    }
    if (StringUtils.isNotBlank(contacts)) {
      map.put("contacts", contacts);
    }
    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    List<Map<String, Object>> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  public ApiResponse add(User user, UseWaterUnitMonitor monitor) {
    ApiResponse response = new ApiResponse();
    /**查询是否已经存在该单位信息*/
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.eq("unit_code", monitor.getUnitCode());
    wrapper.eq("monitor_type", monitor.getMonitorType());
    wrapper.eq("year", monitor.getYear());
    List<UseWaterUnitMonitor> monitors = this.selectList(wrapper);
    if(!monitors.isEmpty()){
      response.recordError("该单位该年监控信息已存在，不能再次添加");
      return  response;
    }
    monitor.setCreatePersonId(user.getId());
    monitor.setCreateTime(new Date());
    monitor.setDeleted("0");//未删除状态
    monitor.setNodeCode(user.getNodeCode());
    this.baseMapper.insert(monitor);
    /**日志*/
    systemLogService.logInsert(user,"用水单位监控","新增",null);
    return  response;
  }

  @Override
  public void delete(List<String> ids) {
   this.baseMapper.updateDeleted(ids);
  }

  @Override
  public void initNextYear(User user, String monitorType) {
    String nodeCode = user.getNodeCode();
    String userId = user.getId();
    this.baseMapper.initNextYear(userId,nodeCode,monitorType);
    /**日志*/
    systemLogService.logInsert(user,"用水单位监控","初始化下一年数据",null);
  }

  @Override
  public ApiResponse export(User user, JSONObject jsonObject,
      HttpServletRequest request,
     HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
  //  String nodeCode = user.getNodeCode();
    String userId = user.getId();
    String monitorType = jsonObject.getString("monitorType");//监控类型:1重点,2节水型
    String unitName = jsonObject.getString("unitName");
    String unitCode = jsonObject.getString("unitCode");
    String unitCodeType = jsonObject.getString("unitCodeType");//用户类型
    Integer year = jsonObject.getInteger("year");
    String industryId = jsonObject.getString("industryId");
    Map<String, Object> map = new HashMap<>();
    map.put("monitorType", monitorType);
    //    map.put("nodeCode", nodeCode);
    if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
      map.put("nodeCode", jsonObject.getString("nodeCode"));
    }else{
      map.put("nodeCode", user.getNodeCode());
    }
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitCodeType)) {
      map.put("unitCodeType", unitCodeType);
    }
    if (null != year) {
      map.put("year", year);
    }
    if (StringUtils.isNotBlank(industryId)) {
      map.put("industryId", industryId);
    }
   List <UseWaterMonitorExportVO> monitors = this.baseMapper.selectExportData(map);
    if (monitors.isEmpty()){
      apiResponse.recordError("没有数据需要导出");
      return apiResponse;
    }
    Map<String, Object> data = new HashMap<>(8);
    data.put("monitors",monitors);

    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("exportTime", dateFmt.format(new Date()));
    //根据数字显示不同颜色的方法
    ColorCellValue color = new ColorCellValue();
    data.put("color",color);//单元格内调用方法是写法${color.showColor(m.firstQuarterRate)}
    String fileName = "用水单位监控情况一览表.xls";
    String operateModule ="";
    if ("1".equals(monitorType)){
      data.put("titleStart",year+"重点");
      fileName=year+"重点用水单位监控情况一览表.xls";
      operateModule="重点用水单位监控";
    }else if ("2".equals(monitorType)){
      data.put("titleStart",year+"节水型");
      fileName=year+"节水型用水单位监控情况一览表.xls";
      operateModule="节水型用水单位监控";
    }
    final String template = "template/WaterUnitMonitoring.xls";
    boolean result = commonService.export(fileName,template,request,response,data);
    if (!result){
      apiResponse.recordError("导出出错");
    }
    /**日志*/
    systemLogService.logInsert(user,operateModule,"导出",null);
    return apiResponse;
  }


}