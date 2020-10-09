package wjy.strategymvc.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.handler.DefaultHandler;
import wjy.strategymvc.viewresolver.ModelAndView;

public class DefaultHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean support(Object handle) {
		return handle instanceof DefaultHandler;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request,HttpServletResponse response,Object handle) throws Exception {
		return ((DefaultHandler)handle).handleAction(request, response);
	}

}
