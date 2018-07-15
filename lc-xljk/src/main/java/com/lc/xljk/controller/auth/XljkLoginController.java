package com.lc.xljk.controller.auth;


import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.xljk.api.weixin.WxMiniprogramApiUtils;
import com.lc.xljk.controller.AbsXljkController;
import com.lc.xljk.exception.BusinessException;
import com.lc.xljk.pub.XljkClientTools;
import com.lc.xljk.service.auth.XljkLoginService;
import com.lc.xljk.vo.wxclientresult.WxClientResultVO;

@Controller
@RequestMapping("/auth")
public class XljkLoginController extends AbsXljkController{
	@Autowired
	private WxMiniprogramApiUtils wxMiniProgramUtils;
	@Autowired
	private XljkLoginService service;
	/**
	 * 执行微信小程序的登陆
	 * @return String 
	 * @author lisq
	 * @date 2018年7月14日
	 */
	@RequestMapping("/login")
	@ResponseBody
	public WxClientResultVO<String> doWxMiniProgramLogin(@NotNull @RequestParam(name="code") String code)throws BusinessException {
		//1. 调用微信api
		Map<String,String> wxResult = wxMiniProgramUtils.doWxApilogin(code);
		//2. 对结果进行分析
		if(!StringUtils.isEmpty(wxResult.get(__S_APPID))) {
			//成功：新增用户，并返回appsession
			return XljkClientTools.getSuccessObj(__S_APPSESSION,service.doLogin(wxResult));
		} else {
			//失败：返回失败信息
			throw new BusinessException("微信API请求发生异常："+wxResult.get("errcode")+","+wxResult.get("errormsg"));
		}
	}
}
