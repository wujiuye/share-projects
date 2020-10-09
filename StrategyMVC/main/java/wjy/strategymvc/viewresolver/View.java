package wjy.strategymvc.viewresolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
	/**
	 * 响应头的ContentType
	 * @return
	 */
	String getContentType();

	/**
	 * 渲染视图
	 * @param model 需要渲染到页面的数据
	 */
	void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
