package com.lc.xljk.vo.wxclientresult;

import java.util.HashMap;
import java.util.Map;

import com.lc.xljk.pub.IXljkConstance;

/**
 * 返回前端的VO
 * 
 * @author lisq
 * @date 2018年7月14日
 */
public class WxClientResultVO<T>  implements IXljkConstance{
	private int result_code = WX_CLIENT_RESULT_SUCCESS;
	private String result_msg = WX_CLIENT_RESULT_SUCCESS_DEFAUTLMSG;
	private Map<String,T> data = new HashMap<String,T>();
	public int getResult_code() {
		return result_code;
	}
	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	public Map<String, T> getData() {
		return data;
	}
	public void setData(Map<String, T> data) {
		this.data = data;
	} 
}
