package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.vo.EndPaperVO;
import com.zjtc.model.vo.SendListVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * EndPaper的Dao接口
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 */
@Mapper
public interface EndPaperMapper extends BaseMapper<EndPaper> {

  int queryNum(Map<String, Object> map);

  List<EndPaperVO> queryPage(Map<String, Object> map);

  /**
   * 更新
   */
  void update(Map<String, Object> map);

  /**
   * 微信端确认后更新数据
   */
  boolean updateFromWeChat(@Param("endPaper") EndPaper endPaper);

  /**
   * 根据id查询办结单信息(包括附件信息，审核流程信息)
   */
  EndPaper selectById(@Param("id") String id, @Param("preViewRealPath") String preViewRealPath);

  /**
   * 查询办结单信息
   */
  List<SendListVO> queryAfterAdjustUnit(Map<String, Object> map);
}