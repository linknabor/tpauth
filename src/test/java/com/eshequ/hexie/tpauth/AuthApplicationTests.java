package com.eshequ.hexie.tpauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eshequ.hexie.tpauth.config.AuthApplication;
import com.eshequ.hexie.tpauth.util.wechat.SHA1;
import com.eshequ.hexie.tpauth.util.wechat.XMLParse;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApplication.class)
public class AuthApplicationTests {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void contextLoads() {
		
		String token = "hongzhitech";
		String xml = "<xml>\n" + 
				"    <AppId><![CDATA[wx0d408844b35d85e2]]></AppId>\n" + 
				"    <Encrypt><![CDATA[gkchgK1S9ur172xK1WWmD9QB+Dh5pF8nmKE/ZQKylti4Aatjm5QIIK4/OqNCPONUZ+nzc7rv83R4IeA7ERW1XjmsNz3ETchjpIYH855u9OQiihCGULUJxLwuYmU5bNRpO2h7cfGwl71uLwaiT5zc/6PsMCTqnkj2LF0SGi5kjRw8Xu61Sg2hXkB/aVW2JMQLqqO9wieaoNpECGdLvFKUHntEugit0t5A88j5NOLjAiiELgb2Gm9zm3Ja/VaIkiBZemwfgobXNIQ1BjhTec6vj27mTRHfBbgp4E/9LycyF57irzJdD+Utuunllj9UG4i64SMC37u3CTnVQa9mec6z4qPjU3yZkwNwqRJkIf7fakmAApeekgEbg3z1BZXHJnsLTqrRSEoTjejeRk3dEoUNxNY9jlY7jCjHD1HkUvuY/pqwNRb9AANbpMyR0A6oCV5npKmnJOaw9IwWLlnLt7zqqQ==]]></Encrypt>\n" + 
				"</xml>";
	
		try {

			Object[] encrypt = XMLParse.extract(xml);
			String signature = SHA1.getSHA1(token, "", "", encrypt[1].toString());
			System.out.println(signature);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
