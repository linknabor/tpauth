package com.eshequ.hexie.tpauth.util;

import com.eshequ.hexie.tpauth.vo.card.EditCardReq;
import com.eshequ.hexie.tpauth.vo.card.EditCardReq.MemberCard;
import com.eshequ.hexie.tpauth.vo.card.EditCardReq.MemberCard.BaseInfo;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CardUtil {
	
	public static void main(String[] args) throws JsonProcessingException {
		
		CardUtil cardUtil = new CardUtil();
		cardUtil.editCard();
		
	}
	
	/**
	 * 编辑微信卡券
	 * @throws JsonProcessingException 
	 */
	public void editCard() throws JsonProcessingException {
		
		String cardId = "phZbVwDCWR5PBv99JjKmRzlVosn0";
		String appBrandUserName = "gh_189e68bcdaee@app";
		String appBrandPass = "longbing_card/pages/shop/index/index";
		
		EditCardReq editCardReq = new EditCardReq();
		editCardReq.setCardId(cardId);
		
		BaseInfo baseInfo = new BaseInfo();
		baseInfo.setCustomUrl("http://www.qq.com");
		baseInfo.setCustomUrlName("会员小程序");
		baseInfo.setCustomAppBrandUserName(appBrandUserName);
		baseInfo.setCustomAppBrandPass(appBrandPass);
		baseInfo.setCustomUrlSubTitle("立即使用");
		
		baseInfo.setPromotionUrlName("更多优惠");
		baseInfo.setPromotionUrl("http://www.qq.com");
		baseInfo.setPromotionAppBrandPass(appBrandPass);
		baseInfo.setPromotionAppBrandUserName(appBrandUserName);
		
		MemberCard memberCard = new MemberCard();
		memberCard.setBaseInfo(baseInfo);
		editCardReq.setMemberCard(memberCard);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String json = objectMapper.writeValueAsString(editCardReq);
		System.out.println(json);
		
	}

}
