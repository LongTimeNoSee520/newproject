package com.zjtc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * WaterSavingUnit的测试
 * 
 * @author 
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WaterSavingUnitTest {
	@Autowired
	private MockMvc mvc;

	/**
	 * 单元测试:查询所有WaterSavingUnit数据的方法
	 * @throws Exception
	 */
	@Test
	public void findTest() throws Exception {
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/WaterSavingUnit")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	
	/**
	 * 单元测试:通过id查询WaterSavingUnit数据的方法
	 * @throws Exception
	 */
	@Test
	public void findOneTest() throws Exception {
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/WaterSavingUnit/{id}")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	
	/**
	 * 单元测试:插入WaterSavingUnit属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void saveTest() throws Exception {
		ResultActions action = mvc.perform(MockMvcRequestBuilders.post("/WaterSavingUnit")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
	
	/**
	 * 单元测试:更新WaterSavingUnit属性不为空的数据方法
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
		ResultActions action = mvc.perform(MockMvcRequestBuilders.put("/WaterSavingUnit")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}

	/**
	 * 单元测试:通过id删除WaterSavingUnit数据方法
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
		ResultActions action = mvc.perform(MockMvcRequestBuilders.delete("/WaterSavingUnit/{id}")).andExpect(MockMvcResultMatchers.status().isOk());
		// TODO 当你看到这个后你应该自己修改模板编写测试内容规则
	}
}