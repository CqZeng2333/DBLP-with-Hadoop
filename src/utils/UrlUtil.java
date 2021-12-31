package utils;

import java.util.HashMap;
import java.util.Map;

public class UrlUtil {
	public static class UrlEntity {
		public String baseUrl;
		public Map<String, String> params;
	}
	
	public static UrlEntity parse(String url) {
		UrlEntity entity = new UrlEntity();
		
		if (url == null) {
			return entity;
		}
		url = url.trim();
		if (url.equals("")) {
			return entity;
		}
		
		String[] params = url.split("&");
		entity.params = new HashMap<>();
		for (String param : params) {
			if (param.contains("=")) {
				String[] keyValue = param.split("=");
				if (keyValue.length == 1) {
					entity.params.put(keyValue[0], "");
				} else {
					keyValue[1] = keyValue[1].replaceAll("%20", " ");
					keyValue[1] = keyValue[1].replaceAll("\\+", " ");
					entity.params.put(keyValue[0], keyValue[1]);
				}
			}
		}
		return entity;
	}
}
