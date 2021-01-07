package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlanAdd;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
public interface UseWaterPlanAddService extends IService<UseWaterPlanAdd> {

  /**
   * 修改备注
   * */
  void updateRemarks(String id, String remarks);

  /**
   * 行内编辑修改调整计划
   * @param useWaterPlanAdds
   * @return
   */
  void updatePlanAdd(List<UseWaterPlanAdd> useWaterPlanAdds);

  /**
   * 分页
   *
   * @param jsonObject 参数
   * @return 响应状态
   */
  ApiResponse queryPage(JSONObject jsonObject, String nodeCode,String userId);

  /**
   * 修改调整表数据状态或者打印状态
   * */
  boolean updateStatusOrPrinted(String id,String status, String printed);

  /**
   * 打印办结单
   * @param ids 数据集
   * @return 响应状态
   */
  ApiResponse printed(List<String> ids);
}
