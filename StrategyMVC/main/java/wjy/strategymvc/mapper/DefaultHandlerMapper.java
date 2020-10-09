package wjy.strategymvc.mapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.interceptor.CharUtf8EncodeInterceptor;
import wjy.strategymvc.interceptor.HandlerInterceptor;
import wjy.strategymvc.testhandler.UserLoginHandler;
import wjy.strategymvc.testhandler.UserRegistHandler;

/**
 * 默认的处理器映射器
 * @author wjy
 *
 */
public class DefaultHandlerMapper implements HandlerMapper {

	private Map<String, Object> handleMap;
	private List<HandlerInterceptor> interceptorList;
	
	public DefaultHandlerMapper() {
		//模拟处理器的注入
		this.handleMap = new HashMap<>();
		this.handleMap.put("userLogin",new UserLoginHandler());
		this.handleMap.put("userRegist", new UserRegistHandler());
		//模拟拦截器的注入
		this.interceptorList = new LinkedList<>();
		//注入一个字符串编码拦截器
		this.interceptorList.add(new CharUtf8EncodeInterceptor());
	}
	
	@Override
	public HandlerExecutionChain getRequestHandle(HttpServletRequest request,HttpServletResponse response) {
		String url = request.getServletPath();
		String handlerName = url.substring(url.indexOf("/")+1);
		//从bean容器中获取处理器
		Object handler = this.handleMap.get(handlerName);
		//将处理器handler和拦截器interceptorList封装为一个处理器执行链
		HandlerExecutionChain  handlerExecutionChain = new HandlerExecutionChain(handler,interceptorList);
		return handlerExecutionChain;
	}

}
