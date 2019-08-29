package com.eshequ.hexie.tpauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eshequ.hexie.tpauth.config.AuthApplication;
import com.eshequ.hexie.tpauth.schedule.ScheduleService;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.vo.AuthorizationResp;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApplication.class)
public class AuthApplicationTests {
	
	
	@Test
	public void testParseXml() throws IOException {
		
		String xml = "<xml><AppId><![CDATA[wx0d408844b35d85e2]]></AppId><Encrypt><![CDATA[oLBdp/nGuBRVnxembvAh55n93bGOsLnkhwIkkLqpuJNMtbQdblQVGx4w9Gy3rHzE05jer0hoB5xX3rPGK8vmAyAzPUzq+TqoOi0Z15ohTBMawW/QKmhZ5PR9/ODHFf8xxTzojJXilGpiN4LKMQby/g7OQ0cgagv3rFvhNxVTopmoMLh3SX7Vf2beJ7ZftVHX32qwRXMZDSL4X77xFVJsT670iig6y4JgyIwoE3kG6rUudx8bZakJvc8AWk5pa8mY+HeQuOSL1fCYN3jLnAWiSc1iFFxhZzsKl2oN0yxrzBF+PvwknhPBnvupQMttI5UBbISjZQADiWvqR9GGrrPQWEtw3LuGa8r6KqLUVXzSnchCQjH1AbUUFZsjuDgyGGsl/52mutKWovQzC8a/ZgOYlkKMTlbdUX4KDDBSJXWQ38upLxKQ5UufSvch2dt1olpr0jLMmJftUUeYpkHqJFiiCw==]]></Encrypt></xml>";
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode jsonNode = xmlMapper.readTree(xml);
		System.out.println(jsonNode.asText());
	}
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Test
	public void testDeserialize() {
		
		scheduleService.updateComponentAccessToken();
		
	}
	
	@Autowired
	private AuthService authService;
	
	@Test
	public void testGetComponentAccessToken() {
		
		authService.getComponentAccessToken("1");
	}
	
	@Test
	public void testStringFormat() {
		
		String link = "baidu.com?a=%s&b=%s";
		String alink = String.format(link, "A", "b");
		
		String link2 = "baidu.com?a=%S&b=%s";
		String blink = String.format(link2, "a", "B");
		
		System.out.println("a : " + alink);
		System.out.println("b : " + blink);
		
	}
	
	@Test
	public void testEncodeUrl() throws UnsupportedEncodingException {
		
		String url = "https://test.e-shequ.com/official/authSuccess.html";
		url = URLEncoder.encode(url, "utf-8");
		System.out.println(url);
	}
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void testDeserializeJson() throws JsonParseException, JsonMappingException, IOException {
		
		String json = "{\"authorization_info\": {\"authorizer_appid\": \"wxf8b4f85f3a794e77\",\"authorizer_access_token\": \"QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM\",\"expires_in\": 7200,\"authorizer_refresh_token\": \"dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY\",\"func_info\": [{\"funcscope_category\": {\"id\": \"1\"}},{\"funcscope_category\": {\"id\": \"2\"}},{\"funcscope_category\": {\"id\": \"3\"}}]}}";
		AuthorizationResp a = objectMapper.readValue(json, AuthorizationResp.class);
		System.out.println("-----------------:" + a);
	}
	
	@Test
	public void testWriteFile() {
		
		authService.writeFile("testFile", "token@@10239jajlwejflajlkfjl");
	}
	
}
