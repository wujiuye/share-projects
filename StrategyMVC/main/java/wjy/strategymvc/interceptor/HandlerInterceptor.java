package wjy.strategymvc.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为了简单一点，拦截器我只定义两个方法， 一个是处理器处理请求之前执行 另一个是处理器处理请求之后执行
 * 
 * @author wjy
 *
 */
public interface HandlerInterceptor {

	/**
	 * 处理器处理请求之前执行
	 * @param request
	 * @param response
	 * @param handler	处理器
	 * @return	如果需要拦截请求则需要返回false
	 * @throws Exception
	 */
	boolean beforeHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

	/**
	 * 处理器处理完成请求之后执行
	 * @param request
	 * @param response
	 * @param handler	处理器
	 * @param model	处理器处理完请求后要渲染到视图的数据，
	 * @throws Exception
	 */
	void afterHandle(HttpServletRequest request, HttpServletResponse response, Object handler, Map<String, Object> model) throws Exception;

}
