package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.EndPaper;
import com.zjtc.model.User;
import com.zjtc.model.vo.SendListVO;
import java.util.List;
import java.util.Map;


/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 */
public interface EndPaperService extends IService<EndPaper> {

  /**
   * 分页查询
   */
  Map<String, Object> queryPage(User user, JSONObject jsonObject);

  /**
   * 撤销办结
   */
  ApiResponse cancelSettlement(List<String> ids);

  /**
      * 办结单审核
   */
  void examineSettlement(User user, JSONObject jsonObject) throws Exception;

  /**
   * 办结单执行
   */
  ApiResponse executeSettlement(User user, JSONObject jsonObject) throws Exception;

  /**
   * 微信办结单
   */
  boolean updateFromWeChat(EndPaper endPaper);

  /**
   * 查询下一环节可提交审核的角色人员
   * @param id 当前退减免单id
   * @param nodeCode 节点便编码
   * @param auditBtn 按钮 ：0 不同意，1 同意
   * @return
   */
  List<Map<String,Object>> nextAuditRole(String id, String nodeCode, String auditBtn);

  /**
   * 分页查询调整调整发送列表
   * @param user
   * @param jsonObject
   * @return
   */
  Map<String,Object> sendInfoPage(User user, JSONObject jsonObject);

  /**
   * 调整结果通知
   * @param user
   * @param sendList
   */
  void adjustResultNotification(User user, List<SendListVO> sendList) throws Exception;
}
