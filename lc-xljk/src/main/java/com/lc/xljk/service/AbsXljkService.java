package com.lc.xljk.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.lc.xljk.conf.XljkProgramConfig;
import com.lc.xljk.pub.IXljkConstance;

/**
 * 提供service的抽象类
 * 
 * @author lisq
 * @date 2018年7月15日
 */
public class AbsXljkService  implements IXljkConstance{
	@Autowired
	protected XljkProgramConfig programConfig;

	public XljkProgramConfig getProgramConfig() {
		return programConfig;
	}

	public void setProgramConfig(XljkProgramConfig programConfig) {
		this.programConfig = programConfig;
	}
}
