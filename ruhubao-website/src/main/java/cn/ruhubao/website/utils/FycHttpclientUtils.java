package cn.ruhubao.website.utils;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FycHttpclientUtils {

	// 使用httpclient的get方法获取对象json字符串

	public static String doget(String urlString) throws Exception {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建httpget连接
		HttpGet httpGet = new HttpGet(urlString);
		CloseableHttpResponse response = null;
		 String content="";
		try {
			// 利用httpClient执行httpGet请求
			response = httpClient.execute(httpGet);
			// 处理结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				 content = EntityUtils.toString(response.getEntity(), "utf-8");
				  // content = EntityUtils.toString(response.getEntity())	;
				   
				// System.out.println("contetn1111111111:"+content);
			}
		} finally {
			if (response != null) {
				
				response.close();
			}
			httpClient.close();
		}
		return content;

	}
	
	

}
