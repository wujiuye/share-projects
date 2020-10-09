package wjy.strategymvc.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置字符编码拦截器
 * @author wjy
 *
 */
public class CharUtf8EncodeInterceptor implements HandlerInterceptor{

	@Override
	public boolean beforeHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		//记得不要拦截请求
		return true;
	}

	@Override
	public void afterHandle(HttpServletRequest request, HttpServletResponse response, Object handler,Map<String, Object> model) throws Exception {
		response.setCharacterEncoding( "utf-8" );
		response.setContentType("text/html;charset=utf-8");
	}

}
