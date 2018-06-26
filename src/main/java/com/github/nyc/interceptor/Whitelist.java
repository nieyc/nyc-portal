package com.github.nyc.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Whitelist {
	
	Category[] value() default { };
	public static enum Category {
		TIME,       // 超时时间
		SIGN,       // 签名
		TOKEN,      // 令牌
		LOGIN,      // 登陆
		ALL         // 全部不校验
	}
}
