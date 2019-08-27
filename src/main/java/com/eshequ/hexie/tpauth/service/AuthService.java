package com.eshequ.hexie.tpauth.service;

import java.io.IOException;

import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.vo.AuthRequest;
import com.eshequ.hexie.tpauth.vo.ComponentAcessToken;
import com.eshequ.hexie.tpauth.vo.ComponentVerifyTicket;

public interface AuthService {
	
	void handleAuthEvent(AuthRequest authRequest) throws AesException, IOException;

	void saveVerifyTicket(ComponentVerifyTicket verifyTicket);
	
	ComponentAcessToken getComponentAccessToken(String verifyTicket);
	
	String clientAuth(String requestHead);
}
