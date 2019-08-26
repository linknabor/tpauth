package com.eshequ.hexie.tpauth.service;

import com.eshequ.hexie.tpauth.vo.ComponentAcessToken;
import com.eshequ.hexie.tpauth.vo.PreAuthCode;
import com.eshequ.hexie.tpauth.vo.VerifyTicket;

public interface AuthService {
	
	void authEventHandle(String xml);

	void saveVerifyTicket(VerifyTicket verifyTicket);
	
	ComponentAcessToken getComponentAccessToken(String verifyTicket);
	
	PreAuthCode getPreAuthCode();
}
