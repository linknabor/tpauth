package com.eshequ.hexie.tpauth.vo.card;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditCardReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5753560481262942499L;

	@JsonProperty("card_id")
	private String cardId;
	
	@JsonProperty("member_card")
	private MemberCard memberCard;
	
	
	public static class MemberCard {
		
		@JsonProperty("wx_activate")
		private String wxActivate;
		@JsonProperty("auto_activate")
		private String autoActivate;
		@JsonProperty("activate_url")
		private String activateUrl;
		@JsonProperty("base_info")
		private BaseInfo baseInfo;
		
		public static class BaseInfo{
			
			//小程序相关的
			@JsonProperty("custom_url_name")
			private String customUrlName;
			@JsonProperty("custom_url")
			private String customUrl;	//固定 https://mp.weixin.qq.com
			@JsonProperty("custom_app_brand_user_name")
			private String customAppBrandUserName;	//gh_189e68bcdaee@app
			@JsonProperty("custom_app_brand_pass")
			private String customAppBrandPass;	//longbing_card/pages/index/index
			@JsonProperty("custom_url_sub_title")
			private String customUrlSubTitle;	//汉字提示
			
			@JsonProperty("promotion_url_name")
			private String promotionUrlName;
			@JsonProperty("promotion_url")
			private String promotionUrl;
			@JsonProperty("promotion_app_brand_user_name")
			private String promotionAppBrandUserName;
			@JsonProperty("promotion_app_brand_pass")
			private String promotionAppBrandPass;
			
			public String getCustomUrl() {
				return customUrl;
			}
			public void setCustomUrl(String customUrl) {
				this.customUrl = customUrl;
			}
			public String getCustomAppBrandUserName() {
				return customAppBrandUserName;
			}
			public void setCustomAppBrandUserName(String customAppBrandUserName) {
				this.customAppBrandUserName = customAppBrandUserName;
			}
			public String getCustomAppBrandPass() {
				return customAppBrandPass;
			}
			public void setCustomAppBrandPass(String customAppBrandPass) {
				this.customAppBrandPass = customAppBrandPass;
			}
			public String getCustomUrlSubTitle() {
				return customUrlSubTitle;
			}
			public void setCustomUrlSubTitle(String customUrlSubTitle) {
				this.customUrlSubTitle = customUrlSubTitle;
			}
			public String getCustomUrlName() {
				return customUrlName;
			}
			public void setCustomUrlName(String customUrlName) {
				this.customUrlName = customUrlName;
			}
			public String getPromotionUrlName() {
				return promotionUrlName;
			}
			public void setPromotionUrlName(String promotionUrlName) {
				this.promotionUrlName = promotionUrlName;
			}
			public String getPromotionUrl() {
				return promotionUrl;
			}
			public void setPromotionUrl(String promotionUrl) {
				this.promotionUrl = promotionUrl;
			}
			public String getPromotionAppBrandUserName() {
				return promotionAppBrandUserName;
			}
			public void setPromotionAppBrandUserName(String promotionAppBrandUserName) {
				this.promotionAppBrandUserName = promotionAppBrandUserName;
			}
			public String getPromotionAppBrandPass() {
				return promotionAppBrandPass;
			}
			public void setPromotionAppBrandPass(String promotionAppBrandPass) {
				this.promotionAppBrandPass = promotionAppBrandPass;
			}
			@Override
			public String toString() {
				return "BaseInfo [customUrlName=" + customUrlName + ", customUrl=" + customUrl
						+ ", customAppBrandUserName=" + customAppBrandUserName + ", customAppBrandPass="
						+ customAppBrandPass + ", customUrlSubTitle=" + customUrlSubTitle + ", promotionUrlName="
						+ promotionUrlName + ", promotionUrl=" + promotionUrl + ", promotionAppBrandUserName="
						+ promotionAppBrandUserName + ", promotionAppBrandPass=" + promotionAppBrandPass + "]";
			}
			
		}

		public String getWxActivate() {
			return wxActivate;
		}

		public void setWxActivate(String wxActivate) {
			this.wxActivate = wxActivate;
		}

		public String getAutoActivate() {
			return autoActivate;
		}

		public void setAutoActivate(String autoActivate) {
			this.autoActivate = autoActivate;
		}

		public String getActivateUrl() {
			return activateUrl;
		}

		public void setActivateUrl(String activateUrl) {
			this.activateUrl = activateUrl;
		}

		public BaseInfo getBaseInfo() {
			return baseInfo;
		}

		public void setBaseInfo(BaseInfo baseInfo) {
			this.baseInfo = baseInfo;
		}

		@Override
		public String toString() {
			return "MemberCard [wxActivate=" + wxActivate + ", autoActivate=" + autoActivate + ", activateUrl="
					+ activateUrl + ", baseInfo=" + baseInfo + "]";
		}
		
		
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public MemberCard getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(MemberCard memberCard) {
		this.memberCard = memberCard;
	}

	@Override
	public String toString() {
		return "EditCardReq [cardId=" + cardId + ", memberCard=" + memberCard + "]";
	}
	
	
	
}
