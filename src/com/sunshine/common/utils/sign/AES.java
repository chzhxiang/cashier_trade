package com.sunshine.common.utils.sign;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES {

	public static boolean initialized = false;

	/**
	 * 填充模式
	 */
	public static final String ALGORITHM = "AES/ECB/PKCS7Padding";

	/** 
	 * @param  String str  要被加密的字符串 
	 * @param  byte[] key  加/解密要用的长度为32的字节数组（256位）密钥 
	 * @return byte[]  加密后的字节数组 
	 */
	public static byte[] Aes256Encode(String str, String key) {
		initialize();
		byte[] result = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			/*IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);*/
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			result = cipher.doFinal(str.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 
	 * @param  byte[] bytes  要被解密的字节数组 
	 * @param  byte[] key    加/解密要用的长度为32的字节数组（256位）密钥 
	 * @return String  解密后的字符串 
	 */
	public static String Aes256Decode(byte[] bytes, String key) {
		initialize();
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); //生成加密解密需要的Key  
			/*IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);*/
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decoded = cipher.doFinal(bytes);
			result = new String(decoded, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) ( charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]) );
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static void initialize() {
		if (initialized)
			return;
		Security.addProvider(new BouncyCastleProvider());
		initialized = true;
	}

	/** 
	 * 16进制字符串转换为字符串 
	 *  
	 * @param s 
	 * @return 
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) ( 0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
			new String();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {

		/*String hexString = "504262455a4372764b673654615762454f5a6f584f6e65516c57633237376d53";
		//PBbEZCrvKg6TaWbEOZoXOneQlWc277mS -------- key
		String key = hexStringToString(hexString);
		System.out.println("密钥:" + key);
		String iv = hexStringToString("504262455a4372764b67365461576245");
		System.out.println(iv);
		byte[] bytes = Aes256Encode(str, key.getBytes());
		System.out.println("加密后：" + bytesToHexString(bytes));*/

		/*String str = "Merchant=102440153110001&OrderNumber=0123456789&OrderAmount=1&OrderCurrency=156&OrderTime=2017-3-15 17:05:59";

		String key = "pZyIFYptU1LHlNsiwFoDNcZyLeh0wNMT";
		byte[] bytes = Aes256Encode(str, key.getBytes());
		System.out.println("加密后：" + Base64.encode(bytes));*/

		String key = "pZyIFYptU1LHlNsiwFoDNcZyLeh0wNMT";
		System.out.println(key.substring(0, 16));
	}
}