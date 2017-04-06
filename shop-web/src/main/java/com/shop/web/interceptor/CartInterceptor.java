package com.shop.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.service.HttpClientService;
import com.shop.common.util.CookieUtils;
import com.shop.web.controller.UserController;
import com.shop.web.pojo.User;
import com.shop.web.threadlocal.UserThreadLocal;

//拦截器实现获取userId，如果userId不存在，不管cookie还是redis没有，直接转向登录页面
public class CartInterceptor implements HandlerInterceptor{
	//拦截器可以利用service
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override	//执行controllre方法前
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1.从cookie中获取值
		String ticket = CookieUtils.getCookieValue(request, UserController.SHOP_COOKIE_NAME);
		if(StringUtils.isNotEmpty(ticket)){
			// 2.到redis中获取数据
			String url = "http://sso.shop.com/user/query/"+ticket;
			String jsonData = httpClientService.doGet(url);
			if(StringUtils.isNotEmpty(jsonData)){
				JsonNode jsonNode = MAPPER.readTree(jsonData);
				String jsonUser = jsonNode.get("data").asText();
				
				User curUser = MAPPER.readValue(jsonUser, User.class);
				UserThreadLocal.set(curUser);
			}else{
				UserThreadLocal.set(null);	//清理，防止旧的数据
			}
		}else{
			UserThreadLocal.set(null);
		}
		
		String userId = ""+UserThreadLocal.getUserId();
		//没有获取到值，转向登录页面
		if(null == userId || "".equals(userId) || "null".equals(userId)){	
			response.sendRedirect("/user/login.html");
			return false;
		}
		
		return true;	//拦截器，放行true，不放行false
	}

	@Override	//在执行controller方法后
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override	//在执行controller方法后，转向页面之前
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
