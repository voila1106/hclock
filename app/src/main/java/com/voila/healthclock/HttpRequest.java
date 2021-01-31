package com.voila.healthclock;

import java.net.*;
import java.io.*;
import java.util.*;

public class HttpRequest
{
	private String url;
	private String method = "GET";
	private String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36";
	private byte[] content = {};
	private String type = "application/x-www-form-urlencoded; charset=UTF-8";
	private String origin;
	private String referer;
	private String cookie;
	private HashMap<String, String> prop = null;

	public HttpRequest(String url, String method)
	{
		this.url = url;
		this.method = method;
	}

	public HttpRequest(String url)
	{
		this.url = url;
		this.method = "GET";
	}

	public String require() throws IOException
	{
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)u.openConnection();

		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("User-Agent", ua);
		conn.setRequestProperty("Cookie", cookie);
		if(origin != null)
			conn.setRequestProperty("Origin", origin);
		if(referer != null)
			conn.setRequestProperty("Referer", referer);
		if(prop != null)
		{
			for(Map.Entry<String, String> entry : prop.entrySet())
				conn.setRequestProperty(entry.getKey(), entry.getValue());
		}
		if(method.equals("POST"))
		{
			conn.setRequestProperty("Content-Length", content.length + "");
			conn.setRequestProperty("Content-Type", type);
			conn.setDoOutput(true);
			conn.getOutputStream().write(content);
		}
		String location = conn.getHeaderField("Location");
		System.out.println(location);
		if(location != null)
		{
			url = location;
			require();
		}
		String line;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while((line = br.readLine()) != null)
			sb.append(line).append("\n");
		br.close();
		return sb.toString();
	}

	public void setContent(byte[] content)
	{
		if(method.equals("GET"))
			throw new RuntimeException("method is \"GET\"");
		this.content = content;
	}

	public void setType(String type)
	{
		if(method.equals("GET"))
			throw new RuntimeException("method is \"GET\"");
		this.type = type;
	}

	public void setOrigin(String origin)
	{
		this.origin = origin;
	}

	public void setReferer(String referer)
	{
		this.referer = referer;
	}

	public void setUA(String ua)
	{
		this.ua = ua;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public void setProp(String key, String value)
	{
		if(prop == null)
			prop = new HashMap<>();
		prop.put(key, value);
	}
}
