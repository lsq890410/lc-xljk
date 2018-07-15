package com.lc.xljk.vo.user;

public class UserVO {
	
    private Integer id;

    private String skey;

    private String createtime;

    private String lastvisittime;

    private String sessionkey;

    private String appsession;

    private String openid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getLastvisittime() {
		return lastvisittime;
	}

	public void setLastvisittime(String lastvisittime) {
		this.lastvisittime = lastvisittime;
	}

	public String getSessionkey() {
		return sessionkey;
	}

	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	public String getAppsession() {
		return appsession;
	}

	public void setAppsession(String appsession) {
		this.appsession = appsession;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

   
}