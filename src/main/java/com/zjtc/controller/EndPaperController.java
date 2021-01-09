package com.zjtc.controller;


import com.zjtc.base.util.JWTUtil;
import com.zjtc.service.EndPaperService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * EndPaper的路由接口服务
 * 
 * @author Justin DaBo
 *
 */
@RestController
@RequestMapping("endPaper")
@Api(description = "xxx rest服务")
@Slf4j
public class EndPaperController {

	/** EndPaperService服务 */
	@Autowired
	private EndPaperService endPaperService;

	@Autowired
  private JWTUtil jwtUtil;
}
