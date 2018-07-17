package com.lc.xljk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.lc.xljk.conf.XljkProgramConfig;
import com.lc.xljk.exception.PlatFormException;
import com.lc.xljk.pub.IXljkConstance;
import com.lc.xljk.pub.XljkClientTools;
import com.lc.xljk.utils.LcJsonUtils;

/**
 * 小程序的权限控制
 * 
 * @author lisq
 * @date 2018年7月14日
 */
@Configuration
public class XljkIdentificationFilter implements Filter,IXljkConstance{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private XljkProgramConfig wxServerConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		try {
			// 1. 安全认证
			doAuthCheck(httpRequest);
			// 2. 功能权限认证
			doFuncCheck(httpRequest);
			// 3. 资源权限认证
			doResCheck(httpRequest);
		} catch (Exception e) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			logger.error("认证失败："+e.getMessage(),e);
			response.getWriter().print(XljkClientTools.getFailjson("认证失败："+e.getMessage()));
			response.getWriter().close();
			return;
		}
		try {
			chain.doFilter(request, response);
		}catch(Throwable e) {
			logger.error("请求失败："+e.getMessage(),e);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(XljkClientTools.getFailjson("请求失败："+e.getMessage()));
			response.getWriter().close();
		}
	}
	
	private void doAuthCheck(HttpServletRequest httpRequest ) throws PlatFormException{
		String skey = httpRequest.getHeader(__S_SKEY);
		if(StringUtils.isEmpty(skey) || !skey.equals(wxServerConfig.getSkey())) {
			throw new PlatFormException("小程序认证失败：检查skey是否正确！！！");
		}
	}
	
	private void doFuncCheck(HttpServletRequest httpRequest )throws PlatFormException{
		//TODO
	}
	
	private void doResCheck(HttpServletRequest httpRequest )throws PlatFormException{
		//TODO
	}

	@Override
	public void destroy() {
		
	}
	@Bean
	public FilterRegistrationBean testFilterRegister(){
		FilterRegistrationBean filterBean = new FilterRegistrationBean(this);
		filterBean.addUrlPatterns("/*");
		filterBean.setName("auth");
		return filterBean;
	}

}
