package cn.ruhubao.website.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/login")
	public String login(String username, String password,HttpServletRequest request,HttpServletResponse response) {
		if ("dengyoucheng".equals(username) && "bolin@123".equals(password)) {
			request.getSession().setAttribute("user", username);
			System.out.println("执行登录操作");
		} else {
			return "login";
		}
		
	
		return"index";

	}

}
