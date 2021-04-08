package com.zjtc;

import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.SmsSendService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RefundOrRefund的测试
 * 
 * @author 
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RefundOrRefundTest {

	@Autowired
	private FlowNodeInfoService flowNodeInfoService;
  @Autowired
	private JWTUtil jwtUtil;
	/**
	 * 单元测试:查询所有RefundOrRefund数据的方法
	 * @throws Exception
	 */
	@Test
	public void findTest() throws Exception {
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
		//System.out.println(flowNodeInfoService.firStAuditRole("payFlow","510100"));
		//flowNodeInfoService.selectAndInsert("510100","111111","payFlow");
		System.out.println(flowNodeInfoService.firstAuditRole("payFlow","510100"));
		System.out.println(flowNodeInfoService.secondAuditRole("payFlow","510100"));
	}
	
	/**
	 * 单元测试:通过id查询RefundOrRefund数据的方法
	 * @throws Exception
	 */
	@Test
	public void findOneTest() throws Exception {
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
		Map<String,String> param =new HashMap<>();
		param.put("aa","");
		param.put("aa","b");
		System.out.println(param);
	}
	
	/**
	 * 单元测试:插入RefundOrRefund属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void saveTest() throws Exception {
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	
	/**
	 * 单元测试:更新RefundOrRefund属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
		//ResultActions action = mvc.perform(MockMvcRequestBuilders.put("/RefundOrRefund")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}

	@Autowired
	private SmsSendService smsSendService;
@Autowired
private WaterUsePayInfoService waterUsePayInfoService;
	/**
	 * 单元测试:通过id删除RefundOrRefund数据方法
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
//		List<SmsSendStatusVo> param=new ArrayList<>();
//		SmsSendStatusVo smsSendStatusVo1=new SmsSendStatusVo();
//		smsSendStatusVo1.setUnitCode("20");
//		param.add(smsSendStatusVo1);
//		SmsSendStatusVo smsSendStatusVo2=new SmsSendStatusVo();
//		smsSendStatusVo2.setUnitCode("30");
//		param.add(smsSendStatusVo2);
//		System.out.println(smsSendService.queryAll(param));
	}

	@Test
	public void roleMess() throws Exception {

		//eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjpudWxsLCJpZCI6IjQ0NDQ0MjAyMTAxMjIxMDU0MTciLCJub2RlQ29kZSI6IjUxMDEwMCIsInVzZXJuYW1lIjpudWxsfQ.3vdzNu8A8mZS43e5BtkMTo26WUGcM9g_OzxOeTIMRtk
		User user=new User();
		user.setId("44444");
		user.setNodeCode("510100");
		System.out.println(jwtUtil.creatToken(user,jwtUtil.getPublicKey()));
		User user1=new User();
		//eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjpudWxsLCJpZCI6IjU1NTU1MjAyMTAxMjMxOTAyMTUiLCJub2RlQ29kZSI6IjUxMDEwMCIsInVzZXJuYW1lIjpudWxsfQ.w2e4M3ET4ngYJFlRbV0xU8BTHV4o7QxpsVD-F9JbHds
		user1.setId("55555");
		user1.setNodeCode("510100");
		System.out.println(jwtUtil.creatToken(user1,jwtUtil.getPublicKey()));
		//eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjpudWxsLCJpZCI6IjY2NjY2MjAyMTAxMjMxOTAyMTUiLCJub2RlQ29kZSI6IjUxMDEwMCIsInVzZXJuYW1lIjpudWxsfQ.41q_WHYFpSiPGMPqKrkkNZUDY8xcOohY0V3p_7ms2OY
		User user2=new User();
		user2.setId("66666");
		user2.setNodeCode("510100");
		System.out.println(jwtUtil.creatToken(user2,jwtUtil.getPublicKey()));
//eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjpudWxsLCJpZCI6IjY2NjY2MjAyMTAxMjYxNzM4MjIiLCJub2RlQ29kZSI6bnVsbCwidXNlcm5hbWUiOm51bGx9.rKdpTMdiDrqn9XTUrsFNxmGTXjU_GBK0KouESFLS_8c
    User user3=new User();
    user3.setId("66666");
    System.out.println(jwtUtil.creatToken(user3,jwtUtil.getPublicKey()));
		//eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjpudWxsLCJpZCI6IjIwMmVhNDQwNWFiNzQ5N2U5MDNhMWFjYmRkNGNjOTFjMjAyMTAyMjUxODA5MDkiLCJub2RlQ29kZSI6IjUxMDEwMCIsInVzZXJuYW1lIjpudWxsfQ.sF2BMaDwCW-d7KCPUdBe_zQSkppFg2YXTvSt_JqdsIQ
		User user6=new User();
		user6.setId("202ea4405ab7497e903a1acbdd4cc91c");
		user6.setNodeCode("510100");
		System.out.println(jwtUtil.creatToken(user6,jwtUtil.getPublicKey()));


	}
}