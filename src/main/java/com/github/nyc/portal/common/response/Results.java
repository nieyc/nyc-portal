package com.github.nyc.portal.common.response;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



/**
 * 响应结果
 * @author nyc
 *
 */
@JsonIgnoreProperties(value = {"empty"})
public class Results implements Serializable {

	private static final long serialVersionUID = 6353922859940199376L;

	public static final String RESCODE_SUCCESS = "0";
	
	public static final Results SUCCESS = new Results(ResultCode.SUCCESS, null);
	
	/**
	 * 响应代码
	 */
	public ResultCode resCode;
	
	/**
	 * 响应信息
	 */
	public String resMsg;
	
	/**
	 * 响应数据
	 */
	public Object results;
	
	public Results() { }
	
	public Results(ResultCode resCode, String resMsg) {
		this.resCode = resCode;
		this.resMsg = resMsg;
	}
	
	public Results(ResultCode resCode, String resMsg, Object results) {
		this.resCode = resCode;
		this.resMsg = resMsg;
		this.results = results;
	} 
	
	public boolean isEmpty() {
		return this.resCode == null;
	}
	
	/**
	 * 响应时间
	 * @return
	 */
	public long getTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 签名
	 * @return
	 */
	public String getSign() {
		return "";
	}
	
	public String getResCode() {
		return resCode.getValue();
	}

	public String getResMsg() {
		return resMsg;
	}

	public Object getResults() {
		return results;
	}

	public void setResCode(ResultCode resCode) {
		this.resCode = resCode;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public void setResults(Object results) {
		this.results = results;
	}
}
