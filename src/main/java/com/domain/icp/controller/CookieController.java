package com.domain.icp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domain.icp.util.JumingCookieUtil;

@Controller
public class CookieController {

	@ResponseBody
	@RequestMapping("/jm_cookie/cookie={cookie}")
	public String getAllDomainList(@PathVariable("cookie") String cookie) {
		JumingCookieUtil.setJMCookie(cookie);
		return "success";
	}
}
