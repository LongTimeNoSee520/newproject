package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.QuotaInfoMapper;
import com.zjtc.model.QuotaInfo;
import com.zjtc.model.User;
import com.zjtc.service.QuotaInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/23
 */

@Service
public class QuotaInfoServiceImpl extends ServiceImpl<QuotaInfoMapper, QuotaInfo> implements
    QuotaInfoService {


  @Override
  public boolean add(User user, QuotaInfo quotaInfo) {
     String nodeCode = user.getNodeCode();
     quotaInfo.setCreateTime(new Date());
     quotaInfo.setDeleted("0");
     quotaInfo.setNodeCode(nodeCode);
     return this.insert(quotaInfo);
  }

  @Override
  public boolean edit(QuotaInfo quotaInfo) {
    boolean  result = this.baseMapper.update(quotaInfo);
    return result;
  }

  @Override
  public boolean delete(List<String> ids) {
    boolean result = this.baseMapper.delete(ids);
    return result;
  }

  @Override
  public List<QuotaInfo> queryTree(String keyword) {

    List<QuotaInfo> result = new ArrayList<>();

    if (null == keyword || "".equals(keyword)){//如果没有通过查询则查询全部
            result = this.baseMapper.queryAll(null);
    }else {
      sumIds = new ArrayList<>();
      List<String> ids  =new ArrayList<>();
      /**通过关键词查询满足条件的行业id*/
      List<QuotaInfo> keywordInfos = this.baseMapper.selectByKeyword(keyword);
      List<String> parentIds = new ArrayList<>();
      if(!keywordInfos.isEmpty()) {
        for (QuotaInfo quotaInfo : keywordInfos) {
          String id = quotaInfo.getId();
          sumIds.add(id);
          String parentId = quotaInfo.getParentId();
          if (null != parentId && !"".equals(parentId)) {
            parentIds.add(parentId);
          }
        }
        /**再通过parentIds递归查询上级行业id*/
        if (!parentIds.isEmpty()){
          getParentIds(parentIds);
        }
        /**id去重*/
        ids = new ArrayList<>(sumIds).stream().distinct().collect(Collectors.toList());
        /**根据id查询详细信息*/
        result = this.baseMapper.queryAll(ids);
      }else  {
        return result;
      }
    }
    return result;
  }


  private List<String> sumIds;
  private void getParentIds(List<String> parentIds) {
    sumIds.addAll(parentIds);
    List<String> ids = new ArrayList<>();
    List<QuotaInfo> quotaInfoList = this.baseMapper.queryAll(parentIds);
    if (!quotaInfoList.isEmpty()){
      for (QuotaInfo quotaInfo : quotaInfoList){
         String parentId = quotaInfo.getParentId();
        if (null != parentId && !"".equals(parentId)) {
          ids.add(parentId);
        }
      }
      if (ids.isEmpty()){
        return;
      }
      else {
        getParentIds(ids);
      }
    }
  }


}
