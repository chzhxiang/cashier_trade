/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月23日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.unionpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.sunshine.framework.config.SystemConfig;

/**
 * @Project: sunshine_trade 
 * @Package: com.sunshine.trade.payrefund.utils.unionpay
 * @ClassName: CertUtil
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2016年4月23日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CertUtil {

	private static Logger logger = LoggerFactory.getLogger(CertUtil.class);
	/**
	 * 证书容器
	 */
	private static KeyStore keyStore = null;

	/** 密码加密证书 */
	private static X509Certificate encryptCert = null;

	/** 验证签名证书. */
	private static X509Certificate validateCert = null;

	public static final String UNIONPAY_CNNAME = "中国银联股份有限公司";

	private static final String MIDDLE_CERT = "middle_cert";

	private static final String ROOT_CERT = "root_cert";

	public static final String IFVALIDATECNNAME = "acpsdk.ifValidateCNName";

	/** 验签中级证书 */
	private static X509Certificate middleCert = null;
	/** 验签根证书 */
	private static X509Certificate rootCert = null;

	/**
	 * 验签证书存储Map
	 */
	private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();

	static {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initSignCert(String pfxKeyFile, String keyPwd, String type) {
		try {
			keyStore = getKeyInfo(pfxKeyFile, keyPwd, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 * @throws IOException 
	 */
	public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) throws IOException {
		logger.info("加载签名证书:{}", pfxkeyfile);
		FileInputStream fis = null;
		try {
			/*if (Security.getProvider("BC") == null) {
				logger.info("add BC provider");
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			} else {
				Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				logger.info("re-add BC provider");
			}*/
			KeyStore ks = null;
			if ("JKS".equals(type)) {
				ks = KeyStore.getInstance(type, "BC");
			} else if ("PKCS12".equals(type)) {
				/*String jdkVendor = System.getProperty("java.vm.vendor");
				String javaVersion = System.getProperty("java.version");
				logger.info("java.vm.vendor:{},java.version:{}", jdkVendor, javaVersion);*/
				ks = KeyStore.getInstance(type, "BC");
			}
			logger.info("Load RSA CertPath:{} , Pwd:{} ", pfxkeyfile, keypwd);
			fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			if (Security.getProvider("BC") == null) {
				logger.info("BC Provider not installed.");
			}
			logger.error("getKeyInfo Error:{}", e);
			if ( ( e instanceof KeyStoreException ) && "PKCS12".equals(type)) {
				Security.removeProvider("BC");
			}
			return null;
		} finally {
			if (null != fis)
				fis.close();
		}
	}

	/**
	 * 获取签名证书私钥（单证书模式）
	 * 
	 * @return
	 */
	public static PrivateKey getSignCertPrivateKey(String pfxKeyFile, String keyPwd, String type) {
		try {
			initSignCert(pfxKeyFile, keyPwd, type);
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取签名证书中的证书序列号（单证书）
	 * 
	 * @return 证书的物理编号
	 */
	public static String getSignCertId(String pfxKeyFile, String keyPwd, String type) {
		try {
			initSignCert(pfxKeyFile, keyPwd, type);
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取加密证书的证书序列号
	 * 
	 * @return
	 */
	public static String getEncryptCertId(String path) {
		if (null == encryptCert) {
			if (!SDKUtil.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else {
				logger.info("ERROR: acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getSerialNumber().toString();
		}
	}

	/**
	 * 证书文件过滤器
	 * 
	 */
	static class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

	/**
	 * 从指定目录下加载验证签名证书
	 * 
	 */
	private static void initValidateCertFromDir(String dir) {
		certMap.clear();
		if (StringUtils.isEmpty(dir)) {
			logger.info("验证签名证书目录不能为空");
			return;
		}
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			File fileDir = new File(dir);
			File[] files = fileDir.listFiles(new CerFilter());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				in = new FileInputStream(file.getAbsolutePath());
				validateCert = (X509Certificate) cf.generateCertificate(in);
				certMap.put(validateCert.getSerialNumber().toString(), validateCert);
			}
			logger.info("LoadVerifyCert Successful");
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过certId获取证书Map中对应证书的公钥
	 * 
	 * @param certId
	 *            证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public static PublicKey getValidateKey(String certId, String dir) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir(dir);
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				logger.info("缺少certId:{}对应的验签证书.", certId);
				return null;
			}
		}
	}

	public static String getCertIdIdByStore(String certFilePath, String certPwd) {
		KeyStore keyStore = null;
		try {
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration<String> aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd) {
		KeyStore keyStore = null;
		try {
			keyStore = getKeyInfo(certPath, certPwd, "PKCS12");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取加密证书公钥.密码加密时需要
	 * 
	 * @return
	 */
	public static PublicKey getEncryptCertPublicKey(String path) {
		if (null == encryptCert) {
			if (!StringUtils.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else {
				logger.error("加密证书公钥路径不能为空");
				return null;
			}
		} else {
			return encryptCert.getPublicKey();
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	private static X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			logger.info("[" + path + "][CertId=" + encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return encryptCertTemp;
	}

	/**
	 * 将字符串转换为X509Certificate对象.
	 * 
	 * @param x509CertString
	 * @return
	 */
	public static X509Certificate genCertificateByStr(String x509CertString) {
		X509Certificate x509Cert = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
			InputStream tIn = new ByteArrayInputStream(x509CertString.getBytes("ISO-8859-1"));
			x509Cert = (X509Certificate) cf.generateCertificate(tIn);
		} catch (Exception e) {
			logger.info("gen certificate error", e);
		}
		return x509Cert;
	}

	/**
	 * 检查证书链
	 * 
	 * @param rootCerts
	 *            根证书
	 * @param cert
	 *            待验证的证书
	 * @return
	 */
	public static boolean verifyCertificate(X509Certificate cert) {

		if (null == cert) {
			logger.info("cert must Not null");
			return false;
		}
		try {
			cert.checkValidity();//验证有效期
			//			cert.verify(middleCert.getPublicKey());
			if (!verifyCertificateChain(cert)) {
				return false;
			}
		} catch (Exception e) {
			logger.info("verifyCertificate fail", e);
			return false;
		}

		boolean isIfValidateCNName = SystemConfig.getBooleanValue(IFVALIDATECNNAME);
		if (isIfValidateCNName) {
			// 验证公钥是否属于银联
			if (!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				logger.info("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		} else {
			// 验证公钥是否属于银联
			if (!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))
					&& !"00040000:SIGN".equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				logger.info("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证书链。
	 * @param cert
	 * @return
	 */
	private static boolean verifyCertificateChain(X509Certificate cert) {

		if (null == cert) {
			logger.info("cert must Not null");
			return false;
		}

		X509Certificate middleCert = CertUtil.getMiddleCert();
		if (null == middleCert) {
			logger.info("middleCert must Not null");
			return false;
		}

		X509Certificate rootCert = CertUtil.getRootCert();
		if (null == rootCert) {
			logger.info("rootCert or cert must Not null");
			return false;
		}

		try {

			X509CertSelector selector = new X509CertSelector();
			selector.setCertificate(cert);

			Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
			trustAnchors.add(new TrustAnchor(rootCert, null));
			PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);

			Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
			intermediateCerts.add(rootCert);
			intermediateCerts.add(middleCert);
			intermediateCerts.add(cert);

			pkixParams.setRevocationEnabled(false);

			CertStore intermediateCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(intermediateCerts), "BC");
			pkixParams.addCertStore(intermediateCertStore);

			CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");

			@SuppressWarnings("unused")
			PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
			logger.info("verify certificate chain succeed.");
			return true;
		} catch (java.security.cert.CertPathBuilderException e) {
			logger.info("verify certificate chain fail.", e);
		} catch (Exception e) {
			logger.info("verify certificate chain exception: ", e);
		}
		return false;
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的中级证书
	 * @return
	 */
	public static X509Certificate getMiddleCert() {
		if (null == middleCert) {
			String path = SystemConfig.getStringValue(MIDDLE_CERT);
			if (!isEmpty(path)) {
				initMiddleCert();
			} else {
				logger.info(" middle_cert 不存在");
				return null;
			}
		}
		return middleCert;
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的根证书
	 * @return
	 */
	public static X509Certificate getRootCert() {
		if (null == rootCert) {
			String path = SystemConfig.getStringValue(ROOT_CERT);
			if (!isEmpty(path)) {
				initRootCert();
			} else {
				logger.info(" root_cert 不存在");
				return null;
			}
		}
		return rootCert;
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	private static void initMiddleCert() {
		logger.info("加载中级证书==>" + SystemConfig.getStringValue(MIDDLE_CERT));
		if (!isEmpty(SystemConfig.getStringValue(MIDDLE_CERT))) {
			middleCert = initCert(SystemConfig.getStringValue(MIDDLE_CERT));
			logger.info("Load MiddleCert Successful");
		} else {
			logger.info("WARN: acpsdk.middle.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	private static void initRootCert() {
		logger.info("加载根证书==>" + SystemConfig.getStringValue(ROOT_CERT));
		if (!isEmpty(SystemConfig.getStringValue(ROOT_CERT))) {
			rootCert = initCert(SystemConfig.getStringValue(ROOT_CERT));
			logger.info("Load RootCert Successful");
		} else {
			logger.info("WARN: acpsdk.rootCert.path is empty");
		}
	}

	/**
	 * 获取证书的CN
	 * @param aCert
	 * @return
	 */
	private static String getIdentitiesFromCertficate(X509Certificate aCert) {
		String tDN = aCert.getSubjectDN().toString();
		String tPart = "";
		if ( ( tDN != null )) {
			String tSplitStr[] = tDN.substring(tDN.indexOf("CN=")).split("@");
			if (tSplitStr != null && tSplitStr.length > 2 && tSplitStr[2] != null)
				tPart = tSplitStr[2];
		}
		return tPart;
	}

	/**
	 * 判断字符串是否为NULL或空
	 * 
	 * @param s
	 *            待判断的字符串数据
	 * @return 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}

}
