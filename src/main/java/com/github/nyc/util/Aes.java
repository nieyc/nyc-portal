package com.github.nyc.util;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

public class Aes {

	private String password;
	
	public Aes(String password) {
		this.password = password;
	}
	
	/**
	 * 获取加密key
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static SecretKeySpec getKey(String password) throws Exception {
		int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		return new SecretKeySpec(keyBytes, "AES");
	}
	
	/**
	 * 加密
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String content, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
		return cipher.doFinal(content.getBytes("UTF-8"));
	}

	/**
	 * 加密为16进制字符串
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encryptToHex(String content, String password) throws Exception {
		return byteToHex(encrypt(content, password));
	}
	
	/**
	 * 加密为16进制字符串
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String encryptToHex(String content) throws Exception {
		return encryptToHex(content, this.password);
	}
	
	/**
	 * 解密
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(byte[] content, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, getKey(password));
		return new String(cipher.doFinal(content), "UTF-8");
	}

	/**
	 * 解密16进制字符串
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String decryptFromHex(String content, String password) {
		try {
			content = StringUtils.trim(decrypt(hexToByte(content), password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 解密16进制字符串
	 * @param content
	 * @return
	 */
	public String decryptFromHex(String content) {
		return decryptFromHex(content, this.password);
	}
	
	/**
	 * 二进制转16进制字符串
	 * @param buf
	 * @return
	 */
	public static String byteToHex(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 16进制字符串转二进制
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexToByte(String hexStr) {
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		Arrays.toString(result);
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		String aa = "111111a";
		System.out.println(aa = encryptToHex(aa, "finace-portal&test%^&"));
		System.out.println(decryptFromHex("5e72e39974d905edd13d74b603ea977a40e3c5b40909fe80ee83d8eff7ecf653", "finace-portal&test%^&"));
	}
}
