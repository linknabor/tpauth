package com.eshequ.hexie.tpauth.model;

import org.springframework.data.annotation.Id;

public class Pointrecord extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7876865010805048437L;

	@Id
    private Long id;

    private Long createdate;

    private String keystr;

    private String memo;

    private Integer point;

    private Integer reason;

    private Integer type;

    private Long userid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Long createdate) {
        this.createdate = createdate;
    }

    public String getKeystr() {
        return keystr;
    }

    public void setKeystr(String keystr) {
        this.keystr = keystr;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}