package com.eth.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller con = inv.getController();
		if(con.getSessionAttr("ethAddress")==null){
			con.redirect("/user/login?redirectUrl=" + con.getRequest().getRequestURI());
			return;
		}
		inv.invoke();
	}

}
