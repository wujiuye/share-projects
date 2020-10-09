package wjy.strategymvc.handler;

import wjy.strategymvc.viewresolver.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DefaultHandler {

	ModelAndView handleAction(HttpServletRequest request, HttpServletResponse response) throws Exception ;
	
}
