package wjy.strategymvc.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.interceptor.HandlerInterceptor;

public class HandlerExecutionChain {

	private final Object handler;
	private List<HandlerInterceptor> interceptorList;
	
	public HandlerExecutionChain(Object handler) {
		this.handler = handler;
		this.interceptorList = new ArrayList<HandlerInterceptor>();
	}
	
	public HandlerExecutionChain(Object handler,List<HandlerInterceptor> interceptors) {
		this.handler = handler;
		this.interceptorList = interceptors;
	}
	
	public Object getHandler() {
		return this.handler;
	}
	
	public void addInterceptor(HandlerInterceptor  interceptor) {
		this.interceptorList.add(interceptor);
	}
	
	public boolean doBefore(HttpServletRequest request,HttpServletResponse response) throws Exception {
		for(HandlerInterceptor interceptor:interceptorList) {
			//返回false表示拦截处理
			if(!interceptor.beforeHandle(request, response, handler)) {
				return false;
			}
		}
		return true;
	}
	
	public void doAfter(HttpServletRequest request,HttpServletResponse response,Map<String, Object> model) throws Exception {
		//从后往前执行
		for(int index=interceptorList.size()-1;index>=0;index--) {
			HandlerInterceptor interceptor = interceptorList.get(index);
			interceptor.afterHandle(request, response, this.handler,model);
		}
	}
}
