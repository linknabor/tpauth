package com.eshequ.hexie.tpauth.vo;

public class TestEntity {

	private long id;
	private String tel;
	private String wuyeId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getWuyeId() {
		return wuyeId;
	}
	public void setWuyeId(String wuyeId) {
		this.wuyeId = wuyeId;
	}
	@Override
	public String toString() {
		return "TestEntity [id=" + id + ", tel=" + tel + ", wuyeId=" + wuyeId + "]";
	}
	

}
