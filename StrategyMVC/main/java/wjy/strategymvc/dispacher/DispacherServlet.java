package wjy.strategymvc.dispacher;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.adapter.DefaultHandlerAdapter;
import wjy.strategymvc.adapter.MappingHandlerAdapter;
import wjy.strategymvc.adapter.HandlerAdapter;
import wjy.strategymvc.mapper.HandlerMapper;
import wjy.strategymvc.mapper.MappingHandlerMapper;
import wjy.strategymvc.viewresolver.ModelAndView;
import wjy.strategymvc.mapper.HandlerExecutionChain;
import wjy.strategymvc.viewresolver.DefaultViewResolver;
import wjy.strategymvc.viewresolver.View;
import wjy.strategymvc.viewresolver.ViewResolver;

/**
 * 调度的Servlet,前端控制器
 * @author wjy
 */
public class DispacherServlet extends HttpServlet{
	
	private HandlerMapper handlerMapper;
	private ViewResolver viewResolver;
	private List<HandlerAdapter> handlerAdapterList;

	/**
	 * 模拟注入的方法缺点：
	 * 		由于每次请求都是一个新的HttpServlet，所以处理器映射器、处理器适配器都不是单例的
	 */
	public DispacherServlet() {
		//模拟bean注入
		//使用默认的处理器映射器
//		this.handlerMapper = new DefaultHandlerMapper();
		//使用注解映射器
		this.handlerMapper = new MappingHandlerMapper();
		//适配器集合
		this.handlerAdapterList = new ArrayList<>();
		this.handlerAdapterList.add(new DefaultHandlerAdapter());
		this.handlerAdapterList.add(new MappingHandlerAdapter());
		//视图解析器
		this.viewResolver = new DefaultViewResolver();
	}
	
	@Override
	public void doGet(HttpServletRequest request,HttpServletResponse response) {
		try {
			this.doDispacher(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request,HttpServletResponse response) {
		try {
			this.doDispacher(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  调度响应客户端请求
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doDispacher(HttpServletRequest request,HttpServletResponse response)throws Exception{
		if(this.handlerMapper ==null)throw new NullPointerException("处理器映射器不存在！！！");
		//根据请求获取处理器执行链
		HandlerExecutionChain handlerExecutionChain = this.handlerMapper.getRequestHandle(request, response);
		if(handlerExecutionChain == null)throw new NullPointerException("处理器不存在！！！");
		//策略模式获取到支持处理该请求的处理器适配器
		HandlerAdapter handlerAdapter = null;
		for(HandlerAdapter adapter: handlerAdapterList) {
			if(adapter.support(handlerExecutionChain.getHandler())) {
				handlerAdapter = adapter;
				break;
			}
		}
		if(handlerAdapter ==null)throw new NullPointerException("处理器适配器不存在！！！");
		//执行拦截器
		boolean isInterceptor = handlerExecutionChain.doBefore(request, response);
		if(!isInterceptor) {
			//被拦截器拦截就交给拦截器处理，这里不需要任何处理
			return;
		}
		//执行处理器
		ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecutionChain.getHandler());
		//执行拦截器
		handlerExecutionChain.doAfter(request, response,modelAndView.getModel());
		//视图解析
		View view = this.viewResolver.resolveViewName(modelAndView.getViewName());
		if(view==null)throw new NullPointerException("视图解析器不存在！！！");
		//视图渲染
		view.render(modelAndView.getModel(), request, response);
	}
}
