package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterSys.DiyMenuMapper;
import com.zjtc.model.DiyMenu;
import com.zjtc.service.DiyMenuService;
import org.springframework.stereotype.Service;

/**
 * DiyMenu的服务接口的实现类
 *
 * @author
 */
@Service
public class DiyMenuServiceImpl extends ServiceImpl<DiyMenuMapper, DiyMenu> implements
    DiyMenuService {

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    DiyMenu entity = jsonObject.toJavaObject(DiyMenu.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    DiyMenu entity = jsonObject.toJavaObject(DiyMenu.class);
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    DiyMenu entity = jsonObject.toJavaObject(DiyMenu.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public Page<DiyMenu> queryPage(JSONObject jsonObject) {
    DiyMenu entity = jsonObject.toJavaObject(DiyMenu.class);
    Page<DiyMenu> page = new Page<DiyMenu>(jsonObject.getInteger("current"),
        jsonObject.getInteger("size"));
    page.setSearchCount(true);
    page.setOptimizeCountSql(true);
    EntityWrapper<DiyMenu> eWrapper = new EntityWrapper<DiyMenu>(entity);
    Page<DiyMenu> result = this.selectPage(page, eWrapper);
    return result;
  }

}