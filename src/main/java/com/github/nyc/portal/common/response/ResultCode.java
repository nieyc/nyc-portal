package com.github.nyc.portal.common.response;

public enum ResultCode {
	//系统级错误
		SUCCESS("0"),//成功
		
		ERROR("-1"),//失败
		
		NO_RECORD("1"),//没有查询到记录
		
		DB_EXCEPTION("100001"),//数据库异常
		
		SYSTEM_EXCEPTION("100002"),//系统异常
		
		THIRD_EXCEPTION("100003"),//第三方系统异常
		
		VERIFY_REQUEST_ERROR("100004"),//请求校验失败
		
		SESSION_VALID("100005"),//session 失效
		
		TIME_OUT("100006"),//超时
		
		//业务逻辑错误码
		NUll_EXCEPTION("100007"),//入参为空值
		
		PARAM_INVALID("100008"),//输入参数不符合规范
	
	    ORDER__STATE_ERROR("100009"),//订单状态错误
	    
	    ORDER__NOT_FUND("1000010"),//订单不存在
	
	    USER_NOT_EXIST("100011"),//用户不存在
	
	    USER_PWD_ERROR("100012"),//用户名或密码错误
	
	    USER_LOGIN_PEOHIBIT("100013"),//用户禁止登录
	
	    GOOD__NOT_FUND("1000014"),//商品不存在
	    
	    GOOD_STATUS_ERROR("1000015");//商品销售状态错误

		
	    private final String value;

	    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	    ResultCode(String value) {
	        this.value = value;
	    }
	    
	    public String getValue() {
	        return value;
	    }
	    
    
}
