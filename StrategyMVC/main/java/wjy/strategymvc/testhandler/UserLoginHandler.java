package wjy.strategymvc.testhandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.handler.DefaultHandler;
import wjy.strategymvc.viewresolver.ModelAndView;

public class UserLoginHandler implements DefaultHandler{

	@Override
	public ModelAndView handleAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String,Object> data = new HashMap<>();
		data.put("msg", "用户登录成功！！！！");
		return new ModelAndView(".json",data);
	}

}
