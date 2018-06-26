package com.github.nyc.interceptor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.github.nyc.interceptor.Whitelist.Category;
import com.github.nyc.util.Aes;
import com.github.nyc.util.Constants;
import com.github.nyc.util.ParamUtils;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器：
 *
 * @author nyc
 *
 */
public class CommonInterceptor implements HandlerInterceptor {
   
	private Logger logger = LogManager.getLogger(getClass());
    
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	private String key;
	public void setKey(String key) {
		this.key = key;
	}
	
	private String skfKey;
	public void setSkfKey(String skfKey) {
		this.skfKey = skfKey;
	}
	
	private String tokenKey;
	public void setTokenKey(String tokenKey) {
		this.tokenKey=tokenKey;
	}
	

	
	/**
	 * 接口请求超时时间
	 */
	private long timeout = 5 * 60 * 1000;
	
	/**
	 * 缓存
	 */
	private Map<Object, boolean[]> cache = new HashMap<Object, boolean[]>(500);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException  {
		
		String msg = null;

		if (handler instanceof HandlerMethod) {
			boolean[] purview = null;
			if ((purview = cache.get(handler)) == null) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				
				Whitelist whitelist = handlerMethod.getMethod().getAnnotation(Whitelist.class);
				if (whitelist == null) {
					whitelist = handlerMethod.getBeanType().getAnnotation(Whitelist.class);
				}
				purview = new boolean[] { true, true, false, true };
				if(whitelist != null) {
					List<Category> list = Arrays.asList(whitelist.value());
					purview[0] = !list.contains(Category.TIME)  && !list.contains(Category.ALL);  // 校验请求时间
					purview[1] = !list.contains(Category.TOKEN) && !list.contains(Category.ALL);  // 校验设备令牌
					purview[2] = !list.contains(Category.LOGIN) && !list.contains(Category.ALL);  // 校验用户登录
					purview[3] = !list.contains(Category.SIGN) && !list.contains(Category.ALL);   // 校验请求签名
				}
				cache.put(handler, purview);
			}
			
			@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = new TreeMap<>(request.getParameterMap());

			String source = null;
			if (msg == null && (purview[1] || purview[3])) {
				if (parameterMap.get("source") != null && StringUtils.isNotBlank(parameterMap.get("source")[0])) {
					source = parameterMap.get("source")[0];
				} else {
					msg = "source is null";
				}
			}
			
			if (msg == null && purview[0]) {
				if (parameterMap.get("timestamp") != null && StringUtils.isNotBlank(parameterMap.get("timestamp")[0])) {
					long time = NumberUtils.toLong(parameterMap.get("timestamp")[0]);
					if (Math.abs(System.currentTimeMillis() - time) > timeout) {
						msg = "request timed out!";
					}
				} else {
					msg = "timestamp is null";
				}
			}

			if (msg == null && purview[1]) {
				if (parameterMap.get("token") == null || StringUtils.isBlank(parameterMap.get("token")[0])) {
					msg = "token is null";
				} else if (parameterMap.get("udid") == null || StringUtils.isBlank(parameterMap.get("udid")[0])) {
					msg = "udid is null";
				}
				
				if (msg == null) {
					String token = parameterMap.get("token")[0];
					//logger.debug("token:"+token);
					
					String token_mall=null;
					try {
						//logger.debug("tokenKey:"+tokenKey);
						token_mall=Aes.decryptFromHex(token, tokenKey);
						//logger.debug("token_mall:"+token);
					} catch (Exception e) {
						 msg="非法访问, token解析异常";
					}
				   if(msg==null) {
						 System.out.println(token_mall);
						 String userId="";
						 if(StringUtils.isNotEmpty(token_mall)) {
							 String[] token_array=token_mall.split("#");
							 if(token_array!=null&&token_array.length==3) {
								    userId=token_array[0];
								    redisTemplate.opsForHash().put(token, "userId", userId);
								    long time = NumberUtils.toLong(token_array[1]);
									if (Math.abs(System.currentTimeMillis() - time) >60*60*1000*12) {//token 12 小时有效
										msg = "token is valid";
									}
								  
							 }else {
								 msg="非法访问, token异常";
							 }
						 }else {
							 msg="非法访问, token异常!";
						 }
				   }
				
					
				}
			}

			
			if (msg == null && purview[3]) {
				if (parameterMap.get("sign") != null && StringUtils.isNotBlank(parameterMap.get("sign")[0])) {
					String sign = parameterMap.remove("sign")[0];
					StringBuilder paramsStr = new StringBuilder("");
					for (Entry<String, String[]> param : parameterMap.entrySet()) {
						if (param.getValue() != null) {
							Arrays.sort(param.getValue(), String.CASE_INSENSITIVE_ORDER);
							for (String value : param.getValue()) {
								value = StringUtils.trimToNull(value);
								if(value != null) {
									paramsStr.append(param.getKey() + "=" + value + "&");
								}
							}
						}
					}
					if("3".equals(source) || "4".equals(source) || "5".equals(source) || "8".equals(source) || "9".equals(source)) {
						paramsStr.append("key=" + skfKey);
					} else {
						paramsStr.append("key=" + key);
					}
					
					if(!sign.equalsIgnoreCase(DigestUtils.md5Hex(paramsStr.toString()))) {
						msg = "signature verification failed!";
					}
				} else {
					msg = "sign is null";
				}
			}

			if (msg != null) {
				logger.warn("方法：" + handler  + "，入参：" + ParamUtils.to(parameterMap) + "，错误：" + msg);
				
				Map<String, String> result = new HashMap<String, String>();
				result.put(Constants.RES_CODE, "-1");
				result.put(Constants.RES_MSG, msg);
				
				response.setContentType("text/json; charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.print(JSONObject.fromObject(result).toString());
				pw.flush();
				pw.close();
			}
		}

		return msg == null;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
   
}
