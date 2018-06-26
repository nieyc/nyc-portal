package com.github.nyc.util;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class PicUtils {
	
	private  Logger logger = LogManager.getLogger(getClass());

	/**
	 * 根据url获取图片尺寸
	 * @param picUrl
	 * @return
	 */
	public static JSONObject getImage(String picUrl) {
		URL url;
		BufferedImage image = null;
		URLConnection connection;
		int srcWidth = 0;
		int srcHeight = 0;
		try {
			url = new URL(picUrl);
			connection = url.openConnection();
			int length = connection.getContentLength();
			if(length==-1) {
				 System.out.print(picUrl+"：image not exist");
			}else {
				image = ImageIO.read(connection.getInputStream());
				if (image != null) {
					srcWidth = image.getWidth(); // 源图宽度
					srcHeight = image.getHeight(); // 源图高度
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("width", srcWidth);
		json.put("height", srcHeight);
		return json;
	}

	public static JSONObject getImg(String picUrl) {
		InputStream murl = null;
		BufferedImage sourceImg = null;
		JSONObject json = new JSONObject();
		int srcWidth = 0;
		int srcHeight = 0;
		try {
			murl = new URL(picUrl).openStream();
			sourceImg = ImageIO.read(murl);
			if (sourceImg != null) {
				srcWidth = sourceImg.getWidth(); // 源图宽度
				srcHeight = sourceImg.getHeight(); // 源图高度
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				murl.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		json.put("width", srcWidth);
		json.put("height", srcHeight);
		return json;
	}

	public static void main(String[] args) {
		JSONObject json = getImg("http://172.16.2.141/jpg/20180605/15281653752891.jpg");
		System.out.println(json);
	}
}
