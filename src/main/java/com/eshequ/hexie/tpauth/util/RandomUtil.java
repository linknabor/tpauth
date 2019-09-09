package com.eshequ.hexie.tpauth.util;

import java.util.UUID;

public class RandomUtil {

	/**
	 * 生成32位随机数
	 */
	public static String buildRandom() {
		
		return UUID.randomUUID().toString().replace("-", "");
	}
}
