package wjy.strategymvc.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.viewresolver.ModelAndView;

public interface HandlerAdapter {

	boolean support(Object handle);
	
	ModelAndView  handle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception;
}
