package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.EndPaper;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;


/**
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 *
 */
public interface EndPaperService extends IService<EndPaper> {

 /**
  * 分页查询
  * */
  Map<String,Object> queryPage(User user, JSONObject jsonObject);
  /**
   * 撤销办结
   * */
  void cancelSettlement(List<String> ids);
}
