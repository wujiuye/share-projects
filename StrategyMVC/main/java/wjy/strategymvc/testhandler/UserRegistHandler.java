package wjy.strategymvc.testhandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.annotations.RequestMapping;
import wjy.strategymvc.annotations.RequestParam;
import wjy.strategymvc.viewresolver.ModelAndView;


@RequestMapping(path = "user")
public class UserRegistHandler {

	@RequestMapping(path = "regist")
	public ModelAndView doRegist(HttpServletRequest request, HttpServletResponse response,
								 @RequestParam(name = "username") String username,
								 @RequestParam(name = "password") String password) throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put("msg", "get请求，用户注册成功！！！");
		data.put("username", username);
		data.put("password", password);
		return new ModelAndView(".json", data);
	}

}
