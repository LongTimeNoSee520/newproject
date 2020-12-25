package com.zjtc.service;


import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Algorithm;
import java.util.List;


/**
 * Algorithm的服务接口
 * 
 * @author 
 *
 */
public interface AlgorithmService extends IService<Algorithm> {

	/**
	* 新增或修改
	* @param algorithms 基础算法、定额算法
	* @return
	*/
	boolean saveOrUpdate(List<Algorithm> algorithms,String nodeCode);


	/**
	 * 查询对应节点下的基础算法和定额算法
	 *
	 * @param nodeCode
	 * @param algorithmTypes 算法类型
	 * @return 算法集合
	 */
	List<Algorithm> queryList(String nodeCode,List<String> algorithmTypes);

	/**
	 * 根据算法类型查询算法
	 *
	 * @param
	 * @return 算法对象
	 */
	Algorithm queryAlgorithm(String nodeCode,String algorithmType);

}
