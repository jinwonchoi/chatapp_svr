package com.gencode.issuetool.obj;

public class NoticeBoardEx extends Pojo {
	protected long id;
	protected String title;
	protected String content;
	protected String registerId;
	protected UserInfo registerUserInfo;
	protected String postType;
	protected int postLevel;
	protected long refId;
	protected String updatedDtm;
	protected String createdDtm;

	public NoticeBoardEx() {
		// TODO Auto-generated constructor stub
	}

	public NoticeBoardEx(String title, String content, UserInfo registerUserInfo, String postType, int postLevel, long refId) {
		super();
		this.title = title;
		this.content = content;
		this.registerId = registerId;
		this.registerUserInfo = registerUserInfo;
		this.postType = postType;
		this.postLevel= postLevel;
		this.refId = refId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public UserInfo getRegisterUserInfo() {
		return registerUserInfo;
	}

	public void setRegisterUserInfo(UserInfo registerUserInfo) {
		this.registerUserInfo = registerUserInfo;
	}

	public String getUpdatedDtm() {
		return updatedDtm;
	}

	public void setUpdatedDtm(String updatedDtm) {
		this.updatedDtm = updatedDtm;
	}

	public String getCreatedDtm() {
		return createdDtm;
	}

	public void setCreatedDtm(String createdDtm) {
		this.createdDtm = createdDtm;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public int getPostLevel() {
		return postLevel;
	}

	public void setPostLevel(int postLevel) {
		this.postLevel = postLevel;
	}

	public long getRefId() {
		return refId;
	}

	public void setRefId(long refId) {
		this.refId = refId;
	}

	
}
