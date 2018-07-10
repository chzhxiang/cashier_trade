package com.sunshine.common.utils.sign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key 支付宝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	* 解密
	* @param content 密文
	* @param private_key 商户私钥
	* @param input_charset 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String private_key, String input_charset) throws Exception {
		PrivateKey prikey = getPrivateKey(private_key);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		//rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ( ( bufl = ins.read(buf) ) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}

			writer.write(cipher.doFinal(block));
		}

		return new String(writer.toByteArray(), input_charset);
	}

	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;
	}

	public static void main(String[] args) {
		String str =
				"_input_charset=UTF-8&notify_url=http://cashierdev.968309.cn/notifyAlipay/callback/&out_trade_no=10208201709280000026&partner=2088221619256701&payment_type=1&return_url=http://www.baidu.com/&seller_id=2088221619256701&service=alipay.wap.create.direct.pay.by.user&subject=处方单&total_fee=0.01";
		String private_key =
				"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANIfZt/9k0GpQqYgXoxEdRfSvDTVpWx5SuE1gu89is8skd7e8aQthHQxT+d6wjbs0B003W+6KVxcvdp9pi6V+UhtoSZmOd2lF7dVfnUzkIyTfSfzUTt/Gl/5ol9/lrh4hsFaRTRheMjfL3T8FHHe9LycU+jmDfzu7nrxxWaS+joFAgMBAAECgYEAiSMvjF8+fatPsYcMzjVbysmiRkVuKuba5TN8mlIrR13KcpBFW6bNfNw2T1jCawCSL42yPFpe5kYtLu20yVDemaRQddsSfBv6lRJzsmEDOrBhRG/n6YKVOYqe2tjuManldqjelx/3FbOfk4x4Wd8JVLy/c3daahh8sZ3AJGDTVyECQQD5Zfk0xj6w2f+cpYqgSHc7ga9wCfpYwhw3eOEihhoRa2NjIewcFPitutUf+AKoWiSPMA9MewiT0hq+OjQgnsHtAkEA169HuMvaGIxnPASPgIVLa9dQ4nBnyuHffDrgokg8DilsNTg32cPN1LDqyvPS1rQjv6sA/rFYFgCZMEuXwaS1eQJABeIb8NzEWdOqrf1Rudo7xdGXW+NAXpulosFdPj0/nC2WWJ5SPY9ZFnAT2v80wNfviNkvYzR7zVDlhJtG44YbOQJASN+ZLQ/7VXYC311vGu5uiDZMxuVRvFPKzc+a3FnpqKNIQ0cGX9ZvciCAhCgToaVlurnNcUB8UZrMJ2wzDyNWiQJBAI1jOZC3ZzzW7ITjledCPFf6NmZC8T2L9yP3qKJomAXvOnqTHGpbigI0v/cD3aEaF1tReKhhsPQBtOVQ9yXHi2Y=";
		System.err.println(sign(str, private_key, "UTF-8"));
	}
}
