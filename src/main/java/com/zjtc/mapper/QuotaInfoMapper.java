package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.QuotaInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 定额信息维护
 * @author lianghao
 * @date 2020/12/23
 */
@Mapper
public interface QuotaInfoMapper extends BaseMapper<QuotaInfo> {

  /**
   * 定额信息修改
   * */
  boolean update(@Param("quotaInfo") QuotaInfo quotaInfo);

  /**
   * 定额信息删除
   * */
  boolean delete(@Param("ids") List<String> ids);

  /**
   * 查询全部
   * */
  List<QuotaInfo> queryAll(@Param("ids") List<String> ids);

  /**
   * 通过关键词查询满足条件的行业id，父级id
   * */
  List<QuotaInfo> selectByKeyword(@Param("keyword")String keyword);
}