package com.eshequ.hexie.tpauth.schedule;

public interface ScheduleService {

	void updateComponentAccessToken();
	void updateAuthorizerAccessToken();
	void updateAuthorizerJsTicket();
	void handleAuthQueue();
	
}
