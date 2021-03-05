package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.zjtc.mapper.waterBiz.AlgorithmMapper;
import com.zjtc.model.Algorithm;
import com.zjtc.service.AlgorithmService;
import com.zjtc.service.UseWaterOriginalPlanService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Algorithm的服务接口的实现类
 *
 * @author
 */
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements
    AlgorithmService {
  @Autowired
  private UseWaterOriginalPlanService useWaterOriginalPlanService;

  @Override
  public boolean saveOrUpdate(List<Algorithm> algorithms, String nodeCode) {
    if (StringUtils.isNotEmpty(nodeCode) && algorithms != null && algorithms.size() > 0) {
      long result = 0;
      for (Algorithm algorithm : algorithms) {
        algorithm.setNodeCode(nodeCode);
        result = algorithm.getId() != null ? this.baseMapper.updateNotNull(algorithm) :
            this.baseMapper.insert(algorithm);
      }
      //todo 调整用水计划数据
      //
      boolean falg= useWaterOriginalPlanService.deleteAllNotplaned(nodeCode);
      return result > 0 ? true : false && falg;
    }
    return false;
  }

  @Override
  public List<Algorithm> queryList(String nodeCode, List<String> algorithmTypes) {
    if (StringUtils.isNotEmpty(nodeCode) && algorithmTypes != null && algorithmTypes.size() > 0) {
      List<Algorithm> algorithms = new ArrayList<>();
      for (String type : algorithmTypes) {
        algorithms.add(this.queryAlgorithm(nodeCode, type));
      }
      return algorithms;
    }
    return null;
  }

  @Override
  public Algorithm queryAlgorithm(String nodeCode, String algorithmType) {
    Wrapper condition = new EntityWrapper();
    condition.eq("node_code", nodeCode);
    condition.eq("algorithm_type", algorithmType);
    List<Algorithm> algorithms = this.baseMapper.selectList(condition);
    return algorithms != null ? algorithms.get(0) : null;
  }
}