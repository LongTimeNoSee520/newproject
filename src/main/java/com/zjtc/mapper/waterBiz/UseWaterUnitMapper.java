package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.vo.OrgTreeVO;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.model.vo.UseWaterUnitVo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Mapper
public interface UseWaterUnitMapper extends BaseMapper<UseWaterUnit> {

  /**
   * 查询当前节点编码、当前批次,节点编码后三位最大值
   */
  String maxUnitCode(@Param("unitCode") String unitCode,
      @Param("id") String id, @Param("nodeCode") String nodeCode);

  /**
   * 分页
   */
  List<UseWaterUnitVo> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   */
  long queryListTotal(JSONObject jsonObject);

  /**
   * 详情界面：关联单位查询
   *
   * @param jsonObject 单位id
   */
  UseWaterUnitVo selectById(JSONObject jsonObject);

  /**
   * @param ids    单位id集合
   * @param UserId 用户id
   * @param id     当前单位id
   */
  List<UseWaterUnitRefVo> queryUnitRef(@Param("ids") List<String> ids,
      @Param("nodeCode") String nodeCode, @Param("userId") String UserId,
      @Param("notIn") String id
  );

  /**
   * 用水单位批量
   *
   * @param ids       关联的单位编号
   * @param updateSql 修改sql
   */
  boolean updateUseWaterUnit(
      @Param("refs") List<String> ids,
      @Param("sql") String updateSql
  );

  /**
   * 用水单位批量
   *
   * @param ids       关联的单位编号
   * @param updateSql 修改sql
   */
  boolean updateMen(
      @Param("refs") List<String> ids,
      @Param("sql") String updateSql
  );

  /**
   * 计划用水户账户审查表导出数据查询 所有未签约、且存在银行账号的数据
   */
  List<Map<String, Object>> exportAccountAudit(String nodeCode);

  /**
   * 计划用水户导出开通格式查询：签约成功的用水户; 付款人名称一期取的法人名称，这里不再取
   *
   * @param nodeCode 节点编码
   */
  List<Map<String, Object>> exportForm(String nodeCode);

  /**
   * 计划用水户导出撤销格式查询：撤销成功的用水户;
   *
   * @param jsonObject 节点编码
   */
  List<Map<String, Object>> exportRevoca(JSONObject jsonObject);

  /**
   * 导出用水单位增减情况表
   *
   * @param nodeCode 节点编码
   */
  List<Map<String, Object>> exportMoreAndLess(@Param("nodeCode") String nodeCode,
      @Param("userId") String userId);

  List<Map<String, Object>> exportQueryData(JSONObject jsonObject);

  /**
   * 单位id集合
   */
  List<Map<String, Object>> selectByIds(@Param("ids") List<String> ids);

  Map<String, Object> selectByUnitCode(@Param("unitCode") String unitCode,
      @Param("nodeCode") String nodeCode);

  /**
   * 根据单位名称和实收金额模糊匹配单位名称和单位编号
   * @param userId 登录者id
   * @param nodeCode 节点编码
   * @param unitName 单位名称
   * @param actualAmount 实收金额
   * @return
   */
  List<Map<String, Object>> selectCodeByName(@Param("userId") String userId,
      @Param("nodeCode") String nodeCode, @Param("unitName") String unitName,
      @Param("actualAmount") Double actualAmount);

  List<String> selectAllType(@Param("nodeCode") String nodeCode);

  /**
   * 数字大屏 查询节水中心：中间地图区域标点
   */
  List<Map<String, Object>> selectUnitMap(JSONObject jsonObject);

  /**
   * 数字大屏 根据用水单位id查询用水户信息
   */
  Map<String, Object> selectUnitById(JSONObject jsonObject);


  /**
   * 数字大屏 查询左侧管理户数、计划用水量、实际用水量
   */
  List<Map<String, Object>> selectLeftData(JSONObject jsonObject);

  /**
   * 查询所有的用水单位类型
   */
  List<OrgTreeVO> selectUnitCode(@Param("nodeCode") String nodeCode,
      @Param("condition") String condition, @Param("userId") String userId);

  /**
   * 查询用水单位信息
   */
//  List<OrgTreeVO> selectByTypeUnitAll(@Param("type") String type,
//      @Param("condition") String condition,@Param("userId")String userId,@Param("nodeCode")String nodeCode);

  /**
   * 查询用水单位信息
   */
  List<OrgTreeVO> selectByTypeUnitAll(@Param("nodeCode") String nodeCode,
      @Param("fathers") List<OrgTreeVO> fathers);


  /**
   * 新增[修改]界面：相关编号下拉回填数据
   */
  List<Map<String, Object>> addUnitCodePage(JSONObject jsonObject);

  long addUnitCodeTotal(JSONObject jsonObject);

  /**
   * 新增[修改]界面：相关编号下拉回填数据
   */
  List<Map<String, Object>> addUnitCodeList(@Param("nodeCode") String nodeCode,
      @Param("userId") String userId);


  /**
   * 异步刷新是否是节水单位
   *
   * @param unitCode 单位编号
   */
  boolean refreshSaveUnitType(@Param("unitCode") String unitCode,
      @Param("createTime") Date createTime);

  /**
   * 异步刷新是否经过水平衡测试
   *
   * @param unitCode 单位编号
   */
  boolean refreshWaterBalance(@Param("unitCode") String unitCode,
      @Param("createTime") Date createTime);
}
