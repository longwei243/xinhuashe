package com.xinhua.xinhuashe.util;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

/**
 * 重定向
 *
 * @author azuryleaves
 * @since 2013-12-13 下午4:41:12
 * @version 1.0
 *
 */
public class ClientRedirectHandler extends DefaultRedirectHandler {

	@Override
	public boolean isRedirectRequested(HttpResponse response,
			HttpContext context) {
		boolean isRedirect = super.isRedirectRequested(response, context);
//		System.out.println("---是否重定向---" + isRedirect + "---");
		return isRedirect;
	}
	
}
