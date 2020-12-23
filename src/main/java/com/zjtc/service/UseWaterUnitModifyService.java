package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnitModify;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 用水单位名称修改日志
 */
public interface UseWaterUnitModifyService extends IService<UseWaterUnitModify> {

  /**
   * 判断单位名称是否被修改
   * @param id 单位编号
   * @param nodeCode 节点编码
   * @param unitName 单位名称
   * @param personName 当前操作的人名
   * @param personId 当前操作人的id
   * @return 添加是否成功
   */
  boolean insertUnitName(String id,String nodeCode,String unitName,String personName,String personId);

  /**
   * 查询修改的日志
   * @param id 部门id
   * @param nodeCode 节点编码
   * @return 日志结果集
   */
 List<UseWaterUnitModify> selectUseWaterUnitModify(String id,String nodeCode);
}
