package wjy.strategymvc.viewresolver;


/**
 * 自定义视图解析器
 */
public class DefaultViewResolver implements ViewResolver {

    //返回jsp页面或html页面需要的前缀，默认为“/WEB-INF/”
    private String prefix = "/WEB-INF/";
    //提供注入方法
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    /**
     * 解析视图
     * @param viewName 视图名称
     * @return
     * @throws Exception
     */
    @Override
    public View resolveViewName(String viewName) throws Exception {
        View actionView = new DefaultView(viewName,prefix);
        return actionView;
    }
}
