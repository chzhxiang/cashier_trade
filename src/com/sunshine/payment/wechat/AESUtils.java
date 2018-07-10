/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月27日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.wechat;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.payment.wechat
 * @ClassName: AESUtils
 * @Description: <p>AES解密</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月27日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class AESUtils {
	/** 
	 * 密钥算法 
	 */
	public static final String ALGORITHM = "AES";
	/** 
	 * 加解密算法/工作模式/填充方式 
	 */
	private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
	/** 
	 * 生成key 
	 */
	private static SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode("your password", "UTF-8").toLowerCase().getBytes(), ALGORITHM);

	/** 
	 * AES加密 
	 *  
	 * @param data 
	 * @return 
	 * @throws Exception 
	 */
	public static String encryptData(String data) throws Exception {
		// 创建密码器  
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		// 初始化  
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encode = java.util.Base64.getEncoder().encode(cipher.doFinal(data.getBytes()));
		String res = new String(encode);
		return res;
	}

	/** 
	 * AES解密 
	 *  
	 * @param base64Data 
	 * @return 
	 * @throws Exception 
	 */
	public static String decryptData(String base64Data) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(base64Data)));
	}

	/**
	 * AES解密 
	 * @param base64Data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptData(String base64Data, SecretKeySpec seckey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, seckey);
		return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(base64Data)));
	}

	public static void main(String[] args) throws Exception {
		String A =
				"oSWxqqMF5lk2EF+gdrdt5wPrOru854za5XjHq5cUXs74zF9+jlxGOo7DHQIuntolVF3kQAruoMoNK5lLRsCulgG2hAT+6sNen8f/f3drMxfsTFOj3aBTKkIHs2p3AVJA4fXpGRCpejq3JJplSQnnSwFljzcxvqe7rU3y/H0KpFyBuYUSEf+msbkHEnHnIHQi4p9JDlLPWoKHramM7R65Qd13GdUU41scNybWCkwl+q/cY2Nv6KUt490JXTbTEgZNE6ArJKGg9woRMUdJEimTnv2OSY16yjo8dlIiozEoHcoQsvSFuMA5DHfHmtk5gbn8y6FVLHbt8XmmOIkfl/CVCXGQ+fGJmazxmqpTLBnAxXogFX2c2h8ZFqrWHW0wWZNSqpRX8HnMBw4V5hUMCiN9ASP3AzkpbtxdkDaeJYagVFgpB7oXxNUlQMy7pCqWCqbhoeLlZtzACx3qNqf57cQLn06T8wrYddf3f78oIYceVWMBses6wcJW2uTUdci4hYOQn5G+iVGLRzMuI8xwQSeBtdrWBor842tEsg4/wgFRxiEgjN+Jl+pCbwULjzt870OwC/UKD9mM3bhyay1jxeKNfkqgks0TH9eZXT1mR6IBfIUipgk9nTrGLFQwt4AAAf7/KoW7A3d1eYGY1vo/QkinixiZsxOJhzw95X6wiiARPa8oe0180lCuhLtIrNRlxyVMbbwA8GQVuCCE6w+/yKIF+el+Gcf7Gm2ljQzV7PEwiomW/DsBqUb5mwGfI52NLRa70kJ8vgaXeMN1xhwWYLzg02muvGGwS2P4kgGO0Sg0L5ycpN7Vp421+HnAPdcW6y/pQi03BKAR6fZT5JQYAIoNN4K8K6ZbgfZiuG32q0q4bwVWrg4jBlyPmj8JwHtbikbAgoJ9sUwWYi7P+Btk1ZHCPLW90p+1mIL8eVpneOaon3mSW0R4JDiIJK8oYLD/1n4NTKRTg9c6OMdSHnK8BUnodw==";
		String B = AESUtils.decryptData(A);
		System.out.println(B);
	}

}
