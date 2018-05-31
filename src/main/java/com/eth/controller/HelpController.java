package com.eth.controller;

import com.jfinal.core.Controller;

public class HelpController extends Controller{
	
	public void transfer(){
		setAttr("nav","transfer");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		render("/help/transfer.html");
	}
	
	public void aboutBlock(){
		setAttr("nav", "aboutBlock");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		render("/help/aboutBlock.html");
	}
	
	public void properNoun(){
		setAttr("nav", "properNoun");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		render("/help/properNoun.html");
	}
	
	public void aboutWallet(){
		setAttr("nav", "abourWallet");
    	setAttr("ethUsername", getSessionAttr("ethUsername"));
    	setAttr("redirectUrl", getRequest().getRequestURI());
		render("/help/aboutWallet.html");
	}
}
