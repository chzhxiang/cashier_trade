package com.sunshine.common.utils.sign;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;

/**
 * 
 * @Project: YiChenTong_trade 
 * @Package: com.sunshine.trade.payrefund.sign
 * @ClassName: RSAUtils
 * @Description: <p>惠州医保RSA签名工具类</p>
 * @JDK version used: 
 * @Author: 赤芍
 * @Create Date: 2017年2月28日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RSAUtils {

	/*private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUKpVSa17dX3ddI2SryY9pfo4e" + "\r"
			+ "kek2I4ZiKcXkgkHkvKiX0zARMaCR3oVlQd7XvalBLJfIhIxmScgWMP3G7BqeDSld" + "\r"
			+ "fqG3xOtLTN4I1xgbgnkSZa9bPPusH11tFrpCEhu9z4o74JrGDmQtB6KtKhN+Qtg+" + "\r" + "CYcIa8zw+oalFdKQ0wIDAQAB" + "\r";

	private static final String DEFAULT_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJQqlVJrXt1fd10j" + "\r"
			+ "ZKvJj2l+jh6R6TYjhmIpxeSCQeS8qJfTMBExoJHehWVB3te9qUEsl8iEjGZJyBYw" + "\r"
			+ "/cbsGp4NKV1+obfE60tM3gjXGBuCeRJlr1s8+6wfXW0WukISG73PijvgmsYOZC0H" + "\r"
			+ "oq0qE35C2D4JhwhrzPD6hqUV0pDTAgMBAAECgYAel0grMyXr+NjxLuyLF1t3wXxg" + "\r"
			+ "TWnkFqOZQtyr2ET4kAcTCbRj6GYpl9Lc3CPBUVRPs4BHEy7Vs6gPBOBwoLDB3rLX" + "\r"
			+ "rM/VjNtTlncJYDUhuHcizq6T+1Mt9j1Z9ZXLrb93009S45PkaTAtfpbMZMy/V5/n" + "\r"
			+ "eyIvpjShAVNyVYjIoQJBAMScrZpydrKc5oVQPtTEDl09dzt8WNqWfYLVLpoQyWkc" + "\r"
			+ "o7QbbxfbY9M1KBj7i2nVEs+6ULVPeZkTa0LUGuLHNwMCQQDA68T+Pv0GGv/2Gl03" + "\r"
			+ "nXEHeaNXbbQ6yyv5RQ6fLQOE5TC4Xg1qRIVEw8VA6M1DlJhu6X/D7JV+i+dEeBDo" + "\r"
			+ "Hu3xAkEAozKmS2AdtK7WDeyI6tT+U1jU4pSZsH9gGTxZHVs3w78ZB0QKV9QsrUEf" + "\r"
			+ "UnVhyGwr3C98FLOubGiZDPTAiGLx/wJBAJP6BH80F2aAfUbrUAcht5oRPi4Iks7q" + "\r"
			+ "Go+33ZBQubtNBULVuZqCdmqZc4rnRtMIfqfMaNkCdmS7sSe+pNE3MDECQQCQEaDK" + "\r"
			+ "UCUTtyq9NHmWX2ZYfMMNUNG1aarIj3eDImHTWxpxoF9J9N0JqbQBwpSboozXYOml" + "\r" + "W5EqNz+Lv3onVDI5" + "\r";*/

	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDi5mhXfwBz2HeTvee+Dgn/OtEHiN5B+i9OPpXu\r"
			+ "iNsBlcgkauQm1vVD9wYSl8IWa6YJNZ3uayWwTE7naoGJ2XuFTHcAhR/iaPjRQdKZ6XQQxNwCtU2e\r"
			+ "6sHNaenrVch/bGOblx7ygM9rq1goEfIXHOwpveDlo14d8ADpUoyDx+ZrSQIDAQAB\r";

	private static final String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOLmaFd/AHPYd5O9574OCf860QeI\n"
			+ "3kH6L04+le6I2wGVyCRq5CbW9UP3BhKXwhZrpgk1ne5rJbBMTudqgYnZe4VMdwCFH+Jo+NFB0pnp\r"
			+ "dBDE3AK1TZ7qwc1p6etVyH9sY5uXHvKAz2urWCgR8hcc7Cm94OWjXh3wAOlSjIPH5mtJAgMBAAEC\r"
			+ "gYEAhOGlJ+DD9hRG62pjRsXYCvZ6Jmx8pa0MeUlj6z+Q6xu6C8Hh/fET0Irhro/b5d+DbI9O/6sW\r"
			+ "dmoqJ5MTbBTHJg4p/hYC/I5c4CPNsOsP5ynwfuKdIMyni2d7ZJx0uccBsXJ4rdHQwp6IrU2DhKTX\r"
			+ "Q2OuvJ/r/qacviGt0trkB5ECQQD8bYy9Hh6YV02bYbthq54w9Jj5N12DASTrMpEdLaDK8BvWzSgD\r"
			+ "b7CjsMPdD0YkyATDSXsspd06SyTeW8M5C9YVAkEA5hxhRae+RRei0jC53gN2QQMKjADIOFjJUVXK\r"
			+ "Sni3xfIy3Y2ZP633Zdan6YKIxWxipKDKs0dmWCk/CdMkbelhZQJAELpVxEaVsnS+oNhmKX/M+OOM\r"
			+ "q58+pCrUPn3YXf2jFS+6OI7Z4vho1UAmiqSzekaDAkoBTqEjOvS12cJs+n/NTQJAMltnlhy9cNVZ\r"
			+ "VnI/WqybRmLp/fV1247ij6AcouuEHBeCtiV0shDaITk2ic19LOcpNynQ8ibf6M8t8nJ4eG0oWQJA\r"
			+ "IMLHtLUcc3fm9l5N+FCv0lZ7KDBktXJKlcz4nMxpqBXnXVpLc97Jisx1mtP0Oiah2ZfknRQ2uZVq\r" + "YBCMeivlgg==\r";

	/**
	 * 最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * 最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	/**
	 * 私钥
	 */
	private RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	private RSAPublicKey publicKey;

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取私钥
	 *
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 *
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 从文件中输入流中加载公钥
	 *
	 * @param in 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ( ( readLine = br.readLine() ) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(String publicKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 *
	 * @param in 私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public void loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ( ( readLine = br.readLine() ) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public void loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 加密过程
	 *
	 * @param publicKey 公钥
	 * @param data 明文数据 BASE64
	 * @return
	 * @throws Exception 加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] data) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return doFinalBlock(cipher, MAX_ENCRYPT_BLOCK, data);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
		return null;
	}

	public byte[] signBySort(PrivateKey privateKey, byte[] data) throws Exception {
		byte[] result = null;
		Signature st = Signature.getInstance("SHA1withRSA");
		st.initSign(privateKey);
		st.update(data);
		result = st.sign();
		return result;
	}

	public String signMap(PrivateKey key, Map<String, String> map) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> en : map.entrySet()) {
			sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
		}
		sb.delete(sb.length() - 1, sb.length());
		byte[] signBytes = signBySort(key, sb.toString().getBytes());
		String signStr = Base64Utils.encodeToString(signBytes, Base64Utils.NO_WRAP);
		return signStr;
	}

	public boolean validateSignByMap(PublicKey key, byte[] signData, Map<String, String> map) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> en : map.entrySet()) {
			sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
		}
		sb.delete(sb.length() - 1, sb.length());
		return validateSignBySoft(key, signData, sb.toString().getBytes());
	}

	public boolean validateSignBySoft(PublicKey publicKey, byte[] signData, byte[] srcData) throws Exception {
		Signature st = Signature.getInstance("SHA1withRSA");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}

	/**
	 * 解密过程
	 *
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return doFinalBlock(cipher, MAX_DECRYPT_BLOCK, cipherData);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 进行专块的加解密运算
	 * @param cipher
	 * @param size
	 * @param data
	 * @return
	 * @throws NullPointerException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IOException
	 */
	private byte[] doFinalBlock(Cipher cipher, int size, byte[] data) throws NullPointerException, BadPaddingException, IllegalBlockSizeException,
			IOException {
		if (null == data || data.length == 0) {
			throw new NullPointerException("待运算的数据为空");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length = data.length;
		int offset = 0;
		int i = 0;
		byte[] tempBytes;
		while (length - offset > 0) {
			if ( ( length - offset ) > size) {
				tempBytes = cipher.doFinal(data, offset, size);
			} else {
				tempBytes = cipher.doFinal(data, offset, length - offset);
			}
			baos.write(tempBytes, 0, tempBytes.length);
			i++;
			offset = i * size;
		}
		byte[] resultBytes = baos.toByteArray();
		baos.close();
		return resultBytes;
	}

	/**
	 * 字节数据转十六进制字符串
	 *
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			//取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[ ( data[i] & 0xf0 ) >>> 4]);
			//取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[ ( data[i] & 0x0f )]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		RSAUtils rsaEncrypt = new RSAUtils();
		//rsaEncrypt.genKeyPair();

		//加载公钥
		try {
			rsaEncrypt.loadPublicKey(RSAUtils.DEFAULT_PUBLIC_KEY);
			System.out.println("加载公钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载公钥失败");
		}

		//加载私钥
		try {
			rsaEncrypt.loadPrivateKey(RSAUtils.DEFAULT_PRIVATE_KEY);
			System.out.println("加载私钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载私钥失败");
		}

		//测试字符串
		String encryptStr = "{\"username\":\"13431891156\",\"userpasv\":\"e10adc3949ba59abbe56e057f20f883e\"}";
		String s =
				"RfkBg+QX6t8/fE+3v+LpbwnZhw8K8TZJiRzfpsmrVXuEd7DFsVCQdAdBZoHlhnKgMuGxxeaKJJeSH9Mi5iRA7hGE4OVI2+6L2cMT1FuKFRp0RTliEWRjlnMMHALI0fwdlQXfjiRllV7T02ewJpX/D4MKTujDkhNNsDNUm85FHvg=";

		try {
			//加密
			//byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(),new BASE64Decoder().decodeBuffer(s));
			//解密
			byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), Base64.decode(s));
			//System.out.println("密文长度:" + cipher.length);
			//System.out.println(RSAEncrypt.byteArrayToString(cipher));
			System.out.println("明文长度:" + plainText.length);
			System.out.println(RSAUtils.byteArrayToString(plainText));
			System.out.println("数据原文：" + new String(plainText, "GBK"));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
