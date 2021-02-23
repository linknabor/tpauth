package com.eshequ.hexie.tpauth;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eshequ.hexie.tpauth.config.AuthApplication;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.schedule.ScheduleService;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizationResp;
import com.eshequ.hexie.tpauth.vo.subscribemsg.EventPopup;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApplication.class)
public class AuthApplicationTests {
	
	private static Logger logger = LoggerFactory.getLogger(AuthApplicationTests.class);
	
	@Value("${component.appid}")
	private String componentAppid;
	
	@Value("${component.secret}")
	private String componetSecret;
	
	@Value("${component.token}")
	private String token;
	
	@Value("${component.aeskey}")
	private String aeskey;
	
	
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
	
	@Test
	public void x() throws AesException, ParserConfigurationException, SAXException, IOException {
		
		String xml = "<xml>\n" + 
				"    <ToUserName><![CDATA[gh_3c884a361561]]></ToUserName>\n" + 
				"    <Encrypt><![CDATA[9viNMC9CfANSZTXbP4GdEAn0AYbHJg68x8QlIuum2iPSs5FUW6MLxMse8MTGEtwqc3ENS8MGUItDLARr1Qk5xiGDw9epqNw3jkrhFDSwnoP3l503xHSuboanR1GsVG3e241dRoKzBRH0q2VQMuJ/AOzn4I562VSiYeOHgrwbeLAe/iyhnZoGMxYaE6Rl6Rt8SDS/BnGoMXZ5cEjy0x1PJ6jS+m3YtRAjSyHCxRnl7xzyIL+LKa1NLeiE8UUgmqDDEnuBgGkgJoYiHya1gwvYmcntRA/xbOIDHeqxBZQdbM2RA5AkK3oCYlxKjaTKPAX7Kie8vNe+QaNOOxYf3hD+1aewcFa0dqMXUts435QfTcqq+1sNXYmrgokfn+xA1DQrWCx6zgLdxnQM1rLWfQiRpIoB3kJQ4+Xz0ETmsyNw7bJuQVAy7Dme7KQ/b54/0AzXy24ubAj5C+zYoRlFrxp7I7c79EbcMJdfF7Bng0O+zM3dcML61KGf5pQl7dgGxstJuqJuuhyqHXvCa3YOpxFaX0siXIPvh7FwtnX1rnws+b0OIzZUT3c35ClJ7OfpQYLNISdvsLFMXiBUAMKHgzC94Q==]]></Encrypt>\n" + 
				"</xml>";
		
		String sinature = "4d0751c77d6e082c23708f0d7d63a1dd32c39250";
		String timestamp = "1568098067";
		String nonce = "66430491";
		String encrypt_type = "aes";
		String msg_signature = "4d0751c77d6e082c23708f0d7d63a1dd32c39250";
		
		String formattedXml = xml.replaceAll("\r", "").replaceAll("\n", "").
				replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode rootNode = xmlMapper.readTree(formattedXml);	//解析xml
		JsonNode userNode = rootNode.path("ToUserName");		//toUserName节点
		JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
		String encryptStr = encryptNode.asText();
		
		WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, aeskey, componentAppid);
		String decryptedContent = msgCrypt.decryptMsg(msg_signature, timestamp, 
				nonce, encryptStr);
		
