package com.gencode.issuetool.obj;

public class UserSns extends Pojo {
	protected long id;
	protected long userId;
	protected String snsType;
	protected String snsId;

	public UserSns() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserSns(long id, long userId, String snsType, String snsId) {
		super();
		this.id = id;
		this.userId = userId;
		this.snsType = snsType;
		this.snsId = snsId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSnsType() {
		return snsType;
	}

	public void setSnsType(String snsType) {
		this.snsType = snsType;
	}

	public String getSnsId() {
		return snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}

	
}
