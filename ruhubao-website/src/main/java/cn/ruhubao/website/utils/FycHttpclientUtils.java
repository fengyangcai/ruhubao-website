package cn.ruhubao.website.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FycHttpclientUtils {

	// 使用httpclient的get方法获取对象json字符串

	public static String doget(String urlString) throws Exception {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建httpget连接
		HttpGet httpGet = new HttpGet(urlString);
		CloseableHttpResponse response = null;
		String content = "";
		try {
			// 利用httpClient执行httpGet请求
			response = httpClient.execute(httpGet);
			// 处理结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				content = EntityUtils.toString(response.getEntity(), "utf-8");
				// content = EntityUtils.toString(response.getEntity()) ;

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

	// 有参数的get方式获取数据
	public static String dogetparam(String url, Map<String, String> map) throws Exception {
		// 创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建请求地址
		URIBuilder uriBuilder = new URIBuilder(url);

		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {

			uriBuilder.setParameter(entry.getKey(), entry.getValue());
		}

		// 以一个uri作爲构造参数创建httpGet对象
		HttpGet httpGet = new HttpGet(uriBuilder.build());

		CloseableHttpResponse response = null;
		String content = null;

		try {
			response = httpClient.execute(httpGet);
			// 处理获取的结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				content = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} finally {
			if (response != null) {
				response.close();
			}
			httpClient.close();
		}
		return content;

	}
	
	
	//使用post请求 无参数
	public static String dopost(String url) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		//这里可以设置请求的头的信息
		//httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
		CloseableHttpResponse response =null;
		String content =null;
		try {
			 response = httpClient.execute(httpPost);
			
			//判断是否请求成功
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				 content = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} finally {
			if (response!=null) {
				response.close();
			}
			httpClient.close();
		}
		return content;
		
	}
	
	//有参数的post请求
	public String dopost(String uri,Map<String,String> map) throws Exception {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<Entry<String,String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		//设置表单项
		httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
		CloseableHttpResponse response = null;
		String content=null;
		try {
			//利用httpClient执行httpGet请求
			response = httpClient.execute(httpPost);
			//处理结果
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				content = EntityUtils.toString(response.getEntity(), "utf-8");
				//System.out.println(content);
				//FileUtils.writeStringToFile(new File("D:\\ccc\\test.html"), content);
			}
		} finally {
			if(response != null){
				response.close();
			}
			httpClient.close();
		}
		return content;
	}
	

}
