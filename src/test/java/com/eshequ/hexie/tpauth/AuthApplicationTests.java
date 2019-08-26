package com.eshequ.hexie.tpauth;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eshequ.hexie.tpauth.config.AuthApplication;
import com.fasterxml.jackson.databind.JsonNode;
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
}
