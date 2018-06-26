package com.github.nyc.portal.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;


@Component
public class HttpUtils {
	
	/**
	 * 请求编码
	 */
	public static final Charset INPUT_CHARSET = Charset.forName("UTF-8");

	/**
	 * 超时时间
	 */
	private static final int TIME_OUT = 10000;
	
	/**
	 * http工厂
	 */
	private HttpClientBuilder clientBuilder;
	
	/**
	 * 请求设置
	 */
	private RequestConfig requestConfig;

	/**
	 * 请求池
	 */
	private HttpClientConnectionManager connManager;
	
	/**
	 * 初始化相关对象
	 */
	@PostConstruct
	public void initialize() {
		this.requestConfig = createRequestConfig();
		this.connManager = createConnectionManager();
		
		this.clientBuilder = createClientBuilder();
		this.clientBuilder.setDefaultRequestConfig(this.requestConfig);
		this.clientBuilder.setConnectionManager(this.connManager);
	}

	/**
	 * 销毁相关对象
	 */
	@PreDestroy
	public void destroy() {
		this.connManager.shutdown();
	}
	
	/**
	 * 创建接口请求设置
	 * @return
	 */
	protected RequestConfig createRequestConfig() {
		return RequestConfig.custom().setConnectionRequestTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).build();
	}

	/**
	 * 创建请求连接池
	 * @return
	 */
	protected PoolingHttpClientConnectionManager createConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(20);
		connectionManager.setMaxTotal(200);
		return connectionManager;
	}

	/**
	 * 创建请求工厂
	 * @return
	 */
	protected HttpClientBuilder createClientBuilder() {
		HttpClientBuilder clientBuilder = HttpClients.custom();

		List<Header> headers = new LinkedList<Header>();
		headers.add(new BasicHeader("http.protocol.content-charset", INPUT_CHARSET.name()));
		headers.add(new BasicHeader("Content-Encoding", INPUT_CHARSET.name()));
		headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + INPUT_CHARSET.name()));
		headers.add(new BasicHeader("Accept", "text/xml,text/javascript,text/html,application/json"));
		clientBuilder.setDefaultHeaders(headers);

		return clientBuilder;
	}

	/**
	 * 构建httpClient
	 * 
	 * @return
	 */
	protected CloseableHttpClient build() {
		return this.clientBuilder.build();
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws ClientProtocolException 
	 * @throws IOException
	 */
	public String httpPost(String url, String content, ContentType contentType) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", contentType.toString());
		if (StringUtils.isNotBlank(content)) {
			post.setEntity(new StringEntity(content, INPUT_CHARSET));
		}
		try (CloseableHttpResponse response = build().execute(post)) {
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity != null) {
				return StringUtils.trim(EntityUtils.toString(entity, INPUT_CHARSET));
			} else {
				return null;
			}
		}
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException 
	 * @throws IOException
	 */
	public String httpPost(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairList=null;
		if (params != null && !params.isEmpty()) {
			pairList = new LinkedList<>();
			for (Entry<String, String> entry : params.entrySet()) {
				pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(pairList, INPUT_CHARSET));
			
		}

		System.out.println(pairList);
		try (CloseableHttpResponse response = build().execute(post)) {
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity != null) {
				return StringUtils.trim(EntityUtils.toString(entity, INPUT_CHARSET));
			} else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String get(String url) throws ClientProtocolException, IOException, URISyntaxException {
		return get(url, null);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String get(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {
				builder.addParameter(entry.getKey(), entry.getValue());
			}
		}
		HttpGet get = new HttpGet(builder.build());

		try (CloseableHttpResponse response = build().execute(get)) {
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity != null) {
				return StringUtils.trim(EntityUtils.toString(entity, INPUT_CHARSET));
			} else {
				return null;
			}
		}
	}
	

}
