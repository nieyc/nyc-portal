package com.github.nyc.portal.account.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.github.nyc.interceptor.Whitelist;
import com.github.nyc.portal.account.service.TaskService;
import com.github.nyc.portal.common.response.ResultCode;
import com.github.nyc.portal.common.response.Results;
import com.github.nyc.portal.util.ResponseUtil;

@Controller
@RequestMapping("/account")
public class AccountController {
	private Logger logger = LogManager.getLogger(getClass());
	
/*	@Autowired
	private RedisTemplate<String, Object> redisTemplate;*/
	
	@Autowired
	ResponseUtil responseUtil;
	
	@Autowired
	TaskService taskService;
	
	@Value("${innerPicPath}")
	private String innerPicPath;
	

	
	
	/**
	 * @whitelist 代表白名单，方法是否需要校验   @see com.github.nyc.interceptor.WxInterceptor
	 * http://localhost:8080/nyc-portal/account/login.do?loginName=1&passWord=2
	 * @param loginName
	 * @param passWord
	 * @return
	 */
	@Whitelist
	@RequestMapping("/login")
	@ResponseBody
	public Object login(String loginName, String passWord) {
		Results result = new Results();
		if (StringUtils.isEmpty(loginName)) {
			result = responseUtil.responseMsg(ResultCode.NUll_EXCEPTION, "loginName is null");
			return result;
		}

		if (StringUtils.isEmpty(passWord)) {
			result = responseUtil.responseMsg(ResultCode.NUll_EXCEPTION, "passWord is null");
			return result;
		}
		logger.info("login success:"+innerPicPath);
		//调用一个异步方法
		taskService.asyncTask("123");
		JSONObject json = new JSONObject();
		result.setResCode(ResultCode.SUCCESS);
		result.setResMsg("success");
		result.setResults(json);
		return result;

	}

}
