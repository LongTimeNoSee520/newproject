package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.File;
import com.zjtc.model.vo.FileVO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
public interface FileService extends IService<File> {

  /**
   * 附件上传
   *
   * @param
   */
  String uploadFile(MultipartFile file);

  /**
   * 删除附件
   */
  boolean removeBusinessId(String fileId);

  /**
   * 更新附件业务ID
   */
  boolean updateBusinessId(String businessId, List<File> sysAttrFiles);

  /**
   * 根据单个业务id批量删除附件
   */
  boolean removeByBusinessId(String businessId);

  /**
   * 根据多个业务id批量删除附件
   */
  boolean removeByBusinessIds(List<String> businessId);

  /**
   * 根据业务Id集合查询（完成全路径的拼接）
   *
   * @param fileContext 附件地址上下文
   */
  List<FileVO> findByBusinessIds(List<String> businessIds, String fileContext);

  /**
   * 根据Id集合查询（完成全路径的拼接）
   *
   * @param path 附件地址上下文
   */
  List<FileVO> findByIds(List<String> ids, String path);

  /**
   * 根据业务Id集合查询（完成全路径的拼接）
   *
   * @param businessId 业务id
   */
  File findByBusinessId(String businessId);
}
