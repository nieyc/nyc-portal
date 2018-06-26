/*
 * Copyright(C) 2014,  CSSWEB TECHNOLOGY CO.,LTD.
 * All rights reserved.
 *
 */

package com.github.nyc.util;

/**
 * @author liuyi
 * @version 0.1 (2014-11-24 上午10:37:15)
 * @since 0.1
 * @see
 */
public class Constants {
	/*---------------------------------ajax前台响应常量定义--------------------------------------------*/

	public static final String RES_CODE = "resCode";

	public static final String RES_MSG = "resMsg";

	public static final int SUCCESS_CODE = 0;

	public static String EXCEPTION_MESSAGE_PROMPT = "系统繁忙,请稍后再试!";

	// 理财平台门户业务系统编码
	public final static String SYSTEM_CODE_PORTAL = "21";

	/**
	 * 登录类型-手机登录
	 */
	public static final String LOGIN_TYPE_MOBILE = "1";

	/**
	 * 登录类型-邮箱登录
	 */
	public static final String LOGIN_TYPE_EMAIL = "2";
	
	/**
	 * 登录类型-闪客蜂登录
	 */
	public static final String LOGIN_TYPE_SHANKE = "3";
	
	/**
	 * 登录类型-闪客蜂第三方账户登录
	 */
	public static final String LOGIN_TYPE_SHANKE_THIRD = "4";

	/**
	 * 账号类型 - 个人
	 */
	public static final String ACCOUNT_TYPE_USER = "1";

	/**
	 * 账号类型 - 企业
	 */
	public static final String ACCOUNT_TYPE_COMPANY = "2";

	/**
	 * 来源--PC端
	 */
	public static final String PC_SOURCE = "1";

	/**
	 * 来源--手机端
	 */
	public static final String MOBILE_SOURCE = "2";

	/**
	 * 证件类型 - 身份证
	 */
	public static final String CARD_TYPE_IDNO = "1";

	/**
	 * 用户密码修改-2代表交易密码
	 */
	public static final String PWD_TRADE = "2";
	/**
	 * 用户密码修改-1代表登录密码
	 */
	public static final String PWD_LOGIN = "1";

	/**
	 * 用户密码修改-0代表支付密码
	 */
	public static final String PWD_PAY = "0";

	/**
	 * 短信验证码-用户注册
	 */
	public static final String SMS_AUTH_CODE_USER_REGISTER = "01";

	/**
	 * 短信验证码-找回支付密码
	 */
	public static final String SMS_AUTH_CODE_FIND_PAYPWD = "02";

	/**
	 * 短信验证码-找回登录密码
	 */
	public static final String SMS_AUTH_CODE_FIND_LOGINPWD = "03";

	/**
	 * 短信验证码-修改绑定手机号码
	 */
	public static final String SMS_AUTH_CODE_MODIFY_BINDING_MOBILE = "04";
	
	/**
	 * 短信验证码-重置密保问题
	 */
	public static final String SMS_AUTH_CODE_QUESTION = "05";
	
	/**
	 * 短信验证码-找回交易密码
	 */
	public static final String SMS_AUTH_CODE_FIND_TRADEPWD = "06";
	
	/**
	 * 短信验证码--购买产品
	 */
	public static final String SMS_AUTH_CODE_SG_PRODUCT = "07";
	
	
	/**
	 * 短信验证码--银行卡更换
	 */
	public static final String SMS_AUTH_CODE_BANKCHANGE = "08";

}
