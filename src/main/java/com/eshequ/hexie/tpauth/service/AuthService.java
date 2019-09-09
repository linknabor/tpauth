package com.eshequ.hexie.tpauth.service;

import java.io.IOException;

import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.vo.EventRequest;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.auth.ComponentAcessToken;

public interface AuthService {
	
	void handleAuthEvent(EventRequest authRequest) throws AesException, IOException;

	ComponentAcessToken getComponentAccessToken(String verifyTicket);
	
	String clientAuth(String requestHead);
	
	void authorizationInfo(String authorizationCode, String authorizerAppid);
	
	ComponentAcessToken getComponentAcessTokenFromCache();

	AuthorizerAccessToken getAuthorizerAccessToken(String authAppId, String authRefreshToken);

	void writeFile(String fileName, String content);

	AuthorizerAccessToken getAuthorizerAccessTokenFromCache(String appId);
	
}
