package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.UseWaterPlanAdd;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
public interface UseWaterPlanAddService extends IService<UseWaterPlanAdd> {

  /**
   * 修改备注
   */
  void updateRemarks(String id, String remarks);

  /**
   * 行内编辑修改调整计划
   *
   * @param useWaterPlanAdds
   * @return
   */
  void updatePlanAdd(List<UseWaterPlanAdd> useWaterPlanAdds);


  /**
   * 修改调整表数据状态或者打印状态
   */
  boolean updateStatusOrPrinted(String id, String status, String printed);

}
