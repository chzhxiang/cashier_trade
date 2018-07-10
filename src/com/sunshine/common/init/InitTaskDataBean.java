package com.sunshine.common.init;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.sunshine.framework.config.SystemConfig;

public class InitTaskDataBean implements InitializingBean, DisposableBean {

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		SystemConfig.loadSystemConfig();
	}

}
