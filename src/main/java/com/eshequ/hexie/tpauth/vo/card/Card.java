package com.eshequ.hexie.tpauth.vo.card;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5753560481262942499L;

	@JsonProperty("card_type")
	private String cardType;
	
	@JsonProperty("member_card")
	private MemberCard memberCard;
	
	
	public static class MemberCard {
		
		@JsonProperty("background_pic_url")
		private String backgroundPicUrl;
		
		@JsonProperty("base_info")
		private BaseInfo baseInfo;
		
		public static class BaseInfo{
			
			
			
		}
	}
	
	
}
