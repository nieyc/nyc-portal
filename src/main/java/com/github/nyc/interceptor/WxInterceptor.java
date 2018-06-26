package com.github.nyc.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.github.nyc.portal.common.response.ResultCode;
import com.github.nyc.portal.common.response.Results;
import com.github.nyc.portal.util.ResponseUtil;

public class WxInterceptor implements HandlerInterceptor {
	
	
private Logger logger = LogManager.getLogger(getClass());
    
/*	@Autowired
	private RedisTemplate<String, Object> redisTemplate;*/
	
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (handler instanceof HandlerMethod) {
            	HandlerMethod handlerMethod = (HandlerMethod) handler;	
            	Whitelist whitelist = handlerMethod.getMethod().getAnnotation(Whitelist.class);
            	if (whitelist == null) {
    				whitelist = handlerMethod.getBeanType().getAnnotation(Whitelist.class);
    			}
            	//logger.info("whitelist:"+whitelist);
            	
            	if(whitelist==null) {
            	    String token=request.getParameter("token");
                    Results result = new Results();
                    if(!StringUtils.isEmpty(token)) {
                    	//logger.debug("user:"+redisTemplate.opsForHash().get(token, "user"));
                    /*	if(redisTemplate.opsForHash().get(token, "user")==null) {
        	            	 result.setResCode(ResultCode.ERROR);
        	           		 result.setResMsg("fail");
        	           		 result.setResults("token is invalid");
        	           		 ResponseUtil.sendJsonMessage(response, result);
        	           		 return false;
                    	}else {
                    		   redisTemplate.expire(token, 30, TimeUnit.MINUTES);
                    	}*/
                    	logger.info("token is:"+token);
                    }else {
                    	 result.setResCode(ResultCode.ERROR);
                		 result.setResMsg("fail");
                		 result.setResults("token is null");
                		 ResponseUtil.sendJsonMessage(response, result);
                		 return false;
                    }
            		
            	}
            	
            }
        return true;
    }
   
   // @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       // logger.info(">>>MyInterceptor>>>>>>>璇锋眰澶勭悊涔嬪悗杩涜璋冪敤锛屼絾鏄湪瑙嗗浘琚覆鏌撲箣鍓嶏紙Controller鏂规硶璋冪敤涔嬪悗锛夛級");
    }

   // @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.info(">>>MyInterceptor>>>>>>>鍦ㄦ暣涓姹傜粨鏉熶箣鍚庤璋冪敤锛屼篃灏辨槸鍦―ispatcherServlet 娓叉煋浜嗗搴旂殑瑙嗗浘涔嬪悗鎵ц锛堜富瑕佹槸鐢ㄤ簬杩涜璧勬簮娓呯悊宸ヤ綔锛�");
    }

}
