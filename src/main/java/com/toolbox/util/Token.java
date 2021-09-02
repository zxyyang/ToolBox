
package com.toolbox.util;

import org.apache.shiro.authc.AuthenticationToken;

public class Token implements AuthenticationToken {

	private String token;

	public Token(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}