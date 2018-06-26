package com.github.nyc.portal.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.nyc.portal.common.response.ResultCode;
import com.github.nyc.portal.common.response.Results;

@Component
public class ResponseUtil {

	/**
	 * 统一出错处理
	 * 
	 * @author nyc
	 * @date 2018年1月29日 下午6:49:19
	 * @param errorCode
	 * @param errorMsg
	 * @return 
	 * @since 0.1
	 * @see
	 */
	public Results responseMsg(ResultCode code, String errorMsg) {
		Results results = new Results();
		results.setResCode(code);
		results.setResMsg(errorMsg);
		return results;
	}

	public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteDateUseDateFormat));
		writer.close();
		response.flushBuffer();
	}

}
