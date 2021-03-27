package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterUseData;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lianghao
 * @date 2020/12/26
 */
public interface WaterQuantityManageService extends IService<WaterUseData> {

	/**
	 * 分页查询
	 * @param user，jsonObject
	 * @return
	 */
	Map<String,Object> queryPage(User user, JSONObject jsonObject);

	/**
	 * 模板下载
	 * @param request，response
	 * @return
	 */
	void downloadTemplate(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 分片上传
	 * @param file
	 * @return
	 */
	boolean zoneUpload(MultipartFile file,String fileProcessId,int chunk);
	/**
	 * 合并
	 * @param fileProcessId
	 * @return
	 */
	boolean merge(String fileProcessId);

	/**
	 * 数据检查
	 * @param fileProcessId
	 * @return
	 */
	ApiResponse checkAndInsertData(User user,String fileProcessId,String fileName) throws Exception;

	/**
	 *原始数据导入完后，将导入的数据写入水量月使用数据表中
	 *
   * @param user
   * @param fileProcessId
	 * */
  void importEnd(User user, String fileProcessId);

	void insertMonthData(User user, String fileProcessId);
}
