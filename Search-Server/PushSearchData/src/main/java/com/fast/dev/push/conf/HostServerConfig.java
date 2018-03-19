package com.fast.dev.push.conf;

public class HostServerConfig {
	// 请求的url
	private String hostUrl;
	// 权限令牌
	private String token;

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
