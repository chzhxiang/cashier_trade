/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月9日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.common;

import com.sunshine.framework.common.mybatisgenerator.CombaGeneratorTools;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.common.mybatisgenerator
 * @ClassName CombaGenerator.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月9日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class MybatisGenerator {

	/**
	 * @Description
	 * @param args
	 * @date 2017年7月9日
	 */
	public static void main(String[] args) {
		String congifFile = MybatisGenerator.class.getClassLoader().getResource("mybatis/generatorConfig.xml").getFile();
		try {
			CombaGeneratorTools.generator(congifFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
