package wjy.strategymvc.viewresolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 自定义视图
 */
public class DefaultView implements View {

    enum ActionViewContentType{
        JSON("application/json;charset=utf-8"),
        HTML("text/html;charset=utf-8"),
        JSP("text/html;charset=utf-8");

        private String name;

        ActionViewContentType(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    private ActionViewContentType actionViewContentType = ActionViewContentType.JSP;

    private String prefix;
    private String viewName;

    /**
     *
     * @param viewName
     */
    public DefaultView(String viewName,String prefix){
        this.prefix = prefix;
        //获取后缀名
        String hz = viewName.substring(viewName.lastIndexOf(".")+1);
        //去掉后缀名
        this.viewName = viewName.substring(0,viewName.lastIndexOf("."));
        if(hz.equals("json")){
            this.actionViewContentType = ActionViewContentType.JSON;
        }else if(hz.equals("html")){
            this.actionViewContentType = ActionViewContentType.HTML;
        }else{
            //默认返回jsp页面
            this.actionViewContentType = ActionViewContentType.JSP;
        }
    }

    /**
     * 获取响应头的ContentType
     * @return
     */
    @Override
    public String getContentType() {
        return this.actionViewContentType.getName();
    }

    /**
     * 渲染视图
     * @param model 需要传给页面的数据
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setResponseContentType(request,response);
        if(this.actionViewContentType==ActionViewContentType.JSON){
            //使用jackson工具将model转换为json
            ObjectMapper mapper = new ObjectMapper();
            String resultJson = mapper.writeValueAsString(model);
            response.getWriter().print(resultJson);
        }else{
            String viewPath = this.prefix + this.viewName;
            if(this.actionViewContentType==ActionViewContentType.HTML){
                viewPath+=".html";
            }else if(this.actionViewContentType==ActionViewContentType.JSP){
                viewPath+=".jsp";
            }
            //将model传入到request中
            exposeModelAsRequestAttributes(model,request);
            //RequestDispatcher，渲染视图
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
            requestDispatcher.include(request, response);
        }
    }


    /**
     * 写入响应ContentType
     * @param request
     * @param response
     */
    protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(getContentType());
    }


    /**
     * 将model注入到request的参数中
     * @param model
     * @param request
     * @throws Exception
     */
    protected void exposeModelAsRequestAttributes(Map<String, ?> model, HttpServletRequest request) throws Exception {
        //遍历Map
        for (Map.Entry<String, ?> entry : model.entrySet()) {
            String modelName = entry.getKey();
            Object modelValue = entry.getValue();
            request.setAttribute(modelName, modelValue);
        }
    }
}
