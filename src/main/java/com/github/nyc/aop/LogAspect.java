package com.github.nyc.aop;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.github.nyc.portal.common.response.Results;


@Aspect
@Component
public class LogAspect {
	
	private Logger logger = LogManager.getLogger(getClass());
    
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    
    @Pointcut("execution(public * com.github.nyc.portal..*.*(..))")
    public void check(){}

    @Before("check()")
    @Order(10)
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    	 startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null) {
            HttpServletRequest request = attributes.getRequest();
            	 // 记录下请求内容
	          logger.info("http请求 : " + request.getRequestURL().toString()+"  方法 : " + request.getMethod()+"  IP地址 : " + request.getRemoteAddr());
	          logger.info("类和方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+"  入参 : " + Arrays.toString(joinPoint.getArgs()));
        }
        }

    @AfterReturning(returning = "ret", pointcut = "check()")
    public void doAfterReturning(Results ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE: " + "ret.getResCode()"+ret.getResCode()+"   ret.getResMsg():"+ret.getResMsg()+"   ret.getResults():"+ret.getResults());
        logger.info("执行总时间 : " + (System.currentTimeMillis() - startTime.get())+"毫秒");
    }

}
