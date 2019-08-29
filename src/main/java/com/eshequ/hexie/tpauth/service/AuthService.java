package com.eshequ.hexie.tpauth.service;

import java.io.IOException;

import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.vo.AuthRequest;
import com.eshequ.hexie.tpauth.vo.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.ComponentAcessToken;

public interface AuthService {
	
	void handleAuthEvent(AuthRequest authRequest) throws AesException, IOException;

	ComponentAcessToken getComponentAccessToken(String verifyTicket);
	
	String clientAuth(String requestHead);
	
	void authorizationInfo(String authorizationCode, String authorizerAppid);
	
	ComponentAcessToken getComponentAcessTokenFromCache();

	AuthorizerAccessToken getAuthorizerAccessToken(String authAppId, String authRefreshToken);

	void writeFile(String fileName, String content);
}
