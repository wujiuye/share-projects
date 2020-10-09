package wjy.strategymvc.mapper;

import wjy.strategymvc.annotations.RequestMapping;
import wjy.strategymvc.interceptor.CharUtf8EncodeInterceptor;
import wjy.strategymvc.interceptor.HandlerInterceptor;
import wjy.strategymvc.testhandler.UserRegistHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 注解处理器映射器
 * @author wjy
 *
 */
public class MappingHandlerMapper implements HandlerMapper {

	private Map<String, Object> handleMap;
	private List<HandlerInterceptor> interceptorList;

	public MappingHandlerMapper() {
		//模拟处理器的注入
		this.handleMap = new HashMap<>();

		//模拟包扫描，将被RequestMapping注解的类实例化加入到容器
		Object handle1 = new UserRegistHandler();
		RequestMapping requestMapping = handle1.getClass().getAnnotation(RequestMapping.class);
		this.handleMap.put(requestMapping.path(),handle1);


		//模拟拦截器的注入
		this.interceptorList = new LinkedList<>();
		//注入一个字符串编码拦截器
		this.interceptorList.add(new CharUtf8EncodeInterceptor());
	}

	@Override
	public HandlerExecutionChain getRequestHandle(HttpServletRequest request,HttpServletResponse response) {
		String url = request.getServletPath();
		String handlerName = url.substring(url.indexOf("/")+1,url.lastIndexOf("/"));
		//从bean容器中获取处理器
		Object handler = this.handleMap.get(handlerName);
		//将处理器handler和拦截器interceptorList封装为一个处理器执行链
		HandlerExecutionChain  handlerExecutionChain = new HandlerExecutionChain(handler,interceptorList);
		return handlerExecutionChain;
	}

}
