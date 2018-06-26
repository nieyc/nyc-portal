package com.github.nyc.portal.account.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.github.nyc.portal.util.HttpUtils;

@Service
public class TaskService {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	HttpUtils httpUtils;
	
	@Async
	public void asyncTask(String param) {
		 try {
		    Thread.sleep(10000);
		    logger.info("param:"+param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
