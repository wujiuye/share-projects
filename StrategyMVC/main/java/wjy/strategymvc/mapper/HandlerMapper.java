package wjy.strategymvc.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMapper {

	/**
	 * 根据请求路径获取处理器
	 * @param request
	 * @param response
	 * @return
	 */
	HandlerExecutionChain getRequestHandle(HttpServletRequest request, HttpServletResponse response);
}
