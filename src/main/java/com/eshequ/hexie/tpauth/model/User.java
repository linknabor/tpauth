package com.eshequ.hexie.tpauth.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class User extends BaseModel {
	
	@Id
    private Long id;

    private Integer age;

    private String city;

    private Long cityid;

    private String country;

    private Long countyid;

    private Long createdate;

    private Long currentaddrid;

    private String headimgurl;

    private String identitycard;

    private String language;

    private Double latitude;

    private Double longitude;

    private Integer lvdou;

    private String memo;

    private String name;

    private String nickname;

    private String openid;

    private String province;

    private Long provinceid;

    private Integer sex;

    private Integer status;

    private Integer subscribe;

    private Date subscribeTime;

    private String tel;

    private String wuyeid;

    private Long xiaoquid;

    private String xiaoquname;

    private Integer zhima;

    private String realname;

    private String county;

    private Long registerdate;

    private Integer couponcount;

    private String sharecode;

    private Boolean newregiste;

    private String officetel;

    private Integer totalBind;

    private String cspid;

    private String sectid;

    private String appid;

    private String orisys;

    private Long oriuserid;

    private Integer totalbind;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCityid() {
        return cityid;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getCountyid() {
        return countyid;
    }

    public void setCountyid(Long countyid) {
        this.countyid = countyid;
    }

    public Long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Long createdate) {
        this.createdate = createdate;
    }

    public Long getCurrentaddrid() {
        return currentaddrid;
    }

    public void setCurrentaddrid(Long currentaddrid) {
        this.currentaddrid = currentaddrid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(String identitycard) {
        this.identitycard = identitycard;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getLvdou() {
        return lvdou;
    }

    public void setLvdou(Integer lvdou) {
        this.lvdou = lvdou;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(Long provinceid) {
        this.provinceid = provinceid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWuyeid() {
        return wuyeid;
    }

    public void setWuyeid(String wuyeid) {
        this.wuyeid = wuyeid;
    }

    public Long getXiaoquid() {
        return xiaoquid;
    }

    public void setXiaoquid(Long xiaoquid) {
        this.xiaoquid = xiaoquid;
    }

    public String getXiaoquname() {
        return xiaoquname;
    }

    public void setXiaoquname(String xiaoquname) {
        this.xiaoquname = xiaoquname;
    }

    public Integer getZhima() {
        return zhima;
    }

    public void setZhima(Integer zhima) {
        this.zhima = zhima;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Long getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Long registerdate) {
        this.registerdate = registerdate;
    }

    public Integer getCouponcount() {
        return couponcount;
    }

    public void setCouponcount(Integer couponcount) {
        this.couponcount = couponcount;
    }

    public String getSharecode() {
        return sharecode;
    }

    public void setSharecode(String sharecode) {
        this.sharecode = sharecode;
    }

    public Boolean getNewregiste() {
        return newregiste;
    }

    public void setNewregiste(Boolean newregiste) {
        this.newregiste = newregiste;
    }

    public String getOfficetel() {
        return officetel;
    }

    public void setOfficetel(String officetel) {
        this.officetel = officetel;
    }

    public Integer getTotalBind() {
        return totalBind;
    }

    public void setTotalBind(Integer totalBind) {
        this.totalBind = totalBind;
    }

    public String getCspid() {
        return cspid;
    }

    public void setCspid(String cspid) {
        this.cspid = cspid;
    }

    public String getSectid() {
        return sectid;
    }

    public void setSectid(String sectid) {
        this.sectid = sectid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOrisys() {
        return orisys;
    }

    public void setOrisys(String orisys) {
        this.orisys = orisys;
    }

    public Long getOriuserid() {
        return oriuserid;
    }

    public void setOriuserid(Long oriuserid) {
        this.oriuserid = oriuserid;
    }

    public Integer getTotalbind() {
        return totalbind;
    }

    public void setTotalbind(Integer totalbind) {
        this.totalbind = totalbind;
    }
}