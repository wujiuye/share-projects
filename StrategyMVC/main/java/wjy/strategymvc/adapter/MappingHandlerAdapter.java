package wjy.strategymvc.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wjy.strategymvc.annotations.RequestMapping;
import wjy.strategymvc.annotations.RequestParam;
import wjy.strategymvc.viewresolver.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解处理器适配器
 * @author wjy
 */
public class MappingHandlerAdapter implements HandlerAdapter {

	/**
	 * 如果处理器使用了RequestMapping注解就交给该适配器
	 * @param handle
	 * @return
	 */
	@Override
	public boolean support(Object handle) {
		return handle.getClass().getAnnotation(RequestMapping.class)!=null;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param handle	handler并非真正的目标方法,而是目标方法所在的对象
	 * @return
	 * @throws Exception
	 */
	@Override
	public ModelAndView handle(HttpServletRequest request,HttpServletResponse response, Object handle) throws Exception {
		String url = request.getServletPath();
		String handlerName = url.substring(url.lastIndexOf("/")+1);
		String method = request.getMethod();
		//获取目标方法
		Method tm = null;
		Method[] methods = handle.getClass().getMethods();
		for(Method targetMethod:methods){
			//方法返回值类型必须是ModelAndView
			if(targetMethod.getReturnType() != ModelAndView.class){
				continue;
			}
			RequestMapping requestMapping = targetMethod.getAnnotation(RequestMapping.class);
			if(requestMapping!=null){
				String path = requestMapping.path();
				if(handlerName.equals(path)
						&&method.equals(requestMapping.method())){
					tm = targetMethod;
				}
			}
		}
		if(tm==null)throw new Exception("访问的路径不存在！");
		//设置参数
		Class<?>[] parameterizedType = tm.getParameterTypes();
		Annotation[][] parameterAnnotations=tm.getParameterAnnotations();
		Object[] param = new Object[parameterizedType.length];
		for(int index=0;index<param.length;index++){
			Class cl = parameterizedType[index];
			if(cl==HttpServletRequest.class){
				param[index]=request;
			}else if(cl==HttpServletResponse.class){
				param[index]=response;
			}else{
				//获取注解
				RequestParam rparam = (RequestParam) parameterAnnotations[index][0];
				Object o = request.getParameter(rparam.name());
				if(o==null&&rparam.required())throw new Exception("参数不全！！！");
				//参数类型转换
				try{
					if(cl==String.class)
						param[index]=o;
					else if(cl==int.class||cl==Integer.class)
						param[index]=Integer.valueOf((String) o);
					else if(cl==float.class||cl==Float.class)
						param[index]=Float.valueOf((String) o);
					else
						param[index]=o;
				}catch (Exception ex){
					throw new Exception("参数不和法！！！");
				}
			}
		}
		return (ModelAndView) tm.invoke(handle,param);
	}

}
