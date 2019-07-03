package cn.ruhubao.website.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class FirstInterceptor implements HandlerInterceptor{

		

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
		
			System.out.println("开始执行拦截+++++++++++++++++++++++++++++++++++");
			
			HttpSession session = request.getSession();
			
			Object user = session.getAttribute("user");
			
			if (user!=null) {
				//放行
				System.out.println("放行-------------------------------------");
				return true;
			}
			//未登录
			response.sendRedirect(request.getContextPath()+"page/login");
			
			return false;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
			
			
		}

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				Exception ex) throws Exception {
		
			
		}

	
}