		logger.info("decrypted content : " + decryptedContent);	//解密后的内容，是个xml
		
		
		
	}
	
	@Test
	public void x2() throws AesException, ParserConfigurationException, SAXException, IOException {
		
		String replyMsg = "<xml><ToUserName><![CDATA[gh_79d799f541be]]></ToUserName><FromUserName><![CDATA[ohZbVwKWwhirszRVObHqSkwNjNBE]]></FromUserName><CreateTime>1568086108</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[1234]]></Content><MsgId>22449593214238793</MsgId></xml>";
		
//		String replyMsg = "测试8";
		
//		replyMsg  = replyMsg.replaceAll("\r", "").replaceAll("\n", "").
//				replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", ""); 
		
		String timestamp = "1568086109";
		String nonce = "1496849230";
		
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, aeskey, componentAppid);
		String afterEncrpt = pc.encryptMsg(replyMsg, timestamp, nonce);
		System.out.println(afterEncrpt);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(afterEncrpt);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		Element root = document.getDocumentElement();
		NodeList nodelist1 = root.getElementsByTagName("Encrypt");
		NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

		String encrypt = nodelist1.item(0).getTextContent();
		String msgSignature = nodelist2.item(0).getTextContent();
		String xmlFormat = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
		
		String fromXML = String.format(xmlFormat, encrypt);
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode rootNode = xmlMapper.readTree(fromXML);	//解析xml
		JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
		String encryptStr = encryptNode.asText();

		// 第三方收到公众号平台发送的消息
		String afterDecrpt = pc.decryptMsg(msgSignature, timestamp, nonce, encryptStr);
		System.out.println(afterDecrpt);
		
	}
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void testRedisTemplate() {
		
//		Long ret = redisTemplate.opsForValue().increment("vericodeIpFrequency_127.0.0.1");
//		System.out.println(ret);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("abc", "12");
		
		redisTemplate.opsForHash().putAll("testHash", map);
		
		map = new HashMap<String, String>();
		map.put("dfg", "244");
		redisTemplate.opsForHash().putAll("testHash", map);
		
		redisTemplate.opsForHash().put("testHash", "xyz", "666");
		
	}
	
	@Autowired
	private RestUtil restUtil;
	
	@Test
	public void testCard() {
		
		String reqUrl = "https://api.weixin.qq.com/card/get?access_token=TOKEN";
		String token = "28_LnBa3HAejtg7ko1Aznw-LIB92iM2DcGd9TSdL_hWJDw7EbMLhM3IlnF5Y4O6CZAaUzvfBsPmp1HrOFDBg0ydPi4DMBf_md9EKZM0FLBEBTJ_fDnIl0sRSZ-Z_m1aewT03mm9Xr0SkfUDBqYaJSWiAIACLP";
		reqUrl = reqUrl.replaceAll("TOKEN", token);
		Map<String, String> map =  new HashMap<>();
		map.put("card_id", "p_3DlwYakaIwGuYk4CfIG5w6gz3I");
		String response = restUtil.postByJson(reqUrl, map, String.class);
		System.out.println(response);
//		o_3Dlwb5LserLCnzuQwDNUMYoypM
	}
	
	@Test
	public void testCardList() {
		
		String reqUrl = "https://api.weixin.qq.com/card/user/getcardlist?access_token=TOKEN";
		String token = "28_Zh5Z_SAAZaZ1623Gkl0ZPvtrcz7kdlgHJKuXDVJugcKUOCx860a30h2Sg5m6x3bn2z2tf1wQux08EU3Fv-1OkbS3ktMHFyN-7EgmTqk6-hm3rzEED-SG22WS0oU8ihlIkwaZFh7wXPasonTVPKAdACAFAQ";
		reqUrl = reqUrl.replaceAll("TOKEN", token);
		Map<String, String> map =  new HashMap<>();
		map.put("card_id", "p_3DlwYakaIwGuYk4CfIG5w6gz3I");
		map.put("openid", "o_3Dlwb5LserLCnzuQwDNUMYoypM");
		String response = restUtil.postByJson(reqUrl, map, String.class);
		System.out.println(response);
//		o_3Dlwb5LserLCnzuQwDNUMYoypM
	}
	
	@Test
	public void testUserCardInfo() {
		
		String reqUrl = "https://api.weixin.qq.com/card/membercard/userinfo/get?access_token=TOKEN";
		String token = "28_Zh5Z_SAAZaZ1623Gkl0ZPvtrcz7kdlgHJKuXDVJugcKUOCx860a30h2Sg5m6x3bn2z2tf1wQux08EU3Fv-1OkbS3ktMHFyN-7EgmTqk6-hm3rzEED-SG22WS0oU8ihlIkwaZFh7wXPasonTVPKAdACAFAQ";
		reqUrl = reqUrl.replaceAll("TOKEN", token);
		Map<String, String> map =  new HashMap<>();
		map.put("card_id", "p_3DlwYakaIwGuYk4CfIG5w6gz3I");
		map.put("code", "815208363832");
		String response = restUtil.postByJson(reqUrl, map, String.class);
		System.out.println(response);
	}
	
	@Test
	public void testEventMsgSubscribePopup() throws IOException {
		
		String xml = "<xml><ToUserName><![CDATA[gh_123456789abc]]></ToUserName>"
	    +"<FromUserName><![CDATA[otFpruAK8D-E6EfStSYonYSBZ8_4]]></FromUserName>"
	    +"<CreateTime>1610969440</CreateTime>"
	    +"<MsgType><![CDATA[event]]></MsgType>"
	    +"<Event><![CDATA[subscribe_msg_popup_event]]></Event>"
	    +"<SubscribeMsgPopupEvent>"
	        +"<List>"
	            +"<TemplateId><![CDATA[VRR0UEO9VJOLs0MHlU0OilqX6MVFDwH3_3gz3Oc0NIc]]></TemplateId>"
	            +"<SubscribeStatusString><![CDATA[accept]]></SubscribeStatusString>"
	            +"<PopupScene>2</PopupScene>"
	        +"</List>"
	        +"<List>"
	            +"<TemplateId><![CDATA[9nLIlbOQZC5Y89AZteFEux3WCXRRRG5Wfzkpssu4bLI]]></TemplateId>"
	            +"<SubscribeStatusString><![CDATA[reject]]></SubscribeStatusString>"
	            +"<PopupScene>2</PopupScene>"
	        +"</List>"
	    +"</SubscribeMsgPopupEvent>"
	    +"</xml>";
		
//		<xml><ToUserName><![CDATA[gh_bac848e71b54]]></ToUserName><FromUserName><![CDATA[o_3Dlwc5fzAfEWhjAXpBbR6BDi0g]]>
//		</FromUserName><CreateTime>1613989348</CreateTime><MsgType><![CDATA[event]]></MsgType>
//		<Event><![CDATA[subscribe_msg_change_event]]></Event>
//		<SubscribeMsgChangeEvent><List><TemplateId><![CDATA[nFQNN0gCejjQBGG8ZyB5uF5zcG8Bu7wd2_QPrAY0FA4]]></TemplateId><SubscribeStatusString>
//		<![CDATA[reject]]></SubscribeStatusString></List></SubscribeMsgChangeEvent></xml>
		
		XmlMapper xmlMapper = new XmlMapper();
		EventPopup ep = xmlMapper.readValue(xml, EventPopup.class);
		System.out.println(ep);
		
	}
	
}
