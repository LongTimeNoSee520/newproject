package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.vo.EndPaperVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * EndPaper的Dao接口
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 *
 */
@Mapper
public interface EndPaperMapper extends BaseMapper<EndPaper> {

  int queryNum(Map<String, Object> map);

  List<EndPaperVO> queryPage(Map<String, Object> map);
}