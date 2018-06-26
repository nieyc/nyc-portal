package com.github.nyc.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class RandomUtil {
	



	public static String generateShortUuid() {
		StringBuilder str=new StringBuilder();//定义变长字符串
		Random random=new Random();
		//随机生成数字，并添加到字符串
		for(int i=0;i<8;i++){
		    str.append(random.nextInt(10));
		}
		System.out.println(str.toString());
		return str.toString();
	}
	

	
	public static void main(String[] args) {
		Set<String> set=new HashSet<String>();
		for(int i=0;i<10;i++) {
			set.add(generateShortUuid());
		}
		System.out.println(set.size());
		
	
	}

}
