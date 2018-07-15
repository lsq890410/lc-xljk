package com.lc.xljk.service.auth;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.xljk.dao.mapper.user.UserMapper;
import com.lc.xljk.exception.BusinessException;
import com.lc.xljk.pub.XljkClientTools;
import com.lc.xljk.pub.lang.LCDateTime;
import com.lc.xljk.service.AbsXljkService;
import com.lc.xljk.vo.user.UserVO;
@Service
public class XljkLoginService extends AbsXljkService{
	@Autowired
	private UserMapper userMapper;
	public String doLogin(Map<String,String> result) throws BusinessException{
		//1.先查到:根据openid查到用户
		UserVO userVO = userMapper.getOneByOpenID(result.get(__S_OPENID));
		if(userVO != null) {
			//2.1 有就更新：sessionkey 返回：appsession
			userVO.setSessionkey(result.get(__S_SESSIONKEY));
			userVO.setLastvisittime(new LCDateTime(new Date()).toString());
			userMapper.update(userVO);
		} else {
			//2.2 没有就新增条，返回appsession
			userVO = createNewUser(result);
		}
		return  userVO.getAppsession();
	}
	
	private UserVO createNewUser(Map<String,String> result) {
		UserVO user = new UserVO();
		user.setAppsession(XljkClientTools.getUUID());
		user.setCreatetime(new LCDateTime(new Date()).toString());
		user.setLastvisittime(new LCDateTime(new Date()).toString());
		user.setOpenid(result.get(__S_OPENID));
		user.setSessionkey(result.get(__S_SESSIONKEY));
		user.setSkey(getProgramConfig().getSkey());
		user.setId(10000);
		userMapper.insert(user);
		return user;
	}
}
